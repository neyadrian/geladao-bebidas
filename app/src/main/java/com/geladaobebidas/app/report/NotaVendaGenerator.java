package com.geladaobebidas.app.report;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.services.ItemVendaService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class NotaVendaGenerator {

    private final ItemVendaService itemVendaService;

    public NotaVendaGenerator(ItemVendaService itemVendaService) {
        this.itemVendaService = itemVendaService;
    }

    public byte[] gerar(Venda venda) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Geladão Bebidas", tituloFont);
        document.add(titulo);

        Paragraph numeroVenda = new Paragraph("Nota de Venda #" + venda.getIdVenda());
        document.add(numeroVenda);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Paragraph data = new Paragraph("Data: " + venda.getDataVenda().format(formatter));
        document.add(data);

        Paragraph cliente = new Paragraph("Cliente: " + venda.getCliente().getNomeCliente());
        document.add(cliente);

        document.add(new Paragraph(" "));

        PdfPTable tabela = new PdfPTable(5);
        tabela.setWidthPercentage(100);

        tabela.addCell("Produto");
        tabela.addCell("Embalagem");
        tabela.addCell("Quantidade");
        tabela.addCell("Preço Unitário");
        tabela.addCell("Subtotal");

        List<ItemVenda> itens = itemVendaService.listarPorVenda(venda);

        for (ItemVenda item : itens) {
            tabela.addCell(item.getProduto().getNomeProduto());
            tabela.addCell(item.getProduto().getTipoEmbalagemProduto().toString());
            tabela.addCell(String.valueOf(item.getQuantidadeItem()));
            tabela.addCell("R$ " + item.getPrecoUnitarioItem());

            BigDecimal subtotal = item.getPrecoUnitarioItem()
                    .multiply(BigDecimal.valueOf(item.getQuantidadeItem()));
            tabela.addCell("R$ " + subtotal);
        }

        document.add(tabela);

        document.add(new Paragraph(" "));
        Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Paragraph total = new Paragraph("Valor Total: R$ " + venda.getValorTotalVenda(), totalFont);
        document.add(total);

        document.close();
        return outputStream.toByteArray();
    }
}
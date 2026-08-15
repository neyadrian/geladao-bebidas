package com.geladaobebidas.app.report;

import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.services.VendaService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class RelatorioMensalGenerator {

    private final VendaService vendaService;

    public RelatorioMensalGenerator(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    public byte[] gerar(int ano, int mes) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        List<Venda> vendas = vendaService.buscarVendasDoMes(ano, mes);

        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        document.add(new Paragraph("Geladão Bebidas", tituloFont));
        document.add(new Paragraph("Relatório Mensal - " + mes + "/" + ano));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Quantidade de vendas: " + vendas.size()));

        BigDecimal totalVendido = BigDecimal.ZERO;
        for (Venda venda : vendas) {
            totalVendido = totalVendido.add(venda.getValorTotalVenda());
        }
        document.add(new Paragraph("Total vendido: R$ " + totalVendido));

        Map.Entry<Produto, Integer> maisVendido = vendaService.produtoMaisVendidoDoMes(vendas);
        if (maisVendido != null) {
            document.add(new Paragraph("Produto mais vendido: " + maisVendido.getKey().getNomeProduto()
                    + " (" + maisVendido.getValue() + " unidades)"));
        } else {
            document.add(new Paragraph("Nenhuma venda registrada nesse mês."));
        }

        document.close();
        return outputStream.toByteArray();
    }
}
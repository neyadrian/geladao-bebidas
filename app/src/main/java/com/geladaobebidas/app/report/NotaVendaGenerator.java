package com.geladaobebidas.app.report;

import com.geladaobebidas.app.entities.Venda;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

import java.io.ByteArrayOutputStream;

@Component
public class NotaVendaGenerator {

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

        Paragraph data = new Paragraph("Data: " + venda.getDataVenda());
        document.add(data);

        Paragraph cliente = new Paragraph("Cliente: " + venda.getCliente().getNomeCliente());
        document.add(cliente);

        document.close();
        return outputStream.toByteArray();
    }
}
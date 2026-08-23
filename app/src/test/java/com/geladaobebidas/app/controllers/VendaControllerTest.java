package com.geladaobebidas.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geladaobebidas.app.dto.ItemVendaRequest;
import com.geladaobebidas.app.dto.RegistrarVendaRequest;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.report.NotaVendaGenerator;
import com.geladaobebidas.app.report.RelatorioMensalGenerator;
import com.geladaobebidas.app.security.JwtService;
import com.geladaobebidas.app.services.VendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService vendaService;

    @MockitoBean
    private NotaVendaGenerator notaVendaGenerator;

    @MockitoBean
    private RelatorioMensalGenerator relatorioMensalGenerator;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveRegistrarVendaComSucesso() throws Exception {
        RegistrarVendaRequest request = criarRegistrarVendaRequest();
        Venda venda = criarVenda(1L);

        when(vendaService.registrarVenda(1L, 2L, request.getItens())).thenReturn(venda);

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVenda").value(1L))
                .andExpect(jsonPath("$.valorTotalVenda").value(50.00));

        verify(vendaService).registrarVenda(1L, 2L, request.getItens());
    }

    @Test
    void deveBuscarVendaPorIdComSucesso() throws Exception {
        Venda venda = criarVenda(1L);

        when(vendaService.buscarPorId(1L)).thenReturn(venda);

        mockMvc.perform(get("/vendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenda").value(1L))
                .andExpect(jsonPath("$.valorTotalVenda").value(50.00));
    }

    @Test
    void deveListarTodasAsVendas() throws Exception {
        Venda venda = criarVenda(1L);

        when(vendaService.listarTodas()).thenReturn(List.of(venda));

        mockMvc.perform(get("/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idVenda").value(1L))
                .andExpect(jsonPath("$[0].valorTotalVenda").value(50.00));
    }

    @Test
    void deveExcluirVendaComSucesso() throws Exception {
        doNothing().when(vendaService).excluir(1L);

        mockMvc.perform(delete("/vendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveGerarNotaVendaEmPdf() throws Exception {
        Venda venda = criarVenda(1L);
        byte[] pdf = "%PDF nota".getBytes();

        when(vendaService.buscarPorId(1L)).thenReturn(venda);
        when(notaVendaGenerator.gerar(venda)).thenReturn(pdf);

        mockMvc.perform(get("/vendas/1/nota"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(content().bytes(pdf))
                .andExpect(header().string("Content-Disposition", "inline; filename=nota-venda-1.pdf"));
    }

    @Test
    void deveGerarRelatorioMensalEmPdf() throws Exception {
        byte[] pdf = "%PDF relatorio".getBytes();

        when(relatorioMensalGenerator.gerar(2026, 8)).thenReturn(pdf);

        mockMvc.perform(get("/vendas/relatorio-mensal")
                        .param("ano", "2026")
                        .param("mes", "8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(content().bytes(pdf))
                .andExpect(header().string("Content-Disposition", "inline; filename=relatorio-8-2026.pdf"));
    }

    private RegistrarVendaRequest criarRegistrarVendaRequest() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(3L);
        item.setQuantidade(5);

        RegistrarVendaRequest request = new RegistrarVendaRequest();
        request.setClienteId(1L);
        request.setUsuarioId(2L);
        request.setItens(List.of(item));

        return request;
    }

    private Venda criarVenda(Long id) {
        Venda venda = new Venda();
        venda.setIdVenda(id);
        venda.setValorTotalVenda(new BigDecimal("50.00"));

        return venda;
    }
}

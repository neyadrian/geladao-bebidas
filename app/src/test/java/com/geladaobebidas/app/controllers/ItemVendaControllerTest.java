package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.security.JwtService;
import com.geladaobebidas.app.services.ItemVendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemVendaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemVendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemVendaService itemVendaService;

    @MockitoBean
    private JwtService jwtService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarItemVendaComSucesso() throws Exception {
        ItemVenda itemVenda = criarItemVenda(1L, 2, "10.50");

        doNothing().when(itemVendaService).salvar(any(ItemVenda.class));

        mockMvc.perform(post("/itens-venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemVenda)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idItemVenda").value(1L))
                .andExpect(jsonPath("$.quantidadeItem").value(2))
                .andExpect(jsonPath("$.precoUnitarioItem").value(10.50));

    }

    @Test
    void deveBuscarItemVendaPorIdComSucesso() throws Exception {
        ItemVenda itemVenda = criarItemVenda(1L, 2, "10.50");

        when(itemVendaService.buscarPorId(1L)).thenReturn(itemVenda);

        mockMvc.perform(get("/itens-venda/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItemVenda").value(1L))
                .andExpect(jsonPath("$.quantidadeItem").value(2))
                .andExpect(jsonPath("$.precoUnitarioItem").value(10.50));
    }

    @Test
    void deveListarTodosOsItensVenda() throws Exception {
        ItemVenda itemVenda = criarItemVenda(1L, 2, "10.50");

        when(itemVendaService.listarTodos()).thenReturn(List.of(itemVenda));

        mockMvc.perform(get("/itens-venda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idItemVenda").value(1L))
                .andExpect(jsonPath("$[0].quantidadeItem").value(2))
                .andExpect(jsonPath("$[0].precoUnitarioItem").value(10.50));
    }

    @Test
    void deveExcluirItemVendaComSucesso() throws Exception {
        doNothing().when(itemVendaService).excluir(1L);

        mockMvc.perform(delete("/itens-venda/1"))
                .andExpect(status().isNoContent());
    }

    private ItemVenda criarItemVenda(Long id, Integer quantidade, String precoUnitario) {
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setIdItemVenda(id);
        itemVenda.setQuantidadeItem(quantidade);
        itemVenda.setPrecoUnitarioItem(new BigDecimal(precoUnitario));

        return itemVenda;
    }
}

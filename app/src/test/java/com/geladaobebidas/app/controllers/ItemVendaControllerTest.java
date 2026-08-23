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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
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
}

package com.geladaobebidas.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geladaobebidas.app.entities.MovimentacaoEstoque;
import com.geladaobebidas.app.services.MovimentacaoEstoqueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimentacaoEstoqueController.class)
public class MovimentacaoEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovimentacaoEstoqueService movimentacaoEstoquesService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve salvar uma movimentação de estoque e retornar status 201 Created")
    public void deveSalvarMovimentacaoEstoque() throws Exception {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();

        mockMvc.perform(post("/movimentacao-estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimentacaoEstoque)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve buscar uma movimentação de estoque pelo ID e retornar status 200 OK")
    public void deveBuscarMovimentacaoEstoquePorId() throws Exception {
        Long id = 1L;
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();

        when(movimentacaoEstoquesService.buscarPorId(id)).thenReturn(movimentacaoEstoque);

        mockMvc.perform(get("/movimentacao-estoque/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve listar todas as movimentações de estoque e retornar status 200 OK")
    public void deveListarTodasMovimentacoesEstoque() throws Exception {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
        List<MovimentacaoEstoque> lista = List.of(movimentacaoEstoque);

        when(movimentacaoEstoquesService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/movimentacao-estoque"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve excluir uma movimentação de estoque e retornar status 204 No Content")
    public void deveExcluirMovimentacaoEstoque() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/movimentacao-estoque/{id}", id))
                .andExpect(status().isNoContent());
    }
}
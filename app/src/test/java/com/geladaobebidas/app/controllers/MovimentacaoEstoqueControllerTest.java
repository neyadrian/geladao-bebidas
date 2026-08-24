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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimentacaoEstoqueController.class)
public class MovimentacaoEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve salvar uma movimentação de estoque e retornar status 201 Created")
    public void deveSalvarMovimentacaoEstoque() throws Exception {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();

        when(movimentacaoEstoqueService.salvar(any(MovimentacaoEstoque.class))).thenReturn(movimentacaoEstoque);

        mockMvc.perform(post("/movimentacao-estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimentacaoEstoque)))
                .andExpect(status().isCreated());
    }

    
}
package com.geladaobebidas.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setNomeProduto("Cerveja Teste");

        doNothing().when(produtoService).salvar(any(Produto.class));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeProduto").value("Cerveja Teste"));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setNomeProduto("Cerveja Teste");

        when(produtoService.buscarPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeProduto").value("Cerveja Teste"));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExiste() throws Exception {
        when(produtoService.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Produto não encontrado: 99"));

        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosOsProdutos() throws Exception {
        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setNomeProduto("Cerveja Teste");

        when(produtoService.listarTodos()).thenReturn(List.of(produto));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveExcluirProdutoComSucesso() throws Exception {
        doNothing().when(produtoService).excluir(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }
}
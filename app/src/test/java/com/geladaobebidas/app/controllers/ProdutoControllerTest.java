package com.geladaobebidas.app.controllers;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.services.ProdutoService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setNomeProduto("Cerveja Teste");

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
}

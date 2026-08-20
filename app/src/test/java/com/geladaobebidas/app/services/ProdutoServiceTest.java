package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveRetornarProdutoQuandoEncontrado() {
        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoFake));

        Produto resultado = produtoService.buscarPorId(1L);

        assertEquals("Cerveja Teste", resultado.getNomeProduto());
    }

    @Test
    void buscarPorIdQuandoNaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            produtoService.buscarPorId(1L);
        });
    }
}
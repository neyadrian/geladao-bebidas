package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
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

    @Test
    void salvarDeveChamarRepositoryComProdutoCorreto() {
        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");

        produtoService.salvar(produtoFake);

        verify(produtoRepository).save(produtoFake);
    }

    @Test
    void listarTodosDeveRetornarListaDeProdutos() {
        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");

        Produto outroProdutoFake = new Produto();
        outroProdutoFake.setIdProduto(2L);
        outroProdutoFake.setNomeProduto("Refrigerante Teste");

        List<Produto> produtosFake = List.of(produtoFake, outroProdutoFake);
        when(produtoRepository.findAll()).thenReturn(produtosFake);

        List<Produto> resultado = produtoService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(produtosFake, resultado);
    }

    @Test
    void exlcuirDeveChamarRepositoryComIdCorreto() {
        Long id = 1L;

        produtoService.excluir(id);

        verify(produtoRepository).deleteById(id);
    }
}
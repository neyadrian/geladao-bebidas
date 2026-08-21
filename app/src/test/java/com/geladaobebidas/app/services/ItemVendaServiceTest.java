package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.ItemVendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemVendaServiceTest {

    @Mock
    private ItemVendaRepository itemVendaRepository;

    @InjectMocks
    private ItemVendaService itemVendaService;

    @Test
    void deveRetornarItemVendaQuandoEncontrado() {
        ItemVenda itemVendaFake = criarItemVendaFake(1L, 3, "12.50");

        when(itemVendaRepository.findById(1L)).thenReturn(Optional.of(itemVendaFake));

        ItemVenda resultado = itemVendaService.buscarPorId(1L);

        assertEquals(1L, resultado.getIdItemVenda());
        assertEquals(3, resultado.getQuantidadeItem());
        assertEquals(new BigDecimal("12.50"), resultado.getPrecoUnitarioItem());
    }

    @Test
    void buscarPorIdQuandoNaoEncontrado() {
        when(itemVendaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            itemVendaService.buscarPorId(1L);
        });
    }

    @Test
    void salvarDeveChamarRepositoryComItemVendaCorreto() {
        ItemVenda itemVendaFake = criarItemVendaFake(1L, 2, "8.00");

        itemVendaService.salvar(itemVendaFake);

        verify(itemVendaRepository).save(itemVendaFake);
    }

    @Test
    void listarTodosDeveRetornarListaDeItensVenda() {
        ItemVenda itemVendaFake = criarItemVendaFake(1L, 2, "8.00");
        ItemVenda outroItemVendaFake = criarItemVendaFake(2L, 4, "10.00");

        List<ItemVenda> itensVendaFake = List.of(itemVendaFake, outroItemVendaFake);
        when(itemVendaRepository.findAll()).thenReturn(itensVendaFake);

        List<ItemVenda> resultado = itemVendaService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(itensVendaFake, resultado);
    }

    @Test
    void listarPorVendaDeveRetornarListaDeItensDaVenda() {
        Venda vendaFake = new Venda();
        vendaFake.setIdVenda(1L);

        ItemVenda itemVendaFake = criarItemVendaFake(1L, 2, "8.00");
        itemVendaFake.setVenda(vendaFake);

        List<ItemVenda> itensVendaFake = List.of(itemVendaFake);
        when(itemVendaRepository.findByVenda(vendaFake)).thenReturn(itensVendaFake);

        List<ItemVenda> resultado = itemVendaService.listarPorVenda(vendaFake);

        assertEquals(itensVendaFake, resultado);
    }

    @Test
    void excluirDeveChamarRepositoryComIdCorreto() {
        Long id = 1L;

        itemVendaService.excluir(id);

        verify(itemVendaRepository).deleteById(id);
    }

    private ItemVenda criarItemVendaFake(Long id, Integer quantidade, String precoUnitario) {
        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");

        ItemVenda itemVendaFake = new ItemVenda();
        itemVendaFake.setIdItemVenda(id);
        itemVendaFake.setQuantidadeItem(quantidade);
        itemVendaFake.setPrecoUnitarioItem(new BigDecimal(precoUnitario));
        itemVendaFake.setProduto(produtoFake);

        return itemVendaFake;
    }
}

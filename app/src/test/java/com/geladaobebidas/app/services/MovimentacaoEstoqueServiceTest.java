package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.MovimentacaoEstoque;
import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.enums.TipoMovimentacao;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.MovimentacaoEstoqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @InjectMocks
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Test
    void deveRetornarMovimentacaoQuandoEncontrada() {
        MovimentacaoEstoque movimentacaoFake = criarMovimentacaoFake(1L, TipoMovimentacao.ENTRADA, 10);

        when(movimentacaoEstoqueRepository.findById(1L)).thenReturn(Optional.of(movimentacaoFake));

        MovimentacaoEstoque resultado = movimentacaoEstoqueService.buscarPorId(1L);

        assertEquals(1L, resultado.getIdMovimentacao());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.getTipoMovimentacao());
        assertEquals(10, resultado.getQuantidadeMovimentacao());
    }

    @Test
    void buscarPorIdQuandoNaoEncontrada() {
        when(movimentacaoEstoqueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            movimentacaoEstoqueService.buscarPorId(1L);
        });
    }

    @Test
    void salvarDeveChamarRepositoryComMovimentacaoCorreta() {
        MovimentacaoEstoque movimentacaoFake = criarMovimentacaoFake(1L, TipoMovimentacao.SAIDA, 5);

        movimentacaoEstoqueService.salvar(movimentacaoFake);

        verify(movimentacaoEstoqueRepository).save(movimentacaoFake);
    }

    @Test
    void listarTodasDeveRetornarListaDeMovimentacoes() {
        MovimentacaoEstoque movimentacaoFake = criarMovimentacaoFake(1L, TipoMovimentacao.ENTRADA, 10);
        MovimentacaoEstoque outraMovimentacaoFake = criarMovimentacaoFake(2L, TipoMovimentacao.SAIDA, 3);

        List<MovimentacaoEstoque> movimentacoesFake = List.of(movimentacaoFake, outraMovimentacaoFake);
        when(movimentacaoEstoqueRepository.findAll()).thenReturn(movimentacoesFake);

        List<MovimentacaoEstoque> resultado = movimentacaoEstoqueService.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals(movimentacoesFake, resultado);
    }

    @Test
    void excluirDeveChamarRepositoryComIdCorreto() {
        Long id = 1L;

        movimentacaoEstoqueService.excluir(id);

        verify(movimentacaoEstoqueRepository).deleteById(id);
    }

    private MovimentacaoEstoque criarMovimentacaoFake(Long id, TipoMovimentacao tipo, Integer quantidade) {
        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");

        MovimentacaoEstoque movimentacaoFake = new MovimentacaoEstoque();
        movimentacaoFake.setIdMovimentacao(id);
        movimentacaoFake.setTipoMovimentacao(tipo);
        movimentacaoFake.setQuantidadeMovimentacao(quantidade);
        movimentacaoFake.setDataMovimentacao(LocalDateTime.of(2026, 8, 21, 14, 30));
        movimentacaoFake.setMotivoMovimentacao("Teste");
        movimentacaoFake.setProduto(produtoFake);

        return movimentacaoFake;
    }
}

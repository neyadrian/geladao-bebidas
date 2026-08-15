package com.geladaobebidas.app.services;

import com.geladaobebidas.app.dto.ItemVendaRequest;
import com.geladaobebidas.app.entities.*;
import com.geladaobebidas.app.enums.TipoMovimentacao;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaService itemVendaService;
    private final ProdutoService produtoService;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    public VendaService(VendaRepository vendaRepository,
                        ItemVendaService itemVendaService,
                        ProdutoService produtoService,
                        MovimentacaoEstoqueService movimentacaoEstoqueService,
                        ClienteService clienteService,
                        UsuarioService usuarioService) {
        this.vendaRepository = vendaRepository;
        this.itemVendaService = itemVendaService;
        this.produtoService = produtoService;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Venda registrarVenda(Long clienteId, Long usuarioId, List<ItemVendaRequest> itensDesejados) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotalVenda(BigDecimal.ZERO);
        vendaRepository.save(venda);

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaRequest itemDesejado : itensDesejados) {
            Produto produto = produtoService.buscarPorId(itemDesejado.getProdutoId());

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidadeItem(itemDesejado.getQuantidade());
            itemVenda.setPrecoUnitarioItem(produto.getPrecoProduto());
            itemVendaService.salvar(itemVenda);

            BigDecimal subtotal = produto.getPrecoProduto().multiply(BigDecimal.valueOf(itemDesejado.getQuantidade()));
            valorTotal = valorTotal.add(subtotal);

            produto.setQuantidadeProduto(produto.getQuantidadeProduto() - itemDesejado.getQuantidade());
            produtoService.salvar(produto);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProduto(produto);
            movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
            movimentacao.setQuantidadeMovimentacao(itemDesejado.getQuantidade());
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setMotivoMovimentacao("Venda #" + venda.getIdVenda());
            movimentacaoEstoqueService.salvar(movimentacao);
        }

        venda.setValorTotalVenda(valorTotal);
        vendaRepository.save(venda);

        return venda;
    }

    public void salvar(Venda venda) {
        vendaRepository.save(venda);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada: " + id));
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public void excluir(Long id) {
        vendaRepository.deleteById(id);
    }

    public List<Venda> buscarVendasDoMes(int ano, int mes) {
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicio = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fim = anoMes.atEndOfMonth().atTime(23, 59, 59);

        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }
}
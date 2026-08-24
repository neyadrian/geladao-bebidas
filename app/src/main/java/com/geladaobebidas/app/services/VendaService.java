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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Venda registrarVenda(Long clienteId, Long usuarioId, String formaPagamento, List<ItemVendaRequest> itensDesejados) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        venda.setDataVenda(LocalDateTime.now());

        // NOVO: Preparando a venda para o Fiado
        venda.setFormaPagamento(formaPagamento);
        if ("FIADO".equalsIgnoreCase(formaPagamento)) {
            venda.setStatusPagamento("PENDENTE"); // O cliente está devendo
        } else {
            venda.setStatusPagamento("PAGO");
        }
        venda.setValorTotalVenda(BigDecimal.ZERO);
        // venda.setValorTotalLucro(BigDecimal.ZERO); // Descomente quando adicionar esse campo na entidade
        vendaRepository.save(venda);
        BigDecimal valorTotal = BigDecimal.ZERO;
        BigDecimal lucroTotal = BigDecimal.ZERO; // Acumulador de lucro
        for (ItemVendaRequest itemDesejado : itensDesejados) {
            Produto produto = produtoService.buscarPorId(itemDesejado.getProdutoId());
            // REGRA 1: Travar venda sem estoque
            if (produto.getQuantidadeProduto() < itemDesejado.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para a bebida: " + produto.getNomeProduto());
            }
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidadeItem(itemDesejado.getQuantidade());
            itemVenda.setPrecoUnitarioItem(produto.getPrecoProduto());
            itemVendaService.salvar(itemVenda);
            // REGRA 2: Cálculo do lucro (Preço Venda - Preço Custo) * Qtd
            BigDecimal precoCusto = produto.getPrecoCusto() != null ? produto.getPrecoCusto() : BigDecimal.ZERO;
            BigDecimal lucroUnitario = produto.getPrecoProduto().subtract(precoCusto);
            BigDecimal lucroDesteItem = lucroUnitario.multiply(BigDecimal.valueOf(itemDesejado.getQuantidade()));
            lucroTotal = lucroTotal.add(lucroDesteItem); // Soma no lucro da venda
            BigDecimal subtotal = produto.getPrecoProduto().multiply(BigDecimal.valueOf(itemDesejado.getQuantidade()));
            valorTotal = valorTotal.add(subtotal);
            // Atualiza o Estoque
            produto.setQuantidadeProduto(produto.getQuantidadeProduto() - itemDesejado.getQuantidade());

            // REGRA 3: Alerta de Estoque Baixo
            if (produto.isEstoqueBaixo()) {
                // Aqui podemos futuramente salvar numa tabela "Alerta" no banco de dados.
                // Por enquanto vai printar no console do servidor:
                System.out.println("⚠️ ALERTA: A bebida " + produto.getNomeProduto() + " atingiu o estoque mínimo de reposição!");
            }

            produtoService.salvar(produto);
            // Salva Movimentação
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProduto(produto);
            movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
            movimentacao.setQuantidadeMovimentacao(itemDesejado.getQuantidade());
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setMotivoMovimentacao("Venda #" + venda.getIdVenda() + (formaPagamento.equals("FIADO") ? " (FIADO)" : ""));
            movimentacaoEstoqueService.salvar(movimentacao);
        }
        venda.setValorTotalVenda(valorTotal);

        // NOVO: Salvando o lucro na venda para gerar relatórios depois
        // venda.setValorTotalLucro(lucroTotal);
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

    public Map.Entry<Produto, Integer> produtoMaisVendidoDoMes(List<Venda> vendas) {
        Map<Produto, Integer> vendasPorProduto = new HashMap<>();

        for (Venda venda : vendas) {
            List<ItemVenda> itens = itemVendaService.listarPorVenda(venda);
            for (ItemVenda item : itens) {
                vendasPorProduto.merge(item.getProduto(), item.getQuantidadeItem(), Integer::sum);
            }
        }

        return vendasPorProduto.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }
}
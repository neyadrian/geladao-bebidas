package com.geladaobebidas.app.services;

import com.geladaobebidas.app.dto.ItemVendaRequest;
import com.geladaobebidas.app.entities.Cliente;
import com.geladaobebidas.app.entities.Usuario;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Venda registarVenda(Long clienteId, Long usuarioId, List<ItemVendaRequest> itensDesejados) {
        Cliente cliente = clienteService.buscarPorNome(clienteId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);


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
}
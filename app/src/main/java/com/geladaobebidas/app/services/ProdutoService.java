package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}
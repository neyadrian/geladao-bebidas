package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.MovimentacaoEstoque;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.MovimentacaoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public void salvar(MovimentacaoEstoque movimentacaoEstoque) {
        movimentacaoEstoqueRepository.save(movimentacaoEstoque);
    }

    public MovimentacaoEstoque buscarPorId(Long id) {
        return movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrado: " + id));
    }

    public List<MovimentacaoEstoque> listarTodas() {
        return movimentacaoEstoqueRepository.findAll();
    }

    public void excluir(Long id) {
        movimentacaoEstoqueRepository.deleteById(id);
    }
}

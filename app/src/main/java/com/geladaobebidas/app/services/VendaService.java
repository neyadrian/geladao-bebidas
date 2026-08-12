package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
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

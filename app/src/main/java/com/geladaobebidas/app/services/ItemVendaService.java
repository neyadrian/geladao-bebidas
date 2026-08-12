package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.repositories.ItemVendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemVendaService {

    private ItemVendaRepository itemVendaRepository;

    public ItemVendaService(ItemVendaRepository itemVendaRepository) {
        this.itemVendaRepository = itemVendaRepository;
    }

    public void salvar(ItemVenda itemVenda) {
        itemVendaRepository.save(itemVenda);
    }

    public ItemVenda buscarPorId(Long id) {
        return itemVendaRepository.findById(id).get();
    }

    public List<ItemVenda> listarTodos() {
        return itemVendaRepository.findAll();
    }

    public void excluir(Long id) {
        itemVendaRepository.deleteById(id);
    }
}

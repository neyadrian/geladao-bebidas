package com.geladaobebidas.app.repositories;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVenda(Venda venda);
}
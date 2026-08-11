package com.geladaobebidas.app.repositories;

import com.geladaobebidas.app.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
}

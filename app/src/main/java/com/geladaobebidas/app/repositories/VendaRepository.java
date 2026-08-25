package com.geladaobebidas.app.repositories;

import com.geladaobebidas.app.entities.Cliente;
import com.geladaobebidas.app.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByClienteAndStatusPagamento(Cliente cliente, String statusPagamento);

}

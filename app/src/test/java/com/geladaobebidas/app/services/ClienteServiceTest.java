package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Cliente;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveRetornarClienteQuandoEncontrado() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);
        clienteFake.setNomeCliente("Cliente Teste");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteFake));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertEquals("Cliente Teste", resultado.getNomeCliente());
    }

    @Test
    void buscarPorIdQuandoNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            clienteService.buscarPorId(1L);
        });
    }

    @Test
    void buscarPorNomeDeveRetornarListaDeClientes() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);
        clienteFake.setNomeCliente("Cliente Teste");

        List<Cliente> clientesFake = List.of(clienteFake);
        when(clienteRepository.findByNomeCliente("Cliente Teste")).thenReturn(clientesFake);

        List<Cliente> resultado = clienteService.buscarPorNome("Cliente Teste");

        assertEquals(clientesFake, resultado);
    }

    @Test
    void salvarDeveChamarRepositoryComClienteCorreto() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);
        clienteFake.setNomeCliente("Cliente Teste");

        clienteService.salvar(clienteFake);

        verify(clienteRepository).save(clienteFake);
    }

    @Test
    void buscarTodosDeveRetornarListaDeClientes() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);
        clienteFake.setNomeCliente("Cliente Teste");

        Cliente outroClienteFake = new Cliente();
        outroClienteFake.setIdCliente(2L);
        outroClienteFake.setNomeCliente("Outro Cliente Teste");

        List<Cliente> clientesFake = List.of(clienteFake, outroClienteFake);
        when(clienteRepository.findAll()).thenReturn(clientesFake);

        List<Cliente> resultado = clienteService.buscarTodos();

        assertEquals(2, resultado.size());
        assertEquals(clientesFake, resultado);
    }

    @Test
    void excluirDeveChamarRepositoryComIdCorreto() {
        Long id = 1L;

        clienteService.excluir(id);

        verify(clienteRepository).deleteById(id);
    }
}

package com.geladaobebidas.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geladaobebidas.app.entities.Cliente;
import com.geladaobebidas.app.security.JwtService;
import com.geladaobebidas.app.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNomeCliente("Cliente Teste");
        cliente.setEnderecoCliente("Rua Teste");
        cliente.setTelefoneCliente("11999999999");

        doNothing().when(clienteService).salvar(any(Cliente.class));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"))
                .andExpect(jsonPath("$.enderecoCliente").value("Rua Teste"))
                .andExpect(jsonPath("$.telefoneCliente").value("11999999999"));
    }

    @Test
    void deveBuscarClientePorIdComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNomeCliente("Cliente Teste");

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1L))
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"));
    }

    @Test
    void deveListarTodosOsClientes() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNomeCliente("Cliente Teste");

        when(clienteService.buscarTodos()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Teste"));
    }

    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Cliente Atualizado");
        cliente.setEnderecoCliente("Rua Atualizada");
        cliente.setTelefoneCliente("11888888888");

        doNothing().when(clienteService).salvar(any(Cliente.class));

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1L))
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Atualizado"))
                .andExpect(jsonPath("$.enderecoCliente").value("Rua Atualizada"))
                .andExpect(jsonPath("$.telefoneCliente").value("11888888888"));
    }

    @Test
    void deveExcluirClienteComSucesso() throws Exception {
        doNothing().when(clienteService).excluir(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveBuscarClientePorNomeComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNomeCliente("Cliente Teste");

        when(clienteService.buscarPorNome("Cliente Teste")).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes/buscar")
                        .param("nome", "Cliente Teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Teste"));
    }
}

package com.geladaobebidas.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geladaobebidas.app.entities.Usuario;
import com.geladaobebidas.app.security.JwtService;
import com.geladaobebidas.app.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("Usuario Teste");
        usuario.setCpf("12345678900");
        usuario.setLogin("usuario.teste");
        usuario.setSenha("123456");

        doNothing().when(usuarioService).salvar(any(Usuario.class));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeUsuario").value("Usuario Teste"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.login").value("usuario.teste"));
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("Usuario Teste");
        usuario.setLogin("usuario.teste");

        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nomeUsuario").value("Usuario Teste"))
                .andExpect(jsonPath("$.login").value("usuario.teste"));
    }

    @Test
    void deveListarTodosOsUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("Usuario Teste");
        usuario.setLogin("usuario.teste");

        when(usuarioService.listarTodos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeUsuario").value("Usuario Teste"))
                .andExpect(jsonPath("$[0].login").value("usuario.teste"));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("Usuario Atualizado");
        usuario.setCpf("12345678900");
        usuario.setLogin("usuario.atualizado");
        usuario.setSenha("654321");

        doNothing().when(usuarioService).salvar(any(Usuario.class));

        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nomeUsuario").value("Usuario Atualizado"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.login").value("usuario.atualizado"));
    }

    @Test
    void deveExcluirUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).excluir(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}

package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Usuario;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    void deveRetornarUsuarioQuandoEncontrado() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setIdUsuario(1L);
        usuarioFake.setNomeUsuario("Usuario Teste");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioFake));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertEquals("Usuario Teste", resultado.getNomeUsuario());

    }

    @Test
    void buscarPorIdQuandoNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            usuarioService.buscarPorId(1L);
        });
    }

    
}
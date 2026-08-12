package com.geladaobebidas.app.services;

import com.geladaobebidas.app.entities.Usuario;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;
import com.geladaobebidas.app.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
}
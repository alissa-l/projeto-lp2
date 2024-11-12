package com.bank.service;

import com.bank.model.Usuario;
import java.util.HashMap;
import java.util.Map;

public class UsuarioService {
    private Map<String, Usuario> usuarios = new HashMap<>();

    public Usuario cadastrarUsuario(String nome, String cpf, String senha) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setSenha(senha);
        usuarios.put(cpf, usuario);
        return usuario;
    }

    public Usuario autenticar(String cpf, String senha) {
        Usuario usuario = usuarios.get(cpf);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        throw new RuntimeException("Autenticação falhou");
    }
}

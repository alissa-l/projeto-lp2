package com.banco.test;

import com.banco.model.Usuario;
import com.banco.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioServiceTest {
    private UsuarioService usuarioService;

    @BeforeEach
    public void setup() {
        usuarioService = new UsuarioService();
        carregarDadosDeTeste();
    }

    private void carregarDadosDeTeste() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Usuario[] usuarios = mapper.readValue(new File("src/test/resources/testdata.json"), Usuario[].class);
            for (Usuario usuario : usuarios) {
                usuarioService.cadastrarUsuario(usuario.getNome(), usuario.getCpf(), usuario.getSenha());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testeAutenticacaoSucesso() {
        Usuario usuario = usuarioService.autenticar("11111111111", "senha123");
        assertNotNull(usuario);
    }

    @Test
    public void testeAutenticacaoFalha() {
        assertThrows(RuntimeException.class, () -> {
            usuarioService.autenticar("11111111111", "senhaErrada");
        });
    }
}

package com.bank.model;

import java.util.HashMap;
import java.util.Map;

public class Banco {
	private Map<String, Usuario> usuarios = new HashMap<>();

	public Usuario cadastrarUsuario(String nome, String cpf, String senha) {
		if (usuarios.containsKey(cpf)) {
			throw new IllegalArgumentException("Usuário com este CPF já existe.");
		}

		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setCpf(cpf);
		usuario.setSenha(senha);
		usuarios.put(cpf, usuario);
		return usuario;
	}

	public Conta cadastrarConta(Usuario usuario, Conta.TipoConta tipoConta) {
		Conta conta = new Conta();
		if (tipoConta == Conta.TipoConta.CORRENTE) {
			conta.setNumero(gerarNumeroContaUnicoCorrente());
		} else {
			conta.setNumero(gerarNumeroContaUnicoPoupanca());
		}
		conta.setUsuario(usuario);
		conta.setTipoConta(tipoConta);
		usuario.getContas().add(conta);
		return conta;
	}

	public Usuario buscarUsuarioPorCpf(String cpf) {
		return usuarios.get(cpf);
	}

	private String gerarNumeroContaUnicoPoupanca() {
		return "POU" + (int)(Math.random() * 1000000);
	}

	private String gerarNumeroContaUnicoCorrente() {
		return "COR" + (int)(Math.random() * 1000000);
	}

	public Map<String, Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Map<String, Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}

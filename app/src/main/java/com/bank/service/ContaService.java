package com.bank.service;

import com.bank.model.Banco;
import com.bank.model.Conta;
import com.bank.model.Usuario;

import java.math.BigDecimal;

public class ContaService {
    private Banco banco;

    public ContaService(Banco banco) {
        this.banco = banco;
    }

    public BigDecimal consultarSaldo(Usuario usuario, String numeroConta) {
        Conta conta = encontrarContaDoUsuario(usuario, numeroConta);
        return conta.getSaldo();
    }

    public void depositar(Usuario usuario, String numeroConta, BigDecimal valor) {
        Conta conta = encontrarContaDoUsuario(usuario, numeroConta);
        conta.depositar(valor);
    }

    public void sacar(Usuario usuario, String numeroConta, BigDecimal valor) {
        Conta conta = encontrarContaDoUsuario(usuario, numeroConta);
        conta.sacar(valor);
    }

    public void transferir(Usuario usuario, String numeroContaOrigem, String numeroContaDestino, BigDecimal valor) {
        Conta contaOrigem = encontrarContaDoUsuario(usuario, numeroContaOrigem);
        Conta contaDestino = encontrarContaPorNumero(numeroContaDestino);
        contaOrigem.transferir(valor, contaDestino);
    }

    private Conta encontrarContaDoUsuario(Usuario usuario, String numeroConta) {
        return usuario.getContas().stream()
                .filter(conta -> conta.getNumero().equals(numeroConta))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o usuário."));
    }

    private Conta encontrarContaPorNumero(String numeroConta) {
        return banco.getUsuarios().values().stream()
                .flatMap(usuario -> usuario.getContas().stream())
                .filter(conta -> conta.getNumero().equals(numeroConta))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));
    }
}

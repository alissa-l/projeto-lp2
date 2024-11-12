package com.bank.model;

import java.math.BigDecimal;

import com.bank.exceptions.SaldoInsuficienteException;

public class Conta {

	private String numero;
    private BigDecimal saldo = BigDecimal.ZERO;
    private Usuario usuario;
    private TipoConta tipoConta;

    public enum TipoConta {
        CORRENTE, POUPANCA
    }
    
    public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

    public void depositar(BigDecimal valor) {
        saldo = saldo.add(valor);
    }

    public void sacar(BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        saldo = saldo.subtract(valor);
    }

    public void transferir(BigDecimal valor, Conta destino) {
        this.sacar(valor);
        destino.depositar(valor);
    }
}

package com.bank;

import com.bank.model.Acao;
import com.bank.model.Banco;
import com.bank.model.Conta;
import com.bank.model.Usuario;
import com.bank.service.ContaService;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    private static Banco banco = new Banco();
    private static ContaService contaService = new ContaService(banco);
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            exibirMenu();
            Acao acao = lerAcao(scanner);

            switch (acao) {
                case CADASTRAR_USUARIO:
                    cadastrarUsuario(scanner);
                    break;
                case CADASTRAR_CONTA:
                    if (usuarioLogado != null) {
                        cadastrarConta(scanner);
                    } else {
                        System.out.println("Você precisa estar autenticado para cadastrar uma conta.");
                    }
                    break;
                case CONSULTAR_SALDO:
                    if (usuarioLogado != null) {
                        consultarSaldo(scanner);
                    } else {
                        System.out.println("Você precisa estar autenticado para consultar o saldo.");
                    }
                    break;
                case DEPOSITAR:
                    if (usuarioLogado != null) {
                        depositar(scanner);
                    } else {
                        System.out.println("Você precisa estar autenticado para realizar um depósito.");
                    }
                    break;
                case SACAR:
                    if (usuarioLogado != null) {
                        sacar(scanner);
                    } else {
                        System.out.println("Você precisa estar autenticado para realizar um saque.");
                    }
                    break;
                case TRANSFERIR:
                    if (usuarioLogado != null) {
                        transferir(scanner);
                    } else {
                        System.out.println("Você precisa estar autenticado para realizar uma transferência.");
                    }
                    break;
                case AUTENTICAR_USUARIO:
                    autenticarUsuario(scanner);
                    break;
                case SAIR:
                    executando = false;
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Ação inválida.");
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\nSelecione uma ação:");
        for (Acao acao : Acao.values()) {
            System.out.println(acao.ordinal() + " - " + acao);
        }
        System.out.print("Escolha uma ação: ");
    }

    private static Acao lerAcao(Scanner scanner) {
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer
        if (escolha < 0 || escolha >= Acao.values().length) {
            System.out.println("Escolha inválida. Tente novamente.");
            return null;
        }
        return Acao.values()[escolha];
    }
    
    private static void cadastrarUsuario(Scanner scanner) {
        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite a senha do usuário: ");
        String senha = scanner.nextLine();

        banco.cadastrarUsuario(nome, cpf, senha);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void autenticarUsuario(Scanner scanner) {
        System.out.print("Digite o CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        try {
            usuarioLogado = banco.buscarUsuarioPorCpf(cpf);
            if (usuarioLogado != null && usuarioLogado.getSenha().equals(senha)) {
                System.out.println("Autenticação realizada com sucesso!");
            } else {
                System.out.println("CPF ou senha incorretos.");
                usuarioLogado = null;
            }
        } catch (Exception e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
        }
    }

    private static void cadastrarConta(Scanner scanner) {
        System.out.print("Digite o tipo da conta (CORRENTE/POUPANCA): ");
        Conta.TipoConta tipoConta = Conta.TipoConta.valueOf(scanner.nextLine().toUpperCase());
        
        Conta conta = banco.cadastrarConta(usuarioLogado, tipoConta);
        System.out.println("Conta cadastrada com sucesso! Número da conta: " + conta.getNumero());
    }

    private static void consultarSaldo(Scanner scanner) {
        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();

        BigDecimal saldo = contaService.consultarSaldo(usuarioLogado, numeroConta);
        System.out.println("Saldo atual: R$ " + saldo);
    }

    private static void depositar(Scanner scanner) {
        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();
        System.out.print("Digite o valor do depósito: ");
        BigDecimal valor = scanner.nextBigDecimal();

        contaService.depositar(usuarioLogado, numeroConta, valor);
        System.out.println("Depósito realizado com sucesso!");
    }

    private static void sacar(Scanner scanner) {
        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();
        System.out.print("Digite o valor do saque: ");
        BigDecimal valor = scanner.nextBigDecimal();

        try {
            contaService.sacar(usuarioLogado, numeroConta, valor);
            System.out.println("Saque realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao sacar: " + e.getMessage());
        }
    }

    private static void transferir(Scanner scanner) {
        System.out.print("Digite o número da conta de origem: ");
        String numeroContaOrigem = scanner.nextLine();
        System.out.print("Digite o número da conta de destino: ");
        String numeroContaDestino = scanner.nextLine();
        System.out.print("Digite o valor da transferência: ");
        BigDecimal valor = scanner.nextBigDecimal();

        try {
            contaService.transferir(usuarioLogado, numeroContaOrigem, numeroContaDestino, valor);
            System.out.println("Transferência realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro na transferência: " + e.getMessage());
        }
    }
}

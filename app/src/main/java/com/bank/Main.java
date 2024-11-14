package com.bank;

import com.bank.model.Acao;
import com.bank.model.Banco;
import com.bank.model.Conta;
import com.bank.model.Usuario;
import com.bank.service.ContaService;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.ActionMap;

public class Main {
    private static Banco banco = new Banco();
    private static ContaService contaService = new ContaService(banco);
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            exibirMenu();

            try {
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
                    case LISTAR_CONTAS:
                        System.out.println("Contas do usuário " + usuarioLogado.getNome() + ":");
                        for (Conta conta : usuarioLogado.getContas()) {
                            System.out.println(conta.getNumero() + " - " + conta.getTipoConta() + " - Saldo: R$ "
                                    + conta.getSaldo());
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
                    case LOGOUT:
                        usuarioLogado = null;
                        System.out.println("Usuário deslogado com sucesso!");
                        break;
                    case SAIR:
                        executando = false;
                        System.out.println("Saindo do sistema...");
                        break;
                    case DADOS_TESTE:
                        popularDadosdeTeste();
                        break;
                    case IMPRIMIR_RELATORIO:
                        imprimirRelatorio();
                        break;
                    default:
                        System.out.println("Ação inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, tente novamente.");
                scanner.next(); // Clear the invalid input
            } catch (NoSuchElementException e) {
                System.out.println("Nenhuma entrada fornecida. Encerrando o programa.");
                executando = false;
            }

        }

        scanner.close();
    }

    private static void exibirMenu() {
        if (usuarioLogado == null) {
            System.out.println("\nSe cadastre ou faça login\n");
            System.out.println("\nSelecione uma ação:");
            for (Acao acao : Acao.values()) {
                if (acao.equals(Acao.CADASTRAR_USUARIO) || acao.equals(Acao.AUTENTICAR_USUARIO)
                        || acao.equals(Acao.SAIR)) {
                    System.out.println(acao.ordinal() + " - " + acao);
                }
            }
        } else {
            System.out.println("\nLogado como: " + usuarioLogado.getNome() + "\n");
            System.out.println("\nSelecione uma ação:");
            for (Acao acao : Acao.values()) {
                if (acao.equals(Acao.AUTENTICAR_USUARIO) || acao.equals(Acao.CADASTRAR_USUARIO)) {
                    continue;
                }
                System.out.println(acao.ordinal() + " - " + acao);
            }
            System.out.print("Escolha uma ação: ");
        }

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

        boolean valid = false;
        Conta.TipoConta tipoConta = null;
        while (!valid) {
            try {
                tipoConta = Conta.TipoConta.valueOf(scanner.nextLine().toUpperCase());
                valid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo de conta inválido. Tente novamente.");
            }
        }

        Conta conta = banco.cadastrarConta(usuarioLogado, tipoConta);

    }

    private static void consultarSaldo(Scanner scanner) {
        try {
            System.out.print("Digite o número da conta: ");
            String numeroConta = scanner.nextLine();

            BigDecimal saldo = contaService.consultarSaldo(usuarioLogado, numeroConta);
            System.out.println("Saldo atual: R$ " + saldo);
        } catch (Exception e) {
            System.out.println("Erro ao consultar saldo: " + e.getMessage());
        }
    }

    private static void depositar(Scanner scanner) {
        try {
            System.out.print("Digite o número da conta: ");
            String numeroConta = scanner.nextLine();
            System.out.print("Digite o valor do depósito: ");
            BigDecimal valor = scanner.nextBigDecimal();

            contaService.depositar(usuarioLogado, numeroConta, valor);
            System.out.println("Depósito realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao depositar: " + e.getMessage());
        }
    }

    private static void sacar(Scanner scanner) {
        try {
            System.out.print("Digite o número da conta: ");
            String numeroConta = scanner.nextLine();
            System.out.print("Digite o valor do saque: ");
            BigDecimal valor = scanner.nextBigDecimal();

            contaService.sacar(usuarioLogado, numeroConta, valor);
            System.out.println("Saque realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao sacar: " + e.getMessage());
        }
    }

    private static void transferir(Scanner scanner) {
        try {
            System.out.println("Contas do usuário " + usuarioLogado.getNome() + ":");
            for (Conta conta : usuarioLogado.getContas()) {
                System.out
                        .println(conta.getNumero() + " - " + conta.getTipoConta() + " - Saldo: R$ " + conta.getSaldo());
            }

            System.out.println("Contas alheias:");
            for (Usuario usuario : banco.getUsuarios().values()) {
                if (!usuario.equals(usuarioLogado)) {
                    for (Conta conta : usuario.getContas()) {
                        System.out.println(conta.getNumero() + " - " + usuario.getNome() + " - " + conta.getTipoConta()
                                + " - Saldo: R$ " + conta.getSaldo());
                    }
                }
            }

            System.out.print("Digite o número da conta de origem: ");
            String numeroContaOrigem = scanner.nextLine();
            System.out.print("Digite o número da conta de destino: ");
            String numeroContaDestino = scanner.nextLine();
            System.out.print("Digite o valor da transferência: ");
            BigDecimal valor = scanner.nextBigDecimal();

            contaService.transferir(usuarioLogado, numeroContaOrigem, numeroContaDestino, valor);
            System.out.println("Transferência realizada com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um valor numérico.");
            scanner.next(); // Clear the invalid input
        } catch (NoSuchElementException e) {
            System.out.println("Nenhuma entrada fornecida. Operação cancelada.");
        } catch (Exception e) {
            System.out.println("Erro na transferência: " + e.getMessage());
        }
    }

    /**
     * Popula a aplicacao com dados de teste
     */
    private static void popularDadosdeTeste() {

        Usuario usuario1 = banco.cadastrarUsuario("João Silva", "12345678900", "senha123");
        Usuario usuario2 = banco.cadastrarUsuario("Maria Oliveira", "98765432100", "senha456");

        Conta conta1 = banco.cadastrarConta(usuario1, Conta.TipoConta.CORRENTE);
        Conta conta2 = banco.cadastrarConta(usuario1, Conta.TipoConta.POUPANCA);
        Conta conta3 = banco.cadastrarConta(usuario2, Conta.TipoConta.CORRENTE);

        contaService.depositar(usuario1, conta1.getNumero(), new BigDecimal("1000.00"));
        contaService.depositar(usuario1, conta2.getNumero(), new BigDecimal("500.00"));
        contaService.depositar(usuario2, conta3.getNumero(), new BigDecimal("2000.00"));

        System.out.println("Dados de teste populados com sucesso!");

    }

    private static void imprimirRelatorio() {

        System.out.println("Relatório de Usuários e Contas:");
        for (Usuario usuario : banco.getUsuarios().values()) {
            System.out.println("Usuário: " + usuario.getNome() + " - CPF: " + usuario.getCpf());
            for (Conta conta : usuario.getContas()) {
                System.out.println("  Conta: " + conta.getNumero() + " - Tipo: " + conta.getTipoConta() + " - Saldo: R$ " + conta.getSaldo());
            }
        }

    }
}

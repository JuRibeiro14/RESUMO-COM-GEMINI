package view;

import service.ConsomeApi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static service.ConsomeApi.fazerPergunta;
import static service.ConsomeApi.formatarResposta;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Fazer uma Pergunta");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();


            switch (opcao) {
                case 1 -> {
                StringBuilder resumo = new StringBuilder("Resumo das respostas:\n\n");
                    for (int i = 1; i <= 3; i++) {
                    System.out.println("\nFaça a pergunta: ");
                    String pgt = scanner.nextLine();
                    String resposta = fazerPergunta(pgt);
                    resumo.append("Pergunta: ").append(pgt).append("\n").append("Resposta: ").append(formatarResposta(resposta)).append("\n\n");
                    }

                salvarResumoEmArquivo(resumo.toString());
                System.out.println("Resumo gerado e salvo em 'Respostas.txt'");
                }

                case 2 -> {
                    System.out.println("\nSaindo...");
                    return;
                }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }
    private static void salvarResumoEmArquivo(String resumo) throws IOException {
        try (FileWriter writer = new FileWriter("Respostas.txt")) {
            writer.write(resumo);
        }
    }
}


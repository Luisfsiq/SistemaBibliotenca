import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Biblioteca {

    private List<Livro> livros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private int proximoIdEmprestimo = 1;

    public static void main(String[] args) {
        Biblioteca sistema = new Biblioteca();
        sistema.inicializarDados();
        sistema.executar();
    }
    private void inicializarDados() {
        Autor autor1 = new Autor(1, "George R. R. Martin", LocalDate.of(1948, 9, 20));
        Autor autor2 = new Autor(2, "J.K. Rowling", LocalDate.of(1965, 7, 31));

        autores.add(autor1);
        autores.add(autor2);

        livros.add(new Livro(1, "Game of Thrones", autor1, true, LocalDate.now(), LocalDate.now()));
        livros.add(new Livro(2, "A Fúria dos Reis", autor1, true, LocalDate.now(), LocalDate.now()));
        livros.add(new Livro(3, "Harry Potter e a Pedra Filosofal", autor2, true, LocalDate.now(), LocalDate.now()));
    }
    private void executar() {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===== SISTEMA DE BIBLIOTECA =====");
            System.out.println("1 - Ver livros disponíveis e Emprestar");
            System.out.println("2 - Devolver um livro");
            System.out.println("3 - Histórico de Empréstimos");
            System.out.println("4 - Detalhes dos Autores");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    listarLivrosDisponiveis();
                    realizarEmprestimo(scanner);
                    break;
                case 2:
                    realizarDevolucao(scanner);
                    break;
                case 3:
                    listarHistoricoEmprestimos();
                    break;
                case 4:
                    listarAutores();
                    break;
                case 0:
                    System.out.println("Obrigado por visitar nossa biblioteca!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
    private void listarLivrosDisponiveis() {
        System.out.println("\n--- Livros Disponíveis ---");
        boolean temLivro = false;
        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                System.out.println("ID: " + livro.getId() + " | Título: " + livro.getTitulo() + " | Autor: " + livro.getAutor().getNome());
                temLivro = true;
            }
        }
        if (!temLivro) {
            System.out.println("Nenhum livro encontra-se disponível!");
        }
    }
    private void realizarEmprestimo(Scanner scanner) {
        System.out.print("\nQual livro você deseja realizar o empréstimo? (Digite o ID ou 0 para cancelar): ");
        int idLivroSolicitado = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        if (idLivroSolicitado == 0) return;

        Livro livroSolicitado = null;
        for (Livro livro : livros) {
            if (livro.getId() == idLivroSolicitado) {
                livroSolicitado = livro;
                break;
            }
        }
        if (livroSolicitado != null && livroSolicitado.isDisponivel()) {
            System.out.print("Digite o seu nome: ");
            String nomeCliente = scanner.nextLine();

            // Atualiza os dados do livro usando os Setters
            livroSolicitado.setDisponivel(false);
            livroSolicitado.setDataAtualizacao(LocalDate.now());

            // Cria o empréstimo
            Emprestimo novoEmprestimo = new Emprestimo(proximoIdEmprestimo++, livroSolicitado, nomeCliente, LocalDate.now());
            emprestimos.add(novoEmprestimo);

            System.out.println("✅ Sucesso! O livro '" + livroSolicitado.getTitulo() + "' foi emprestado para " + nomeCliente + ".");
        } else {
            System.out.println("❌ Erro: Livro não encontrado ou já está emprestado.");
        }
    }
    // NOVA FUNÇÃO: Devolução (Usa Setters do Empréstimo e do Livro)
    private void realizarDevolucao(Scanner scanner) {
        System.out.print("\nDigite o ID do livro que deseja devolver: ");
        int idLivro = scanner.nextInt();
        scanner.nextLine();

        boolean encontrou = false;
        for (Emprestimo emp : emprestimos) {
            // Busca um empréstimo ativo para aquele livro (onde dataDevolucao é null)
            if (emp.getLivro().getId() == idLivro && emp.getDataDevolucao() == null) {

                // Atualiza o Empréstimo
                emp.setDataDevolucao(LocalDate.now());

                // Atualiza o Livro para voltar à prateleira
                emp.getLivro().setDisponivel(true);
                emp.getLivro().setDataAtualizacao(LocalDate.now());

                System.out.println("✅ Livro '" + emp.getLivro().getTitulo() + "' devolvido com sucesso por " + emp.getNomeCliente() + "!");
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            System.out.println("❌ Nenhum empréstimo pendente encontrado para este ID de livro.");
        }
    }
    private void listarHistoricoEmprestimos() {
        System.out.println("\n--- Histórico de Empréstimos ---");
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo realizado ainda.");
            return;
        }

        for (Emprestimo emp : emprestimos) {
            String status = (emp.getDataDevolucao() == null) ? "Pendente" : "Devolvido em " + emp.getDataDevolucao();
            System.out.println("ID Empréstimo: " + emp.getId() +
                    " | Cliente: " + emp.getNomeCliente() +
                    " | Livro: " + emp.getLivro().getTitulo() +
                    " | Status: " + status);
        }
    }
    private void listarAutores() {
        System.out.println("\n--- Detalhes dos Autores ---");
        for (Autor autor : autores) {
            System.out.println("ID: " + autor.getId() +
                    " | Nome: " + autor.getNome() +
                    " | Nascido em: " + autor.getDataNascimento());
        }
    }
}
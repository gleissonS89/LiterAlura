package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DadosLivro;
import com.alura.literalura.model.DadosResultado;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConverteDados;
import com.alura.literalura.service.DadosLivroConverter;

import java.util.*;

public class Principal {

    private final Scanner leitor = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    private static final String ENDERECO = "https://gutendex.com/books";

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        int opcao = -1;
        while (opcao != 6) {
            System.out.println("""
                    ----------------
                    Escolha o número da sua opção:
                    1- Buscar livro pelo título
                    2- Listar livros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                    6- Sair
                    """);
            opcao = leitor.nextInt();
            leitor.nextLine(); // limpar buffer

            switch (opcao) {
                case 1: buscarLivroPeloTitulo();
                break;
                case 2: listarLivrosRegistrados();
                break;
                case 3: listarAutoresRegistrados();
                break;
                case 4: listarAutoresVivosEmAno();
                break;
                case 5: listarLivrosPorIdioma();
                break;
                case 6: System.out.println("Saindo...");
                default: System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.print("Insira o nome do livro que você deseja procurar: ");
        String titulo = leitor.nextLine();

        String url = ENDERECO + "?search=" + titulo.replace(" ", "+");
        System.out.println("Buscando em: " + url);

        String json = consumo.obterDados(url);
        if (json == null || json.isBlank()) {
            System.out.println("⚠️ A resposta da API veio vazia.");
            return;
        }

        try {
            DadosResultado resultado = conversor.obterDados(json, DadosResultado.class);

            if (resultado.getResults().isEmpty()) {
                System.out.println("⚠️ Nenhum livro encontrado com esse título.");
                return;
            }

            DadosLivro dadosLivro = resultado.getResults().get(0);
            List<Autor> autores = DadosLivroConverter.converterAutores(dadosLivro.getAuthors());
            String idioma = String.join(", ", dadosLivro.getLanguages());

            Livro livro = new Livro();
            livro.setTitulo(dadosLivro.getTitle());
            livro.setIdioma(dadosLivro.getLanguages().get(0));
            livro.setDownloads(dadosLivro.getDownloads());
            livro.setAutores(autores);
            livroRepository.save(livro);

            System.out.println("✅ Livro salvo com sucesso:");
            System.out.println(livro);

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        livros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("❌ Nenhum autor encontrado.");
            return;
        }

        for (Autor autor : autores) {
            System.out.println("📚 Autor: " + autor.getNome());
            System.out.println("   🗓️ Nascimento: " + (autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido"));
            System.out.println("   ☠️ Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));

            List<Livro> livros = autor.getLivros();

            if (livros == null || livros.isEmpty()) {
                System.out.println("   📕 Livros: Nenhum livro registrado.");
            } else {
                System.out.println("   📕 Livros:");
                for (Livro livro : livros) {
                    System.out.println("     - " + livro.getTitulo());
                }
            }

            System.out.println();
        }
    }

    private void listarAutoresVivosEmAno() {
        System.out.print("Digite o ano desejado: ");
        int ano = leitor.nextInt();
        leitor.nextLine();

        // Filtra e armazena autores vivos no ano informado
        List<Autor> autoresVivos = autorRepository.findAll().stream()
                .filter(a -> a.getAnoNascimento() != null && a.getAnoNascimento() <= ano &&
                        (a.getAnoFalecimento() == null || a.getAnoFalecimento() >= ano))
                .toList(); // ou .collect(Collectors.toList())

        // Verifica e exibe resultado
        if (autoresVivos.isEmpty()) {
            System.out.println("❌ Nenhum autor encontrado vivo no ano de " + ano + ".");
        } else {
            System.out.println("\n📅 Autores vivos no ano " + ano + ":\n");
            autoresVivos.forEach(autor -> {
                System.out.println("📖 Nome: " + autor.getNome());
                System.out.println("🗓️ Nascimento: " + autor.getAnoNascimento());
                System.out.println("☠️ Falecimento: " +
                        (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));
                System.out.println("------------------------------------");
            });
        }
    }

    private void listarLivrosPorIdioma() {
        Map<String, String> idiomasSuportados = Map.of(
                "pt", "português",
                "en", "inglês",
                "es", "espanhol",
                "fr", "francês",
                "de", "alemão",
                "it", "italiano"
        );

        List<String> idiomasNoBanco = livroRepository.findAll().stream()
                .map(Livro::getIdioma)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .filter(idiomasSuportados::containsKey)
                .toList();

        if (idiomasNoBanco.isEmpty()) {
            System.out.println("❌ Nenhum idioma conhecido encontrado nos livros.");
            return;
        }

        System.out.println("\n🌐 Idiomas disponíveis:");
        idiomasNoBanco.forEach(codigo ->
                System.out.println(" " + codigo + " - " + idiomasSuportados.get(codigo))
        );

        System.out.print("\nDigite o código do idioma desejado (ex: pt, en, es): ");
        String codigoSelecionado = leitor.nextLine().trim().toLowerCase();

        if (!idiomasNoBanco.contains(codigoSelecionado)) {
            System.out.println("❌ Idioma inválido ou não disponível.");
            return;
        }

        List<Livro> livros = livroRepository.findAll().stream()
                .filter(l -> codigoSelecionado.equalsIgnoreCase(l.getIdioma()))
                .toList();

        if (livros.isEmpty()) {
            System.out.println("❌ Nenhum livro encontrado para o idioma: " + codigoSelecionado);
            return;
        }

        System.out.println("\n📚 Livros em " + idiomasSuportados.get(codigoSelecionado) + ":\n");

        livros.forEach(System.out::println); // Usa o toString formatado de Livro
    }
}

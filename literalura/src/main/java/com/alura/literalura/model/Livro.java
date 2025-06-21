package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private Integer downloads;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    public Livro() {}

    public Livro(String titulo, String idioma, List<Autor> autores) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.autores = autores;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Integer geDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        String Autores = (autores != null)
                ? autores.stream()
                .map(Autor::getNome) // 👈 PEGA SÓ O NOME
                .collect(Collectors.joining(", "))
                : "Desconhecido";
        return "-----Livro-----"+ "\n"+
                "Título: " + titulo + "\n" +
                "Autor: " + Autores + "\n" +
                "Idioma: " + idioma + "\n" +
                "Numero de downloads: " + downloads + "\n"+
                "--------------------"+ "\n";

    }
}

package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DadosAutor;

import java.util.List;
import java.util.stream.Collectors;

public class DadosLivroConverter {

    public static List<Autor> converterAutores(List<DadosAutor> dadosAutores) {
        return dadosAutores.stream()
                .map(a -> new Autor(
                        a.getName(),
                        a.getBirth_year(),
                        a.getDeath_year()
                )).collect(Collectors.toList());
    }
}

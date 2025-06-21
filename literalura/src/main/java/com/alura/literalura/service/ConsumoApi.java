package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obterDados(String endereco) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println("Status code: " + response.statusCode());
            //System.out.println("Body length: " + (response.body() != null ? response.body().length() : "null"));
            //System.out.println("Body preview: " + (response.body() != null && response.body().length() > 200
                    //? response.body().substring(0, 200) + "..." : response.body()));

            if (response.statusCode() == 200 && response.body() != null && !response.body().isBlank()) {
                return response.body();
            } else {
                System.out.println("⚠️ Resposta inválida da API.");
                return null;
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao consumir API: " + e.getMessage(), e);
        }
    }
}

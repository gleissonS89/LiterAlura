package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosLivro {
    private int id;
    private String title;
    private List<String> languages;
    private List<DadosAutor> authors;
    private List<String> summaries;
    private List<Autor> translators;
    @JsonProperty("download_count")
    private Integer downloads;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<DadosAutor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<DadosAutor> authors) {
        this.authors = authors;
    }

    public List<String> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<String> summaries) {
        this.summaries = summaries;
    }

    public List<Autor> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Autor> translators) {
        this.translators = translators;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}

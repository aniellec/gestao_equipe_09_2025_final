//arquivo Projeto.java

package modelo;

import java.time.LocalDate;

public class Projeto {

    private int id;
    private String nome;
    private String descricao;
    private LocalDate inicio;
    private LocalDate fim;
    private int idGerente; // ID do gerente responsável
    private String status;

    public Projeto(String nome, String descricao, LocalDate inicio, LocalDate fim, int idGerente, String status) {
        this.nome = nome;
        this.descricao = descricao;
        this.inicio = inicio;
        this.fim = fim;
        this.idGerente = idGerente;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public LocalDate getInicio() { return inicio; }
    public LocalDate getFim() { return fim; }
    public int getIdGerente() { return idGerente; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public void setFim(LocalDate fim) { this.fim = fim; }
    public void setIdGerente(int idGerente) { this.idGerente = idGerente; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Status: " + status + " | Gerente: " + idGerente;
    }
}

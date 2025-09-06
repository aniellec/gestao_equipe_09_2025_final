//arquivo Tarefa.java

package modelo;

// Título, descrição, projeto vinculado, responsável (usuário),
// status (pendente, em execução, concluída), data de início e fim previstas e reais.
// Cada tarefa pertence a um único projeto.

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Tarefa {
    private int idTarefa;
    private int idProjeto;      // chave estrangeira
    private String titulo;
    private String descricao;
    private String status;
    private int responsavel;    // id do usuário
    private LocalDateTime criadoEm;

    // Getters e Setters
    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(int responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return "Tarefa ID: " + idTarefa +
                " | Projeto: " + idProjeto +
                " | Título: " + titulo +
                " | Status: " + status +
                " | Responsável: " + responsavel +
                " | Criado em: " + criadoEm;
    }
}

//arquivo Equipe.java

package modelo;
import swing.EquipeSwing;

// Nome da equipe, descrição, membros (usuários vinculados).
// Uma equipe pode atuar em vários projetos.


import java.util.List;

public class Equipe {
    private int idEquipe;
    private String nome;
    private String descricao;
    private List<Usuario> membros;  // lista de usuários da equipe
    private List<Projeto> projetos; // lista de projetos em que a equipe atua

    // Construtor
    public Equipe(int idEquipe, String nome, String descricao, List<Usuario> membros, List<Projeto> projetos) {
        this.idEquipe = idEquipe;
        this.nome = nome;
        this.descricao = descricao;
        this.membros = membros;
        this.projetos = projetos;
    }

    public Equipe(String nome, String descricao, List<Usuario> membros, List<Projeto> projetos) {
        this.nome = nome;
        this.descricao = descricao;
        this.membros = membros;
        this.projetos = projetos;
    }

    public Equipe() {
        // construtor vazio, usado quando ainda não temos dados
    }

    // Getters e Setters
    public int getIdEquipe() { return idEquipe; }
    public void setIdEquipe(int idEquipe) { this.idEquipe = idEquipe; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Usuario> getMembros() { return membros; }
    public void setMembros(List<Usuario> membros) { this.membros = membros; }

    public List<Projeto> getProjetos() { return projetos; }
    public void setProjetos(List<Projeto> projetos) { this.projetos = projetos; }

    @Override
    public String toString() {
        return "Equipe: " + nome + " | Membros: " + membros.size() + " | Projetos: " + projetos.size();
    }
}

//arquivo Usuario.java

package modelo;
// Nome completo, CPF, e-mail, cargo, login, senha.
// perfil: administrador, gerente ou colaborador.



public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private String login;
    private String senha;
    private String perfil;

    // Construtor completo (sem ID)
    public Usuario(String nome, String cpf, String email, String cargo, String login, String senha, String perfil) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Construtor com ID
    public Usuario(int id, String nome, String cpf, String email, String cargo, String login, String senha, String perfil) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }


    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | CPF: %s | Cargo: %s | Perfil: %s",
                id, nome, cpf, cargo, perfil);
    }


}

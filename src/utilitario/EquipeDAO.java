package utilitario;

import modelo.Equipe;
import modelo.Usuario;
import modelo.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import utilitario.ConexaoDB;


public class EquipeDAO {

    // Salvar equipe com membros e projetos
    public void salvarEquipe(Equipe equipe) throws SQLException {
        try (Connection conn = ConexaoDB.getConnection()) {
            conn.setAutoCommit(false); // transação

            try {
                // 1. Inserir equipe
                String sqlEquipe = "INSERT INTO Equipe (nome, descricao) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlEquipe, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, equipe.getNome());
                    stmt.setString(2, equipe.getDescricao());
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        equipe.setIdEquipe(rs.getInt(1));
                    }
                }

                // 2. Inserir membros
                String sqlMembros = "INSERT INTO Equipe_usuario (id_equipe, id_usuario) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlMembros)) {
                    for (Usuario u : equipe.getMembros()) {
                        stmt.setInt(1, equipe.getIdEquipe());
                        stmt.setInt(2, u.getId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                // 3. Inserir projetos
                String sqlProjetos = "INSERT INTO Equipe_projeto (id_equipe, id_projeto) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlProjetos)) {
                    for (Projeto p : equipe.getProjetos()) {
                        stmt.setInt(1, equipe.getIdEquipe());
                        stmt.setInt(2, p.getId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                conn.commit(); // sucesso
            } catch (Exception e) {
                conn.rollback(); // erro → desfaz
                throw e;
            }
        }
    }

    // Listar equipes
    public List<Equipe> listarEquipes() throws SQLException {
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Equipe")) {

            while (rs.next()) {
                int id = rs.getInt("id_equipe");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");

                List<Usuario> membros = getMembros(conn, id);
                List<Projeto> projetos = getProjetos(conn, id);

                equipes.add(new Equipe(id, nome, descricao, membros, projetos));
            }
        }
        return equipes;
    }

    // Buscar membros de uma equipe
    private List<Usuario> getMembros(Connection conn, int idEquipe) throws SQLException {
        List<Usuario> membros = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nome FROM Usuario u " +
                "JOIN Equipe_usuario eu ON u.id_usuario = eu.id_usuario " +
                "WHERE eu.id_equipe = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                membros.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nome")));
            }
        }
        return membros;
    }

    // Buscar projetos de uma equipe
    private List<Projeto> getProjetos(Connection conn, int idEquipe) throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.id_projeto, p.nome FROM Projeto p " +
                "JOIN Equipe_projeto ep ON p.id_projeto = ep.id_projeto " +
                "WHERE ep.id_equipe = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                projetos.add(new Projeto(rs.getInt("id_projeto"), rs.getString("nome")));
            }
        }
        return projetos;
    }

    public void deletarEquipe(int idEquipe) throws SQLException {
        String sql = "DELETE FROM equipe WHERE idEquipe = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            stmt.executeUpdate();
        }
    }

    // Atualizar equipe (nome, desc, membros, projetos)
    public void atualizarEquipe(Equipe equipe) throws SQLException {
        try (Connection conn = ConexaoDB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Atualizar nome e descrição
                String sqlEquipe = "UPDATE Equipe SET nome = ?, descricao = ? WHERE id_equipe = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlEquipe)) {
                    stmt.setString(1, equipe.getNome());
                    stmt.setString(2, equipe.getDescricao());
                    stmt.setInt(3, equipe.getIdEquipe());
                    stmt.executeUpdate();
                }

                // Atualizar membros
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Equipe_usuario WHERE id_equipe = ?")) {
                    stmt.setInt(1, equipe.getIdEquipe());
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Equipe_usuario (id_equipe, id_usuario) VALUES (?, ?)")) {
                    for (Usuario u : equipe.getMembros()) {
                        stmt.setInt(1, equipe.getIdEquipe());
                        stmt.setInt(2, u.getId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                // Atualizar projetos
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Equipe_projeto WHERE id_equipe = ?")) {
                    stmt.setInt(1, equipe.getIdEquipe());
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Equipe_projeto (id_equipe, id_projeto) VALUES (?, ?)")) {
                    for (Projeto p : equipe.getProjetos()) {
                        stmt.setInt(1, equipe.getIdEquipe());
                        stmt.setInt(2, p.getId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}

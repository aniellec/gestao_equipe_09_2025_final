//arquivo ProjetoDAO.java

package utilitario;

import modelo.Projeto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    // Adicionar projeto
    public boolean adicionar(Projeto projeto) {
        if (!gerenteExiste(projeto.getIdGerente())) {
            System.out.println("Erro: gerente com ID " + projeto.getIdGerente() + " não existe.");
            return false;
        }

        String sql = "INSERT INTO Projeto (nome, descricao, inicio, fim, id_gerente, status) VALUES (?, ?, ?, ?, ?, ?)";
       //String sql = "INSERT INTO Projeto (nome, descricao, inicio, fim, id_gerente, status, criado_em) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, projeto.getInicio() != null ? Date.valueOf(projeto.getInicio()) : null);
            stmt.setDate(4, projeto.getFim() != null ? Date.valueOf(projeto.getFim()) : null);
            stmt.setInt(5, projeto.getIdGerente());
            stmt.setString(6, projeto.getStatus());

            //Timestamp now = new Timestamp(System.currentTimeMillis());
            //stmt.setTimestamp(6, now); // criado_em
            //stmt.setTimestamp(8, now); // atualizado_em
            //stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            //stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean gerenteExiste(int idGerente) {
        String sql = "SELECT 1 FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGerente);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // retorna true se encontrou o usuário

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos os projetos
    public List<Projeto> listar() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM Projeto";

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Projeto p = new Projeto(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDate("inicio") != null ? rs.getDate("inicio").toLocalDate() : null,
                        rs.getDate("fim") != null ? rs.getDate("fim").toLocalDate() : null,
                        rs.getInt("id_gerente"),
                        rs.getString("status")
                );
                p.setId(rs.getInt("id_projeto"));
                projetos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projetos;
    }

    // Deletar projeto por ID
    public boolean deletar(int id) {
        String sql = "DELETE FROM Projeto WHERE id_projeto = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Dentro de ProjetoDAO
    public Projeto buscarPorId(int id) {
        String sql = "SELECT * FROM Projeto WHERE id_projeto = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Projeto p = new Projeto(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDate("inicio") != null ? rs.getDate("inicio").toLocalDate() : null,
                        rs.getDate("fim") != null ? rs.getDate("fim").toLocalDate() : null,
                        rs.getInt("id_gerente"),
                        rs.getString("status")
                );
                p.setId(rs.getInt("id_projeto"));
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // não encontrado
    }

    // Dentro de ProjetoDAO
    public boolean atualizar(Projeto projeto) {
        //String sql = "UPDATE Projeto SET nome = ?, descricao = ?, inicio = ?, fim = ?, id_gerente = ?, status = ?" +
                //"WHERE id_projeto = ?";
        //String sql = "INSERT INTO Projeto (nome, descricao, inicio, fim, id_gerente, status) VALUES (?, ?, ?, ?, ?, ?)";
        //String sql = "UPDATE INTO Projeto (nome, descricao, inicio, fim, id_gerente, status, criado_em) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "UPDATE Projeto SET nome = ?, descricao = ?, inicio = ?, fim = ?, id_gerente = ?, status = ? WHERE id_projeto = ?";


        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, projeto.getInicio() != null ? Date.valueOf(projeto.getInicio()) : null);
            stmt.setDate(4, projeto.getFim() != null ? Date.valueOf(projeto.getFim()) : null);
            stmt.setInt(5, projeto.getIdGerente());
            stmt.setString(6, projeto.getStatus());
            //stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(7, projeto.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Projeto> listarProjetos() throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT id_projeto, nome, inicio, fim, status, id_gerente FROM Projeto";

        //   referencia:     String sql = "UPDATE Projeto SET nome = ?, descricao = ?, inicio = ?, fim = ?, id_gerente = ?, status = ? WHERE id_projeto = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Projeto projeto = new Projeto(
                        rs.getInt("id_projeto"),
                        rs.getString("nome"),
                        rs.getDate("inicio").toLocalDate(),
                        rs.getDate("fim").toLocalDate(),
                        rs.getInt("id_gerente"),
                        rs.getString("status")
                );
                projetos.add(projeto);
            }
        }
        return projetos;
    }

    public List<Projeto> listarProjetosPorUsuario(int idUsuario) throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.* FROM Projeto p " +
                "JOIN Equipe_projeto ep ON p.id_projeto = ep.id_projeto " +
                "JOIN Equipe_usuario eu ON ep.id_equipe = eu.id_equipe " +
                "WHERE eu.id_usuario = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_projeto");
                    String nome = rs.getString("nome");
                    String status = rs.getString("status");
                    projetos.add(new Projeto(id, nome, status));
                }
            }
        }
        return projetos;
    }



}
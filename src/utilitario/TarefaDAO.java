//arquivo TarefaDAO.java
//
package utilitario;

import modelo.Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public void inserir(Tarefa tarefa) {
        String sql = "INSERT INTO Tarefa (id_projeto, titulo, descricao, status, responsavel) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tarefa.getIdProjeto());
            stmt.setString(2, tarefa.getTitulo());
            stmt.setString(3, tarefa.getDescricao());
            stmt.setString(4, tarefa.getStatus());
            stmt.setInt(5, tarefa.getResponsavel());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tarefa> listarPorProjeto(int idProjeto) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM Tarefa WHERE id_projeto = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProjeto);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tarefa t = new Tarefa();
                t.setIdTarefa(rs.getInt("id_tarefa"));
                t.setIdProjeto(rs.getInt("id_projeto"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescricao(rs.getString("descricao"));
                t.setStatus(rs.getString("status"));
                t.setResponsavel(rs.getInt("responsavel"));
                t.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());
                tarefas.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarefas;
    }

    public void atualizar(Tarefa tarefa) {
        String sql = "UPDATE Tarefa SET titulo=?, descricao=?, status=?, responsavel=? WHERE id_tarefa=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus());
            stmt.setInt(4, tarefa.getResponsavel());
            stmt.setInt(5, tarefa.getIdTarefa());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int idTarefa) {
        String sql = "DELETE FROM Tarefa WHERE id_tarefa=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

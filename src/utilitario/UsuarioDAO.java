//arquivo UsuarioDAO.java

package utilitario;

import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Adicionar novo usuário
    public boolean adicionar(Usuario u) {
        String sql = "INSERT INTO Usuario (nome, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getCpf());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getCargo());
            stmt.setString(5, u.getLogin());
            stmt.setString(6, u.getSenha()); // ideal: usar hash
            stmt.setString(7, u.getPerfil());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos os usuários
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try (Connection conn = ConexaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("cargo"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Deletar usuário pelo ID
    public boolean deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
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

    // Autenticar usuário (login e senha)
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM Usuario WHERE login = ? AND senha = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("cargo"),
                            rs.getString("login"),
                            rs.getString("senha"),
                            rs.getString("perfil")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login ou senha inválidos
    }
}

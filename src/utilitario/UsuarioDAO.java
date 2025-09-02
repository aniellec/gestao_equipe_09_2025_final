//arquivo UsuarioDAO.java

package utilitario;

import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    //private static final String URL = "jdbc:mysql://sql.freedb.com:3306/freedb_aniellecasagrande";
    //private static final String USER = "freedb_aniellecasagrande";
    //private static final String PASSWORD = "ST5zkvjBAk%2cf4";

    private static final String URL = "jdbc:mysql://docequotidiano.com.br:3306/doceq065_gestao_projetos_0825";
    private static final String USER = "doceq065_gestao_projetos_0825";
    private static final String PASSWORD = "gestao_projetos_0825";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void adicionar(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, 'Estudante', ?, 'senha123', 'COLABORADOR')";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getCpf());
            pst.setString(3, usuario.getEmail());
            pst.setString(4, usuario.getNome().toLowerCase());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

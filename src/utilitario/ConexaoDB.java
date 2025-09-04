package utilitario;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {


    private static Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream("src/config.properties")) {

            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler config.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String host = props.getProperty("DB_HOST");
        String port = props.getProperty("DB_PORT");
        String dbName = props.getProperty("DB_NAME");
        String user = props.getProperty("DB_USER");
        String pass = props.getProperty("DB_PASS");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        return DriverManager.getConnection(url, user, pass);
    }

    // Apenas para testar, opcional
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

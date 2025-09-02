import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {
    public static void main(String[] args) {
        Properties props = new Properties();

        // Lê o arquivo do classpath
        try (InputStream input = ConexaoDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Arquivo config.properties não encontrado!");
                return;
            }

            props.load(input);

            String host = props.getProperty("DB_HOST");
            String port = props.getProperty("DB_PORT");
            String dbName = props.getProperty("DB_NAME");
            String user = props.getProperty("DB_USER");
            String pass = props.getProperty("DB_PASS");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexão bem-sucedida!");
            conn.close();

        } catch (IOException e) {
            System.out.println("Erro ao ler config.properties");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco");
            e.printStackTrace();
        }
    }
}

package swing;

import utilitario.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginSwing {

    private UsuarioDAO dao = new UsuarioDAO();
    private JTextField loginField;
    private JPasswordField senhaField;

    public void iniciar() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // centraliza a janela

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Login:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        JButton loginButton = new JButton("Entrar");
        loginButton.addActionListener(e -> autenticarUsuario());
        panel.add(new JLabel()); // célula vazia
        panel.add(loginButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void autenticarUsuario() {
        String loginDigitado = loginField.getText();
        String senhaDigitada = new String(senhaField.getPassword());

        Usuario usuarioLogado = dao.autenticar(loginDigitado, senhaDigitada);

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(null, "Bem-vindo, " + usuarioLogado.getNome() + "!");

            // Direciona conforme perfil
            switch (usuarioLogado.getPerfil()) {
                case "GERENTE":
                    new GerenteSwing().iniciar();
                    break;
                case "ADMIN":
                    // Aqui você pode chamar uma tela administrativa
                    JOptionPane.showMessageDialog(null, "Tela de ADMIN ainda não implementada");
                    break;
                case "COLABORADOR":
                    // Aqui você pode chamar uma tela de colaborador
                    JOptionPane.showMessageDialog(null, "Tela de COLABORADOR ainda não implementada");
                    break;
            }

            // Fecha a tela de login
            SwingUtilities.getWindowAncestor(loginField).dispose();

        } else {
            JOptionPane.showMessageDialog(null, "Login ou senha incorretos!");
            senhaField.setText("");
        }
    }
}

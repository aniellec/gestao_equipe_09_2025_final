//arquivo UsuarioSwing.java

package swing;

import utilitario.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;


public class UsuarioSwing {

    private UsuarioDAO dao = new UsuarioDAO();
    private JTextArea textArea;
    private JTextField nomeField, cpfField, emailField, cargoField, loginField;
    private JPasswordField senhaField;
    private JComboBox<String> perfilBox;

    public void iniciar() {
        JFrame frame = new JFrame("Sistema de projetos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Área de texto com padding
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de inputs
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        nomeField = new JTextField();
        cpfField = new JTextField();
        emailField = new JTextField();
        cargoField = new JTextField();
        loginField = new JTextField();
        senhaField = new JPasswordField();
        perfilBox = new JComboBox<>(new String[]{"ADMIN", "GERENTE", "COLABORADOR"});

        inputPanel.add(new JLabel("Nome completo:")); inputPanel.add(nomeField);
        inputPanel.add(new JLabel("CPF:"));           inputPanel.add(cpfField);
        inputPanel.add(new JLabel("E-mail:"));        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Cargo:"));         inputPanel.add(cargoField);
        inputPanel.add(new JLabel("Login:"));         inputPanel.add(loginField);
        inputPanel.add(new JLabel("Senha:"));         inputPanel.add(senhaField);
        inputPanel.add(new JLabel("Perfil:"));        inputPanel.add(perfilBox);

        JButton addButton = new JButton("Adicionar");
        addButton.setMargin(new Insets(10, 20, 10, 20));
        addButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String email = emailField.getText();
            String cargo = cargoField.getText();
            String login = loginField.getText();
            String senha = new String(senhaField.getPassword());
            String perfil = (String) perfilBox.getSelectedItem();

            Usuario usuario = new Usuario(nome, cpf, email, cargo, login, senha, perfil);

            dao.adicionar(usuario);

            atualizarLista();
        });





        JButton loginButton = new JButton("Login");
        loginButton.setMargin(new Insets(10, 20, 10, 20));
        loginButton.addActionListener(e -> {
            // Abre a tela de login
            new LoginSwing().iniciar();
        });
        inputPanel.add(loginButton);
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        atualizarLista();
    }

    private void atualizarLista() {
        textArea.setText("");
        for (Usuario u : dao.listar()) {
            textArea.append(u + "\n");
        }
        nomeField.setText("");
        cpfField.setText("");
        emailField.setText("");
        cargoField.setText("");
        loginField.setText("");
        senhaField.setText("");
        perfilBox.setSelectedIndex(0);
    }


}

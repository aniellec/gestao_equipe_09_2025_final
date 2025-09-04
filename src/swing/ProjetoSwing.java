// arquivo ProjetoSwing.java

package swing;

import utilitario.ProjetoDAO;
import modelo.Projeto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ProjetoSwing {

    private ProjetoDAO dao = new ProjetoDAO();
    private JTextArea textArea;
    private JTextField nomeField, descricaoField, inicioField, fimField, gerenteField;
    private JComboBox<String> statusBox;

    public void iniciar() {
        JFrame frame = new JFrame("Gestão de Projetos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Área de texto para listar projetos
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de inputs
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        nomeField = new JTextField();
        descricaoField = new JTextField();
        inicioField = new JTextField(); // formato yyyy-MM-dd
        fimField = new JTextField();    // formato yyyy-MM-dd
        gerenteField = new JTextField();
        statusBox = new JComboBox<>(new String[]{"Planejado", "Em Andamento", "Concluído", "Cancelado"});

        inputPanel.add(new JLabel("Nome:"));        inputPanel.add(nomeField);
        inputPanel.add(new JLabel("Descrição:"));   inputPanel.add(descricaoField);
        inputPanel.add(new JLabel("Início (yyyy-mm-dd):"));      inputPanel.add(inicioField);
        inputPanel.add(new JLabel("Fim (yyyy-mm-dd):"));         inputPanel.add(fimField);
        inputPanel.add(new JLabel("Gerente (ID):"));inputPanel.add(gerenteField);
        inputPanel.add(new JLabel("Status:"));      inputPanel.add(statusBox);

        // Botão adicionar
        JButton addButton = new JButton("Adicionar");
        addButton.setMargin(new Insets(10, 20, 10, 20));
        addButton.addActionListener(e -> {
            try {
                String nome = nomeField.getText();
                String descricao = descricaoField.getText();

                LocalDate inicio = inicioField.getText().isEmpty() ? null : LocalDate.parse(inicioField.getText());
                LocalDate fim = fimField.getText().isEmpty() ? null : LocalDate.parse(fimField.getText());

                //String id_gerente = gerenteField.getText();
                int idGerente = Integer.parseInt(gerenteField.getText());
                //String status = (String) statusBox.getSelectedItem();
                String status = (statusBox.getSelectedItem() != null)
                        ? statusBox.getSelectedItem().toString().trim()
                        : "Planejado";

                //Projeto projeto = new Projeto(nome, descricao, inicio, fim, null, status, gerente);
                Projeto projeto = new Projeto(nome, descricao, inicio, fim, idGerente, status);

                if (!dao.adicionar(projeto)) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar projeto!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
            atualizarLista();
        });
        inputPanel.add(addButton);

        // Botão deletar
        JButton deleteButton = new JButton("Deletar por ID");
        deleteButton.setMargin(new Insets(10, 20, 10, 20));
        deleteButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Digite o ID do projeto:");
            if (input != null && !input.isEmpty()) {
                try {
                    int id = Integer.parseInt(input);
                    if (!dao.deletar(id)) {
                        JOptionPane.showMessageDialog(null, "ID não encontrado!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID inválido!");
                }
            }
            atualizarLista();
        });
        inputPanel.add(deleteButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        atualizarLista();
    }

    private void atualizarLista() {
        textArea.setText("");
        for (Projeto p : dao.listar()) {
            textArea.append(p + "\n");
        }
        nomeField.setText("");
        descricaoField.setText("");
        inicioField.setText("");
        fimField.setText("");
        gerenteField.setText("");
        statusBox.setSelectedIndex(0);
    }


}

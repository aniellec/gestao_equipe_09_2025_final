package swing;

import utilitario.ProjetoDAO;
import modelo.Projeto;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class GerenteSwing {

    private ProjetoDAO dao = new ProjetoDAO();
    private JTextArea textArea;
    private JTextField idField;

    public void iniciar() {
        JFrame frame = new JFrame("Área do Gerente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panelBotoes = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 botões
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Botão 1: Listar Usuários
        JButton listarUsuarios = new JButton("Listar Usuários");
        listarUsuarios.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Função listar usuários aqui!");
        });
        panelBotoes.add(listarUsuarios);

        JButton novoProjeto = new JButton("Novo Projeto");
        novoProjeto.addActionListener(e -> {
            // Abre a tela de cadastro de projetos
            new ProjetoSwing().iniciar();
        });
        panelBotoes.add(novoProjeto);


        // Botão 4: Listar Projetos
        JButton listarProjetos = new JButton("Listar Projetos");
        listarProjetos.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Função listar projetos aqui!");
        });
        panelBotoes.add(listarProjetos);

        frame.add(panelBotoes, BorderLayout.CENTER);
        frame.setVisible(true);

        // Lista de projetos
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de ações
        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        idField = new JTextField();

        actionPanel.add(new JLabel("ID do Projeto:"));
        actionPanel.add(idField);

        JButton atualizarButton = new JButton("Atualizar Status");
        atualizarButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Projeto projeto = dao.buscarPorId(id);
                if (projeto != null) {
                    String[] opcoes = {"Planejado", "Em Andamento", "Concluído", "Cancelado"};
                    String novoStatus = (String) JOptionPane.showInputDialog(
                            frame,
                            "Escolha o novo status:",
                            "Atualizar Status",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            opcoes,
                            projeto.getStatus()
                    );
                    if (novoStatus != null) {
                        projeto.setStatus(novoStatus);
                        dao.atualizar(projeto);
                        atualizarLista();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Projeto não encontrado!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        });
        actionPanel.add(atualizarButton);



        frame.add(actionPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        atualizarLista();
    }

    private void atualizarLista() {
        textArea.setText("");
        for (Projeto p : dao.listar()) {
            textArea.append(p + "\n");
        }
        idField.setText("");
    }

    private void abrirTelaNovoProjeto() {
        // Aqui você pode abrir uma nova tela para criar projetos
        JOptionPane.showMessageDialog(null, "Abrir tela de criação de projeto");
        // ou criar um JFrame/JDialog específico para cadastrar projeto
    }
}

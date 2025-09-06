//arquivo TarefaSwing.java
//
package swing;

import modelo.Tarefa;
import utilitario.TarefaDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TarefaSwing {
    private int idProjeto;
    private TarefaDAO dao = new TarefaDAO();
    private JPanel listaPanel;

    public TarefaSwing(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public void iniciar() {
        JFrame frame = new JFrame("Gerenciar Tarefas - Projeto " + idProjeto);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaPanel);

        JButton novaTarefaBtn = new JButton("Nova Tarefa");
        novaTarefaBtn.addActionListener(e -> abrirFormulario());

        frame.add(novaTarefaBtn, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        atualizarLista();
    }

    //nova tarefa
    private void abrirFormulario() {
        JTextField nomeField = new JTextField();
        JTextField descricaoField = new JTextField();
        JTextField responsavelField = new JTextField();
        String[] statusOptions = {"PENDENTE", "EM_ANDAMENTO", "CONCLUIDA"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Descrição:"));
        panel.add(descricaoField);
        //panel.add(new JLabel("Prazo (AAAA-MM-DD):"));
        //panel.add(prazoField);
        panel.add(new JLabel("Responsável (ID):"));
        panel.add(responsavelField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Nova Tarefa", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Tarefa t = new Tarefa();
                t.setIdProjeto(idProjeto);
                t.setTitulo(nomeField.getText());
                t.setDescricao(descricaoField.getText());
                t.setResponsavel(Integer.parseInt(responsavelField.getText()));
                t.setStatus((String) statusCombo.getSelectedItem());
                dao.inserir(t);
                atualizarLista();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
        }
    }

    private void atualizarLista() {
        listaPanel.removeAll();
        for (Tarefa t : dao.listarPorProjeto(idProjeto)) {
            JPanel tarefaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(t.toString());

            JButton atualizarBtn = new JButton("Atualizar");
            atualizarBtn.addActionListener(e -> {
                String[] statusOptions = {"PENDENTE", "EM_ANDAMENTO", "CONCLUIDA"};
                String novoStatus = (String) JOptionPane.showInputDialog(
                        listaPanel,
                        "Atualizar status da tarefa:",
                        "Status",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        statusOptions,
                        t.getStatus()
                );
                if (novoStatus != null) {
                    t.setStatus(novoStatus);
                    dao.atualizar(t);
                    atualizarLista();
                }
            });

            JButton deletarBtn = new JButton("Deletar");
            deletarBtn.addActionListener(e -> {
                dao.deletar(t.getIdTarefa());
                atualizarLista();
            });

            tarefaPanel.add(label);
            tarefaPanel.add(atualizarBtn);
            tarefaPanel.add(deletarBtn);
            listaPanel.add(tarefaPanel);
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }
}

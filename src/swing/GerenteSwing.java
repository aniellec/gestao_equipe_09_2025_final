// arquivo GerenteSwing.java

package swing;
import java.util.List;
import java.util.ArrayList;
import modelo.Usuario;
import utilitario.ProjetoDAO;
import utilitario.UsuarioDAO;
import utilitario.EquipeDAO;
import modelo.Projeto;
import modelo.Equipe;


import javax.swing.*;
import java.awt.*;

public class GerenteSwing {

    private ProjetoDAO dao = new ProjetoDAO();
    private JPanel listaProjetosPanel; // substitui o JTextArea

    public void iniciar() {
        JFrame frame = new JFrame("Área do Gerente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Painel lateral de botões
        JPanel panelBotoes = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton listarUsuarios = new JButton("Usuários");
        listarUsuarios.addActionListener(e -> {
            //JOptionPane.showMessageDialog(null, "Função listar usuários aqui!");
        });
        panelBotoes.add(listarUsuarios);


        //JButton btnEquipes = new JButton("Equipes");
        //btnEquipes.addActionListener(e -> {
            //new EquipeSwing().iniciar(); // chama o metodo sem argumentos
        //});
        //panelBotoes.add(btnEquipes);

        JButton listarEquipes = new JButton("Equipes");
        listarEquipes.addActionListener(e -> {
            try {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                ProjetoDAO projetoDAO = new ProjetoDAO();

                List<Usuario> usuarios = usuarioDAO.listar();
                List<Projeto> projetos = projetoDAO.listarProjetos();

                new EquipeSwing().iniciar(usuarios, projetos);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao carregar dados: " + ex.getMessage());
            }
        });
        panelBotoes.add(listarEquipes);



        JButton novoProjeto = new JButton("Novo Projeto");
        novoProjeto.addActionListener(e -> {
            new ProjetoSwing().iniciar();
        });
        panelBotoes.add(novoProjeto);

        frame.add(panelBotoes, BorderLayout.WEST);

        // Painel central para lista de projetos
        listaProjetosPanel = new JPanel();
        listaProjetosPanel.setLayout(new BoxLayout(listaProjetosPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaProjetosPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        atualizarLista();
    }

    private void atualizarLista() {
        listaProjetosPanel.removeAll();



        for (Projeto p : dao.listar()) {
            JPanel projetoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            projetoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel label = new JLabel(
                    "ID: " + p.getId() +
                            " | Nome: " + p.getNome() +
                            " | Status: " + p.getStatus()
            );


            // Botão atualizar
            JButton atualizarButton = new JButton("Atualizar");

            atualizarButton.addActionListener(e -> {
                String[] opcoes = {"Planejado", "Em Andamento", "Concluído", "Cancelado"};
                String novoStatus = (String) JOptionPane.showInputDialog(
                        listaProjetosPanel,
                        "Escolha o novo status:",
                        "Atualizar Status",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoes,
                        p.getStatus()
                );
                if (novoStatus != null) {
                    p.setStatus(novoStatus);
                    dao.atualizar(p);
                    atualizarLista();
                }
            });

            // Botão deletar
            JButton deletarButton = new JButton("Deletar");
            deletarButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        listaProjetosPanel,
                        "Tem certeza que deseja deletar o projeto \"" + p.getNome() + "\"?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deletar(p.getId()); // precisa ter esse metodo no ProjetoDAO
                    atualizarLista();
                }
            });

            JButton tarefasButton = new JButton("Gerenciar Tarefas");
            tarefasButton.addActionListener(e -> {
                new TarefaSwing(p.getId()).iniciar();
            });


            projetoPanel.add(label);

            projetoPanel.add(tarefasButton);
            projetoPanel.add(atualizarButton);
            projetoPanel.add(deletarButton);

            listaProjetosPanel.add(projetoPanel);
        }

        listaProjetosPanel.revalidate();
        listaProjetosPanel.repaint();
    }
}

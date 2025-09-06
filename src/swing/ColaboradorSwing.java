package swing;

import modelo.Usuario;
import modelo.Equipe;
import modelo.Projeto;
import utilitario.EquipeDAO;
import utilitario.ProjetoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ColaboradorSwing {

    private JFrame frame;
    private Usuario usuario; // o colaborador logado

    public ColaboradorSwing(Usuario usuario) {
        this.usuario = usuario;
    }

    public void iniciar() {
        frame = new JFrame("Perfil do Colaborador: " + usuario.getNome());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(painelPrincipal);
        frame.add(scroll, BorderLayout.CENTER);

        // Seção Equipes
        JLabel labelEquipes = new JLabel("Equipes:");
        labelEquipes.setFont(new Font("Arial", Font.BOLD, 16));
        painelPrincipal.add(labelEquipes);

        try {
            EquipeDAO equipeDAO = new EquipeDAO();
            List<Equipe> equipes = equipeDAO.listarEquipesPorUsuario(usuario.getId());

            if (equipes.isEmpty()) {
                painelPrincipal.add(new JLabel("Você não participa de nenhuma equipe."));
            } else {
                for (Equipe e : equipes) {
                    JLabel equipeLabel = new JLabel("- " + e.getNome() + " | " + e.getDescricao());
                    painelPrincipal.add(equipeLabel);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao carregar equipes: " + ex.getMessage());
        }

        // Espaço entre seções
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // Seção Projetos
        JLabel labelProjetos = new JLabel("Projetos:");
        labelProjetos.setFont(new Font("Arial", Font.BOLD, 16));
        painelPrincipal.add(labelProjetos);

        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            List<Projeto> projetos = projetoDAO.listarProjetosPorUsuario(usuario.getId());

            if (projetos.isEmpty()) {
                painelPrincipal.add(new JLabel("Você não participa de nenhum projeto."));
            } else {
                for (Projeto p : projetos) {
                    JLabel projetoLabel = new JLabel("- " + p.getNome() + " | Status: " + p.getStatus());
                    projetoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    painelPrincipal.add(projetoLabel);

                    // Lista de tarefas do projeto
                    List<String> tarefas = projetoDAO.listarTarefasPorProjeto(p.getId());
                    if (tarefas.isEmpty()) {
                        painelPrincipal.add(new JLabel("   Nenhuma tarefa cadastrada."));
                    } else {
                        for (String t : tarefas) {
                            painelPrincipal.add(new JLabel("   • " + t));
                        }
                    }

                    // Espaço entre projetos
                    painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao carregar projetos: " + ex.getMessage());
        }

        frame.setVisible(true);
    }
}

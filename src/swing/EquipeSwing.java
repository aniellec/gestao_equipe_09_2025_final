// arquivo EquipeSwing.java

package swing;

import modelo.Equipe;
import modelo.Usuario;
import modelo.Projeto;
import utilitario.EquipeDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeSwing {

    private JFrame frame;
    private DefaultListModel<Equipe> modelEquipes;
    private JList<Equipe> listaEquipes;

    private List<Usuario> usuarios;
    private List<Projeto> projetos;

    public void iniciar(List<Usuario> usuarios, List<Projeto> projetos) {
        this.usuarios = usuarios;
        this.projetos = projetos;

        frame = new JFrame("Gerenciamento de Equipes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        // Lista de equipes
        modelEquipes = new DefaultListModel<>();
        listaEquipes = new JList<>(modelEquipes);
        listaEquipes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEquipes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Equipe equipeSelecionada = listaEquipes.getSelectedValue();
                if (equipeSelecionada != null) {
                    abrirFormularioEditar(equipeSelecionada);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listaEquipes);
        frame.add(scroll, BorderLayout.CENTER);

        // Botão adicionar equipe
        JButton btnAdicionar = new JButton("Nova Equipe");
        btnAdicionar.addActionListener(e -> abrirFormularioAdicionar());
        frame.add(btnAdicionar, BorderLayout.SOUTH);

        carregarEquipes();
        frame.setVisible(true);
    }

    // Método sem argumentos
    public void iniciar() {
        iniciar(new ArrayList<>(), new ArrayList<>());
    }

    private void carregarEquipes() {
        try {
            modelEquipes.clear();
            EquipeDAO dao = new EquipeDAO();
            List<Equipe> equipes = dao.listarEquipes();
            for (Equipe e : equipes) {
                modelEquipes.addElement(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao carregar equipes: " + ex.getMessage());
        }
    }

    private void abrirFormularioAdicionar() {
        abrirFormulario(null);
    }

    private void abrirFormularioEditar(Equipe equipe) {
        abrirFormulario(equipe);
    }

    private void abrirFormulario(Equipe equipeExistente) {
        boolean editando = (equipeExistente != null);

        JDialog dialog = new JDialog(frame, editando ? "Editar Equipe" : "Nova Equipe", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField txtNome = new JTextField(editando ? equipeExistente.getNome() : "");
        JTextArea txtDescricao = new JTextArea(editando ? equipeExistente.getDescricao() : "", 5, 20);

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Descrição:"));
        panel.add(new JScrollPane(txtDescricao));

        // Lista de usuários
        DefaultListModel<Usuario> modelUsuarios = new DefaultListModel<>();
        for (Usuario u : usuarios) modelUsuarios.addElement(u);
        JList<Usuario> listaUsuarios = new JList<>(modelUsuarios);
        listaUsuarios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Pré-seleciona membros se estiver editando
        if (editando && equipeExistente.getMembros() != null) {
            List<Integer> ids = equipeExistente.getMembros().stream().map(Usuario::getId).toList();
            int[] indices = usuarios.stream()
                    .filter(u -> ids.contains(u.getId()))
                    .mapToInt(usuarios::indexOf).toArray();
            listaUsuarios.setSelectedIndices(indices);
        }

        panel.add(new JLabel("Membros:"));
        panel.add(new JScrollPane(listaUsuarios));

        // Lista de projetos
        DefaultListModel<Projeto> modelProjetos = new DefaultListModel<>();
        for (Projeto p : projetos) modelProjetos.addElement(p);
        JList<Projeto> listaProjetos = new JList<>(modelProjetos);
        listaProjetos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Pré-seleciona projetos se estiver editando
        if (editando && equipeExistente.getProjetos() != null) {
            List<Integer> ids = equipeExistente.getProjetos().stream().map(Projeto::getId).toList();
            int[] indices = projetos.stream()
                    .filter(p -> ids.contains(p.getId()))
                    .mapToInt(projetos::indexOf).toArray();
            listaProjetos.setSelectedIndices(indices);
        }

        panel.add(new JLabel("Projetos:"));
        panel.add(new JScrollPane(listaProjetos));

        dialog.add(panel, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnSalvar = new JButton(editando ? "Salvar Alterações" : "Salvar");
        JButton btnExcluir = new JButton("Excluir");
        if (!editando) btnExcluir.setEnabled(false);

        botoes.add(btnSalvar);
        botoes.add(btnExcluir);

        dialog.add(botoes, BorderLayout.SOUTH);

        // Ação salvar
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            List<Usuario> membrosSelecionados = listaUsuarios.getSelectedValuesList();
            List<Projeto> projetosSelecionados = listaProjetos.getSelectedValuesList();

            if (nome.isEmpty() || membrosSelecionados.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nome e pelo menos um membro são obrigatórios.");
                return;
            }

            Equipe equipe = editando ? equipeExistente : new Equipe();
            equipe.setNome(nome);
            equipe.setDescricao(descricao);
            equipe.setMembros(membrosSelecionados);
            equipe.setProjetos(projetosSelecionados);

            EquipeDAO dao = new EquipeDAO();
            try {
                dao.salvarEquipe(equipe);
                carregarEquipes();
                dialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar equipe: " + ex.getMessage());
            }
        });

        // Ação excluir
        btnExcluir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Deseja realmente excluir esta equipe?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    EquipeDAO dao = new EquipeDAO();
                    dao.deletarEquipe(equipeExistente.getIdEquipe());
                    carregarEquipes();
                    dialog.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Erro ao excluir equipe: " + ex.getMessage());
                }
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}

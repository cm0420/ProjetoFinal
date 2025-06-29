package com.mycompany.oficina.gui;
import java.util.List;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

public class AgendamentoDialogo extends JDialog {

    private final AtendenteController controller;
    private final JComboBox<Cliente> clienteCombo;
    private final JComboBox<Carro> carroCombo;
    private final JComboBox<Funcionario> mecanicoCombo;
    private final JComboBox<TipoServico> servicoCombo;
    private final JTextField dataHoraField = new JTextField("dd/MM/yyyy HH:mm", 15);

    private Agendamento agendamento;
    private boolean confirmado;

    public AgendamentoDialogo(Frame owner, AtendenteController controller) {
        super(owner, "Novo Agendamento", true);
        this.controller = controller;

        clienteCombo = new JComboBox<>(new Vector<>(controller.listarClientes()));
        carroCombo = new JComboBox<>();
        mecanicoCombo = new JComboBox<>(new Vector<>(controller.listarMecanicosDisponiveis()));
        servicoCombo = new JComboBox<>(new Vector<>(List.of(TipoServico.values())));

        setupLayout();
        setupComboBoxRenderers();
        setupListeners();

        pack();
        setLocationRelativeTo(owner);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();

        addField(panel, gbc, 0, "Cliente:", clienteCombo);
        addField(panel, gbc, 1, "Veículo:", carroCombo);
        addField(panel, gbc, 2, "Mecânico:", mecanicoCombo);
        addField(panel, gbc, 3, "Serviço:", servicoCombo);
        addField(panel, gbc, 4, "Data e Hora:", dataHoraField);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupComboBoxRenderers() {
        clienteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    setText(((Cliente) value).getNome());
                }
                return this;
            }
        });

        // Repetir para Carro e Funcionario...
    }

    private void setupListeners() {
        clienteCombo.addActionListener(e -> {
            Cliente selecionado = (Cliente) clienteCombo.getSelectedItem();
            carroCombo.removeAllItems();
            if (selecionado != null) {
                controller.listarVeiculosDoCliente(selecionado.getCpf()).forEach(carroCombo::addItem);
                carroCombo.setEnabled(true);
            } else {
                carroCombo.setEnabled(false);
            }
        });
    }

    private void onSave() {
        // Validação
        if (clienteCombo.getSelectedItem() == null || carroCombo.getSelectedItem() == null || mecanicoCombo.getSelectedItem() == null || servicoCombo.getSelectedItem() == null || dataHoraField.getText().isBlank()) {
            AlertsSwing.showAlert("Campos Vazios", "Todos os campos são obrigatórios.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraField.getText(), formatter);

            this.agendamento = new Agendamento(
                    (Cliente) clienteCombo.getSelectedItem(),
                    (Carro) carroCombo.getSelectedItem(),
                    (Funcionario) mecanicoCombo.getSelectedItem(),
                    (TipoServico) servicoCombo.getSelectedItem(),
                    null,
                    dataHora
            );
            this.confirmado = true;
            dispose();

        } catch (DateTimeParseException e) {
            AlertsSwing.showAlert("Erro de Formato", "O formato da data/hora está inválido. Use dd/MM/yyyy HH:mm.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Agendamento getAgendamento() { return agendamento; }
    public boolean isConfirmado() { return confirmado; }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(component, gbc);
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
}
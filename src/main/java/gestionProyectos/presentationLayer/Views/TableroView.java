package gestionProyectos.presentationLayer.Views;

import gestionProyectos.domainLayer.Proyect;
import gestionProyectos.domainLayer.User;
import gestionProyectos.domainLayer.Work;
import gestionProyectos.presentationLayer.Controllers.ProyectController;
import gestionProyectos.presentationLayer.Controllers.WorkController;
import gestionProyectos.presentationLayer.Models.ProyectTableModel;
import gestionProyectos.presentationLayer.Models.WorkTableModel;
import gestionProyectos.utilities.EstadoTarea;
import gestionProyectos.utilities.Prioridad;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class TableroView extends javax.swing.JFrame {
    private JPanel MainContentPanel;
    private JPanel WorksPanel;
    private JPanel ProyectPanel;
    private JTable AssigmentsTable;
    private JScrollPane AssigmentTablePanel;
    private JPanel LabelWorksPanel;
    private JLabel ProyectSelectedLabel;
    private JPanel CreateWorkPanel;
    private JPanel ButtonCreatePanel;
    private JButton CreateWorkButton;
    private JPanel DescriptionWorkPanel;
    private JTextField DescriptionWorkField;
    private JLabel DescriptionWorkLabel;
    private JPanel ExpirePanel;
    private JLabel ExpireLabel;
    private JTextField ExpireField;
    private JComboBox PrioritiComboBox;
    private JComboBox StateComboBox;
    private JComboBox ResponsabilityComboBox;
    private JPanel PriorityPanel;
    private JLabel PriorityLabel;
    private JPanel StatePanel;
    private JLabel StateLabel;
    private JPanel Responsability;
    private JLabel ResponsabilityLabel;
    private JTable ProyectTable;
    private JScrollPane ProyectTablePanel;
    private JPanel CreatePanel;
    private JPanel CreateButtonPanel;
    private JButton CreateProyectButton;
    private JPanel DescriptionProyectPanel;
    private JTextField DescriptionProyectField;
    private JLabel DescriptionProyectLabel;
    private JPanel InChargePanel;
    private JComboBox InChargeComboBox;
    private JLabel InChargeLabel;

    private final ProyectController proyectController;
    private final WorkController workController;

    private final ProyectTableModel proyectTableModel;
    private final WorkTableModel workTableModel;

    private Proyect proyectSeleccionado;

    public TableroView(ProyectController proyectController, WorkController workController, ProyectTableModel proyectTableModel, WorkTableModel workTableModel, List<User> users) {
        setTitle("Gestión de Proyectos");
        setContentPane(MainContentPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        this.proyectController = proyectController;
        this.workController = workController;
        this.proyectTableModel = proyectTableModel;
        this.workTableModel = workTableModel;

        initComboBoxes(users);
        addListeners();
        loadData();
    }

    private void initComboBoxes(List<User> users) {
        InChargeComboBox.removeAllItems();
        ResponsabilityComboBox.removeAllItems();

        users.forEach(user -> {
            InChargeComboBox.addItem(user);
            ResponsabilityComboBox.addItem(user);
        });

        PrioritiComboBox.removeAllItems();
        for (Prioridad p : Prioridad.values()) {
            PrioritiComboBox.addItem(p);
        }

        StateComboBox.removeAllItems();
        for (EstadoTarea e : EstadoTarea.values()) {
            StateComboBox.addItem(e);
        }
    }

    private void loadData() {
        proyectTableModel.setRows(proyectController.getProyects());
        ProyectTable.setModel(proyectTableModel);

        workTableModel.setRows(List.of());
        AssigmentsTable.setModel(workTableModel);
    }

    private void addListeners() {
        CreateProyectButton.addActionListener(e -> agregarProyect());
        CreateWorkButton.addActionListener(e -> agregarWork());

        ProyectTable.getSelectionModel().addListSelectionListener(this::onProyectSelection);

        AssigmentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = AssigmentsTable.getSelectedRow();
                    if (row >= 0) {
                        Work work = workTableModel.getAt(row);
                        abrirFormularioEdicion(work, row);
                    }
                }
            }
        });
    }

    private void onProyectSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = ProyectTable.getSelectedRow();
        if (row < 0) return;

        proyectSeleccionado = proyectTableModel.getAt(row);
        if (proyectSeleccionado != null) {
            workTableModel.setRows(proyectSeleccionado.getWorks());
            ProyectSelectedLabel.setText("Proyecto seleccionado: " + proyectSeleccionado.getDescripcion());
        }
    }

    private void agregarProyect() {
        String descripcion = DescriptionProyectField.getText();
        User encargado = (User) InChargeComboBox.getSelectedItem();

        if (descripcion.isEmpty() || encargado == null) {
            JOptionPane.showMessageDialog(this, "Debe ingresar descripción y encargado.");
            return;
        }

        proyectController.agregar(descripcion, encargado);

        List<Proyect> proyectosActualizados = proyectController.getProyects();
        proyectTableModel.setRows(proyectosActualizados);

        proyectSeleccionado = proyectosActualizados.get(proyectosActualizados.size() - 1);
        ProyectSelectedLabel.setText("Proyecto seleccionado: " + proyectSeleccionado.getDescripcion());

        JOptionPane.showMessageDialog(this, "Proyecto creado y seleccionado correctamente.");
    }

    private void agregarWork() {
        if (proyectSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proyecto primero.");
            return;
        }

        String descripcion = DescriptionWorkField.getText();
        String fecha = ExpireField.getText().trim();
        Prioridad prioridad = (Prioridad) PrioritiComboBox.getSelectedItem();
        EstadoTarea estado = (EstadoTarea) StateComboBox.getSelectedItem();
        User responsable = (User) ResponsabilityComboBox.getSelectedItem();

        if (descripcion.isEmpty() || fecha.isEmpty() || prioridad == null || estado == null || responsable == null) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos del work.");
            return;
        }

        LocalDate fechaFin;
        try {
            fechaFin = LocalDate.parse(fecha);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD. Valor recibido: " + fecha);
            return;
        }

        try {
            workController.agregar(proyectSeleccionado.getId(), descripcion, fechaFin, prioridad, estado, responsable);

            proyectSeleccionado = proyectController.getProyect(proyectSeleccionado.getId());

            workTableModel.setRows(proyectSeleccionado.getWorks());

            JOptionPane.showMessageDialog(this, "Tarea creada y guardada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el work: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void abrirFormularioEdicion(Work work, int rowIndex) {
        JComboBox<Prioridad> prioridadBox = new JComboBox<>(Prioridad.values());
        prioridadBox.setSelectedItem(work.getPrioridad());

        JComboBox<EstadoTarea> estadoBox = new JComboBox<>(EstadoTarea.values());
        estadoBox.setSelectedItem(work.getEstado());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Editar Prioridad:"));
        panel.add(prioridadBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Editar Estado:"));
        panel.add(estadoBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Editar tarea",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            work.setPrioridad((Prioridad) prioridadBox.getSelectedItem());
            work.setEstado((EstadoTarea) estadoBox.getSelectedItem());
            workTableModel.fireTableRowsUpdated(rowIndex, rowIndex);

            try {
                proyectController.actualizar(proyectSeleccionado);
                JOptionPane.showMessageDialog(this, "Tarea actualizada y guardada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
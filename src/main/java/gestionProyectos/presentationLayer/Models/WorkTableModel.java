package gestionProyectos.presentationLayer.Models;

import gestionProyectos.domainLayer.Work;
import gestionProyectos.serviceLayer.IServiceObserver;
import gestionProyectos.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class WorkTableModel extends AbstractTableModel implements IServiceObserver<Work> {
    private final String[] cols = {"ID", "Descripción", "Fecha fin", "Prioridad", "Estado", "Responsable"};
    private final Class<?>[] types = {Integer.class, String.class, String.class, String.class, String.class, String.class};

    private final List<Work> rows = new ArrayList<>();

    public void setRows(List<Work> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Work getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Class<?> getColumnClass(int c) { return types[c]; }
    @Override public boolean isCellEditable(int r, int c) { return false; }

    @Override
    public Object getValueAt(int r, int c) {
        Work w = rows.get(r);
        switch (c) {
            case 0: return w.getId();
            case 1: return w.getDescripcion();
            case 2: return w.getFechaFinEsperada();
            case 3: return w.getPrioridad();
            case 4: return w.getEstado();
            case 5: return w.getResponsable().getName();
            default: return null;
        }
    }

    // ----- Observer -----
    @Override
    public void onDataChanged(ChangeType type, Work entity) {
        switch (type) {
            case CREATED: {
                rows.add(entity);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }
            case UPDATED: {
                int i = indexOf(entity.getId());
                if (i >= 0) {
                    rows.set(i, entity);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            case DELETED: {
                int i = indexOf(entity.getId());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
        }
    }

    private int indexOf(int id) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getId() == id) return i;
        }
        return -1;
    }
}
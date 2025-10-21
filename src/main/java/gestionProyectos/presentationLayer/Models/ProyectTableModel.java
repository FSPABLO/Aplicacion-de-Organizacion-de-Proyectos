package gestionProyectos.presentationLayer.Models;

import gestionProyectos.domainLayer.Proyect;
import gestionProyectos.serviceLayer.IServiceObserver;
import gestionProyectos.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProyectTableModel extends AbstractTableModel implements IServiceObserver<Proyect> {
    private final String[] cols = {"ID", "Descripción", "Encargado"};
    private final Class<?>[] types = {Integer.class, String.class, String.class};

    private final List<Proyect> rows = new ArrayList<>();

    public void setRows(List<Proyect> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Proyect getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Class<?> getColumnClass(int c) { return types[c]; }
    @Override public boolean isCellEditable(int r, int c) { return false; }

    @Override
    public Object getValueAt(int r, int c) {
        Proyect p = rows.get(r);
        switch (c) {
            case 0: return p.getId();
            case 1: return p.getDescripcion();
            case 2: return p.getEncargado().getName();
            default: return null;
        }
    }

    @Override
    public void onDataChanged(ChangeType type, Proyect entity) {
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
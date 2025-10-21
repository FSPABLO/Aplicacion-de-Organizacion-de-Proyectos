package gestionProyectos.serviceLayer;

import gestionProyectos.dataAccesLayer.IFileStore;
import gestionProyectos.domainLayer.Proyect;
import gestionProyectos.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class ProyectService implements IService<Proyect> {
    private final IFileStore<Proyect> fileStore;
    private final List<IServiceObserver<Proyect>> listeners = new ArrayList<>();

    public ProyectService(IFileStore<Proyect> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Proyect entity) {
        List<Proyect> proyects = fileStore.readAll();
        int nextId = proyects.stream()
                .mapToInt(Proyect::getId)
                .max()
                .orElse(0) + 1;
        entity.setId(nextId);
        proyects.add(entity);
        fileStore.writeAll(proyects);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void actualizar(Proyect entity) {
        List<Proyect> proyects = fileStore.readAll();
        for (int i = 0; i < proyects.size(); i++) {
            if (proyects.get(i).getId() == entity.getId()) {
                proyects.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(proyects);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Proyect> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Proyect leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Proyect> listener) {
        if (listener != null) listeners.add(listener);
    }

    @Override
    public void removeObserver(IServiceObserver<Proyect> listener) {
        listeners.remove(listener);
    }

    private void notifyObservers(ChangeType type, Proyect entity) {
        for (IServiceObserver<Proyect> l : listeners) {
            l.onDataChanged(type, entity);
        }
    }
}
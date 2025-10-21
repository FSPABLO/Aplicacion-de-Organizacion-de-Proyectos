package gestionProyectos.serviceLayer;

import gestionProyectos.domainLayer.Proyect;
import gestionProyectos.domainLayer.Work;
import gestionProyectos.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class WorkService implements IService<Work> {
    private final ProyectService proyectService;
    private final List<IServiceObserver<Work>> listeners = new ArrayList<>();

    public WorkService(ProyectService proyectService) {
        this.proyectService = proyectService;
    }

    @Override
    public void agregar(Work entity) {
        throw new UnsupportedOperationException("Error al agregar");
    }

    public void agregar(int proyectId, Work entity) {
        Proyect proyect = proyectService.leerPorId(proyectId);
        if (proyect != null) {
            proyect.addWork(entity);
            proyectService.actualizar(proyect);
            notifyObservers(ChangeType.CREATED, entity);
        }
    }

    @Override
    public void actualizar(Work entity) {
        throw new UnsupportedOperationException("Error al actualizar");
    }

    public void actualizar(int proyectId, Work entity) {
        Proyect proyect = proyectService.leerPorId(proyectId);
        if (proyect != null) {
            List<Work> works = proyect.getWorks();
            for (int i = 0; i < works.size(); i++) {
                if (works.get(i).getId() == entity.getId()) {
                    works.set(i, entity);
                    break;
                }
            }
            proyectService.actualizar(proyect);
            notifyObservers(ChangeType.UPDATED, entity);
        }
    }

    @Override
    public List<Work> leerTodos() {
        throw new UnsupportedOperationException("Error al leer todos");
    }

    public List<Work> leerTodos(int proyectId) {
        Proyect proyect = proyectService.leerPorId(proyectId);
        return (proyect != null) ? proyect.getWorks() : new ArrayList<>();
    }

    @Override
    public Work leerPorId(int id) {
        throw new UnsupportedOperationException("Error al leer por id");
    }

    public Work leerPorId(int proyectId, int workId) {
        Proyect proyect = proyectService.leerPorId(proyectId);
        if (proyect != null) {
            return proyect.getWorks()
                    .stream()
                    .filter(w -> w.getId() == workId)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public void addObserver(IServiceObserver<Work> listener) {
        if (listener != null) listeners.add(listener);
    }

    @Override
    public void removeObserver(IServiceObserver<Work> listener) {
        listeners.remove(listener);
    }

    private void notifyObservers(ChangeType type, Work entity) {
        for (IServiceObserver<Work> l : listeners) {
            l.onDataChanged(type, entity);
        }
    }
}
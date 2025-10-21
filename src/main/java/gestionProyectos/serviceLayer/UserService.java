package gestionProyectos.serviceLayer;

import gestionProyectos.dataAccesLayer.IFileStore;
import gestionProyectos.domainLayer.User;
import gestionProyectos.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User>{
    private final IFileStore<User> fileStore;
    private final List<IServiceObserver<User>> listeners = new ArrayList<>();

    public UserService(IFileStore<User> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(User entity) {
        List<User> users = fileStore.readAll();
        users.add(entity);
        fileStore.writeAll(users);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void actualizar(User entity) {
        List<User> users = fileStore.readAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == entity.getId()) {
                users.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(users);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<User> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public User leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<User> listener) {
        if (listener != null) listeners.add(listener);
    }

    @Override
    public void removeObserver(IServiceObserver<User> listener) {
        listeners.remove(listener);
    }

    private void notifyObservers(ChangeType type, User entity) {
        for (IServiceObserver<User> l : listeners) {
            l.onDataChanged(type, entity);
        }
    }
}

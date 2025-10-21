package gestionProyectos.serviceLayer;

import java.util.List;

public interface IService<T> {
    void agregar(T entity);
    List<T> leerTodos();
    T leerPorId(int id);
    void actualizar(T entity);

    // Observer
    void addObserver(IServiceObserver<T> listener);
    void removeObserver(IServiceObserver<T> listener);
}

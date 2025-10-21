package gestionProyectos.serviceLayer;

import gestionProyectos.utilities.ChangeType;

public interface IServiceObserver<T> {
    void onDataChanged(ChangeType type, T entity);
}


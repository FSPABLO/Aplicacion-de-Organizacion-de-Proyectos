package gestionProyectos.presentationLayer.Controllers;

import gestionProyectos.domainLayer.Proyect;
import gestionProyectos.domainLayer.User;
import gestionProyectos.domainLayer.Work;
import gestionProyectos.serviceLayer.IService;

import java.util.List;

public class ProyectController {
    private final IService<Proyect> service;

    public ProyectController(IService<Proyect> service) {
        this.service = service;
    }

    public List<Proyect> getProyects() {
        return service.leerTodos();
    }

    public Proyect getProyect(int id) {
        return service.leerPorId(id);
    }

    public void agregar(String descripcion, User encargado) {
        Proyect proyect = new Proyect(descripcion, encargado);
        service.agregar(proyect);
    }

    public void actualizar(int id, String descripcion, User encargado, List<Work> works) {
        Proyect proyect = service.leerPorId(id);
        if (proyect != null) {
            proyect.setDescripcion(descripcion);
            proyect.setEncargado(encargado);
            proyect.getWorks().clear();
            proyect.getWorks().addAll(works);
            service.actualizar(proyect);
        }
    }

    public void actualizar(Proyect proyect) {
        service.actualizar(proyect);
    }
}
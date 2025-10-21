package gestionProyectos.presentationLayer.Controllers;

import gestionProyectos.domainLayer.User;
import gestionProyectos.domainLayer.Work;
import gestionProyectos.serviceLayer.WorkService;
import gestionProyectos.utilities.EstadoTarea;
import gestionProyectos.utilities.Prioridad;

import java.time.LocalDate;
import java.util.List;

public class WorkController {
    private final WorkService service;

    public WorkController(WorkService service) {
        this.service = service;
    }

    public void agregar(int proyectId, String descripcion, LocalDate fechaFin,
                        Prioridad prioridad, EstadoTarea estado, User responsable) {
        Work work = new Work(descripcion, fechaFin, prioridad, estado, responsable);
        service.agregar(proyectId, work);
    }

    public void actualizar(int proyectId, int workId, String descripcion,
                           LocalDate fechaFin, Prioridad prioridad,
                           EstadoTarea estado, User responsable) {
        Work work = service.leerPorId(proyectId, workId);
        if (work != null) {
            work.setDescripcion(descripcion);
            work.setFechaFinEsperada(fechaFin);
            work.setPrioridad(prioridad);
            work.setEstado(estado);
            work.setResponsable(responsable);
            service.actualizar(proyectId, work);
        }
    }

    public List<Work> getWorks(int proyectId) {
        return service.leerTodos(proyectId);
    }

    public Work getWork(int proyectId, int workId) {
        return service.leerPorId(proyectId, workId);
    }
}
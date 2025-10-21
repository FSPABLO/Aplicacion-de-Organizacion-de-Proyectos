package gestionProyectos.domainLayer;

import gestionProyectos.utilities.EstadoTarea;
import gestionProyectos.utilities.Prioridad;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement(name = "work")
@XmlAccessorType(XmlAccessType.FIELD)
public class Work {
    private static int contador = 1;

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "descripcion")
    private String descripcion;

    @XmlElement(name = "fechaFinEsperada")
    private String fechaFinEsperada;

    @XmlElement(name = "prioridad")
    private Prioridad prioridad;

    @XmlElement(name = "estado")
    private EstadoTarea estado;

    @XmlElement(name = "responsable")
    private User responsable;

    public Work() {}

    public Work(String descripcion, LocalDate fechaFinEsperada,
                Prioridad prioridad, EstadoTarea estado, User responsable) {
        this.id = contador++;
        this.descripcion = descripcion;
        this.fechaFinEsperada = fechaFinEsperada.toString(); // ISO yyyy-MM-dd
        this.prioridad = prioridad;
        this.estado = estado;
        this.responsable = responsable;
    }

    public int getId() { return id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaFinEsperada() {
        return (fechaFinEsperada != null && !fechaFinEsperada.isEmpty())
                ? LocalDate.parse(fechaFinEsperada)
                : null;
    }

    public void setFechaFinEsperada(LocalDate fechaFinEsperada) {
        this.fechaFinEsperada = (fechaFinEsperada != null) ? fechaFinEsperada.toString() : null;
    }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }

    public User getResponsable() { return responsable; }
    public void setResponsable(User responsable) { this.responsable = responsable; }

    @Override
    public String toString() {
        return id + " - " + descripcion + " (" + prioridad + ", " + estado + ")";
    }
}
package gestionProyectos.domainLayer;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "proyect")
@XmlAccessorType(XmlAccessType.FIELD)
public class Proyect {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "descripcion")
    private String descripcion;

    @XmlElement(name = "entregado")
    private User encargado;

    @XmlElement(name = "works")
    private List<Work> works;

    public Proyect() {
        this.works = new ArrayList<>();
    }

    public Proyect(String descripcion, User encargado) {
        this.descripcion = descripcion;
        this.encargado = encargado;
        this.works = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public User getEncargado() { return encargado; }
    public void setEncargado(User encargado) { this.encargado = encargado; }

    public List<Work> getWorks() { return works; }
    public void addWork(Work work) { this.works.add(work); }

    @Override
    public String toString() {
        return id + " - " + descripcion + " (Encargado: " + encargado.getName() + ")";
    }

    public void setId(int id) {
        this.id = id;
    }
}
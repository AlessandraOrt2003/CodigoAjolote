package model;

import java.util.ArrayList;
import java.util.List;

public class Modulo {
    private String id;
    private String titulo;
    private String descripcion;
    private int duracionMinutos;
    private int orden;
    private List<Leccion> lecciones;

    public Modulo(String id, String titulo, String descripcion, int duracionMinutos, int orden) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.orden = orden;
        this.lecciones = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public int getOrden() { return orden; }
    public List<Leccion> getLecciones() { return lecciones; }

    // Métodos de negocio
    public void agregarLeccion(Leccion leccion) {
        this.lecciones.add(leccion);
    }

    public int getTotalLecciones() {
        return lecciones.size();
    }

    public String getDuracionFormateada() {
        if (duracionMinutos < 60) {
            return duracionMinutos + " min";
        } else {
            int horas = duracionMinutos / 60;
            int minutos = duracionMinutos % 60;
            return horas + "h " + minutos + "min";
        }
    }

    public int getLeccionesCompletadas() {
        return (int) lecciones.stream().filter(Leccion::isCompletada).count();
    }

    public double getProgresoModulo() {
        if (lecciones.isEmpty()) return 0.0;
        return (getLeccionesCompletadas() * 100.0) / lecciones.size();
    }

    @Override
    public String toString() {
        return titulo + " (" + getTotalLecciones() + " lecciones)";
    }
}
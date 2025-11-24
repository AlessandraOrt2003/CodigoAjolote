package model;

import java.util.List;
import java.util.ArrayList;

public class Modulo {
    private String id;
    private String titulo;
    private String descripcion;
    private int duracionMinutos;
    private int orden;
    private List<Leccion> lecciones;
    private boolean completado;

    public Modulo() {
        this.lecciones = new ArrayList<>();
        this.completado = false;
    }

    public Modulo(String id, String titulo, String descripcion, int duracionMinutos, int orden) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.orden = orden;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public List<Leccion> getLecciones() { return lecciones; }
    public void setLecciones(List<Leccion> lecciones) { this.lecciones = lecciones; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }

    // Métodos de utilidad
    public void agregarLeccion(Leccion leccion) {
        this.lecciones.add(leccion);
        leccion.setOrden(this.lecciones.size());
    }

    public void removerLeccion(Leccion leccion) {
        this.lecciones.remove(leccion);
    }

    public int getTotalLecciones() {
        return lecciones.size();
    }

    public int getLeccionesCompletadas() {
        return (int) lecciones.stream()
                .filter(Leccion::isCompletada)
                .count();
    }

    public double getProgresoModulo() {
        if (lecciones.isEmpty()) return 0.0;
        return (double) getLeccionesCompletadas() / lecciones.size() * 100;
    }

    public String getDuracionFormateada() {
        int horas = duracionMinutos / 60;
        int minutos = duracionMinutos % 60;
        return String.format("%d h %d min", horas, minutos);
    }

    public void marcarComoCompletado() {
        this.completado = true;
        lecciones.forEach(Leccion::marcarCompletada);
    }

    @Override
    public String toString() {
        return orden + ". " + titulo + " (" + getTotalLecciones() + " lecciones)";
    }
}
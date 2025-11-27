package model;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String instructor;
    private int duracionTotal;
    private int totalLecciones;
    private String descripcionLarga;
    private double rating;
    private int totalEstudiantes;
    private String imagenUrl;
    private boolean activo;
    private List<Modulo> modulos;

    public Curso(String id, String titulo, String descripcion, String categoria,
                 String instructor, int duracionTotal, int totalLecciones) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.instructor = instructor;
        this.duracionTotal = duracionTotal;
        this.totalLecciones = totalLecciones;
        this.modulos = new ArrayList<>();
        this.activo = true;
        this.rating = 4.5;
        this.totalEstudiantes = 100;
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public String getInstructor() { return instructor; }
    public int getDuracionTotal() { return duracionTotal; }
    public int getTotalLecciones() { return totalLecciones; }
    public String getDescripcionLarga() { return descripcionLarga; }
    public double getRating() { return rating; }
    public int getTotalEstudiantes() { return totalEstudiantes; }
    public String getImagenUrl() { return imagenUrl; }
    public boolean isActivo() { return activo; }
    public List<Modulo> getModulos() { return modulos; }

    // Setters
    public void setDescripcionLarga(String descripcionLarga) { this.descripcionLarga = descripcionLarga; }
    public void setRating(double rating) { this.rating = rating; }
    public void setTotalEstudiantes(int totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Métodos de negocio
    public void agregarModulo(Modulo modulo) {
        this.modulos.add(modulo);
    }

    public int getTotalModulos() {
        return modulos.size();
    }

    public String getDuracionFormateada() {
        if (duracionTotal < 60) {
            return duracionTotal + " min";
        } else {
            int horas = duracionTotal / 60;
            int minutos = duracionTotal % 60;
            return horas + "h " + minutos + "min";
        }
    }

    public double getProgresoCurso() {
        int leccionesCompletadas = getLeccionesCompletadas();
        if (totalLecciones == 0) return 0.0;
        return (leccionesCompletadas * 100.0) / totalLecciones;
    }

    // MÉTODO NUEVO PARA SPRINT 4
    public int getLeccionesCompletadas() {
        int completadas = 0;
        for (Modulo modulo : modulos) {
            for (Leccion leccion : modulo.getLecciones()) {
                if (leccion.isCompletada()) {
                    completadas++;
                }
            }
        }
        return completadas;
    }

    @Override
    public String toString() {
        return titulo + " - " + instructor + " [" + getDuracionFormateada() + "]";
    }
}
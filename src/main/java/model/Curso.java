package model;

import java.util.List;
import java.util.ArrayList;

public class Curso {
    private String id;
    private String titulo;
    private String descripcion;
    private String descripcionLarga;
    private String categoria;
    private String instructor;
    private String imagenUrl;
    private int duracionHoras;
    private int totalLecciones;
    private double rating;
    private int totalEstudiantes;
    private List<Modulo> modulos;
    private boolean activo;

    public Curso() {
        this.modulos = new ArrayList<>();
        this.activo = true;
    }

    public Curso(String id, String titulo, String descripcion, String categoria,
                 String instructor, int duracionHoras, int totalLecciones) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.instructor = instructor;
        this.duracionHoras = duracionHoras;
        this.totalLecciones = totalLecciones;
        this.rating = 5.0;
        this.totalEstudiantes = 0;
    }

    // GETTERS Y SETTERS COMPLETOS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcionLarga() { return descripcionLarga; }
    public void setDescripcionLarga(String descripcionLarga) { this.descripcionLarga = descripcionLarga; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(int duracionHoras) { this.duracionHoras = duracionHoras; }

    public int getTotalLecciones() { return totalLecciones; }
    public void setTotalLecciones(int totalLecciones) { this.totalLecciones = totalLecciones; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(int totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }

    public List<Modulo> getModulos() { return modulos; }
    public void setModulos(List<Modulo> modulos) { this.modulos = modulos; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // MÉTODOS DE UTILIDAD
    public void agregarModulo(Modulo modulo) {
        this.modulos.add(modulo);
    }

    public void removerModulo(Modulo modulo) {
        this.modulos.remove(modulo);
    }

    public int getTotalModulos() {
        return modulos.size();
    }

    public int getDuracionTotalMinutos() {
        return modulos.stream()
                .mapToInt(Modulo::getDuracionMinutos)
                .sum();
    }

    public String getDuracionFormateada() {
        int horas = duracionHoras;
        int minutos = getDuracionTotalMinutos() % 60;
        return String.format("%d h %d min", horas, minutos);
    }

    public double getProgresoCurso() {
        if (modulos.isEmpty()) return 0.0;

        long leccionesCompletadas = modulos.stream()
                .flatMap(modulo -> modulo.getLecciones().stream())
                .filter(Leccion::isCompletada)
                .count();

        long totalLecciones = modulos.stream()
                .mapToInt(modulo -> modulo.getLecciones().size())
                .sum();

        return totalLecciones > 0 ? (double) leccionesCompletadas / totalLecciones * 100 : 0.0;
    }

    @Override
    public String toString() {
        return titulo + " (" + categoria + ") - " + instructor;
    }
}
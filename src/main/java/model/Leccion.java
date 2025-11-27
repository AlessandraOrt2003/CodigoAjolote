package model;

import service.UserDataManager;

public class Leccion {
    private String id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private int duracionMinutos;
    private int orden;
    private String recursoUrl;
    private String contenido;
    private boolean completada;
    private transient UserDataManager userDataManager;

    public Leccion(String id, String titulo, String descripcion, String tipo, int duracionMinutos, int orden) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.duracionMinutos = duracionMinutos;
        this.orden = orden;
        this.completada = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public int getOrden() { return orden; }
    public String getRecursoUrl() { return recursoUrl; }
    public String getContenido() { return contenido; }
    public boolean isCompletada() {
        if (userDataManager != null) {
            return userDataManager.isLeccionCompletada(this.id);
        }
        return completada;
    }

    // Setters
    public void setRecursoUrl(String recursoUrl) { this.recursoUrl = recursoUrl; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public void setCompletada(boolean completada) { this.completada = completada; }
    public void setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    // Métodos de negocio
    public void marcarCompletada() {
        this.completada = true;
        if (userDataManager != null) {
            userDataManager.marcarLeccionCompletada(this.id, this.getCursoId());
        }
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

    public String getIconoTipo() {
        switch (tipo.toLowerCase()) {
            case "video": return "🎥";
            case "texto": return "📖";
            case "practica": return "💻";
            case "quiz": return "📝";
            default: return "📚";
        }
    }

    private String getCursoId() {
        if (this.id != null && this.id.contains("-")) {
            String prefix = this.id.split("-")[0];
            switch (prefix) {
                case "L1": return "JAVA-001";
                case "L2": return "JAVA-001";
                case "L3": return "JAVAFX-001";
                case "L4": return "BD-001";
                case "L5": return "WEB-001";
                case "L6": return "PAT-001";
                default: return "JAVA-001";
            }
        }
        return "JAVA-001";
    }

    @Override
    public String toString() {
        return titulo + " (" + getDuracionFormateada() + ") " + (isCompletada() ? "✅" : "⏳");
    }
}
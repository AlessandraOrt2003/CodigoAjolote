package model;

public class Leccion {
    private String id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String tipo;
    private String recursoUrl;
    private int duracionMinutos;
    private int orden;
    private boolean completada;
    private boolean disponible;

    public Leccion() {
        this.completada = false;
        this.disponible = true;
    }

    public Leccion(String id, String titulo, String descripcion, String tipo, int duracionMinutos, int orden) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRecursoUrl() { return recursoUrl; }
    public void setRecursoUrl(String recursoUrl) { this.recursoUrl = recursoUrl; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // Métodos de utilidad
    public void marcarCompletada() {
        this.completada = true;
    }

    public void marcarIncompleta() {
        this.completada = false;
    }

    public String getDuracionFormateada() {
        if (duracionMinutos < 60) {
            return duracionMinutos + " min";
        } else {
            int horas = duracionMinutos / 60;
            int minutos = duracionMinutos % 60;
            return String.format("%d h %d min", horas, minutos);
        }
    }

    public String getIconoTipo() {
        switch (tipo.toLowerCase()) {
            case "video": return "🎥";
            case "texto": return "📖";
            case "quiz": return "📝";
            case "practica": return "💻";
            default: return "📄";
        }
    }

    public boolean esVideo() {
        return "video".equalsIgnoreCase(tipo);
    }

    public boolean esTexto() {
        return "texto".equalsIgnoreCase(tipo);
    }

    public boolean esQuiz() {
        return "quiz".equalsIgnoreCase(tipo);
    }

    public boolean esPractica() {
        return "practica".equalsIgnoreCase(tipo);
    }

    @Override
    public String toString() {
        return orden + ". " + titulo + " [" + getDuracionFormateada() + "]";
    }
}
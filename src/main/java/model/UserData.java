package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Boolean> leccionesCompletadas;
    private Map<String, Integer> progresoCursos;
    private int totalLeccionesCompletadas;
    private int totalCursosInscritos;
    private String nombreUsuario;

    public UserData() {
        this.leccionesCompletadas = new HashMap<>();
        this.progresoCursos = new HashMap<>();
        this.totalLeccionesCompletadas = 0;
        this.totalCursosInscritos = 0;
        this.nombreUsuario = "Estudiante";
    }

    // Getters
    public Map<String, Boolean> getLeccionesCompletadas() {
        return leccionesCompletadas;
    }

    public Map<String, Integer> getProgresoCursos() {
        return progresoCursos;
    }

    public int getTotalLeccionesCompletadas() {
        return totalLeccionesCompletadas;
    }

    public int getTotalCursosInscritos() {
        return totalCursosInscritos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    // Setters
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Métodos de negocio
    public void marcarLeccionCompletada(String leccionId, String cursoId) {
        if (!leccionesCompletadas.containsKey(leccionId)) {
            leccionesCompletadas.put(leccionId, true);
            totalLeccionesCompletadas++;
            actualizarProgresoCurso(cursoId);
        }
    }

    public boolean isLeccionCompletada(String leccionId) {
        return leccionesCompletadas.getOrDefault(leccionId, false);
    }

    private void actualizarProgresoCurso(String cursoId) {
        int leccionesCompletadasEnCurso = contarLeccionesCompletadasEnCurso(cursoId);
        progresoCursos.put(cursoId, Math.min(100, leccionesCompletadasEnCurso * 20));
    }

    private int contarLeccionesCompletadasEnCurso(String cursoId) {
        return (int) leccionesCompletadas.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(cursoId.substring(0, 3)) && entry.getValue())
                .count();
    }

    public void incrementarCursosInscritos() {
        totalCursosInscritos++;
    }

    public int getProgresoCurso(String cursoId) {
        return progresoCursos.getOrDefault(cursoId, 0);
    }

    public double getProgresoGeneral() {
        if (progresoCursos.isEmpty()) return 0.0;
        return progresoCursos.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
}
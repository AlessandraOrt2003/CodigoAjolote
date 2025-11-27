package service;

import model.UserData;
import java.io.*;

public class UserDataManager {
    private static final String USER_DATA_FILE = "userdata.ser";
    private UserData userData;

    public UserDataManager() {
        this.userData = cargarUserData();
    }

    public UserData getUserData() {
        return userData;
    }

    public void guardarUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(userData);
            System.out.println("✅ Datos de usuario guardados en: " + USER_DATA_FILE);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar datos de usuario: " + e.getMessage());
        }
    }

    private UserData cargarUserData() {
        File archivo = new File(USER_DATA_FILE);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                UserData datos = (UserData) ois.readObject();
                System.out.println("✅ Datos de usuario cargados desde: " + USER_DATA_FILE);
                return datos;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("❌ Error al cargar datos de usuario: " + e.getMessage());
            }
        }
        System.out.println("🆕 Creando nuevos datos de usuario...");
        return new UserData();
    }

    // Métodos de progreso
    public void marcarLeccionCompletada(String leccionId, String cursoId) {
        userData.marcarLeccionCompletada(leccionId, cursoId);
        guardarUserData();
        System.out.println("📝 Lección completada: " + leccionId + " | Curso: " + cursoId);
    }

    public boolean isLeccionCompletada(String leccionId) {
        return userData.isLeccionCompletada(leccionId);
    }

    public void incrementarCursosInscritos() {
        userData.incrementarCursosInscritos();
        guardarUserData();
        System.out.println("🎓 Nuevo curso inscrito. Total: " + userData.getTotalCursosInscritos());
    }

    public int getProgresoCurso(String cursoId) {
        return userData.getProgresoCurso(cursoId);
    }

    public int getTotalLeccionesCompletadas() {
        return userData.getTotalLeccionesCompletadas();
    }

    public int getTotalCursosInscritos() {
        return userData.getTotalCursosInscritos();
    }

    public double getProgresoGeneral() {
        return userData.getProgresoGeneral();
    }

    public void mostrarEstadisticas() {
        System.out.println("📊 === ESTADÍSTICAS DEL USUARIO ===");
        System.out.println("   👤 Usuario: " + userData.getNombreUsuario());
        System.out.println("   ✅ Lecciones completadas: " + getTotalLeccionesCompletadas());
        System.out.println("   🎓 Cursos inscritos: " + getTotalCursosInscritos());
        System.out.println("   📈 Progreso general: " + String.format("%.1f", getProgresoGeneral()) + "%");
        System.out.println("   📚 Progreso por curso: " + userData.getProgresoCursos());
        System.out.println("=================================");
    }
}
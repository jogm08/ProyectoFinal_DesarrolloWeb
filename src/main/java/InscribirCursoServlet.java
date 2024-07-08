import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "InscribirCursoServlet", urlPatterns = {"/InscribirCursoServlet"})
public class InscribirCursoServlet extends HttpServlet {

    // Datos de conexión a la base de datos (ajusta según tu configuración)
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/cursos_db";
    private static final String usuarioDB = "tu_usuario";
    private static final String contraseñaDB = "tu_contraseña";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Obtener parámetros del formulario
        String nombre = request.getParameter("nombre");
        String curso = request.getParameter("curso");

        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            // Establecer conexión con la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(jdbcUrl, usuarioDB, contraseñaDB);

            // Preparar consulta SQL parametrizada
            String sql = "INSERT INTO inscripciones (nombre, curso) VALUES (?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nombre);
            statement.setString(2, curso);

            // Ejecutar la inserción
            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                response.getWriter().println("<h1>Inscripción exitosa!</h1>");
            } else {
                response.getWriter().println("<h1>Error al inscribirse.</h1>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().println("<h1>Error en la conexión o consulta: " + e.getMessage() + "</h1>");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                response.getWriter().println("<h1>Error al cerrar la conexión: " + ex.getMessage() + "</h1>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

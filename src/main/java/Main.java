import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import expresscorreos.model.Cartero;
import expresscorreos.model.Coche;
import expresscorreos.model.Oficina;

public class Main {
    // @TODO: Sustituya xxxx por los parámetros de su conexión

    private static final String DB_SERVER = "localhost";

    private static final int DB_PORT = 3306;

    private static final String DB_NAME = "enviospractica";

    private static final String DB_USER = "root";

    private static final String DB_PASS = "";

    private static Connection conn;

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);

        // @TODO pruebe sus funciones

        nuevoCartero("12483449Z", "Paco", "Torres");
        System.out.println("-----------------------------------");
        carterosRepartoCochePeriodo(7).forEach(e -> System.out.println(e.getNombre() + " " + e.getApellidos() + " | " + e.getDNI()));
        System.out.println("-----------------------------------");
        oficinasAsociadasCalle("Alcalá").forEach(e -> System.out.println(e.getNombreM() + " | ID Oficina: " + e.getIdO()));
        System.out.println("-----------------------------------");
        cochesSinUtilizarPeriodo(30).forEach(System.out::println);

        conn.close();
    }

    // @TODO resuelva las siguientes funciones...

    public static void nuevoCartero(String DNI, String nombre, String apellidos) {
        // @TODO: complete este método para que cree un nuevo cartero en la base de datos
        try {
            String sql = "INSERT INTO cartero(dniCA, nombre, apellidos) VALUES(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, DNI);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellidos);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Cartero> carterosRepartoCochePeriodo(int periodo) {
        // @TODO: complete este método para que muestre por pantalla una lista de carteros que han
        // realizado un reparto con coche en el periodo comprendido por los últimos "periodo" días
        // (implementar para periodo=7)
        // Tenga en cuenta que la consulta a la base de datos le devolverá un ResultSet sobre el que deberá
        // ir iterando y creando un objeto con cada Cartero que cumpla con las condiciones,
        // y añadirlos a la lista

        List<Cartero> lista = new ArrayList<>();

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT cartero.dniCA, cartero.nombre, cartero.apellidos FROM cartero" +
                    " WHERE dniCA IN (" +
                    "    SELECT dniCA FROM reparto" +
                    "    WHERE matricula IN (" +
                    "        SELECT matricula FROM coche" +
                    "        )" +
                    "    AND FechaCreacion > DATE_ADD(CURRENT_DATE, INTERVAL -? DAY)" +
                    "    )");
            statement.setInt(1, periodo);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                lista.add(new Cartero(rs.getString("dniCA"), rs.getString("nombre"), rs.getString("apellidos")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return lista;
    }

    public static List<Oficina> oficinasAsociadasCalle(String calle) {
        // @TODO: complete este método para que muestre por pantalla una lista de las oficinas que
        // dan servicio a la C/Alcalá de Madrid.
        // Tenga en cuenta que la consulta a la base de datos le devolverá un ResultSet sobre el que deberá
        // ir iterando y creando un objeto con cada Oficina que tenga asociada algún segmento de esa calle,
        // y añadirlos a la lista
        List<Oficina> lista = new ArrayList<>();

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM oficina" +
                    " WHERE NombreM IN (" +
                    "    SELECT NombreM FROM municipio" +
                    "    WHERE municipio.NombreM IN (" +
                    "        SELECT NombreM FROM calle" +
                    "        WHERE calle.NombreC = ?" +
                    "        )" +
                    "    )");
            statement.setString(1, calle);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                lista.add(new Oficina(rs.getString("idO"), rs.getString("NombreM"), rs.getString("idCC")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Coche> cochesSinUtilizarPeriodo(int periodo) {
        // @TODO: complete este método para que muestre por pantalla una lista de los coches que no se han
        // utilizado en los últimos "periodo" días (implementar para periodo=30)
        List<Coche> lista = new ArrayList<>();

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM coche" +
                    " WHERE Matricula NOT IN(" +
                    "    SELECT reparto.matricula FROM reparto" +
                    "        WHERE reparto.FechaCreacion > DATE_ADD(CURRENT_DATE, INTERVAL -? DAY)" +
                    "    )");
            statement.setInt(1, periodo);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                lista.add(new Coche(rs.getString("Matricula"), rs.getFloat("Capacidad"), rs.getString("idO")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}

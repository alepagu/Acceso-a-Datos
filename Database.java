package practguiada;
//Importamos las librerías.
//Para ficheros
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//Para la fecha
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//Para mySql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Database {
    //Declaración de constantes
    final static String c_ruta_padre = "C:\\Users\\padilla.guale\\Desktop\\Padre";
    final static String c_log_file = "//log_data_base_";
    final static String c_tipo_info = "I"; //Para inidicar Información
    final static String c_tipo_error = "E"; //Para indicar Errores
    final static String c_tipo_aviso = "W"; //Para indicar algún aviso, Warning.

    //Creación de Método para escritura en el fichero log.
    private static void escribe_log(BufferedWriter v_log_buf, String v_tipo, String v_traza) {
        //Obtención de la fecha actual
        Date v_fecha_actual = new Date();   //Declaración de nuevo objeto para la fecha actual
        DateFormat v_fecha_hora_actual = new SimpleDateFormat("yyyyMMdd HH:mm:ss");//Formato de fecha
        try {//v_traza es el comentario que le damos al log
            v_log_buf.write(v_fecha_hora_actual.format(v_fecha_actual) + " - " + v_tipo + " - " + v_traza + "\n");//Muestra la información
            v_log_buf.flush();//Limpiar el buffer
        } catch (IOException e){
            System.out.println("Error de IO en el fichero de log: " + e.toString());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error escribiendo en el fichero de log: " + e.toString());
            System.exit(1);//Indicamos con esta salida el error ocurrido
        }
        return; //Devuelve el control
    }
    //Método para la conexión con las base de datos
    private static Connection conecta_db(BufferedWriter v_log_buf){
        Connection v_conexion = null;
        escribe_log(v_log_buf, c_tipo_info, " Estableciendo conexión");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (Exception ex){
            System.out.println("SQLException: " + ex.getMessage());
            escribe_log(v_log_buf, c_tipo_info, " Error estableciendo conexión");
        }
        try{
            v_conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/world", "app", "DAM23/24");
            escribe_log(v_log_buf, c_tipo_info, "Conexión establecida");
        }catch(SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            escribe_log(v_log_buf, c_tipo_info, " Error estableciendo conexión " + ex.getMessage());
        }
        return v_conexion;
    }
    //Función para escribir la tabla.
    private static void imprimeInforme(ResultSet i_datos, BufferedWriter v_log_buf) throws SQLException{
        ResultSetMetaData v_rs_metadatos = i_datos.getMetaData();
        int v_num_cols = v_rs_metadatos.getColumnCount();
        escribe_log(v_log_buf, c_tipo_info, "Imprimiendo informe");
        while (i_datos.next()){
            for(int i = 1; i<= v_num_cols; i++){
                if (i > 1) System.out.print(" | ");
                System.out.print(i_datos.getString(i));
            }
            System.out.println("");
        }
        escribe_log(v_log_buf, c_tipo_info, "Informe impreso");
        return;
    }

    //Método para lanzar consultas a la base de datos
    private static void lanzaConsulta(Connection i_conexion, String i_consulta, BufferedWriter v_log_buf) {

        Statement v_sentencia = null;
        ResultSet v_resultado = null;

        escribe_log(v_log_buf, c_tipo_info, "Lanzando consulta: " + i_consulta);

        try {
            v_sentencia = i_conexion.createStatement();
            v_resultado = v_sentencia.executeQuery(i_consulta);
            escribe_log(v_log_buf, c_tipo_info, "Consulta ejeutada");
            imprimeInforme(v_resultado, v_log_buf);//Imprime el resultado.

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (v_resultado != null) {
                try {
                    v_resultado.close();
                } catch (SQLException sqlEx) {
                }//Se ignora

                v_resultado = null;

            }
            if (v_sentencia != null) {
                try {
                    v_sentencia.close();
                } catch (SQLException sqlEx) {
                }//Se ignora

                v_sentencia = null;
            }
        }
    }


    public static void main(String[] args) throws IOException{

        String v_ruta_log = c_ruta_padre + "//Logs";
        //Datos del fichero Log
        Date v_fecha = new Date();
        DateFormat v_fecha_hora = new SimpleDateFormat("yyyyMMdd_HHmmss");
        FileWriter v_file_log = new FileWriter(v_ruta_log + c_log_file + v_fecha_hora.format(v_fecha) + ".log");
        BufferedWriter v_log_Writer = new BufferedWriter(v_file_log);

        //Manejo de excepciones
        try{
            System.out.println("Comienza ejecución");
            escribe_log(v_log_Writer, c_tipo_info, "Comienza ejecución");
            //Realizamnos la consulta
            Connection v_con_db = conecta_db(v_log_Writer);
            lanzaConsulta(v_con_db, "select Name, Population\n" +
                    "From country\n" +
                    "order by Population desc limit 1;", v_log_Writer);

            System.out.println("Fin de la ejecución");
            escribe_log(v_log_Writer, c_tipo_info, "Fin de la ejecución");
        }catch (Exception e){
            System.out.println("Error en la ejecución");

        }finally {
            if (v_log_Writer != null) v_log_Writer.close();
        }
    }
}
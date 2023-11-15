package practica3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class funcionesp3 {
    /*Declaración de constantes*/
    final static String c_tipo_info = "I";//Informativo
    final static String c_tipo_aviso = "W";//Aviso o Warning
    final static String c_tipo_error = "E";//Error

/* Método para escribir mensajes con la fecha y hora actual, el tipo del mensaje
   y una traza para indicar con un mensaje lo que se está realizando en ese momento.
 */
    public static void mensaje_log(BufferedWriter v_log_buf, String v_tipo, String v_traza){
        //Generamos un objeto de la clase date, para darle el formato fecha y hora actual
        Date v_fecha_actual = new Date();
        DateFormat v_fecha_hora = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        //Creamos un Manejo de excepciones
        try{
            v_log_buf.write(v_fecha_hora.format(v_fecha_actual) + " - " + v_tipo + v_traza + "\n");
            v_log_buf.flush();//Aseguramos la escritura total en el log
            //Capturamos las excepciones si las hay
        }catch(IOException e){
            System.out.println("Error de entrada o salida en el fichero Logs " + e.toString());
            System.exit(1);
        }catch(Exception e){
            System.out.println("Error escribiendo en el fichero Logs " + e.toString());
            System.exit(1);
        }
        return; //Devolvemos el control del programa
    }

    /*Método para conectar las bases de datos*/
    public static Connection conecta_bd(BufferedWriter v_log_buf){
        //Creo una variable conexión con estado nulo y escribimos en el fichero
        Connection v_conexion = null;
        mensaje_log(v_log_buf, c_tipo_info, "Estableciendo conexión");
        //Introducimos una carga de la clase para el uso del driver
        //En mi caso es necesario
        try{
            Class.forName("com.mysq.cj.jdbc.Driver");
            //Vemos el motivo de la excepcion con SQLException
        }catch(Exception ex){
            System.out.println("SQLException: " + ex.getMessage());
            mensaje_log(v_log_buf, c_tipo_error, " Error estableciendo conexión");
        }
        //Conectamos la base de datos si no tenemos fallo al conectar el Driver.
        try{
            v_conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bbdd", "usr", "pas");
            mensaje_log(v_log_buf,c_tipo_info,"Conexión establecida");
            //Capturamos todos los datos del porque ha fallado la conexión
        }catch(SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            mensaje_log(v_log_buf, c_tipo_error, " Error estableciendo conexión " + ex.getMessage());
        }
        return v_conexion;//Devolvemos el estado de la conexión
    }

    //Método para la escritura del contenido con formato de tabla
    //
    public static void imprimeTabla(ResultSet i_datos, BufferedWriter v_log_buf) throws SQLException{
        //Con el ResultSet obtenemos grandes cantidades de información.
        //Por eso se trabaja con metadatos
        ResultSetMetaData v_rs_metadatos = i_datos.getMetaData();
        int v_num_colum = v_rs_metadatos.getColumnCount();
        mensaje_log(v_log_buf, c_tipo_info, "Imprimiendo el resultado obtenido");
        /*Aplicamos un bucle while hasta que no se encuentren más datos*/
        while(i_datos.next()){
        /*Bucle for para recorrer los resultados en forma de filas*/
            for(int i = 1; i <= v_num_colum; i++){
                if (i > 1) System.out.println(" | ");
                System.out.println(i_datos.getString(i));//Datos de la fila
            }
            System.out.println("");
        }
        mensaje_log(v_log_buf, c_tipo_info, "Se a terminado de escribir la tabla");
        return; //Devolvemos el control
    }

    public static void lanzaConsulta(Connection i_conexion, String i_consulta, BufferedWriter v_log_buf){
        //Creamos las variables para el resultado y el estado de la setencia
        //Se crean null, ya que de primeras no tienen valor hasta que lo recogen
        Statement v_sentencia = null;
        ResultSet v_resultado = null;
        mensaje_log(v_log_buf, c_tipo_info, "Lanzando ejecutada" + i_consulta);

        //Manejo de Excepciones
        try{
            //Igualamos las variables a los datos que nos van a permitir obtener el resultado
            v_sentencia = i_conexion.createStatement();
            v_resultado = v_sentencia.executeQuery(i_consulta);
            mensaje_log(v_log_buf, c_tipo_info, "Consulta ejecutada");
            imprimeTabla(v_resultado, v_log_buf);

        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        }finally {
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
}

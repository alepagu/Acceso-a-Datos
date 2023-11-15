package practica3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static practica3.funcionesp3.*;

public class consultadatos {
    //Creo las constantes necesarias para recopilar el funcionamiento general
    final static String c_ruta_padre = "";
    final static String c_log_file = "log_data_base_";

    public static void main(String[] args) throws IOException {//Clase principal
        //Variables y Objetos de clases necesarios para el funcionamiento
        String v_ruta_log = c_ruta_padre + "Logs";
        //Objetos necesarios para el Logs
        Date v_fecha = new Date();
        DateFormat v_fecha_hora = new SimpleDateFormat("yyyyMMdd_HHmmss");
        //Objetos para el manejo de Ficheros.
        FileWriter v_file_log = new FileWriter(v_ruta_log + c_log_file + v_fecha_hora.format(v_fecha) + ".log");
        BufferedWriter v_log_Writer = new BufferedWriter(v_file_log);

        //MANEJO DE EXCEPCIONES
        try{
            System.out.println("--- Comienza la ejecución ---");
            mensaje_log(v_log_Writer, c_tipo_info, "Comienza la ejecución");
            //CONECTAMOS LA BASE DE DATOS
            Connection v_con_db = conecta_bd(v_log_Writer);
            System.out.println("--------------------------------------");
            //Lanzamos la consulta
            lanzaConsulta(v_con_db, "consulta;", v_log_Writer);

            System.out.println("--- Fin de la ejecución");
            mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución");

        }catch (Exception e){
            //Captura cualquier error
            System.out.println("Error en la ejecución");
            mensaje_log(v_log_Writer, c_tipo_error, "Ha surgido un error durante la ejecución");

        }finally{
            //SE EJECUTA CON O SIN ERRORES
            //Cerramos el bufferedWriter
            if (v_log_Writer  != null) v_log_Writer.close();
        }
    }
}

package practicas;

import java.io.File;    //Manejo de ficheros
import java.nio.file.Files;     //Copiar ficheros
import java.nio.file.StandardCopyOption;    //Modificar ficheros
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App1 {
    //Declaración de constantes
    final static String c_ruta_padre = "C:\\Users\\padilla.guale\\Desktop\\Padre";
    final static String  c_log_file = "//log_mueve_fichero_";
    final static String c_tipo_info = "I"; //Para inidicar Información
    final static String c_tipo_error = "E"; //Para indicar Errores
    final static String c_tipo_aviso = "W"; //Para indicar algún aviso, Warning.

    //Creación de Método
    private static void escribe_log(BufferedWriter v_log_buf, String v_tipo, String v_traza){
        //Obtención de la fecha actual
        Date v_fecha_actual = new Date();   //Declaración de nuevo objeto para la fecha actual
        DateFormat v_fecha_hora_actual = new SimpleDateFormat("yyyyMMdd HH:mm:ss");//Formato de fecha
        try{
            v_log_buf.write(v_fecha_hora_actual.format(v_fecha_actual) + " - " + v_tipo + " - " + v_traza +"\n");//Muestra la información
            v_log_buf.flush();//Limpiar el buffer
        }catch (Exception e){
            System.out.println("Error escribiendo en el fichero de log: " + e.toString());
        }
        return;
    }

    public static void main(String[] args) throws Exception{
        //Variables
        //Ruta origen
        String v_ruta_origen = c_ruta_padre + "//Origen";
        //Ruta destino
        String v_ruta_destino = c_ruta_padre + "//Destino//";
        //Ruta Log
        String v_ruta_log = c_ruta_padre + "//Logs";
        //Declaración de objetos.
        Date v_fecha = new Date(); //Fecha actual
        DateFormat v_fecha_hora = new SimpleDateFormat("yyyyMMdd_HHmmss"); //Formato de fecha
        FileWriter v_file_log = new FileWriter(v_ruta_log + c_log_file + v_fecha_hora.format(v_fecha) + ".log");
        BufferedWriter v_log_Writer = new BufferedWriter(v_file_log); //Para escribir

        System.out.println("Comienza ejecución");
        escribe_log(v_log_Writer,c_tipo_info, "Comienza ejecución"); //Llamada a la función.
        //Creamos varible a partir de la ruta padre
        File v_file_origFile = new File(v_ruta_origen); //Ruta origen
        //Lista de archivos/ficheros origen
        File v_file_list[] = v_file_origFile.listFiles();
        //Comprobación de si hay archivos
        if (v_file_list == null || v_file_list.length == 0){//Si no contiene archivos
            System.out.println("La ruta origen está vacía");
            escribe_log(v_log_Writer,c_tipo_aviso, "La ruta está vacía");
        }
        else{//Cuántos tiene archivos
            System.out.println("La ruta contine " + v_file_list.length + " archivos.");
            escribe_log(v_log_Writer,c_tipo_info, "Archivos que contiene");
            //Recorremos el conjunto de archivos
            for (int i=0; i < v_file_list.length; i++){
                File v_file = v_file_list[i];
                //Conversión a cadena para poder mostrar el contenido
                System.out.println("Procesando archivo: " + v_file.toString());
                escribe_log(v_log_Writer,c_tipo_info, "Procesando archivo");
                //Si es directorio
                if(v_file.isDirectory()){
                    System.out.println("El archivo es un directorio.");
                    escribe_log(v_log_Writer,c_tipo_aviso, "Es un directorio");
                }
                //Si es un fichero
                else{
                    System.out.println("Es un fichero");
                    escribe_log(v_log_Writer,c_tipo_aviso, "Es un fichero");
                    System.out.println("Copiando fichero");
                    escribe_log(v_log_Writer,c_tipo_info, "Copia del fichero");
                   try{
                       //Se copia el fichero. Con el toPath() devolvemos las rutas
                       Files.copy(v_file.toPath(), (new File(v_ruta_destino + v_file.getName())).toPath()
                                               , StandardCopyOption.REPLACE_EXISTING);
                                      //Dormimos 2 seg el proceso
                                       Thread.sleep(2000);
                                       System.out.println("Fichero copiado a " + v_ruta_destino);
                                       if (i == 1) throw  new Exception("se ha producido un error forzado");
                   }catch (Exception e){
                       escribe_log(v_log_Writer,c_tipo_error, "Error copiando fichero: " + e.getMessage());
                   }

                }
            }
        }
        System.out.println("Fin de la ejecución");
        escribe_log(v_log_Writer,c_tipo_info, "Comienza ejecución");
        v_log_Writer.close();
    }
}

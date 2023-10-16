package practicas;

import java.io.File;    //Manejo de ficheros
import java.nio.file.Files;     //Copiar ficheros
import java.nio.file.StandardCopyOption;    //Modificar ficheros

public class App {
    final static String c_ruta_padre = "C:\\Users\\padilla.guale\\Desktop\\Padre";

    public static void main(String[] args) throws Exception{
        //Ruta origen
        String v_ruta_origen = c_ruta_padre + "//Origen";
        //Ruta destino
        String v_ruta_destino = c_ruta_padre + "//Destino//";
        System.out.println("Comienza ejecución");
        //Creamos varible a partir de la ruta padre
        File v_file_origFile = new File(v_ruta_origen); //Ruta origen
        //Lista de archivos/ficheros origen
        File v_file_list[] = v_file_origFile.listFiles();
        //Comprobación de si hay archivos
        if (v_file_list == null || v_file_list.length == 0){//Si no contiene archivos
            System.out.println("La ruta origen está vacía");
        }
        else{//Cuántos tiene archivos
            System.out.println("La ruta contine " + v_file_list.length + " archivos.");
            //Recorremos el conjunto de archivos
            for (int i=0; i < v_file_list.length; i++){
                File v_file = v_file_list[i];
                //Conversión a cadena para poder mostrar el contenido
                System.out.println("Procesando archivo: " + v_file.toString());
                //Si es directorio
                if(v_file.isDirectory()){
                    System.out.println("El archivo es un directorio.");
                }
                //Si es un fichero
                else{
                    System.out.println("Es un fichero");
                    System.out.println("Copiando fichero");
                    //Se copia el fichero. Con el toPath() devolvemos las rutas
                    Files.copy(v_file.toPath(), (new File(v_ruta_destino + v_file.getName())).toPath()
                    , StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Fichero copiado a " + v_ruta_destino);
                }
            }
        }
        System.out.println("Fin de la ejecución");
    }
}

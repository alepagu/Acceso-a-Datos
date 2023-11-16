package practica3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
        //Declaración de variables y objetos que se van a utilizar para el BUCLE DO WHILE
        Scanner sc = new Scanner(System.in);
        int opcion, op;
        boolean salir = false, sal = false;

        //MANEJO DE EXCEPCIONES
        try{
            System.out.println("--- Comienza la ejecución ---\n");
            mensaje_log(v_log_Writer, c_tipo_info, "Comienza el programa");
            do {
                System.out.println("------------------------------------");
                System.out.println("----- Bienevenido al Menú del Usuario -----");
                System.out.println("Se van a mostrar diversas opciones para realizar operaciones de tu taller:");
                System.out.println("0 -> Salir del programa");
                System.out.println("1 -> Realizar una consultas preparadas sobre el taller");
                System.out.println("2 -> Actualizar datos de la base de datos del taller");
                System.out.println("3 -> Realizar una copia de seguridad en otra carpeta distinta de Logs");
                System.out.println("------------------------------------\n");
                System.out.print("¿Qué opción desea realizar? ");
                opcion = sc.nextInt();System.out.println("\n");
                switch (opcion) {
                    case 0:
                        salir = true;
                        System.out.println("--- Hasta la próxima ---");
                        break;

                    case 1:
                        do {
                            System.out.println("Aquí muestro las consultas que puedes realizar: ");
                            System.out.println("0 -> Salir al menú Principal");
                            System.out.println("1 -> Realizar una consulta sobre los clientes que viven en Priego de Córdoba");
                            System.out.println("2 -> Realizar una consulta sobre los vehículos que tiene cada cliente");
                            System.out.println("3 -> Realizar una consulta sobre los coches con menos kilómetros");
                            System.out.println("4 -> Realizar una consulta sobre los dueños de vehículos TDI (Turbo y Diésel)");
                            System.out.println("------------------------------------\n");
                            System.out.print("¿Qué opción desea realizar? ");
                            op = sc.nextInt();
                            System.out.println("\n");
                            Connection v_con_db = conecta_bd(v_log_Writer);

                            switch (op) {
                                case 0:
                                    sal = true;
                                    System.out.println("--- Volviendo al menú principal");
                                    System.out.println();
                                    break;

                                case 1:
                                    mensaje_log(v_log_Writer, c_tipo_info, "Inicio de la ejecución de la consulta 1");
                                    System.out.println("--------------------------------------");
                                    //Lanzamos la consulta
                                    lanzaConsulta(v_con_db, "SELECT concat(Nombre, \" \", Apellidos) as NombreCompleto, Calle, Localidad\n" +
                                            "FROM taller.cliente\n" +
                                            "INNER JOIN taller.direccion ON ID_direccion = calle\n" +
                                            "WHERE Localidad = \"Priego de Córdoba\";\n", v_log_Writer);

                                    System.out.println("--- Fin de la ejecución de la consulta 1 ---");
                                    mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución de la consulta 1");
                                    System.out.println();
                                    break;

                                case 2:
                                    mensaje_log(v_log_Writer, c_tipo_info, "Inicio de la ejecución de la consulta 2");
                                    System.out.println("--------------------------------------");
                                    //Lanzamos la consulta
                                    lanzaConsulta(v_con_db, "SELECT concat(Nombre, \" \", Apellidos) as Nombre_Apellidos, Matricula, Marca, Kilometraje\n" +
                                            "FROM taller.vehiculos \n" +
                                            "INNER JOIN taller.cliente ON ID_vehiculo = Matricula\n" +
                                            "ORDER BY Kilometraje desc;\n", v_log_Writer);

                                    System.out.println("--- Fin de la ejecución de la consulta 2 ---");
                                    mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución de la consulta 2");
                                    System.out.println();
                                    break;

                                case 3:
                                    mensaje_log(v_log_Writer, c_tipo_info, "Inicio de la ejecución de la consulta 3");
                                    System.out.println("--------------------------------------");
                                    //Lanzamos la consulta
                                    lanzaConsulta(v_con_db, "SELECT concat(Nombre, \" \", Apellidos) as Nombre_Apellidos, Marca, Kilometraje\n" +
                                            "FROM taller.cliente\n" +
                                            "INNER JOIN taller.vehiculos ON Matricula = ID_vehiculo\n" +
                                            "ORDER BY Kilometraje asc limit 3;\n", v_log_Writer);

                                    System.out.println("--- Fin de la ejecución de la consulta 3 ---");
                                    mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución de la consulta 3");
                                    System.out.println();
                                    break;

                                case 4:
                                    mensaje_log(v_log_Writer, c_tipo_info, "Inicio de la ejecución de la consulta 4");
                                    System.out.println("--------------------------------------");
                                    //Lanzamos la consulta
                                    lanzaConsulta(v_con_db, "SELECT concat(Nombre, \" \", Apellidos) as Nombre_Apellidos, Marca, Kilometraje\n" +
                                            "FROM taller.cliente\n" +
                                            "INNER JOIN taller.vehiculos ON Matricula = ID_vehiculo\n" +
                                            "WHERE Marca like \"%TDI%\";\n", v_log_Writer);

                                    System.out.println("--- Fin de la ejecución de la consulta 4 ---");
                                    mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución de la consulta 4");
                                    System.out.println();
                                    break;

                                default:
                                    System.out.println("Introduce una opción que se encuentre en el menú porfavor");
                                    System.out.println();
                                    break;
                            }

                        }while (!sal) ;

                    case 2:
                        break;

                    case 3:
                        break;

                    default:
                        System.out.println("Introduce una opción que se encuentre en el menú porfavor");
                        System.out.println();
                        break;
                }
                mensaje_log(v_log_Writer, c_tipo_info, "Fin de la ejecución");

            }while(!salir);





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

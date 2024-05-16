import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorDB gestorDB = new GestorDB("TRANSPORTS_GEOXML.xml");
        boolean salir = false;
        int opcion;

        while (!salir) {
            System.out.println();
            System.out.println("###################################################################");
            System.out.println("##                            MENÚ                               ##");
            System.out.println("## 1. Mostrar puntos registrados                                 ##");
            System.out.println("## 2. Insertar punto                                             ##");
            System.out.println("## 3. Eliminar punto                                             ##");
            System.out.println("## 4. Modificar punto                                            ##");
            System.out.println("## 5. Salir                                                      ##");
            System.out.println("###################################################################");

            try {
                System.out.print("Escribe una de las opciones: ");
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        scanner.nextLine(); // Consumir la nueva línea en el búfer
                        System.out.print("Introduce el Tooltip del punto que deseas mostrar: ");
                        String tooltipToShow = scanner.nextLine();
                        gestorDB.mostrarPuntos(tooltipToShow);
                        break;
                    case 2:
                        insertarPunto(gestorDB);
                        break;
                    case 3:
                        eliminarPunto(gestorDB);
                        break;
                    case 4:
                        modificarPunto(gestorDB);
                        break;
                    case 5:
                        salir = true;
                        gestorDB.cerrarSesion();
                        break;
                    default:
                        System.out.println("Solo números entre 1 y 5");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                scanner.next();
            }
        }
    }

    public static void insertarPunto(GestorDB gestorDB) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("ED50 Coordenada X: ");
        double ed50CoordX = scanner.nextDouble();

        System.out.print("ED50 Coordenada Y: ");
        double ed50CoordY = scanner.nextDouble();

        System.out.print("ETRS89 Coordenada X: ");
        double etrs89CoordX = scanner.nextDouble();

        System.out.print("ETRS89 Coordenada Y: ");
        double etrs89CoordY = scanner.nextDouble();

        System.out.print("Longitud: ");
        double longitud = scanner.nextDouble();

        System.out.print("Latitud: ");
        double latitud = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Icono URL: ");
        String icon = scanner.nextLine();

        System.out.print("Tooltip: ");
        String tooltip = scanner.nextLine();

        System.out.print("URL: ");
        String url = scanner.nextLine();

        Dades punt = new Dades(ed50CoordX, ed50CoordY, etrs89CoordX, etrs89CoordY, longitud, latitud, icon, tooltip, url);
        gestorDB.insertarPunto(punt);
    }

    public static void eliminarPunto(GestorDB gestorDB) {
        Scanner scanner = new Scanner(System.in);

        //System.out.print("Indica la coordenada X de ED50 del punto que deseas eliminar: ");
        //double ed50CoordX = scanner.nextDouble();

        //System.out.print("Indica la coordenada Y de ED50 del punto que deseas eliminar: ");
        //double ed50CoordY = scanner.nextDouble();
        //scanner.nextLine(); // Consumir la nueva línea en el búfer

        System.out.print("Introduce el Tooltip del punto que deseas eliminar: ");
        String tooltip = scanner.nextLine();

        gestorDB.eliminarPunto(tooltip);
    }


    public static void modificarPunto(GestorDB gestorDB) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el Tooltip del punto que deseas modificar: ");
        String oldTooltip = scanner.nextLine();

        System.out.print("Nuevo Tooltip: ");
        String newTooltip = scanner.nextLine();

        System.out.print("Nueva coordenada X de ED50: ");
        double ed50CoordX = scanner.nextDouble();

        System.out.print("Nueva coordenada Y de ED50: ");
        double ed50CoordY = scanner.nextDouble();

        System.out.print("Nueva coordenada X de ETRS89: ");
        double etrs89CoordX = scanner.nextDouble();

        System.out.print("Nueva coordenada Y de ETRS89: ");
        double etrs89CoordY = scanner.nextDouble();

        System.out.print("Nueva longitud: ");
        double longitud = scanner.nextDouble();

        System.out.print("Nueva latitud: ");
        double latitud = scanner.nextDouble();

        scanner.nextLine();

        System.out.print("Nuevo Icono URL: ");
        String icon = scanner.nextLine();

        System.out.print("Nuevo URL: ");
        String url = scanner.nextLine();

        gestorDB.modificarPunto(oldTooltip, newTooltip, ed50CoordX, ed50CoordY, etrs89CoordX, etrs89CoordY, longitud, latitud, icon, url);
    }
}

import javax.xml.xquery.XQException;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private GestorDB gestorDB;

    public Menu() throws XQException {
        scanner = new Scanner(System.in);
        gestorDB = new GestorDB("/db/EjercicioIvanSara/TRANSPORTS_GEOXML.xml");
    }

    public void run() throws Exception {
        boolean salir = false;
        int opcion;

        while (!salir) {
            mostrarMenu();
            System.out.print("Escribe una de las opciones: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Introduce el Tooltip del punto que deseas mostrar: ");
                    String tooltipToShow = scanner.nextLine();
                    gestorDB.mostrarPuntos(tooltipToShow);
                    break;
                case 2:
                    insertarPunto(scanner, gestorDB);
                    break;
                case 3:
                    //System.out.print("Introduce el Tooltip del punto que deseas eliminar: ");
                    //String tooltipEliminar = scanner.nextLine(); // Leer el Tooltip a eliminar
                    //eliminarPunto(scanner, gestorDB);

                    System.out.print("Introduce el Tooltip del punto que deseas eliminar: ");
                    String tooltip = scanner.next();
                    gestorDB.eliminarPunto(tooltip);
                    break;
                case 4:
                    scanner.nextLine(); // Consumir la nueva línea en el búfer
                    modificarPunto(scanner, gestorDB);
                    break;
                case 5:
                    salir = true;
                    gestorDB.cerrarSesion();
                    break;
                default:
                    System.out.println("Solo números entre 1 y 5");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("###################################################################");
        System.out.println("##                            MENÚ                               ##");
        System.out.println("## 1. Mostrar puntos registrados                                 ##");
        System.out.println("## 2. Insertar punto                                             ##");
        System.out.println("## 3. Eliminar punto                                             ##");
        System.out.println("## 4. Modificar punto                                            ##");
        System.out.println("## 5. Salir                                                      ##");
        System.out.println("###################################################################");
    }

    private void mostrarPuntos() {
        scanner.nextLine(); // Consumir la nueva línea en el búfer
        System.out.print("Introduce el Tooltip del punto que deseas mostrar: ");
        String tooltipToShow = scanner.nextLine();
        gestorDB.mostrarPuntos(tooltipToShow);
    }

    public static void insertarPunto(Scanner scanner, GestorDB gestorDB) {
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

    public static void modificarPunto(Scanner scanner, GestorDB gestorDB) {

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

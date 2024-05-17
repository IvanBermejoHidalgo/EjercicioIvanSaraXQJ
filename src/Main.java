import javax.xml.xquery.XQException;

public class Main {
    public static void main(String[] args) {
        try {
            Menu menu = new Menu();
            menu.run();
        } catch (XQException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

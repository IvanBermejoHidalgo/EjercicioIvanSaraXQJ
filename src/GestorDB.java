import javax.xml.xquery.*;
import net.xqj.exist.ExistXQDataSource;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.xml.sax.InputSource;

public class GestorDB {
    private XQConnection conn;
    //private Document document;
    private String collectionPath = "/db/EjercicioIvanSara/TRANSPORTS_GEOXML.xml";

    public GestorDB(String s) throws XQException {
        XQDataSource xqs = new ExistXQDataSource();
        xqs.setProperty("serverName", "localhost");
        xqs.setProperty("port", "8080");
        conn = xqs.getConnection();
    }

    private Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public void mostrarPuntos(String tooltip) {
        try {
            String query = "for $p in doc('" + collectionPath + "')//Punt where $p/Tooltip/text() = '" + tooltip + "' return $p";
            XQPreparedExpression expr = conn.prepareExpression(query);
            XQResultSequence result = expr.executeQuery();
            if (result.next()) {
                String xmlContent = result.getItemAsString(null);
                Document document = loadXMLFromString(xmlContent);
                System.out.println("Contenido del XML para el punto con tooltip '" + tooltip + "':\n" + xmlContent);
            } else {
                System.out.println("Punto con Tooltip '" + tooltip + "' no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar los puntos: " + e.getMessage());
        }
    }





    public void insertarPunto(Dades punt) {
        try {
            String query = "update insert " + puntToElementoString(punt) + " into doc('" + collectionPath + "')/Guiamap_Xchange";
            XQExpression expr = conn.createExpression();
            expr.executeCommand(query);
            System.out.println("Punto insertado correctamente.");
        } catch (XQException e) {
            System.out.println("Error al insertar el punto en la base de datos: " + e.getMessage());
        }
    }

    private String puntToElementoString(Dades punt) {
        String elemento = "<Punt>" +
                "<Coord>" +
                "<ED50_COORD_X>" + punt.getEd50CoordX() + "</ED50_COORD_X>" +
                "<ED50_COORD_Y>" + punt.getEd50CoordY() + "</ED50_COORD_Y>" +
                "<ETRS89_COORD_X>" + punt.getEtrs89CoordX() + "</ETRS89_COORD_X>" +
                "<ETRS89_COORD_Y>" + punt.getEtrs89CoordY() + "</ETRS89_COORD_Y>" +
                "<Longitud>" + punt.getLongitud() + "</Longitud>" +
                "<Latitud>" + punt.getLatitud() + "</Latitud>" +
                "</Coord>" +
                "<Icon>" + punt.getIcon() + "</Icon>" +
                "<Tooltip>" + punt.getTooltip() + "</Tooltip>" +
                "<URL>" + punt.getUrl() + "</URL>" +
                "</Punt>";
        return elemento;
    }


    public void eliminarPunto(String tooltip) throws Exception {
        try {
            String query = "update delete doc('" + collectionPath + "')//Punt[Tooltip='" + tooltip + "']";
            XQExpression expr = conn.createExpression();
            expr.executeCommand(query);
            System.out.println("Punto eliminado correctamente.");
        } catch (XQException e) {
            System.out.println("Error al eliminar el punto en la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void modificarPunto(String oldTooltip, String newTooltip, double ed50CoordX, double ed50CoordY, double etrs89CoordX, double etrs89CoordY, double longitud, double latitud, String icon, String url) {
        try {
            // Construye la consulta XQuery para actualizar el punto con el tooltip antiguo
            String query = "update replace doc('" + collectionPath + "')//Punt[Tooltip='" + oldTooltip + "']" +
                    " with <Punt>" +
                    "<Coord>" +
                    "<ED50_COORD_X>" + ed50CoordX + "</ED50_COORD_X>" +
                    "<ED50_COORD_Y>" + ed50CoordY + "</ED50_COORD_Y>" +
                    "<ETRS89_COORD_X>" + etrs89CoordX + "</ETRS89_COORD_X>" +
                    "<ETRS89_COORD_Y>" + etrs89CoordY + "</ETRS89_COORD_Y>" +
                    "<Longitud>" + longitud + "</Longitud>" +
                    "<Latitud>" + latitud + "</Latitud>" +
                    "</Coord>" +
                    "<Icon>" + icon + "</Icon>" +
                    "<Tooltip>" + newTooltip + "</Tooltip>" +
                    "<URL>" + url + "</URL>" +
                    "</Punt>";

            XQExpression expr = conn.createExpression();
            expr.executeCommand(query);

            System.out.println("Punto modificado correctamente.");
        } catch (XQException e) {
            System.out.println("Error al modificar el punto en la base de datos: " + e.getMessage());
        }
    }


    public void cerrarSesion() throws XQException {
        if (conn != null) {
            conn.close();
            System.out.println("Sesión cerrada.");
        }
    }

}

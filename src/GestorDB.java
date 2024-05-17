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
    private Document document;
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

    private String getDocumentAsString() throws XQException {
        String query = "doc('" + collectionPath + "')";
        XQPreparedExpression expr = conn.prepareExpression(query);
        XQResultSequence result = expr.executeQuery();
        result.next();
        return result.getItemAsString(null);
    }

    private void saveDocument(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();

        String query = "xmldb:store('/db/EjercicioIvanSara/TRANSPORTS_GEOXML.xml', " + "'" + xmlString + "')";
        XQPreparedExpression expr = conn.prepareExpression(query);
        expr.executeQuery();
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

            // Ejecuta la consulta XQuery para actualizar el punto en la base de datos
            XQExpression expr = conn.createExpression();
            expr.executeCommand(query);

            System.out.println("Punto modificado correctamente.");
        } catch (XQException e) {
            System.out.println("Error al modificar el punto en la base de datos: " + e.getMessage());
        }
    }




    private void guardarCambios() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(collectionPath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public void cerrarSesion() throws XQException {
        if (conn != null) {
            conn.close();
            System.out.println("Sesión cerrada.");
        }
    }

    private Element puntToElemento(Dades punt) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element puntElement = doc.createElement("Punt");
            Element coordElement = doc.createElement("Coord");

            Element ed50CoordX = doc.createElement("ED50_COORD_X");
            ed50CoordX.appendChild(doc.createTextNode(String.valueOf(punt.getEd50CoordX())));
            coordElement.appendChild(ed50CoordX);

            Element ed50CoordY = doc.createElement("ED50_COORD_Y");
            ed50CoordY.appendChild(doc.createTextNode(String.valueOf(punt.getEd50CoordY())));
            coordElement.appendChild(ed50CoordY);

            Element etrs89CoordX = doc.createElement("ETRS89_COORD_X");
            etrs89CoordX.appendChild(doc.createTextNode(String.valueOf(punt.getEtrs89CoordX())));
            coordElement.appendChild(etrs89CoordX);

            Element etrs89CoordY = doc.createElement("ETRS89_COORD_Y");
            etrs89CoordY.appendChild(doc.createTextNode(String.valueOf(punt.getEtrs89CoordY())));
            coordElement.appendChild(etrs89CoordY);

            Element longitud = doc.createElement("Longitud");
            longitud.appendChild(doc.createTextNode(String.valueOf(punt.getLongitud())));
            coordElement.appendChild(longitud);

            Element latitud = doc.createElement("Latitud");
            latitud.appendChild(doc.createTextNode(String.valueOf(punt.getLatitud())));
            coordElement.appendChild(latitud);

            puntElement.appendChild(coordElement);

            Element icon = doc.createElement("Icon");
            icon.appendChild(doc.createTextNode(punt.getIcon()));
            puntElement.appendChild(icon);

            Element tooltip = doc.createElement("Tooltip");
            tooltip.appendChild(doc.createTextNode(punt.getTooltip()));
            puntElement.appendChild(tooltip);

            Element url = doc.createElement("URL");
            url.appendChild(doc.createTextNode(punt.getUrl()));
            puntElement.appendChild(url);

            return puntElement;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Dades elementoToPunt(Element elemento) {
        Element coordElement = (Element) elemento.getElementsByTagName("Coord").item(0);

        double ed50CoordX = Double.parseDouble(coordElement.getElementsByTagName("ED50_COORD_X").item(0).getTextContent());
        double ed50CoordY = Double.parseDouble(coordElement.getElementsByTagName("ED50_COORD_Y").item(0).getTextContent());
        double etrs89CoordX = Double.parseDouble(coordElement.getElementsByTagName("ETRS89_COORD_X").item(0).getTextContent());
        double etrs89CoordY = Double.parseDouble(coordElement.getElementsByTagName("ETRS89_COORD_Y").item(0).getTextContent());
        double longitud = Double.parseDouble(coordElement.getElementsByTagName("Longitud").item(0).getTextContent());
        double latitud = Double.parseDouble(coordElement.getElementsByTagName("Latitud").item(0).getTextContent());
        String icon = elemento.getElementsByTagName("Icon").item(0).getTextContent();
        String tooltip = elemento.getElementsByTagName("Tooltip").item(0).getTextContent();
        String url = elemento.getElementsByTagName("URL").item(0).getTextContent();

        return new Dades(ed50CoordX, ed50CoordY, etrs89CoordX, etrs89CoordY, longitud, latitud, icon, tooltip, url);
    }

}

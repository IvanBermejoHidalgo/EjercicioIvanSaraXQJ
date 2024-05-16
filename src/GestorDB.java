import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class GestorDB {
    private String filePath;
    private Document document;

    public GestorDB(String filePath) {
        this.filePath = filePath;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(filePath);
            if (file.exists()) {
                document = builder.parse(file);
            } else {
                document = builder.newDocument();
                Element root = document.createElement("Guiamap_Xchange");
                root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                document.appendChild(root);
                guardarCambios();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarPuntos() {
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                System.out.println(elementoToPunt(elemento));
            }
        }
    }

    public void insertarPunto(Dades punt) {
        Element puntoElement = puntToElemento(punt);
        document.getDocumentElement().appendChild(puntoElement);
        guardarCambios();
        System.out.println("Punto insertado correctamente.");
    }

    // En la clase GestorDB
    public void eliminarPunto(String tooltip) {
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        boolean found = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                Element coordElement = (Element) elemento.getElementsByTagName("Coord").item(0);
                //double x = Double.parseDouble(coordElement.getElementsByTagName("ED50_COORD_X").item(0).getTextContent());
                //double y = Double.parseDouble(coordElement.getElementsByTagName("ED50_COORD_Y").item(0).getTextContent());
                String tooltipElement = elemento.getElementsByTagName("Tooltip").item(0).getTextContent();
                if (tooltipElement.equals(tooltip)) {
                    document.getDocumentElement().removeChild(node);
                    guardarCambios();
                    System.out.println("Punto eliminado correctamente.");
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("Punto no encontrado o el Tooltip proporcionado no coincide.");
        }
    }


    public void modificarPunto(String oldTooltip, String newTooltip, double ed50CoordX, double ed50CoordY, double etrs89CoordX, double etrs89CoordY, double longitud, double latitud, String icon, String url) {
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        boolean found = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                String tooltipElement = elemento.getElementsByTagName("Tooltip").item(0).getTextContent();
                if (tooltipElement.equals(oldTooltip)) {
                    Element coordElement = (Element) elemento.getElementsByTagName("Coord").item(0);
                    coordElement.getElementsByTagName("ED50_COORD_X").item(0).setTextContent(String.valueOf(ed50CoordX));
                    coordElement.getElementsByTagName("ED50_COORD_Y").item(0).setTextContent(String.valueOf(ed50CoordY));
                    coordElement.getElementsByTagName("ETRS89_COORD_X").item(0).setTextContent(String.valueOf(etrs89CoordX));
                    coordElement.getElementsByTagName("ETRS89_COORD_Y").item(0).setTextContent(String.valueOf(etrs89CoordY));
                    coordElement.getElementsByTagName("Longitud").item(0).setTextContent(String.valueOf(longitud));
                    coordElement.getElementsByTagName("Latitud").item(0).setTextContent(String.valueOf(latitud));
                    elemento.getElementsByTagName("Icon").item(0).setTextContent(icon);
                    elemento.getElementsByTagName("Tooltip").item(0).setTextContent(newTooltip);
                    elemento.getElementsByTagName("URL").item(0).setTextContent(url);
                    guardarCambios();
                    System.out.println("Punto modificado correctamente.");
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("Punto no encontrado o el Tooltip proporcionado no coincide.");
        }
    }

    private void guardarCambios() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() {
        System.out.println("Sesión cerrada.");
    }

    private Element puntToElemento(Dades punt) {
        Element puntElement = document.createElement("Punt");

        Element coordElement = document.createElement("Coord");

        Element ed50CoordX = document.createElement("ED50_COORD_X");
        ed50CoordX.appendChild(document.createTextNode(String.valueOf(punt.getEd50CoordX())));
        coordElement.appendChild(ed50CoordX);

        Element ed50CoordY = document.createElement("ED50_COORD_Y");
        ed50CoordY.appendChild(document.createTextNode(String.valueOf(punt.getEd50CoordY())));
        coordElement.appendChild(ed50CoordY);

        Element etrs89CoordX = document.createElement("ETRS89_COORD_X");
        etrs89CoordX.appendChild(document.createTextNode(String.valueOf(punt.getEtrs89CoordX())));
        coordElement.appendChild(etrs89CoordX);

        Element etrs89CoordY = document.createElement("ETRS89_COORD_Y");
        etrs89CoordY.appendChild(document.createTextNode(String.valueOf(punt.getEtrs89CoordY())));
        coordElement.appendChild(etrs89CoordY);

        Element longitud = document.createElement("Longitud");
        longitud.appendChild(document.createTextNode(String.valueOf(punt.getLongitud())));
        coordElement.appendChild(longitud);

        Element latitud = document.createElement("Latitud");
        latitud.appendChild(document.createTextNode(String.valueOf(punt.getLatitud())));
        coordElement.appendChild(latitud);

        puntElement.appendChild(coordElement);

        Element icon = document.createElement("Icon");
        icon.appendChild(document.createTextNode(punt.getIcon()));
        puntElement.appendChild(icon);

        Element tooltip = document.createElement("Tooltip");
        tooltip.appendChild(document.createTextNode(punt.getTooltip()));
        puntElement.appendChild(tooltip);

        Element url = document.createElement("URL");
        url.appendChild(document.createTextNode(punt.getUrl()));
        puntElement.appendChild(url);

        return puntElement;
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

package es.hotelsol.reservas;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HotelXmlService {
    public List<Habitacion> cargarHabitaciones() throws Exception {
        try (InputStream input = getClass().getResourceAsStream("/data/reservas.xml")) {
            if (input == null) {
                throw new IllegalStateException("No se encuentra /data/reservas.xml en los recursos.");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document = factory.newDocumentBuilder().parse(input);
            NodeList nodes = document.getElementsByTagName("habitacion");
            List<Habitacion> habitaciones = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                habitaciones.add(new Habitacion(
                        Integer.parseInt(e.getAttribute("numero")),
                        e.getAttribute("estado"),
                        Integer.parseInt(e.getAttribute("planta")),
                        texto(e, "tipo"),
                        Integer.parseInt(texto(e, "capacidad")),
                        Double.parseDouble(texto(e, "precioNoche")),
                        texto(e, "vista"),
                        texto(e, "accesible")
                ));
            }
            return habitaciones;
        }
    }

    private String texto(Element parent, String tag) {
        return parent.getElementsByTagName(tag).item(0).getTextContent().trim();
    }
}
package gestionProyectos.dataAccesLayer;

import gestionProyectos.domainLayer.Proyect;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProyectFileStore implements IFileStore<Proyect> {
    private final File xmlFile;

    public ProyectFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile(this.xmlFile);
    }

    @Override
    public List<Proyect> readAll() {
        List<Proyect> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Proyect.class);
            Unmarshaller u = ctx.createUnmarshaller();

            NodeList proyectNodes = doc.getElementsByTagName("proyect");

            for (int i = 0; i < proyectNodes.getLength(); i++) {
                Node proyectNode = proyectNodes.item(i);
                if (proyectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Proyect proyect = (Proyect) u.unmarshal(proyectNode);
                    out.add(proyect);
                }
            }

        } catch (Exception ex) {
            System.err.println("[WARN] Error leyendo " + xmlFile + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void writeAll(List<Proyect> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Proyect.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("proyects");

            if (data != null) {
                for (Proyect p : data) {
                    m.marshal(p, xw);
                }
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            System.err.println("[WARN] Error escribiendo " + xmlFile);
            ex.printStackTrace();
        }
    }
}
package gestionProyectos.dataAccesLayer;

import gestionProyectos.domainLayer.User;
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

public class UserFileStore implements IFileStore<User> {
    private final File xmlFile;

    public UserFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile(this.xmlFile);
    }

    @Override
    public List<User> readAll() {
        List<User> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(User.class);
            Unmarshaller u = ctx.createUnmarshaller();

            NodeList userNodes = doc.getElementsByTagName("user");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Node equipoNode = userNodes.item(i);
                if (equipoNode.getNodeType() == Node.ELEMENT_NODE) {
                    User equipo = (User) u.unmarshal(equipoNode);
                    out.add(equipo);
                }
            }

        } catch (Exception ex) {
            System.err.println("[WARN] Error leyendo " + xmlFile + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void writeAll(List<User> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(User.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("data");
            xw.writeStartElement("users");

            if (data != null) {
                for (User c : data) {
                    m.marshal(c, xw);
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

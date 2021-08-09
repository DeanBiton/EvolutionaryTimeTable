package Xml;
import javax.xml.bind.*;
import java.io.File;
import Xml.JAXBClasses.*;

public class SchoolHourXMLLoader {

    public static ETTDescriptor LoadXML(String XMLPath) throws Exception {
        JAXBContext jaxbContext;
        ETTDescriptor descriptor = null;

        try {
            File xmlFile = getXMLFile(XMLPath);
            jaxbContext = getJAXBContent(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = getUnmarshaller(jaxbContext);
            descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);

            if(descriptor == null)
            {
                throw new RuntimeException("Error in creating the descriptor object");
            }
        }
        catch (JAXBException e) {
            throw e;
        }
        return descriptor;
    }

    private static Unmarshaller getUnmarshaller(JAXBContext jaxbContext) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = null;

        try
        {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        }
        catch (JAXBException e) {
            throw new JAXBException("An error was encountered while creating the Unmarshaller object");
        }

        return jaxbUnmarshaller;
    }

    private static JAXBContext getJAXBContent(Class klass) throws JAXBException {
        if(klass == null)
        {
            throw new NullPointerException("The class given for creating JAXBContext is null");
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(klass);

        return jaxbContext;
    }

    private static File getXMLFile(String XMLPath) {
        if(XMLPath == null)
        {
            throw new NullPointerException("The path given is null");
        }

        if(!XMLPath.endsWith(".xml"))
        {
            throw new IllegalArgumentException("The file is not a xml file");
        }

        File xmlFile = new File(XMLPath);
        if(!xmlFile.exists())
        {
            throw new IllegalArgumentException("The file does not exist");
        }

        return xmlFile;
    }
}

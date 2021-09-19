package Problem;

import Engine.SchoolHourManager;
import Engine.Xml.JAXBClasses.ETTDescriptor;
import Engine.Xml.SchoolHourXMLLoader;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.InputStream;

public class TimeTableProblem {

    private ETTDescriptor descriptor;
    private SchoolHourManager schoolHourManager;
    private int numberOfTriedUsers=0;
    private String uploadUser;
    private double maxFitness;

    public TimeTableProblem(InputStream xmlInputStream,String uploaderName) throws Exception {
        descriptor = SchoolHourXMLLoader.LoadXML(xmlInputStream);
        schoolHourManager=new SchoolHourManager();
        schoolHourManager.LoadXML(descriptor);
        this.uploadUser=uploaderName;
    }

  //  public File getXmlFile() {
 //       return xmlFile;
 //   }

    public SchoolHourManager getSchoolHourManager() {
        return schoolHourManager;
    }

    public int getNumberOfTriedUsers() {
        return numberOfTriedUsers;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public double getMaxFitness() {
        return maxFitness;
    }
}

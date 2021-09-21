package Problem;

import Engine.SchoolHourManager;
import Engine.Xml.JAXBClasses.ETTDescriptor;
import Engine.Xml.SchoolHourXMLLoader;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TimeTableProblem {

    private int id;
    private ETTDescriptor descriptor;
    private SchoolHourManager schoolHourManager;
    private int numberOfTriedUsers=0;
    private String uploadUser;
    private double maxFitness;
    private Map<String,SchoolHourManager> user2manager;

    public TimeTableProblem(InputStream xmlInputStream,String uploaderName, int id) throws Exception {
        descriptor = SchoolHourXMLLoader.LoadXML(xmlInputStream);
        schoolHourManager=new SchoolHourManager();
        schoolHourManager.LoadXML(descriptor);
        this.uploadUser=uploaderName;
        this.id=id;
        user2manager=new HashMap<>();
    }

    public void addUserManager(String userName)
    {
        SchoolHourManager localManager= new SchoolHourManager();
        try {
            localManager.LoadXML(descriptor);
        } catch (Exception e) {

        }
        user2manager.putIfAbsent(userName,localManager);
    }

    public SchoolHourManager getUserManager(String userName)
    {
        return user2manager.get(userName);
    }


    public SchoolHourManager getSchoolHourManager() {
        return schoolHourManager;
    }

    public int getId() {
        return id;
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

package Problem;

import Engine.SchoolHourManager;
import Engine.Xml.JAXBClasses.ETTDescriptor;
import Engine.Xml.SchoolHourXMLLoader;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TimeTableProblem {

    private int id;
    private ETTDescriptor descriptor;
    private SchoolHourManager schoolHourManager;
    private String uploadUser;
    private Map<String,SchoolHourManager> user2manager;
    private double fitness = 0;

    public Map<String, SchoolHourManager> getUser2manager() {
        return Collections.unmodifiableMap(user2manager);
    }

    public TimeTableProblem(InputStream xmlInputStream, String uploaderName, int id) throws Exception {
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
        return user2manager.size();
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public double getMaxFitness() {
        double maxFitness = 0;

        if(user2manager.size() != 0)
        {
            maxFitness = user2manager.values().stream().
                    mapToDouble(manager -> {
                        double fitness = 0;

                        try {
                            fitness = manager.getMaximumFitnessFound();
                        }
                        catch (Exception exception){}

                        return fitness;
                    }).
                    max().getAsDouble();
        }

        if(maxFitness > fitness)
            fitness = maxFitness;

        return fitness;
    }
}

package Problem;

import Engine.SchoolHourManager;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ProblemsManager {
    List<TimeTableProblem> problems;

    public ProblemsManager() {
        problems =new LinkedList<>();
    }

    public synchronized void addproblem(InputStream xmlFile,String uploaderName) throws Exception {

        problems.add(new TimeTableProblem(xmlFile, uploaderName,problems.size()+1));
    }

    public synchronized List<TimeTableProblem> getProblems() {
        return Collections.unmodifiableList(problems);
    }

    public SchoolHourManager getSchoolHourManager(int id, String userName)
    {
        return problems.get(id-1).getUserManager(userName);
    }
}

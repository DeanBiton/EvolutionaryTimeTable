package Engine;

import Engine.DTO.*;
import Engine.Evolution.*;
import Engine.Evolution.EndCondition.EndCondition;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.MyMutation.Mutation;
import Engine.Evolution.MySolution.SchoolHourData;
import Engine.Evolution.MySolution.TupleGroup;

import Engine.Evolution.Selection.Selection;
import Engine.Xml.JAXBClasses.ETTDescriptor;
import Engine.Xml.SchoolHourXMLLoader;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class SchoolHourManager {

    private SchoolHourData data;
    private DTOSchoolHoursData dtoData;
    private SchoolHourEvolutionaryAlgorithm schoolHourEvolutionaryAlgorithm;
    private Thread currentThread = null;
    private final String saveAndLoadFileEnding = ".txt";

    private boolean xmlFileLoadedSuccessfully = false;
    private boolean isSuspended= false;
    private Boolean evolutionaryAlgorithmRunned = false;

    public void LoadXML(String XMLPath) throws Exception {
        File xmlFile = getXMLFile(XMLPath);
        LoadXML(xmlFile);
    }

    private static File getXMLFile(String XMLPath) {
        if(XMLPath == null)
        {
            throw new ShowException("The path given is null");
        }

        if(!XMLPath.endsWith(".xml"))
        {
            throw new ShowException("The file is not a xml file");
        }

        File xmlFile = new File(XMLPath);
        if(!xmlFile.exists())
        {
            throw new ShowException("The file does not exist");
        }

        return xmlFile;
    }

    public void LoadXML(ETTDescriptor descriptor) throws Exception
    {

        data = new SchoolHourData(descriptor);
        dtoData = new DTOSchoolHoursData(data);
        EvolutionaryAlgorithmData eaData=new EvolutionaryAlgorithmData(data.getMutations());
        schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(eaData, data);


        //  Selection selection=getSelection(discriptor.getETTEvolutionEngine());
        //  Crossover crossover = getCrossover(discriptor.getETTEvolutionEngine().getETTCrossover());
        //   int initialPopulation=discriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
        // schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(initialPopulation, selection, crossover, data);


        stopAlgorithm();
        xmlFileLoadedSuccessfully = true;
        currentThread = null;
        evolutionaryAlgorithmRunned = false;
    }

    public void LoadXML(InputStream XMLfile) throws Exception
    {
        ETTDescriptor discriptor = SchoolHourXMLLoader.LoadXML(XMLfile);
        data = new SchoolHourData(discriptor);
        dtoData = new DTOSchoolHoursData(data);
        EvolutionaryAlgorithmData eaData=new EvolutionaryAlgorithmData(data.getMutations());
        schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(eaData, data);


        //  Selection selection=getSelection(discriptor.getETTEvolutionEngine());
        //  Crossover crossover = getCrossover(discriptor.getETTEvolutionEngine().getETTCrossover());
        //   int initialPopulation=discriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
        // schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(initialPopulation, selection, crossover, data);


        stopAlgorithm();
        xmlFileLoadedSuccessfully = true;
        currentThread = null;
        evolutionaryAlgorithmRunned = false;
    }
    public void LoadXML(File XMLfile) throws Exception
    {
        ETTDescriptor discriptor = SchoolHourXMLLoader.LoadXML(XMLfile);
        data = new SchoolHourData(discriptor);
        dtoData = new DTOSchoolHoursData(data);
        EvolutionaryAlgorithmData eaData=new EvolutionaryAlgorithmData(data.getMutations());
        schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(eaData, data);


        //  Selection selection=getSelection(discriptor.getETTEvolutionEngine());
        //  Crossover crossover = getCrossover(discriptor.getETTEvolutionEngine().getETTCrossover());
        //   int initialPopulation=discriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
        // schoolHourEvolutionaryAlgorithm = new SchoolHourEvolutionaryAlgorithm(initialPopulation, selection, crossover, data);


        stopAlgorithm();
        xmlFileLoadedSuccessfully = true;
        currentThread = null;
        evolutionaryAlgorithmRunned = false;
    }

    public DTODataAndAlgorithmSettings getDataAndAlgorithmSettings(){
        if(!xmlFileLoadedSuccessfully)
        {
            throw new RuntimeException("Engine.SchoolHourManager did not load a file");
        }

        DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings = new DTOEvolutionaryAlgorithmSettings(
                schoolHourEvolutionaryAlgorithm.getInitialPopulation(),
                dtoData.getDtoMutations(),
                DTOSelection.getDTOSelection(schoolHourEvolutionaryAlgorithm.getSelection()),
                DTOCrossover.getDTOCrossover(schoolHourEvolutionaryAlgorithm.getCrossover()),
                schoolHourEvolutionaryAlgorithm.getDTOAlgorithmEndConditions());

        return new DTODataAndAlgorithmSettings(dtoEvolutionaryAlgorithmSettings, dtoData);
    }

    public void runEvolutionaryAlgorithm(List<EndCondition> endConditions, int printEveryThisNumberOfGenerations, SchoolHourUIAdapter uiAdapter)
    {
        if(!xmlFileLoadedSuccessfully)
        {
            throw new RuntimeException("Engine.SchoolHourManager did not load a file");
        }
        stopAlgorithm();
        currentThread=new Thread(() -> {
            schoolHourEvolutionaryAlgorithm.runAlgorithm(endConditions, printEveryThisNumberOfGenerations, uiAdapter);
            synchronized (evolutionaryAlgorithmRunned)
            {
                evolutionaryAlgorithmRunned = true;
            }
            uiAdapter.algorithmEnded();
        }
                ,"EvolutionaryAlgorithm");
        currentThread.start();

    }

    public DTOTupleGroupWithFitnessDetails getBestSolution()
    {
        if(schoolHourEvolutionaryAlgorithm==null||!schoolHourEvolutionaryAlgorithm.isActivated())
        {
            throw new RuntimeException("Engine.SchoolHourManager is not running the algorithm");
        }

        return new DTOTupleGroupWithFitnessDetails((TupleGroup)schoolHourEvolutionaryAlgorithm.getBestSolution(), dtoData);
    }

    public DTOEveryGenAndItsBestSolution getEveryGenAndItsBestSolution()
    {
        if(schoolHourEvolutionaryAlgorithm==null||!schoolHourEvolutionaryAlgorithm.isActivated())
        {
            throw new RuntimeException("Engine.SchoolHourManager is not running the algorithm");
        }

        boolean showFullList;
        if(isEvolutionaryAlgurithmIsRunning())
        {
            showFullList = false;
        }
        else
        {
            showFullList = true;
        }
        return new DTOEveryGenAndItsBestSolution(schoolHourEvolutionaryAlgorithm.getEveryGenAndItsBestSolution(), showFullList);
    }

    public void stopAlgorithm()
    {
        if(currentThread!=null)
        {
            if(currentThread.isAlive())
            {
                currentThread.interrupt();
                try {
                    currentThread.join();
                    isSuspended = false;
                } catch (InterruptedException e) {}
            }
        }
    }

    public void suspend(){
        if(currentThread!=null)
        {
            if(currentThread.isAlive()) {

                schoolHourEvolutionaryAlgorithm.suspend();
                isSuspended=true;
            }
        }
    }

    public void resume() {
        if(isSuspended)
            schoolHourEvolutionaryAlgorithm.resume();

        isSuspended = false;
    }

    public void LoadFile(String filePath){
        if(!CheckFileEndsWith(filePath,saveAndLoadFileEnding))
            throw new ShowException("Load fail, doesnt end with "+saveAndLoadFileEnding);

        if(!CheckFileExist(filePath))
            throw new ShowException("file doesnt exist");

        try{
            ObjectInputStream objectInputStream= new ObjectInputStream(new FileInputStream(filePath));

            data = (SchoolHourData) objectInputStream.readObject();
            schoolHourEvolutionaryAlgorithm=(SchoolHourEvolutionaryAlgorithm)objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            throw  new ShowException("File load failed");
        }

        dtoData = new DTOSchoolHoursData(data);
        stopAlgorithm();
        xmlFileLoadedSuccessfully = true;
        evolutionaryAlgorithmRunned = schoolHourEvolutionaryAlgorithm.getBestSolution() != null;
    }

    public boolean CheckFileEndsWith(String filePath,String endString)
    {
        return filePath.endsWith(endString);
    }

    public boolean CheckFileExist(String filePath)
    {
        File file= Paths.get(filePath).toFile();
        return file.exists();
    }

    public void SaveFile(String filePath) {
        if(!CheckFileEndsWith(filePath,saveAndLoadFileEnding))
            throw new ShowException("save fail, doesnt end with"+saveAndLoadFileEnding);

        stopAlgorithm();

        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            objectOutputStream.writeObject(data);
            objectOutputStream.writeObject(schoolHourEvolutionaryAlgorithm);

            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (Exception e){
            throw  new ShowException("File save failed");
        }
    }

    public boolean isXMLFileLoadedSuccessfully() {
        return xmlFileLoadedSuccessfully;
    }

    public boolean isEvolutionaryAlgurithmIsRunning() {
        boolean evolutionaryAlgurithmIsRunning = false;

        if(currentThread != null)
        {
            if(currentThread.isAlive())
            {
                evolutionaryAlgurithmIsRunning = true;
            }
        }

        return evolutionaryAlgurithmIsRunning;
    }

    public boolean getEvolutionaryAlgorithmRunned() {
        synchronized (evolutionaryAlgorithmRunned)
        {
            return evolutionaryAlgorithmRunned;
        }
    }

    public String getSaveAndLoadFileEnding() {
        return saveAndLoadFileEnding;
    }

    public void setCrossover(Crossover crossover)
    {
        schoolHourEvolutionaryAlgorithm.setCrossover(crossover);
    }

    public void setSelection(Selection selection) {
        schoolHourEvolutionaryAlgorithm.setSelection(selection);
    }

    public void setMutations(List<Mutation> mutations){
        schoolHourEvolutionaryAlgorithm.setMutations(mutations);
        dtoData = new DTOSchoolHoursData(data);
    }

    public void setInitialPopulation(int initialPopulation)
    {
        schoolHourEvolutionaryAlgorithm.setInitialPopulation(initialPopulation);
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public DTOAlgorithmContidions getAlgorithmConditions() {return new DTOAlgorithmContidions(schoolHourEvolutionaryAlgorithm.getEndCondtionsStatus());}

    public void setEndConditions(List<EndCondition> endConditions)
    {
        schoolHourEvolutionaryAlgorithm.setEndConditions(endConditions);
    }
}


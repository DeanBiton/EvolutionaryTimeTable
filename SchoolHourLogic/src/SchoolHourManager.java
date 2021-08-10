import DTO.*;
import Evolution.*;
import Evolution.EndCondition.EndCondition;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.MySolution.MyMutation.Mutation;
import Evolution.MySolution.SchoolHourData;
import Evolution.MySolution.TupleGroup;

import Evolution.Selection.Selection;
import Xml.JAXBClasses.ETTDescriptor;
import Xml.SchoolHourXMLLoader;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class SchoolHourManager {

    public SchoolHourData data;
    private DTOSchoolHoursData dtoData;
    private SchoolHourEvolutionaryAlgorithm schoolHourEvolutionaryAlgorithm;
    private boolean xmlFileLoadedSuccessfully = false;
    private Boolean evolutionaryAlgorithmRunned = false;
    private Thread currentThread = null;
    private final String saveAndLoadFileEnding = ".txt";
    private boolean isSuspended= false;


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

    public void LoadXML(String XMLPath) throws Exception {
        ETTDescriptor discriptor = SchoolHourXMLLoader.LoadXML(XMLPath);
        data = new SchoolHourData(discriptor);
        dtoData = new DTOSchoolHoursData(data);
        EvolutionaryAlgorithmData eaData=new EvolutionaryAlgorithmData(discriptor.getETTEvolutionEngine());
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
            throw new RuntimeException("SchoolHourManager did not load a file");
        }

        DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings = new DTOEvolutionaryAlgorithmSettings(
                schoolHourEvolutionaryAlgorithm.eaData.getInitialPopulation(),
                dtoData.getDtoMutations(),
                DTOSelection.getDTOSelection(schoolHourEvolutionaryAlgorithm.eaData.getSelection()),
                DTOCrossover.getDTOCrossover(schoolHourEvolutionaryAlgorithm.eaData.getCrossover()));

        return new DTODataAndAlgorithmSettings(dtoEvolutionaryAlgorithmSettings, dtoData);
    }

    public void runEvolutionaryAlgorithm(List<EndCondition> endConditions, int printEveryThisNumberOfGenerations)
    {
        if(!xmlFileLoadedSuccessfully)
        {
            throw new RuntimeException("SchoolHourManager did not load a file");
        }
        stopAlgorithm();
        currentThread=new Thread(() -> {
            schoolHourEvolutionaryAlgorithm.runAlgorithm(endConditions, printEveryThisNumberOfGenerations);
            synchronized (evolutionaryAlgorithmRunned)
            {
                evolutionaryAlgorithmRunned = true;
            }
        }
                ,"EvolutionaryAlgorithm");
        currentThread.start();
    }

    public DTOTupleGroupWithFitnessDetails getBestSolution()
    {
        if(schoolHourEvolutionaryAlgorithm==null||!schoolHourEvolutionaryAlgorithm.isActivated())
        {
            throw new RuntimeException("SchoolHourManager is not running the algorithm");
        }

        return new DTOTupleGroupWithFitnessDetails((TupleGroup)schoolHourEvolutionaryAlgorithm.eaData.getBestSolution(), dtoData);
    }

    public DTOEveryGenAndItsBestSolution getEveryGenAndItsBestSolution()
    {
        if(schoolHourEvolutionaryAlgorithm==null||!schoolHourEvolutionaryAlgorithm.isActivated())
        {
            throw new RuntimeException("SchoolHourManager is not running the algorithm");
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
        return new DTOEveryGenAndItsBestSolution(schoolHourEvolutionaryAlgorithm.eaData.getEveryGenAndItsBestSolution(), showFullList);
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
    public void resume()
    {
        if(isSuspended)
            schoolHourEvolutionaryAlgorithm.resume();
    }

    public void LoadFile(String filePath){
        if(!CheckFileEndsWith(filePath,saveAndLoadFileEnding))
            throw new RuntimeException("Load fail, doesnt end with "+saveAndLoadFileEnding);

        if(!CheckFileExist(filePath))
            throw new RuntimeException("file doesnt exist");

        try{
            ObjectInputStream objectInputStream= new ObjectInputStream(new FileInputStream(filePath));

            data = (SchoolHourData) objectInputStream.readObject();
            schoolHourEvolutionaryAlgorithm=(SchoolHourEvolutionaryAlgorithm)objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            throw  new RuntimeException("File load failed");
        }

        dtoData = new DTOSchoolHoursData(data);
        stopAlgorithm();
        xmlFileLoadedSuccessfully = true;
        evolutionaryAlgorithmRunned = schoolHourEvolutionaryAlgorithm.eaData.getBestSolution() != null;
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
            throw new RuntimeException("save fail, doesnt end with"+saveAndLoadFileEnding);

        stopAlgorithm();

        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            objectOutputStream.writeObject(data);
            objectOutputStream.writeObject(schoolHourEvolutionaryAlgorithm);

            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (Exception e){
            throw  new RuntimeException("File save failed");
        }
    }

    public void setCrossover(Crossover crossover)
    {
        schoolHourEvolutionaryAlgorithm.eaData.setCrossover(crossover);
    }
    public void setSelection(Selection selection) {schoolHourEvolutionaryAlgorithm.eaData.setSelection(selection);}
    public void setMutationProbability(Mutation mutation,Double probabilty){}
}


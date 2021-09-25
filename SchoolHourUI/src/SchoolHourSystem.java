import Engine.DTO.DTODataAndAlgorithmSettings;
import Engine.SchoolHourManager;
import Engine.Evolution.EndCondition.ByFitness;
import Engine.Evolution.EndCondition.EndCondition;
import Engine.Evolution.EndCondition.NumberOfGenerations;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.Selection.Selection;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SchoolHourSystem {

    private static SchoolHourManager manager;

    public static void run()
    {
        manager = new SchoolHourManager();
        boolean exit = false;
        eMenuOption option;

        while(!exit)
        {
            try {
                option = getUserOption();
                exit = runOption(option);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static List<Integer> getOptionNumbers()
    {
        List<Integer> optionNumbers = new ArrayList<>();

        optionNumbers.add(0);
        optionNumbers.add(1);

        if(manager.isXMLFileLoadedSuccessfully())
        {
            optionNumbers.add(2);
            optionNumbers.add(3);
        }

        if(manager.isEvolutionaryAlgurithmIsRunning() || manager.getEvolutionaryAlgorithmRunned())
        {
            optionNumbers.add(4);
            optionNumbers.add(5);
        }

        if(manager.isXMLFileLoadedSuccessfully())
        {
            optionNumbers.add(6);
        }

        optionNumbers.add(7);

        if(manager.isSuspended())
        {
            optionNumbers.add(9);
        }
        else if(manager.isEvolutionaryAlgurithmIsRunning())
        {
            optionNumbers.add(8);
        }


        optionNumbers.add(10);
        optionNumbers.add(11);

        return optionNumbers;
    }

    private enum eMenuOption
    {
        EXIT{ String getName(){return "Exit"; }},
        LOADXML{ String getName(){return "Load Engine.Xml"; }},
        SHOWSETTINGS{String getName(){return "Show settings"; }},
        RUNEVOLUTIONARYALGORITHM{String getName(){return "Run evolutionary algorithm"; }},
        WATCHTHEBESTSOLUTION{String getName(){return "Watch the best solution"; }},
        WATCHALGORITHMPROCESS{String getName(){return "Watch Algorithm Process"; }},
        SAVE{String getName(){return "Save to file"; }},
        LOAD{String getName(){return "Load from file"; }},
        PAUSEALGORITM{String getName(){return "Pause algorithm"; }},
        RESUMEALGORITHM{String getName(){return "resume algorithm"; }},
        SETSELECTION{String getName(){return "Set selection"; }},
        SETCROSSOVER{String getName(){return "Set crossover"; }}
        ;

        abstract String getName();

        private static String getMainMenuString(List<Integer> optionNumbers)
        {
            StringBuilder message = new StringBuilder();
            message.append("Choose Menu option number: " + System.lineSeparator());

            for(int i = 0; i < optionNumbers.size(); i++)
            {
                eMenuOption option = eMenuOption.values()[optionNumbers.get(i)];
                message.append(i + ". " + option.getName() + System.lineSeparator());
            }

            return message.toString();
        }
    }

    private static SchoolHourSystem.eMenuOption getUserOption()
    {
        String input;
        int optionNumber;
        boolean validInput = false;
        Scanner sc = new Scanner(System.in);
        List<Integer> optionNumbers = getOptionNumbers();
        SchoolHourSystem.eMenuOption option = null;

        while (!validInput)
        {
            try {
                String string = SchoolHourSystem.eMenuOption.getMainMenuString(optionNumbers);
                System.out.println(string);
                input = sc.nextLine();
                optionNumber = Integer.parseInt(input);

                if(optionNumber >= 0 && optionNumber < optionNumbers.size())
                {
                    option = SchoolHourSystem.eMenuOption.values()[optionNumbers.get(optionNumber)];
                    validInput = true;
                }
                else
                {
                    System.out.println("Please choose a number between 0 and " + (optionNumbers.size() - 1));
                }
            }
            catch (Exception ex)
            {
                System.out.println("The Input is not a number");
            }
        }

        return option;
    }

    private static boolean runOption(eMenuOption option)
    {
        boolean exit = false;

        try {
            switch (option) {
                case EXIT:
                    Exit();
                    exit = true;
                    break;
                case LOADXML:
                    loadXML();
                    break;
                case SHOWSETTINGS:
                    showSettings();
                    break;
                case RUNEVOLUTIONARYALGORITHM:
                    runEvolutionaryAlgorithms();
                    break;
                case WATCHTHEBESTSOLUTION:
                    watchTheBestSolution();
                    break;
                case WATCHALGORITHMPROCESS:
                    watchAlgorithmProcess();
                    break;
                case SAVE:
                    save();
                    break;
                case LOAD:
                    load();
                    break;
                case PAUSEALGORITM:
                    pause();
                    break;
                case RESUMEALGORITHM:
                    resume();
                    break;
                case SETSELECTION:
                    setSelection();
                    break;
                case SETCROSSOVER:
                    setCrossover();
                    break;
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        return exit;
    }

    private static void Exit()
    {
        manager.stopAlgorithm();
    }

    private static void loadXML() throws Exception {

        boolean loadXML = true;

        if(manager.isEvolutionaryAlgurithmIsRunning())
        {
            loadXML = InputsFromUser.getLoadXMLWhileAlgorithmIsRunningConformation();
        }

        if(loadXML)
        {
            String XMLPath = InputsFromUser.getXMLPath();
            manager.LoadXML(XMLPath);
        }
    }

    private static void showSettings()
    {
        DTODataAndAlgorithmSettings dtoDataAndAlgorithmSettings = manager.getDataAndAlgorithmSettings();
        String string = DataAndAlgorithmSettingsStringCreator.getDataAndAlgorithmSettingsString(dtoDataAndAlgorithmSettings);
        System.out.println(string);
    }

    protected enum eEndConditions
    {
        RUNALGORITHM {@Override String getName() { return "Run Algorithm"; }},
        NUMBEROFGENERATIONS{@Override String getName() { return "Number Of Generations"; }},
        BYFITNESS {@Override String getName() { return "By Fitness"; }};


        abstract String getName();

        public static String getSolutionViewString(List<EndCondition> endConditions)
        {
            StringBuilder message = new StringBuilder();
            message.append("Choose end conditions for the algorithm ");
            message.append(System.lineSeparator());
            message.append("To continue choose 0");
            message.append(System.lineSeparator());
            message.append("To cancel a condition choose it again");
            message.append(System.lineSeparator());

            for(int i = 0; i < eEndConditions.values().length; i++)
            {
                eEndConditions option = eEndConditions.values()[i];
                if((option == NUMBEROFGENERATIONS && endConditions.stream().anyMatch(endCondition -> endCondition instanceof NumberOfGenerations)
                )|| (option == BYFITNESS && endConditions.stream().anyMatch(endCondition -> endCondition instanceof ByFitness)))
                {
                    message.append("V ");
                }
                else
                {
                    message.append("  ");
                }
                message.append(i + ". " + option.getName() + System.lineSeparator());
            }

            return message.toString();
        }
    }

    private static void runEvolutionaryAlgorithms()
    {
        boolean runAlgorithm = true;

        if(manager.isEvolutionaryAlgurithmIsRunning())
        {
            runAlgorithm = InputsFromUser.getConfirmationToRunAlgorithm();
        }

        if(runAlgorithm)
        {
            List<EndCondition> endConditions = InputsFromUser.getEndConditions();
            int printEveryThisNumberOfGenerations = InputsFromUser.getPositiveInput("In how many generations would you like the information to be represented?", Integer.class);
          //  manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations, null);
        }
    }

    protected enum eSolutionViewType
    {
        RAW{@Override String getName() { return "Raw"; }},
        TEACHER {@Override String getName() { return "Teacher"; }},
        CLASS {@Override String getName() { return "Class"; }};

        abstract String getName();

        private static String getSolutionViewString()
        {
            StringBuilder message = new StringBuilder();
            message.append("Choose solution view option number: " + System.lineSeparator());

            for(int i = 1; i <= eSolutionViewType.values().length; i++)
            {
                eSolutionViewType option = eSolutionViewType.values()[i - 1];
                message.append(i + ". " + option.getName() + System.lineSeparator());
            }

            return message.toString();
        }
    }

    private static void watchTheBestSolution()
    {
        eSolutionViewType.getSolutionViewString();
        int viewOptionNumber = InputsFromUser.getOptionNumber(1, eSolutionViewType.values().length, eSolutionViewType.getSolutionViewString());
        eSolutionViewType viewType = eSolutionViewType.values()[viewOptionNumber - 1];

        System.out.println(BestSolutionStringCreator.getBestSolutionString(viewType, manager.getBestSolution()));
    }

    private static void watchAlgorithmProcess() {
        List<Pair<Integer, Double>> everyGenAndItsBestSolution = manager.getEveryGenAndItsBestSolution().getEveryGenAndItsBestSolution();
        StringBuilder stringBuilder = new StringBuilder();

        if(everyGenAndItsBestSolution != null)
        {
            if(!everyGenAndItsBestSolution.isEmpty())
            {
                stringBuilder.append("Generation = ");
                stringBuilder.append(everyGenAndItsBestSolution.get(0).getKey());
                stringBuilder.append(", Best solution fitness = ");
                stringBuilder.append(String.format("%.3f", everyGenAndItsBestSolution.get(0).getValue()));
                stringBuilder.append(System.lineSeparator());
            }

            for (int i = 1; i < everyGenAndItsBestSolution.size(); i++)
            {
                if(i % 10000 == 0)
                {
                    System.out.println(stringBuilder);
                    stringBuilder = new StringBuilder();
                }

                stringBuilder.append("Generation = ");
                stringBuilder.append(everyGenAndItsBestSolution.get(i).getKey());
                stringBuilder.append(", Best solution fitness = ");
                stringBuilder.append(String.format("%.3f", everyGenAndItsBestSolution.get(i).getValue()));
                stringBuilder.append(", Change = ");
                stringBuilder.append(String.format("%.3f", everyGenAndItsBestSolution.get(i).getValue() - everyGenAndItsBestSolution.get(i - 1).getValue()));
                stringBuilder.append(System.lineSeparator());
            }

            System.out.println(stringBuilder);
        }
    }

    private static void save()
    {
        boolean saveTheFile = true;
        if(manager.isEvolutionaryAlgurithmIsRunning())
        {
            saveTheFile = InputsFromUser.getStopAlgorithmToSaveFileConformation();
        }

        if(saveTheFile)
        {
            String savePath = InputsFromUser.getSavingOrLoadingPath(manager.getSaveAndLoadFileEnding());
            if(manager.CheckFileExist(savePath))
            {
                saveTheFile = InputsFromUser.getOverrideSaveFileConformation();
            }

            if(saveTheFile)
            {
                manager.SaveFile(savePath);
                System.out.println("File saved");
            }
        }
    }

    private static void load ()
    {
        String loadPath = InputsFromUser.getSavingOrLoadingPath(manager.getSaveAndLoadFileEnding());
        manager.LoadFile(loadPath);
        System.out.println("File loaded");
    }

    private static void pause()
    {
        manager.suspend();
    }

    private static void resume()
    {
        manager.resume();
    }

    private static void setSelection()
    {
        Selection selection = InputsFromUser.getSelection();
        manager.setSelection(selection);
    }

    private static void setCrossover()
    {
        Crossover crossover = InputsFromUser.getCrossover();
        manager.setCrossover(crossover);
    }
}

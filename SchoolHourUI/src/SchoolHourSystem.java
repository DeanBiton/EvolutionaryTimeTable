import DTO.DTODataAndAlgorithmSettings;
import Evolution.EndCondition.ByFitness;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
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

        return optionNumbers;
    }

    private enum eMenuOption
    {
        EXIT{ String getName(){return "Exit"; }},
        LOADXML{ String getName(){return "Load Xml"; }},
        SHOWSETTINGS{String getName(){return "Show settings"; }},
        RUNEVOLUTIONARYALGORITHM{String getName(){return "Run evolutionary algorithm"; }},
        WATCHTHEBESTSOLUTION{String getName(){return "Watch the best solution"; }},
        WATCHALGORITHMPROCESS{String getName(){return "Watch Algorithm Process"; }},
        SAVE{String getName(){return "Save to file"; }},
        LOAD{String getName(){return "Load from file"; }}
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
            manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations);
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
                    System.out.println(stringBuilder.toString());
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

            System.out.println(stringBuilder.toString());
        }
    }

    private static void save() throws Exception
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

    private static void load () throws Exception
    {
        String loadPath = InputsFromUser.getSavingOrLoadingPath(manager.getSaveAndLoadFileEnding());
        manager.LoadFile(loadPath);
        System.out.println("File loaded");
    }
}

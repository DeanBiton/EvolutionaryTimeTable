import Evolution.EndCondition.ByFitness;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputsFromUser {

    public static int getOptionNumber(int minOptionNumber, int maxOptionNumber, String textWithOptions)
    {
        int optionNumber = -1;
        boolean validInput = false;

        while (!validInput)
        {
             optionNumber = getNumberFromUser(textWithOptions, Integer.class);

             if(optionNumber >= minOptionNumber && optionNumber <= maxOptionNumber)
             {
                 validInput = true;
             }
             else
             {
                    System.out.println("Please choose a number between " + minOptionNumber +" and " + maxOptionNumber);
             }
        }

        return optionNumber;
    }

    public static <T> T getNumberFromUser(String messageForUser, Class<T> klass)
    {
        T number = null;
        if(klass != Integer.class && klass != Double.class)
        {
            throw new RuntimeException("Class" + klass.getName() + "is not supported");
        }

        Integer n1 = 0;
        Double n2 = 0.0;
        String input;
        boolean validInput = false;
        Scanner sc = new Scanner(System.in);

        while (!validInput)
        {
            try {
                System.out.println(messageForUser);
                input = sc.nextLine();
                if(klass == Integer.class)
                {
                    n1 = Integer.parseInt(input);
                    validInput = true;
                }
                else if(klass == Double.class)
                {
                    n2 = Double.parseDouble(input);
                    validInput = true;
                }
            }
            catch (Exception ex)
            {
                System.out.println("The Input is not a number");
            }
        }

        if(klass == Integer.class)
        {
            number = (T) n1;
        }
        else if(klass == Double.class)
        {
            number = (T) n2;
        }

        return number;
    }

    public static <T> T getPositiveInput (String messageForUser, Class<T> klass)
    {
        T positiveNumber = null;
        if(klass != Integer.class && klass != Double.class)
        {
            throw new RuntimeException("Class" + klass.getName() + "is not supported");
        }


        boolean validInput = false;

        while (!validInput)
        {
            positiveNumber = getNumberFromUser(messageForUser, klass);
            Integer n1 = 1;
            Double n2 = 1.0;
            if(positiveNumber instanceof Integer)
            {
                 n1 = (Integer) positiveNumber;
            }
            else if(positiveNumber instanceof Double)
            {
                n2 = (Double) positiveNumber;
            }

            if(n1 > 0 && n2 > 0)
            {
                validInput = true;
            }
            else
            {
                System.out.println("Input is not a positive number.");
            }
        }

        return positiveNumber;
    }

    public static boolean getConfirmation(String questionMessage)
    {
        StringBuilder message = new StringBuilder();
        boolean confirmation;
        int optionNumber;

        message.append(questionMessage);
        message.append("1. Yes");
        message.append(System.lineSeparator());
        message.append("2. No");
        message.append(System.lineSeparator());

        optionNumber = getOptionNumber(1, 2, message.toString());
        if(optionNumber == 1)
        {
            confirmation = true;
        }
        else
        {
            confirmation = false;
        }

        return confirmation;
    }

    public static boolean getLoadXMLWhileAlgorithmIsRunningConformation()
    {
        StringBuilder message = new StringBuilder();

        message.append("The algorithm is still running.");
        message.append(System.lineSeparator());
        message.append("Do you wish to stop the algorithm and the load the XML file?");
        message.append(System.lineSeparator());

        return getConfirmation(message.toString());
    }

    public static String getXMLPath()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("please enter XML file path");
        return sc.nextLine();
    }

    public static boolean getConfirmationToRunAlgorithm()
    {
        StringBuilder message = new StringBuilder();

        message.append("The algorithm has already been ran, once you run the algorithm again all past data will be deleted.");
        message.append(System.lineSeparator());
        message.append("Do you wish to run the algorithm?");
        message.append(System.lineSeparator());

        return getConfirmation(message.toString());
    }

    public static int getNumberOfGenerations()
    {
        String message = "Enter number of generations (must be at least 100)";
        boolean validInput = false;
        int numberOfgenerations = 0;

        while (!validInput)
        {
            numberOfgenerations = getPositiveInput(message, Integer.class);
            if(numberOfgenerations >= 100)
            {
                validInput = true;
            }
            else
            {
                System.out.println("Number of generations must be at least 100");
            }
        }

        return numberOfgenerations;
    }

    public static List<EndCondition> getEndConditions()
    {
        List<EndCondition> endConditions = new ArrayList<>();
        boolean confirmation;
        int optionNumber = -1;

        while (optionNumber != 0)
        {
            optionNumber = getOptionNumber(0, SchoolHourSystem.eEndConditions.values().length - 1, SchoolHourSystem.eEndConditions.getSolutionViewString(endConditions));

            switch (optionNumber)
            {
                case 0:
                    if(endConditions.isEmpty())
                    {
                        System.out.println("must choose at least 1 end condition");
                        optionNumber = -1;
                    }
                    break;
                case 1:
                    if(endConditions.stream().anyMatch(endCondition -> endCondition instanceof NumberOfGenerations))
                    {
                        endConditions = endConditions.stream().filter(endCondition -> !(endCondition instanceof NumberOfGenerations)).collect(Collectors.toList());
                    }
                    else
                    {
                        endConditions.add(new NumberOfGenerations(getNumberOfGenerations()));
                    }

                    break;
                case 2:
                    if(endConditions.stream().anyMatch(endCondition -> endCondition instanceof ByFitness))
                    {
                        endConditions = endConditions.stream().filter(endCondition -> !(endCondition instanceof ByFitness)).collect(Collectors.toList());
                    }
                    else
                    {
                        endConditions.add(new ByFitness(getPositiveInput("Enter fitness score", Double.class)));
                    }
                    break;
                default:
                    break;
            }
        }

        return endConditions;
    }

    public static String getSavingOrLoadingPath(String endsWith)
    {
        Scanner sc = new Scanner(System.in);
        String path;

        System.out.println("please enter a file path that ends with " + endsWith);
        path = sc.nextLine();

        if(!path.endsWith(endsWith))
        {
            throw new RuntimeException("file path doesn't end with " + endsWith);
        }

        return path;
    }

    public static boolean getStopAlgorithmToSaveFileConformation()
    {
        StringBuilder message = new StringBuilder();

        message.append("The algorithm is still running.");
        message.append(System.lineSeparator());
        message.append("Do you wish to stop the algorithm and save the data?");
        message.append(System.lineSeparator());

        return getConfirmation(message.toString());
    }

    public static boolean getOverrideSaveFileConformation()
    {
        StringBuilder message = new StringBuilder();

        message.append("This file already have a saved data in it.");
        message.append(System.lineSeparator());
        message.append("Do you wish to override the saved file?");
        message.append(System.lineSeparator());

        return getConfirmation(message.toString());
    }
}

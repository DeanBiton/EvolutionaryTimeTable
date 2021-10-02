package App.Servlets;

import Engine.DTO.DTOEvolutionaryAlgorithmSettings;
import Engine.Evolution.EndCondition.ByFitness;
import Engine.Evolution.EndCondition.ByTime;
import Engine.Evolution.EndCondition.EndCondition;
import Engine.Evolution.EndCondition.NumberOfGenerations;
import Engine.Evolution.MySolution.Crossover.AspectOriented;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.Crossover.DayTimeOriented;
import Engine.Evolution.MySolution.MyMutation.Flipping;
import Engine.Evolution.MySolution.MyMutation.Mutation;
import Engine.Evolution.MySolution.MyMutation.Sizer;
import Engine.Evolution.Selection.RouletteWheel;
import Engine.Evolution.Selection.Selection;
import Engine.Evolution.Selection.Tournament;
import Engine.Evolution.Selection.Truncation;
import Engine.SchoolHourManager;
import com.google.gson.Gson;
import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static App.Utils.ServletUtils.getCurrentSchoolHourManager;

@WebServlet({"/Algorithm/updateSettings"})
public class UpdateAlgorithmSettingsServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();

            Map<String, String> messages = new HashMap<>();
            SchoolHourManager manager = getCurrentSchoolHourManager(request);

            setShowEveryGeneration(manager, messages, request);
            setInitialPopulation(manager, messages, request.getParameter("initialPopulation"));
            setSelection(manager, messages, request);
            setCrossover(manager, messages, request);
            setMutations(manager, request);
            setEndConditions(manager, messages, request);

            String json = gson.toJson(messages);
            out.println(json);
            out.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void setShowEveryGeneration(SchoolHourManager manager, Map<String, String> messages, HttpServletRequest request)
    {
        StringBuilder showEveryGenerationMessage = new StringBuilder();
        boolean passToConstructor = true;
        Integer showEveryGeneration;

        showEveryGeneration = parseIntegerParameter("showEveryGeneration", showEveryGenerationMessage, request, "Show Every Generation");
        passToConstructor = showEveryGeneration != null;

        if(passToConstructor)
        {
            try
            {
                manager.setShowEveryGeneration(showEveryGeneration);
            }
            catch (Exception exception)
            {
                showEveryGenerationMessage.append(exception.getMessage());
            }
        }

        if(showEveryGenerationMessage.length() == 0)
            showEveryGenerationMessage.append("updated successfully");

        messages.put("showEveryGeneration", showEveryGenerationMessage.toString());
    }

    private void setInitialPopulation(SchoolHourManager manager, Map<String, String> messages, String initialPopulationString)
    {
        try {
            int initialPopulation = Integer.parseInt(initialPopulationString);

            manager.setInitialPopulation(initialPopulation);
            messages.put("initialPopulation", "updated successfully");
        }
        catch (NumberFormatException ex)
        {
            messages.put("initialPopulation", "Input must be an integer");
        }
        catch (Exception ex)
        {
            messages.put("initialPopulation", ex.getMessage());
        }
    }

    private Integer parseIntegerParameter(String parameterString, StringBuilder parameterMessage, HttpServletRequest request, String parameterName)
    {
        Integer parameter;

        try {
            parameter = Integer.parseInt(request.getParameter(parameterString));
        }
        catch (Exception exception)
        {
            if(parameterMessage.length() != 0)
                parameterMessage.append(System.lineSeparator());
            parameterMessage.append(parameterName).append(" must be an integer");
            parameter = null;
        }

        return parameter;
    }

    private Double parseDoubleParameter(String parameterString, StringBuilder parameterMessage, HttpServletRequest request, String parameterName)
    {
        Double parameter;

        try {
            parameter = Double.parseDouble(request.getParameter(parameterString));
        }
        catch (Exception exception)
        {
            if(parameterMessage.length() != 0)
                parameterMessage.append(System.lineSeparator());
            parameterMessage.append(parameterName).append(" must be an double");
            parameter = null;
        }

        return parameter;
    }

    private void setSelection(SchoolHourManager manager, Map<String, String> messages, HttpServletRequest request)
    {
        StringBuilder selectionMessage = new StringBuilder();
        String selectionString = request.getParameter("selection");
        boolean passToConstructor = true;
        Integer elitism;
        Integer topPercent = null;
        Double pte = null;

        elitism = parseIntegerParameter("elitism", selectionMessage, request, "elitism");
        passToConstructor = elitism != null;

        if(selectionString.equals("Truncation"))
        {
            topPercent = parseIntegerParameter("selectionParameter", selectionMessage, request, "Top percent");
            if(topPercent == null)
                passToConstructor = false;
        }
        else if(selectionString.equals("Tournament"))
        {
            pte = parseDoubleParameter("selectionParameter", selectionMessage, request, "PTE");
            if(pte == null)
                passToConstructor = false;
        }

        if(passToConstructor)
        {
            try
            {
                Selection selection = null;
                switch (selectionString){
                    case "Truncation":
                        selection = new Truncation(topPercent, elitism);
                        break;
                    case "RouletteWheel":
                        selection = new RouletteWheel(elitism);
                        break;
                    case "Tournament":
                        selection = new Tournament(elitism, pte);
                        break;
                }

                manager.setSelection(selection);
            }
            catch (Exception exception)
            {
                selectionMessage.append(exception.getMessage());
            }
        }

        if(selectionMessage.length() == 0)
            selectionMessage.append("updated successfully");

        messages.put("selection", selectionMessage.toString());
    }

    private void setCrossover(SchoolHourManager manager, Map<String, String> messages, HttpServletRequest request)
    {
        StringBuilder crossoverMessage = new StringBuilder();
        String crossoverString = request.getParameter("crossover");
        boolean passToConstructor = true;
        Integer numberOfSeparators;
        AspectOriented.OrientationType orientationType = null;

        if(crossoverString.equals("AspectOriented"))
            orientationType = AspectOriented.OrientationType.valueOf(request.getParameter("orientationType"));

        numberOfSeparators = parseIntegerParameter("numberOfSeparators", crossoverMessage, request, "Number of separators");
        passToConstructor = numberOfSeparators != null;

        if(passToConstructor)
        {
            try
            {
                Crossover crossover = null;
                switch (crossoverString){
                    case "DayTimeOriented":
                        crossover = new DayTimeOriented(numberOfSeparators);
                        break;
                    case "AspectOriented":
                        crossover = new AspectOriented(numberOfSeparators, orientationType);
                        break;
                }

                manager.setCrossover(crossover);
            }
            catch (Exception exception)
            {
                crossoverMessage.append(exception.getMessage());
            }
        }

        if(crossoverMessage.length() == 0)
            crossoverMessage.append("updated successfully");

        messages.put("crossover", crossoverMessage.toString());
    }

    private void setMutations(SchoolHourManager manager, HttpServletRequest request)
    {
        Double probability;
        Integer mutationParameter = 0;
        Flipping.FlippingComponent flippingComponent;
        List<Mutation> mutations = new ArrayList<>();

        String[] mutationTypes = request.getParameterValues("mutation");
        String[] probabilities = request.getParameterValues("probability");
        String[] mutationParameters = request.getParameterValues("mutationParameter");
        String[] flippingComponents = request.getParameterValues("flippingComponent");

        if(mutationTypes != null) {
            for (int mutationNumber = 0; mutationNumber < mutationTypes.length; mutationNumber++) {
                probability = Double.parseDouble(probabilities[mutationNumber]);
                mutationParameter = Integer.parseInt(mutationParameters[mutationNumber]);
                if (mutationTypes[mutationNumber].equals("Flipping")) {
                    flippingComponent = Flipping.FlippingComponent.valueOf(flippingComponents[mutationNumber]);
                    mutations.add(new Flipping(probability, mutationParameter, flippingComponent));
                } else
                    mutations.add(new Sizer(probability, mutationParameter));
            }
        }

        manager.setMutations(mutations);
    }

    private void setEndConditions(SchoolHourManager manager, Map<String, String> messages, HttpServletRequest request)
    {
        StringBuilder endConditionsMessage = new StringBuilder();
        boolean passToConstructor = true;
        Double fitness;
        Integer numberOfGenerations;
        Integer time;
        List<EndCondition> endConditions = new ArrayList<>();

        if(request.getParameter("fitnessCheckBox").equals("true"))
        {
            fitness = parseDoubleParameter("fitness", endConditionsMessage, request, "fitness ");
            if(fitness != null)
            {
                try {
                    endConditions.add(new ByFitness(fitness));
                }
                catch (Exception exception) {
                    passToConstructor = false;
                    endConditionsMessage.append(exception.getMessage());
                }
            }
            else
                passToConstructor = false;
        }

        if(request.getParameter("generationNumberCheckBox").equals("true"))
        {
            numberOfGenerations = parseIntegerParameter("generationNumber", endConditionsMessage, request, "Generation number ");
            if(numberOfGenerations != null)
            {
                try {
                    endConditions.add(new NumberOfGenerations(numberOfGenerations));
                }
                catch (Exception exception) {
                    passToConstructor = false;
                    endConditionsMessage.append(endConditionsMessage.length() != 0? "\n" : "");
                    endConditionsMessage.append(exception.getMessage());
                }
            }
            else
                passToConstructor = false;
        }

        if(request.getParameter("timeCheckBox").equals("true"))
        {
            time = parseIntegerParameter("time", endConditionsMessage, request, "Time ");
            if(time != null)
            {
                try {
                    endConditions.add(new ByTime(time));
                }
                catch (Exception exception) {
                    passToConstructor = false;
                    endConditionsMessage.append(endConditionsMessage.length() != 0? "\n" : "");
                    endConditionsMessage.append(exception.getMessage());
                }
            }
            else
                passToConstructor = false;
        }

        if(passToConstructor)
        {
            if(endConditions.size() == 0)
                endConditionsMessage.append("Must choose at least 1 end condition.");
            else
            {
                manager.setEndConditions(endConditions);
                endConditionsMessage.append("updated successfully");
            }
        }

        messages.put("endConditions", endConditionsMessage.toString());
    }
}

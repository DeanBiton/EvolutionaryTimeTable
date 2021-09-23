package App.Servlets;

import Engine.DTO.DTOEvolutionaryAlgorithmSettings;
import Engine.Evolution.MySolution.Crossover.AspectOriented;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.Crossover.DayTimeOriented;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

            setInitialPopulation(manager, messages, request.getParameter("initialPopulation"));
            setSelection(manager, messages, request);
            setCrossover(manager, messages, request);

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
}

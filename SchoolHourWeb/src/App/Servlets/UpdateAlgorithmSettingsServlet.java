package App.Servlets;

import Engine.DTO.DTOEvolutionaryAlgorithmSettings;
import Engine.Evolution.Selection.Tournament;
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

    private void setSelection(SchoolHourManager manager, Map<String, String> messages, HttpServletRequest request)
    {
        StringBuilder selectionMessage = new StringBuilder();
        String selectionString = request.getParameter("selection");
        int elitism;
        int topPercent;
        double pte;
        boolean passToConstructor = true;

        try{
            elitism = Integer.parseInt(request.getParameter("elitism"));
        }
        catch (NumberFormatException ex)
        {
            selectionMessage.append("elitism must be an integer");
            passToConstructor = false;
        }

        try{
            topPercent = Integer.parseInt(request.getParameter("selectionParameter"));
        }
        catch (Exception ex)
        {
            if(selectionMessage.length()!=0)
                selectionMessage.append(System.lineSeparator());
            selectionMessage.append("Top percent must be an integer");
            passToConstructor = false;
        }

        try{
            pte = Double.parseDouble(request.getParameter("selectionParameter"));
        }
        catch (Exception ex)
        {
            if(selectionMessage.length()!=0)
                selectionMessage.append(System.lineSeparator());
            selectionMessage.append("PTE must be a double");
            passToConstructor = false;
        }

        if(passToConstructor)
        {
            try
            {
                switch (selectionString){
                    case "Truncation":
                        break;
                    case "RouletteWheel":
                        break;
                    case "Tournament":
                        break;
                }
            }
            catch (Exception exception)
            {

            }
        }
    }
}

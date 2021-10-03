package App.Servlets;

import App.Utils.ServletUtils;
import App.Utils.SessionUtils;
import Engine.SchoolHourManager;
import Problem.ProblemsManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



@WebServlet({"/Pages/Algorithm/enter"})

public class EnterAlgorithmServlet  extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML

        ProblemsManager problemsManager = ServletUtils.getProblemsManager(request.getServletContext());

        String id=request.getParameter("id");
        SchoolHourManager schoolHourManager=problemsManager.getProblems().get(Integer.parseInt(id)-1).getUserManager(SessionUtils.getUsername(request));
        if(schoolHourManager==null)
        {
            problemsManager.getProblems().get(Integer.parseInt(id)-1).addUserManager(SessionUtils.getUsername(request)); //add manager for username
            response.getOutputStream().println("/Pages/Algorithm/settings.html?id="+Integer.parseInt(id));
        }
        else
        {
            response.getOutputStream().println("/Pages/Algorithm/data.html?id="+Integer.parseInt(id));


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
}

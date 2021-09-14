package App.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckUserName", urlPatterns = {"/Pages/CheckUserName"})
public class CheckUserNameServlet extends HttpServlet {

    private final String LOGIN_URL = "/Pages/login/login.html";
    private final String UserPage_URL = "/Pages/UserPage/user.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = null;

        for(Cookie cookie : request.getCookies())
        {
            if(cookie.getName().equals("userName"))
            {
                usernameFromSession = cookie.getValue();
                break;
            }
        }

        if(usernameFromSession == null)
        {
            response.sendRedirect(LOGIN_URL);
        }
        else
        {
            response.sendRedirect(UserPage_URL);
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

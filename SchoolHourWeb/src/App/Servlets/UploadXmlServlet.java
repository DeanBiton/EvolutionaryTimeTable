package App.Servlets;

import App.Constants.Constants;
import App.Utils.ServletUtils;
import App.Utils.SessionUtils;
import Problem.ProblemsManager;
import Users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@WebServlet({"/Pages/UserPage/UploadXmlServlet"})
@MultipartConfig(
        fileSizeThreshold = 1048576,
        maxFileSize = 5191680L
)
public class UploadXmlServlet  extends HttpServlet {
    private static final long serialVersionUID = 1L;

   // public static final String USER_URL = "Pages/UserPage/user.html";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SessionUtils.isLoggedIn(request)) {
            ProblemsManager problemsManager = ServletUtils.getProblemsManager(request.getServletContext());

            Part filePart= request.getPart("file");
            List<Part> fileParts= new ArrayList<>();
            fileParts.add(filePart);

            //Part xmlFile = (Part)fileParts.stream().findFirst().get();
            for(Part xmlFile: fileParts)
            {
                synchronized(this) {
                    //      XmlApiTimeTable xmlApi = new XmlApiTimeTable(2);

                    try {

                        problemsManager.addproblem(xmlFile.getInputStream(),SessionUtils.getUsername(request));
                        // XmlApiTimeTable.checkValidationOfXmlFileName(xmlFile.getSubmittedFileName());
                        //    TimeTableProblem problem = xmlApi.loadTimeTableProblem(xmlFile.getInputStream());
                        //   serverManager.addTimeProblem(ServerUtils.getUserName(request), problem);
                        // ServerUtils.writeJson(response, new Response("file uploaded succesfully"));
                    } catch (Exception e) {
                        //   ServerUtils.writeJson(response, new Response(var9.getMessage(), xmlApi.getAllErrors()));
                        System.out.println("exception");

                    }

                }
            }

            //response.sendRedirect(USER_URL);

        }
    }
}

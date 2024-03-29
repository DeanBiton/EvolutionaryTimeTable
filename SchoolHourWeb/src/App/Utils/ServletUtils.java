package App.Utils;

import Engine.SchoolHourManager;
import Problem.ProblemsManager;
import Users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static App.Constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String PROBLEMS_MANAGER_ATTRIBUTE_NAME = "problemsManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object problemsManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
	public static ProblemsManager getProblemsManager(ServletContext servletContext) {

		synchronized (problemsManagerLock) {
			if (servletContext.getAttribute(PROBLEMS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(PROBLEMS_MANAGER_ATTRIBUTE_NAME, new ProblemsManager());
			}
		}
		return (ProblemsManager) servletContext.getAttribute(PROBLEMS_MANAGER_ATTRIBUTE_NAME);
	}

	public static SchoolHourManager getCurrentSchoolHourManager(HttpServletRequest request)
	{
		ProblemsManager problemsManager = ServletUtils.getProblemsManager(request.getServletContext());
		int id = Integer.parseInt(request.getParameter("id"));

		return problemsManager.getSchoolHourManager(id, SessionUtils.getUsername(request));
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}

package com.mato.demo.jco;

import static com.mato.demo.jco.Authenication.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

@WebServlet("/execute")
public class JcoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String JCO_DESTINATION = "jco.destination";
	private static final String JCO_PROPERTIES = "jco.properties";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isLoggedin(request)) {
			response.sendRedirect(request.getContextPath());
			return;
		}

		String requtext = request.getParameter("requtext");
		if (requtext == null) {
			requtext = "Requested with no parameter.";
		}

		try {
			JCoDestination d = getDestiation();
			JCoFunction f = d.getRepository().getFunction("STFC_CONNECTION");

			f.getImportParameterList().setValue("REQUTEXT", requtext);

			f.execute(d);

			JCoParameterList exports = f.getExportParameterList();

			request.setAttribute("ECHOTEXT", "" + exports.getString("ECHOTEXT"));
			request.setAttribute("RESPTEXT", "" + exports.getString("RESPTEXT"));

			request.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(request, response);

		} catch (JCoException e) {
			e.printStackTrace(response.getWriter());

		}

	}

	private JCoDestination getDestiation() throws FileNotFoundException, IOException, JCoException {
		String path = this.getServletContext().getRealPath("/WEB-INF/" + JCO_PROPERTIES);

		InputStream is = new FileInputStream(path);

		Properties p = new Properties();
		p.load(is);
		is.close();

		return JCoDestinationManager.getDestination(p.getProperty(JCO_DESTINATION));
	}
}

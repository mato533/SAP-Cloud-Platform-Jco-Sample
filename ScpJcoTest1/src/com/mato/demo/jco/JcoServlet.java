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

	public static final String PRAM_REQUTEXT = "REQUTEXT";
	public static final String PRAM_ECHOTEXT = "ECHOTEXT";
	public static final String PRAM_RESPTEXT = "RESPTEXT";
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

		String requtext = request.getParameter(PRAM_REQUTEXT);
		if (requtext == null) {
			requtext = "Requested with no parameter.";
		}

		// execute RFC and forward jsp
		try {
			JCoDestination d = getDestiation();
			JCoFunction f = d.getRepository().getFunction("STFC_CONNECTION");

			f.getImportParameterList().setValue(PRAM_REQUTEXT, requtext);

			f.execute(d);

			JCoParameterList exports = f.getExportParameterList();

			request.setAttribute(PRAM_ECHOTEXT, "" + exports.getString(PRAM_ECHOTEXT));
			request.setAttribute(PRAM_RESPTEXT, "" + exports.getString(PRAM_RESPTEXT));

			request.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(request, response);

		} catch (JCoException e) {
			e.printStackTrace(response.getWriter());

		}

	}

	private JCoDestination getDestiation() throws FileNotFoundException, IOException, JCoException {
		String path = this.getServletContext().getRealPath("/WEB-INF/" + JCO_PROPERTIES);

		InputStream is = new FileInputStream(path);
		Properties p = new Properties();

		// load jco destination name
		p.load(is);
		is.close();

		return JCoDestinationManager.getDestination(p.getProperty(JCO_DESTINATION));
	}
}

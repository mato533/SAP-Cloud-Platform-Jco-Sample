package com.mato.demo.jco;

import static com.mato.demo.jco.Authenication.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;

@WebServlet("/execute")
public class JcoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	public static final String JCO_DESTINATION = "jco.destination";
	private static final String JCO_PROPERTIES = "jco.properties";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			login(request.getRemoteUser());
			logger.debug("Remote user : " + request.getRemoteUser());

			JCoDestination destination = getDestiation();

			JCoRepository repo = destination.getRepository();

			JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");

			String strReqText = request.getParameter("requtext");
			stfcConnection.getImportParameterList().setValue("REQUTEXT", "" + strReqText);
			logger.debug("Post Parameter(requtext) : " + strReqText);

			logger.debug("Execugte Remote function call : STFC_CONNECTION");
			stfcConnection.execute(destination);

			JCoParameterList exports = stfcConnection.getExportParameterList();

			String echotext = exports.getString("ECHOTEXT");
			String resptext = exports.getString("RESPTEXT");

			request.setAttribute("ECHOTEXT", ("" + echotext));
			request.setAttribute("RESPTEXT", ("" + resptext));

			logger.debug("Return Values :");
			logger.debug("ECHOTEXT : " + echotext);
			logger.debug("RESPTEXT : " + resptext);

			if (!response.isCommitted()) {
				request.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(request, response);
			}

		} catch (JCoException | LoginException e) {
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

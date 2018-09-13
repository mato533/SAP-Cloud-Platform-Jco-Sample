package com.mato.demo.jco;

import static com.mato.demo.jco.Authenication.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.conn.jco.JCoException;

@WebServlet("/execute")
public class JcoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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

		String requtext = request.getParameter(StfcConnection.PRAM_REQUTEXT);
		if (requtext == null) {
			requtext = "Requested with no parameter.";
		}

		// execute RFC and forward jsp
		StfcConnection rfc = new StfcConnection(this.getServletContext().getRealPath("/WEB-INF"), requtext);
		try {
			rfc.execute();

			request.setAttribute(StfcConnection.PRAM_ECHOTEXT, rfc.getEchoText());
			request.setAttribute(StfcConnection.PRAM_RESPTEXT, rfc.getRespText());

			request.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(request, response);

		} catch (JCoException e) {
			e.printStackTrace(response.getWriter());
		}
	}
}

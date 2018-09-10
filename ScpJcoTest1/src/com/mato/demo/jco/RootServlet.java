package com.mato.demo.jco;

import static com.mato.demo.jco.Authenication.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/")
public class RootServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(Authenication.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter responseWriter = response.getWriter();

		try {
			login(request.getRemoteUser());
			logger.debug("Remote user : " + request.getRemoteUser());

			if (!response.isCommitted()) {
				request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			}
		} catch (LoginException e) {
			responseWriter.append("Login Failed.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}

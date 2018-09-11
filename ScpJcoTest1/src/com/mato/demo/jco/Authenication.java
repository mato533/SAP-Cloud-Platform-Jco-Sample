package com.mato.demo.jco;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.auth.login.LoginContextFactory;

public class Authenication {

	private final static Logger logger = LoggerFactory.getLogger(Authenication.class);

	protected static void login(String user) throws LoginException {
		//Authenticates the User
		if (user == null) {

			logger.debug("User is not logged in.");

			//authenticate the User
			LoginContext loginContext = LoginContextFactory.createLoginContext("FORM");
			loginContext.login();
		}

	}

	protected static void logout(String user) throws LoginException {
		//Authenticates the User
		if (user != null) {

			logger.debug("User is logged in.");

			//logout
			LoginContext loginContext = LoginContextFactory.createLoginContext("FORM");
			loginContext.logout();
		}

	}

	protected static boolean isLoggedin(HttpServletRequest request) {
		if (request.getRemoteUser() == null) {
			return false;
		} else {
			return true;
		}

	}

}
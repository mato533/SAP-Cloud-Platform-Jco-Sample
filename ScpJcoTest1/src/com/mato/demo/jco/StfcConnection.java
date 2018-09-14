package com.mato.demo.jco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

public class StfcConnection {

	public static final String PRAM_REQUTEXT = "REQUTEXT";
	public static final String PRAM_ECHOTEXT = "ECHOTEXT";
	public static final String PRAM_RESPTEXT = "RESPTEXT";
	private static final String FUNCTION_NAME = "STFC_CONNECTION";
	private static final String JCO_DESTINATION = "jco.destination";
	private static final String JCO_PROPERTIES = "jco.properties";

	private String propFilePath;
	private String requText;
	private String echoText;
	private String respText;

	public StfcConnection(String propDirPath, String requText) {
		this.propFilePath = Paths.get(propDirPath, JCO_PROPERTIES).toString();

		this.requText = requText;
	}

	public void execute() throws JCoException, FileNotFoundException, IOException {
		JCoDestination d = getDestiation();
		JCoFunction f = d.getRepository().getFunction(FUNCTION_NAME);

		f.getImportParameterList().setValue(PRAM_REQUTEXT, requText);

		f.execute(d);

		JCoParameterList exports = f.getExportParameterList();

		echoText = exports.getString(PRAM_ECHOTEXT);
		respText = exports.getString(PRAM_RESPTEXT);
	}

	private JCoDestination getDestiation() throws FileNotFoundException, IOException, JCoException {

		InputStream is = new FileInputStream(propFilePath);
		Properties p = new Properties();

		// load jco destination name
		p.load(is);
		is.close();

		return JCoDestinationManager.getDestination(p.getProperty(JCO_DESTINATION));
	}

	public String getEchoText() {
		return echoText;
	}

	public String getRespText() {
		return respText;
	}
}

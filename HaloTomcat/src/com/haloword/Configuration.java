package com.haloword;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Configuration {
	public static Document doc;
	static{
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Configuration.doc = db.parse(new File(Constants.CONF_PATH));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080625   210817 samindaw@wso2.com - Saminda Wijeratne, Setting the proxyBean and proxyEndPoint values - Refactoring
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Axis2CoreUtils {
	
	private static boolean alreadyComputedTempAxis2Directory = false;
	private static String tempAxis2Dir = null;
	
	public static String tempAxis2Directory() {
		if (!alreadyComputedTempAxis2Directory){
			String[] nodes = {Axis2Constants.DIR_DOT_METADATA,
					Axis2Constants.DIR_DOT_PLUGINS,
					Axis2Constants.TEMP_AXIS2_FACET_DIR};
			tempAxis2Dir =FileUtils.addNodesToPath(
					ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(), nodes); 
			alreadyComputedTempAxis2Directory= true;
		}
		return tempAxis2Dir;
	}
	
	public static String tempAxis2WebappFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.WEBAPP_EXPLODED_SERVER_LOCATION_FILE);
	}
	
	
	public static String tempRuntimeStatusFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.SERVER_STATUS_LOCATION_FILE);
	}
	
	public static String tempWarStatusFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.WAR_STATUS_LOCATION_FILE);
	}
	
	public static String addAnotherNodeToPath(String currentPath, String newNode) {
		return currentPath + File.separator + newNode;
	}
	
	public static void  writePropertyToFile(File file,String key, String value) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file), "8859_1"));
	       out.write(key+"="+value+"\n");
	       out.close();
	}

	public static String getServiceEndPointFromWSDL(String fileName, String serviceName){
		String proxyEndPoint=null;
		try{
			Document doc;
			doc = Axis2CoreUtils.getDocumentFromLocation(fileName);
			Element documentElement = doc.getDocumentElement();
			HashMap<String, String> portElements=new HashMap<String, String>();

			//Iterate the root element children to find service nodes
			for (int i = 0; i < documentElement.getChildNodes().getLength(); i++) {
				Node serviceElement = documentElement.getChildNodes().item(i);
				if (serviceElement.getNodeName().equals("wsdl:service")){
					if (serviceName.equalsIgnoreCase(serviceElement.getAttributes().getNamedItem("name").getNodeValue())){

						//iterate the service node children to find wsdl:port nodes
						for (int j = 0; j < serviceElement.getChildNodes().getLength(); j++) {
							Node portElement = serviceElement.getChildNodes().item(j);
							if (portElement.getNodeName().equals("wsdl:port")){
								String portBinding=portElement.getAttributes().getNamedItem("binding").getNodeValue().toUpperCase();
	
								//iterate the port node children to find the soap address node
								for (int k = 0; k < portElement.getChildNodes().getLength(); k++) {
									Node soapElement = portElement.getChildNodes().item(k);
									if (!soapElement.getNodeName().equals("#text")){
										String soapLocation=soapElement.getAttributes().getNamedItem("location").getNodeValue();
										while (portElements.containsKey(portBinding))
											portBinding="K"+portBinding;
										portElements.put(portBinding, soapLocation);
									}
								}
							}
						}
					}
				}
			}
			String portBindType="";
			String soap11B="SOAP11Binding".toUpperCase();
			String soap12B="SOAP12Binding".toUpperCase();
			String httpB="HttpBinding".toUpperCase();
			String https="https";

			//iterating through all found end points to determine the required endpoint in the order soap11, soap12, http
			for (String string : portElements.keySet()) {
				if (proxyEndPoint==null){
					proxyEndPoint=portElements.get(string);
					portBindType=string;
				}
				if (string.endsWith(soap11B)){
					if (proxyEndPoint.startsWith(https)||(!portBindType.endsWith(soap11B))){
						proxyEndPoint=portElements.get(string);
						portBindType=string;
					}
				}
				if ((!portBindType.endsWith(soap11B))&&(string.endsWith(soap12B))){
					if (proxyEndPoint.startsWith(https)||(!portBindType.endsWith(soap12B))){
						proxyEndPoint=portElements.get(string);
						portBindType=string;
					}
				}
				if (!((portBindType.endsWith(soap11B))||(portBindType.endsWith(soap12B)))&&(string.endsWith(httpB))){
					if (proxyEndPoint.startsWith(https)||(!portBindType.endsWith(httpB))){
						proxyEndPoint=portElements.get(string);
						portBindType=string;
					}
				}
			}
		}catch(Exception e){}
		return proxyEndPoint;
	}

	public static String getServiceNameFromWSDL(String fileName){
		String serviceName="";
		try{
			Document doc;
			doc = Axis2CoreUtils.getDocumentFromLocation(fileName);
			Element documentElement = doc.getDocumentElement();

			//Iterate the root element children to find services
			for (int i = 0; i < documentElement.getChildNodes().getLength(); i++) {
				Node serviceElement = documentElement.getChildNodes().item(i);
				if (serviceElement.getNodeName().equals("wsdl:service")){
					serviceName=serviceElement.getAttributes().getNamedItem("name").getNodeValue();
				}
			}
		}catch(Exception e){}
		return serviceName;
	} 

	private static Document getDocumentFromLocation(String location){
		Document doc=null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(false);
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			if (location.toUpperCase().startsWith("HTTP:")){
				URL url = new URL(location);
				doc=db.parse(url.openStream());
			}else{
				if (location.toUpperCase().startsWith("FILE:")) location=location.substring(5,location.length());
				doc=db.parse(new File(location));
			}		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return doc;
	}
}

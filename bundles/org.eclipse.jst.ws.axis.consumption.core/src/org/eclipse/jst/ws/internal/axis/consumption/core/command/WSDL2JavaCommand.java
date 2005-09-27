/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.axis.wsdl.toJava.GeneratedFileInfo;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class WSDL2JavaCommand extends EnvironmentalOperation {

	private final String DEPLOY_TYPE = "deploy"; //$NON-NLS-1$
	private final String UNDEPLOY_TYPE = "undeploy"; //$NON-NLS-1$
	private JavaWSDLParameter javaWSDLParam;
	private String wsdlURI;
	private String httpBasicAuthUsername;
	private String httpBasicAuthPassword;
	private ResourceBundle resource = ResourceBundle.getBundle("org.eclipse.jst.ws.axis.consumption.core.consumption"); //$NON-NLS-1$
	

	public WSDL2JavaCommand() {
	}

	  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	  {
	    Environment environment = getEnvironment();
		Status status;
		if (javaWSDLParam == null) {
			status = new SimpleStatus("WSDL2JavaCommand", //$NON-NLS-1$
					getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		if (wsdlURI != null) //bottom up case has already has the correct WSDL on javaWSDLParam
		{
			javaWSDLParam.setInputWsdlLocation(wsdlURI);
		}
		javaWSDLParam.setHTTPUsername(httpBasicAuthUsername);
		javaWSDLParam.setHTTPPassword(httpBasicAuthPassword);
		
		Emitter wsdl2Java = new Emitter();
		if (environment.getLog().isEnabled("emitter")) {
			wsdl2Java.setVerbose(true);
		} else {
			wsdl2Java.setVerbose(false);
		}
		boolean serverSide = javaWSDLParam.getServerSide() == JavaWSDLParameter.SERVER_SIDE_BEAN;
		wsdl2Java.setServerSide(serverSide);
		if (serverSide) {
			wsdl2Java.setSkeletonWanted(javaWSDLParam.isSkeletonDeploy());
		}
		if (javaWSDLParam.isMetaInfOnly()) {
			// for the case Java2WSDL-WSDL2Java
			wsdl2Java.setOutputDir(removeFileProtocol(javaWSDLParam.getOutput()));
			HashMap pck2nsMap = javaWSDLParam.getMappings();
			if (pck2nsMap != null) {
				HashMap ns2pckMap = new HashMap();
				Iterator keys = pck2nsMap.keySet().iterator();
				while (keys.hasNext()) {
					String pakage = (String) keys.next();
					String namespace = (String) pck2nsMap.get(pakage);
					ns2pckMap.put(namespace, pakage);
				}
				wsdl2Java.setNamespaceMap(ns2pckMap);
			}
		} else {
			//  for the case WSDL2Java
			wsdl2Java.setOutputDir(removeFileProtocol(javaWSDLParam.getJavaOutput()));
			if (javaWSDLParam.getMappings() != null) {
				wsdl2Java.setNamespaceMap(javaWSDLParam.getMappings());
			}
		}
		environment.getLog().log(Log.INFO, 5019, this, "execute", "Java output = " + javaWSDLParam.getJavaOutput());
		if (javaWSDLParam.getHTTPPassword() != null) {
			wsdl2Java.setPassword(javaWSDLParam.getHTTPPassword());
			environment.getLog().log(Log.INFO, 5081, this, "execute", "password: " + javaWSDLParam.getHTTPPassword());
		}
		if (javaWSDLParam.getHTTPUsername() != null) {
			wsdl2Java.setUsername(javaWSDLParam.getHTTPUsername());
			environment.getLog().log(Log.INFO, 5082, this, "execute", "username: " + javaWSDLParam.getHTTPUsername());
		}
		environment.getLog().log(Log.INFO, 5020, this, "execute", "WSDL Location = " + javaWSDLParam.getInputWsdlLocation());
		environment.getProgressMonitor().report(getMessage("MSG_PARSING_WSDL", javaWSDLParam.getInputWsdlLocation() ) );
		try {
			wsdl2Java.run(javaWSDLParam.getInputWsdlLocation());
			if (serverSide) {
				// set deployment files
				List deploymentFiles1 = wsdl2Java.getGeneratedFileInfo().findType(DEPLOY_TYPE);
				List deploymentFiles2 = wsdl2Java.getGeneratedFileInfo().findType(UNDEPLOY_TYPE);
				ArrayList deplFiles = new ArrayList();
				if (deploymentFiles1 != null && deploymentFiles2 != null) {
					deploymentFiles1.addAll(deploymentFiles2);
					for (int i = 0; i < deploymentFiles1.size(); i++) {
						GeneratedFileInfo.Entry entry = (GeneratedFileInfo.Entry) deploymentFiles1.get(i);
						deplFiles.add(entry.fileName);
					}
					String[] deplFilesArray = new String[deplFiles.size()];
					deplFiles.toArray(deplFilesArray);
					javaWSDLParam.setDeploymentFiles(deplFilesArray);
				}
				// set java files
				List javaFiles = wsdl2Java.getGeneratedFileNames();
				javaFiles.removeAll(deplFiles);
				String[] javaFileNames = new String[javaFiles.size()];
				javaFiles.toArray(javaFileNames);
				javaWSDLParam.setJavaFiles(javaFileNames);
			}
			
		} catch (Exception e) {
			environment.getLog().log(Log.ERROR, 5021, this, "execute", e);
			status = new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
					getMessage("MSG_ERROR_WSDL_JAVA_GENERATE") + " " //$NON-NLS-1$
							+ e.toString(), Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}
		return new SimpleStatus("");
	}

	/*
	 * Hack: Axis doesn't like file URLs
	 */
	private String removeFileProtocol(String s) {
		if (s.startsWith("file:")) {
			String newS = s.substring(5, s.length());
			int i = newS.indexOf(':');
			if (i != -1) {
				String protocol = newS.substring(0, i);
				int j = protocol.indexOf('/');
				int k = protocol.indexOf('\\');
				int max = Math.max(j, k);
				if (max != -1)
					newS = newS.substring(max + 1, newS.length());
			}
			return newS;
		}
		return s;
	}

	public Status undo(Environment environment) {
		return null;
	}

	public Status redo(Environment environment) {
		return null;
	}

	private String getMessage(String messageId, String parm1) {
		String message = resource.getString(messageId);
		return MessageFormat.format(message, new String[]{parm1});
	}

	/**
	 * Returns the message string identified by the given key from
	 * plugin.properties.
	 * 
	 * @return The String message.
	 */
	public String getMessage(String key) {
		return resource.getString(key);
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam = javaWSDLParam;
	}

	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam;
	}

	/**
	 * @param httpBasicAuthPassword
	 *            The httpBasicAuthPassword to set.
	 */
	public void setHttpBasicAuthPassword(String httpBasicAuthPassword) {
		this.httpBasicAuthPassword = httpBasicAuthPassword;
	}

	/**
	 * @param httpBasicAuthUsername
	 *            The httpBasicAuthUsername to set.
	 */
	public void setHttpBasicAuthUsername(String httpBasicAuthUsername) {
		this.httpBasicAuthUsername = httpBasicAuthUsername;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}

}

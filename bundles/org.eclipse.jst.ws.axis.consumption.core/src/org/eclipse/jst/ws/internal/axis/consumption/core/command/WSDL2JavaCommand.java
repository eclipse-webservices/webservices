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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.axis.wsdl.toJava.GeneratedFileInfo;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.core.plugin.WebServiceAxisConsumptionCorePlugin;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class WSDL2JavaCommand extends AbstractDataModelOperation {

	private final String DEPLOY_TYPE = "deploy"; //$NON-NLS-1$
	private final String UNDEPLOY_TYPE = "undeploy"; //$NON-NLS-1$
	private final String TEMP = "temp"; //$NON-NLS-1$
	private JavaWSDLParameter javaWSDLParam;
	private String wsdlURI;
	private String httpBasicAuthUsername;
	private String httpBasicAuthPassword;
	private List deployFiles;
	private List javaFiles;
	private File tempOutputFile;

	public WSDL2JavaCommand() {
	}

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	{
		IEnvironment environment = getEnvironment();
		IStatus status;
		if (javaWSDLParam == null) {
			status = StatusUtils.errorStatus(
					AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
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
		
		// create temporary directory to use as output directory for wsdl2java
		tempOutputFile = createTempDir();
		wsdl2Java.setOutputDir(tempOutputFile.toString());
		
		try {
			if (javaWSDLParam.isMetaInfOnly()) {
				// for the case Java2WSDL-WSDL2Java
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
				if (javaWSDLParam.getMappings() != null) {
					wsdl2Java.setNamespaceMap(javaWSDLParam.getMappings());
				}
			}
			environment.getLog().log(ILog.INFO, 5019, this, "execute", "Java output = " + javaWSDLParam.getJavaOutput());
			if (javaWSDLParam.getHTTPPassword() != null) {
				wsdl2Java.setPassword(javaWSDLParam.getHTTPPassword());
				environment.getLog().log(ILog.INFO, 5081, this, "execute", "password: " + javaWSDLParam.getHTTPPassword());
			}
			if (javaWSDLParam.getHTTPUsername() != null) {
				wsdl2Java.setUsername(javaWSDLParam.getHTTPUsername());
				environment.getLog().log(ILog.INFO, 5082, this, "execute", "username: " + javaWSDLParam.getHTTPUsername());
			}
			environment.getLog().log(ILog.INFO, 5020, this, "execute", "WSDL Location = " + javaWSDLParam.getInputWsdlLocation());
			
			// The default timeout for wsdl2java is 45 seconds.  
			// The user can change the timeout value by setting 
			// "-DAxisWsdl2JavaTimeout=<timeout value in seconds>" as VM argument
			// For example, enter "-DAxisWsdl2JavaTimeout=300" 
			// to change the timeout to 300 seconds.
			
			long timeout=45;
			String wsdl2JavaTimeoutProperty = System.getProperty("AxisWsdl2JavaTimeout");
			if (wsdl2JavaTimeoutProperty != null) {
				timeout = new Integer(wsdl2JavaTimeoutProperty).longValue();
			}
			wsdl2Java.setTimeout(timeout);
			environment.getLog().log(ILog.INFO, 5091, this, "execute", "Timeout = " + timeout);
			
			ProgressUtils.report(monitor, NLS.bind(AxisConsumptionCoreMessages.MSG_PARSING_WSDL, javaWSDLParam.getInputWsdlLocation() ) );
			

			
			wsdl2Java.run(javaWSDLParam.getInputWsdlLocation());
			
			javaFiles = wsdl2Java.getGeneratedFileNames();
			
			deployFiles = new ArrayList();
			if (serverSide) {
				// set deployment files
				List deploymentFiles1 = wsdl2Java.getGeneratedFileInfo().findType(DEPLOY_TYPE);
				List deploymentFiles2 = wsdl2Java.getGeneratedFileInfo().findType(UNDEPLOY_TYPE);
				if (deploymentFiles1 != null && deploymentFiles2 != null) {
					deploymentFiles1.addAll(deploymentFiles2);
					for (int i = 0; i < deploymentFiles1.size(); i++) {
						GeneratedFileInfo.Entry entry = (GeneratedFileInfo.Entry) deploymentFiles1.get(i);
						deployFiles.add(entry.fileName);
					}
				}	
				javaFiles.removeAll(deployFiles);
			}
			
			status = moveGeneratedFiles(environment, monitor);
			
		} catch (Exception e) {
			environment.getLog().log(ILog.ERROR, 5021, this, "execute", e);
			status = StatusUtils.errorStatus(
					AxisConsumptionCoreMessages.MSG_ERROR_WSDL_JAVA_GENERATE + " " //$NON-NLS-1$
					+ e.toString());
			environment.getStatusHandler().reportError(status);
		} finally {
			deleteDir(tempOutputFile); 			
		}
		return status;	
	}
	  
	public IStatus moveGeneratedFiles( IEnvironment environment, IProgressMonitor monitor ) 
	{
		IStatus status = Status.OK_STATUS;
		
		FileInputStream finStream = null;
		
		try {
			String outputDir, javaOutput;
			outputDir = removeFileProtocol(javaWSDLParam.getOutput());
			javaOutput = removeFileProtocol(javaWSDLParam.getJavaOutput());
			ResourceContext context = WebServicePlugin.getInstance().getResourceContext();				
			
			IPath outputPath = new Path (outputDir);
			outputPath = FileResourceUtils.getWorkspaceRootRelativePath(outputPath);
			
			String fileName;
			IPath targetPath=null;
			IStatusHandler statusHandler = environment.getStatusHandler();
			String deployFile;
			Iterator iterator;
			
			String tempOutputDir = tempOutputFile.toString();
			String [] movedDeployFiles = new String [deployFiles.size()];
			iterator = deployFiles.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				deployFile = (String) iterator.next();
				File source = new File(deployFile);
				finStream = new FileInputStream(source);
				if (finStream != null) {
					if (deployFile.startsWith(tempOutputDir)) {
						fileName = deployFile.substring(tempOutputDir.length());
						targetPath = outputPath.append(fileName).makeAbsolute();
						FileResourceUtils.createFile(context,  
								targetPath,
								finStream,
								monitor,
								statusHandler);
						movedDeployFiles[i++]= ResourceUtils.findResource(targetPath).getLocation().toString();
					}
					finStream.close();
				}
			}
			
			javaWSDLParam.setDeploymentFiles(movedDeployFiles);
			
			IPath javaOutputPath = new Path (javaOutput);
			javaOutputPath = FileResourceUtils.getWorkspaceRootRelativePath(javaOutputPath);
			
			String javaFile;
			String fullClassName = null;
			String [] movedJavaFiles =  new String [javaFiles.size()];
			iterator = javaFiles.iterator();
			i = 0;
			while (iterator.hasNext()) {
				javaFile = (String) iterator.next();
				File source = new File(javaFile);
				finStream = new FileInputStream(source);
				if (finStream != null) {
					// for the case Java2WSDL-WSDL2Java, no need to move Java files, just delete them
					if (!javaWSDLParam.isMetaInfOnly()) {
						// for the case WSDL2Java, move Java files to Java output directory
						if (javaFile.startsWith(tempOutputDir)) {
							fullClassName = javaFile.substring(tempOutputDir.length());
							targetPath = javaOutputPath.append(fullClassName).makeAbsolute();
							FileResourceUtils.createFile(context,  
									targetPath,
									finStream,
									monitor,
									statusHandler);
							movedJavaFiles[i++]= ResourceUtils.findResource(targetPath).getLocation().toString();
						}
					}
					
					finStream.close();
				}
			}
			javaWSDLParam.setJavaFiles(movedJavaFiles);
			
		} catch (Exception e) {
			status = StatusUtils.errorStatus(NLS.bind(AxisConsumptionCoreMessages.MSG_ERROR_MOVE_RESOURCE,new String[]{e.getLocalizedMessage()}), e);
			environment.getStatusHandler().reportError(status);
			
		} finally {
			if (finStream != null) {
				try {
					finStream.close();
				} catch (IOException e) {
				}
			}			
		}
		
		return status;
	}

	/**
	 * Deletes all files and subdirectories under dir. 
	 * Just ignore and keep going if delete is not successful
	 * 
	 * @param dir directory to delete
	 */
	public void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		// The directory is now empty so delete it
		dir.delete();
	}
	
	/**
	 * Creates a temporary directory under the plugin's state location (i.e.
	 * .metadata/.plugin directory)
	 * 
	 * @return File
	 */
	private File createTempDir() {
		String pluginTempDir = WebServiceAxisConsumptionCorePlugin
		.getInstance().getStateLocation().toString();
		File tempDir = new File(pluginTempDir);
		File newTempDir = null;
		try {
			newTempDir = File.createTempFile(TEMP, "", tempDir);
			// delete the temp file and create a temp directory instead
			if (newTempDir.delete()) {
				if (newTempDir.mkdir()) {
					tempDir = newTempDir;
				}
			}
			return tempDir;
		} catch (Exception e) {
			return tempDir;
		}
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

	public Status undo(IEnvironment environment) {
		return null;
	}

	public Status redo(IEnvironment environment) {
		return null;
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

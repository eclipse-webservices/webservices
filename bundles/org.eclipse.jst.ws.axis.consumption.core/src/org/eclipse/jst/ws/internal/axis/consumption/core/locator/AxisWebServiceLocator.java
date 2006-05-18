/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060517   140832 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.core.locator;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.ws.internal.wsfinder.AbstractWebServiceLocator;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;
import org.xml.sax.SAXException;

/**
 * @author cbrealey
 * The Axis Web service locator plugs itself into the
 * Web Services Finder framework and does the job of
 * locating Axis clients and services.
 */
public class AxisWebServiceLocator extends AbstractWebServiceLocator
{
	private String AXIS_STUB = "org.apache.axis.client.Stub";
	
	/**
	 * Searches the workspace for Axis clients as
	 * identified by non-stub classes that implement
	 * SEIs that are implemented by stubs that implement
	 * org.apache.axis.client.Stub.
	 * @param monitor A progress monitor, possibly null.
	 * @return A possibly empty list of WebServiceClientInfo objects.
	 */
	public List getWebServiceClients (IProgressMonitor monitor)
	{
		List list = new LinkedList();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IJavaModel javaModel = JavaCore.create(root);
		try
		{
			javaModel.open(monitor);
			IJavaProject[] javaProjects = javaModel.getJavaProjects();
			for (int i=0; i<javaProjects.length; i++)
			{
				// We're only interested in Java projects within which
				// the Axis runtime's client "Stub" class is loadable:
				IType axisStub = javaProjects[i].findType(AXIS_STUB);
				if (axisStub != null)
				{
					// Find "Proxy" classes in the project and add to the list.
					addAxisProxies(javaProjects[i],axisStub,list,monitor);
				}
			}
			javaModel.close();
		}
		catch (Exception e)
		{
			// Fall thru and return an empty list.
		}
		return list;
	}
	
	/**
	 * Tries to find Axis "Proxy" classes related the given Stub.
	 * @param axisStub The Axis client "Stub" from which to search.
	 * @param list A list to which we'll add and WebServiceClientInfo
	 * objects for "Proxy" classes we find in the search. 
	 */
	private void addAxisProxies (IJavaProject javaProject, IType axisStub, List list, IProgressMonitor monitor)
	{
		try
		{
			// Compute a hierarchy to help us find all the
			// generated Stub subclasses of the Axis "Stub"
			// class across the entire workspace:
			ITypeHierarchy axisStubHierarchy = axisStub.newTypeHierarchy(javaProject,monitor);
			IType[] stubs = axisStubHierarchy.getSubtypes(axisStub);
			for (int s=0; s<stubs.length; s++)
			{
				// For each stub, find all its super-interfaces of which
				// there should be one, namely the generated SEI:
				IType seis[] = axisStubHierarchy.getSuperInterfaces(stubs[s]);
				for (int i=0; i<seis.length; i++)
				{
					try
					{
						// Compute a hierarchy to help us find all the
						// generated classes that implement the SEI
						// confined to the current project:
						ITypeHierarchy seiHierarchy = seis[i].newTypeHierarchy(javaProject,monitor);
						IType[] proxies = seiHierarchy.getSubtypes(seis[i]);
						for (int p=0; p<proxies.length; p++)
						{
							// Under normal circumstances we should find two subclasses:
							// 1. The generated stub that got us here (two loops above).
							// 2. The generated "Proxy" that we're looking for.
							// Skip #1 and capture #2:
							if (!proxies[p].getFullyQualifiedName().equals(stubs[s].getFullyQualifiedName()))
							{
								WebServiceClientInfo wscInfo = newWebServiceClientInfo(proxies[p]);
								if (wscInfo != null)
								{
									list.add(wscInfo);
								}
							}
						}
					}
					catch (Exception e)
					{
						// We'll land here if the JDT was unable to compute
						// a type hierarchy for the current SEI.
						// This should never happen, but if it does,
						// there's not much we can do except bypass the SEI
						// and loop on to the next one.
					}
				}
			}
		}
		catch (Exception e)
		{
			// We'll land here if JDT was unable to compute
			// a type hierarchy for the Axis client "Stub" class.
			// This should never happen, but if it does,
			// there's not much we can do except bail out, having
			// added nothing to the list.
		}
	}
	
	/**
	 * Creates a new WebServiceClientInfo object for a
	 * JDT IType object representing a generated Proxy.
	 * @param axisProxy The IType of the generated Proxy.
	 * @return A new WebServiceClientInfo object describing the Proxy,
	 * or null if the IType does not appear to describe a normal Proxy. 
	 */
	private WebServiceClientInfo newWebServiceClientInfo (IType axisProxy)
	{
		WebServiceClientInfo wscInfo = null;
		try
		{
			IResource resource = axisProxy.getUnderlyingResource();
			if (resource != null)
			{
				IPath proxyPath = resource.getLocation();
				if (proxyPath != null)
				{
					String proxyURL = proxyPath.toFile().toURL().toString();
					wscInfo = new WebServiceClientInfo();
					wscInfo.setImplURL(proxyURL);
				}
			}
		}
		catch (Exception e)
		{
			// We'll land here if we could not convert the IType
			// into an IResource, or if we could not compute a URL
			// from that resource. Either way, it definitely isn't
			// a normal generated Proxy, so get out and return null.
		}
		return wscInfo;		
	}

	/**
	 * Searches the workspace for Axis services as
	 * identified by server-config.wsdd <service> elements
	 * in projects with an Axis servlet registered
	 * in the web.xml descriptor.
	 * @param monitor A progress monitor, possibly null.
	 * @return A possibly empty list of WebServiceInfo objects.
	 */
	public List getWebServices (IProgressMonitor monitor)
	{
		Vector webServices = new Vector();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		
		for (int i = 0; i < projects.length; i++)
		{
			IProject project = projects[i];
			
			// we are only intersted in dynamic web project
			if (J2EEUtils.isWebComponent(project))
			{
				try 
				{
					webServices.addAll(getWebServicesFromProject(project, monitor));
				} 
				catch (Exception e) 
				{
					// Fall thru and return an empty list.
				} 
			}
		}
		return webServices;
	}
	
	/**
	 * Searches for a particular web project for Axis services as
	 * identified by server-config.wsdd <service> elements
	 * with an Axis servlet registered
	 * in the web.xml descriptor.
	 * @param monitor A progress monitor, possibly null.
	 * @return A possibly empty list of WebServiceInfo objects.
	 */
	private List getWebServicesFromProject(IProject project, IProgressMonitor monitor) throws CoreException, ParserConfigurationException, SAXException, IOException
	{	
		// Search the web.xml in fixed location
		IPath webDescriptorPath = J2EEUtils.getWebInfPath(project).append("/web.xml");
		IFile webXmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(webDescriptorPath);
		
		if (webXmlFile != null && isAxisServletExisted(webXmlFile, monitor))
		{
			// Search for all server-config.wsdd inside this project
			ServerConfigWSDDVisitor visitor = new ServerConfigWSDDVisitor();
			project.accept(visitor);		
			visitor.visit(project);
			Vector wsddFiles = visitor.getWsddFiles();
			Vector servicesNames = new Vector(); 
			for (int i = 0; i < wsddFiles.size(); i++)
			{
				servicesNames.addAll(getWebServicesNamesFromAxisConfig((IFile)wsddFiles.get(i), monitor));
			}
			return getWebServices(project, servicesNames, monitor);
		}
		else
		{
			return new Vector();
		}
	}
	
	/*
	 * Determine whether web.xml contains axis servlet
	 */
	private boolean isAxisServletExisted(IFile file, IProgressMonitor monitor) throws CoreException, ParserConfigurationException, SAXException, IOException
	{
		InputStream inputStream = file.getContents();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser parser = factory.newSAXParser();
		AxisServletSaxHandler handler = new AxisServletSaxHandler();
		parser.parse(inputStream, handler);
		inputStream.close();
		return handler.isThereAxisServlet();
	}
	
	/*
	 * Given a list of services, find the servers this project is associated,
	 * Combine them and form various end point
	 */
	private List getWebServices(IProject project, List servicesNames, IProgressMonitor monitor)
	{
		Vector webServices = new Vector();
		IServer[] servers = ServerUtil.getServersByModule(ServerUtil.getModule(project),monitor);
		for (int i = 0; i < servers.length; i++)
		{
			String host = servers[i].getHost();
			ServerPort httpPort = getHttpPort(servers[i].getServerPorts(monitor));
			if (httpPort != null)
			{
				for (int j = 0; j < servicesNames.size(); j++)
				{
					WebServiceInfo wsInfo = new WebServiceInfo();
					String endPointURL = "http://" + host + ":" + httpPort.getPort() + "/" + J2EEUtils.getFirstWebModuleName(project) + "/services/" + (String)servicesNames.get(j);
					wsInfo.setEndPointURL(endPointURL);
					wsInfo.setWsdlURL(endPointURL+"?wsdl");
		            webServices.add(wsInfo);
				}
			}
		}
		return webServices;
	}
	
	/*
	 * Parse the server-config.wsdd file, return a list of names of web services 
	 */
	private List getWebServicesNamesFromAxisConfig(IFile file, IProgressMonitor monitor) throws CoreException, ParserConfigurationException, SAXException, IOException
	{
		InputStream inputStream = file.getContents();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser parser = factory.newSAXParser();
		AxisServicesSaxHandler handler = new AxisServicesSaxHandler();
		parser.parse(inputStream, handler);
		inputStream.close();
		return handler.getWebServicesNames();	
	}
	
	/*
	 * find the right port (http) from a list or ports
	 */
	private ServerPort getHttpPort(ServerPort[] ports)
	{
		for (int i = 0; i < ports.length; i++)
		{
			String name = ports[i].getName(); 
			if ( name != null && name.trim().toLowerCase().equals("http")) return ports[i];
		}
		return null;
	}
}

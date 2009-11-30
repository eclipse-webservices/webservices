/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

@SuppressWarnings("restriction")
public final class SpringUtils {
	private static Namespace SPRING_BEANS_NS = Namespace
	.getNamespace("http://www.springframework.org/schema/beans"); //$NON-NLS-1$
	private static Namespace XMLNS_XSI = Namespace.getNamespace("xsi", //$NON-NLS-1$
	"http://www.w3.org/2001/XMLSchema-instance"); //$NON-NLS-1$
	private static Namespace JAXWS_NS = Namespace.getNamespace("jaxws", "http://cxf.apache.org/jaxws"); //$NON-NLS-1$ //$NON-NLS-2$
	private static Namespace SOAP_NS = Namespace.getNamespace("soap", "http://cxf.apache.org/bindings/soap"); //$NON-NLS-1$ //$NON-NLS-2$

	private static String DOC_ROOT = "beans"; //$NON-NLS-1$

	private SpringUtils() {
	}

	public static IFile getBeansFile(IProject project) throws IOException {
		IFile beansFile = null;
		IPath webContentPath = J2EEUtils.getWebContentPath(project);
		if (!webContentPath.hasTrailingSeparator()) {
			webContentPath = webContentPath.addTrailingSeparator();

			IPath webINFPath = webContentPath.append(new Path("WEB-INF/")); //$NON-NLS-1$
			IFolder webINFFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(webINFPath);
			beansFile = webINFFolder.getFile("beans.xml"); //$NON-NLS-1$
			if (!beansFile.exists()) {
				try {
					IProgressMonitor progressMonitor = new NullProgressMonitor();
					beansFile.create(new ByteArrayInputStream(new byte[] {}), true, progressMonitor);
					createBaseBeansFile(beansFile);
				} catch (CoreException ce) {
					CXFCorePlugin.log(ce);
				}
			}
		}
		return beansFile;
	}

	public static IFile createHandlerFile(IProject project) throws IOException {
		IFile handlerFile = null;
		IPath webContentPath = J2EEUtils.getWebContentPath(project);
		if (!webContentPath.hasTrailingSeparator()) {
			webContentPath = webContentPath.addTrailingSeparator();

			IPath webINFPath = webContentPath.append(new Path("WEB-INF/")); //$NON-NLS-1$
			IFolder webINFFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(webINFPath);
			handlerFile = webINFFolder.getFile("handler.xml"); //$NON-NLS-1$
			if (!handlerFile.exists()) {
				try {
					IProgressMonitor progressMonitor = new NullProgressMonitor();
					handlerFile.create(new ByteArrayInputStream(new byte[] {}), true, progressMonitor);
					createBaseBeansFile(handlerFile);
				} catch (CoreException ce) {
					CXFCorePlugin.log(ce);
				}
			}
		}
		return handlerFile;
	}

	public static IFile getBeansFile(String projectName) throws IOException {
		return SpringUtils.getBeansFile(FileUtils.getProject(projectName));
	}

	/**
	 * Returns a handle to the cxf-servlet file in the Web projects WEB-INF
	 * folder.
	 * 
	 * @param project
	 * @return {@link IFile}
	 */
	public static IFile getCXFServlet(IProject project) throws IOException {
		IFile cxfServlet = null;
		IPath webContentPath = J2EEUtils.getWebContentPath(project);
		if (!webContentPath.hasTrailingSeparator()) {
			webContentPath = webContentPath.addTrailingSeparator();

			IPath webINFPath = webContentPath.append(new Path("WEB-INF/")); //$NON-NLS-1$
			IFolder webINFFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(webINFPath);
			cxfServlet = webINFFolder.getFile("cxf-servlet.xml"); //$NON-NLS-1$
			if (!cxfServlet.exists()) {
				try {
					IProgressMonitor progressMonitor = new NullProgressMonitor();

					cxfServlet.create(new ByteArrayInputStream(new byte[] {}), true, progressMonitor);
					createBaseCXFServletFile(cxfServlet);
				} catch (CoreException ce) {
					CXFCorePlugin.log(ce.getStatus());
				}
			}
		}
		return cxfServlet;
	}

	public static IFile getCXFServlet(String projectName) throws IOException {
		return SpringUtils.getCXFServlet(FileUtils.getProject(projectName));
	}

	private static void createBaseCXFServletFile(IFile cxfServlet)  throws IOException {
		Element beans = new Element(DOC_ROOT);
		beans.setNamespace(SPRING_BEANS_NS);
		beans.addNamespaceDeclaration(XMLNS_XSI);
		beans.addNamespaceDeclaration(JAXWS_NS);
		beans.addNamespaceDeclaration(SOAP_NS);

		Attribute schemaLocation = new Attribute(
				"schemaLocation", //$NON-NLS-1$
				"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd " //$NON-NLS-1$
				+ "http://cxf.apache.org/bindings/soap http://cxf.apache.org/schemas/configuration/soap.xsd " //$NON-NLS-1$
				+ "http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd", XMLNS_XSI); //$NON-NLS-1$

		beans.setAttribute(schemaLocation);

		writeConfig(new Document(beans), cxfServlet);
	}

	private static void createBaseBeansFile(IFile beansFile) throws IOException {
		Element beans = new Element(DOC_ROOT);
		beans.setNamespace(SPRING_BEANS_NS);
		beans.addNamespaceDeclaration(XMLNS_XSI);
		beans.addNamespaceDeclaration(JAXWS_NS);

		Attribute schemaLocation = new Attribute(
				"schemaLocation", //$NON-NLS-1$
				"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd " //$NON-NLS-1$
				+ "http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd", XMLNS_XSI); //$NON-NLS-1$

		beans.setAttribute(schemaLocation);

		Element importCXFResource = new Element("import", SPRING_BEANS_NS); //$NON-NLS-1$
		importCXFResource.setAttribute("resource", "classpath:META-INF/cxf/cxf.xml"); //$NON-NLS-1$ //$NON-NLS-2$

		Element importCXFSoapExtensionResource = new Element("import", SPRING_BEANS_NS); //$NON-NLS-1$
		importCXFSoapExtensionResource.setAttribute("resource", //$NON-NLS-1$
		"classpath:META-INF/cxf/cxf-extension-soap.xml"); //$NON-NLS-1$

		Element importCXFServeltResource = new Element("import", SPRING_BEANS_NS); //$NON-NLS-1$
		importCXFServeltResource.setAttribute("resource", "classpath:META-INF/cxf/cxf-servlet.xml"); //$NON-NLS-1$ //$NON-NLS-2$

		beans.addContent(importCXFResource);
		beans.addContent(importCXFSoapExtensionResource);
		beans.addContent(importCXFServeltResource);

		writeConfig(new Document(beans), beansFile);
	}

	public static boolean isSpringBeansFile(IFile springBeansFile) throws IOException {
		FileInputStream springBeansInputStream = new FileInputStream(springBeansFile.getLocation().toFile());
		if (springBeansInputStream.available() > 0) {
			SAXBuilder builder = new SAXBuilder();
			try {
				Document doc = builder.build(springBeansInputStream);
				Element root = doc.getRootElement();
				if (root.getName().equals(DOC_ROOT) && root.getNamespace().equals(SPRING_BEANS_NS)) {
					return true;
				}
			} catch (JDOMException jdome) {
				CXFCorePlugin.log(jdome);
			} finally {
				springBeansInputStream.close();
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static boolean isBeanDefined(CXFDataModel cxfDataModel, String projectName, String elementName,
			Namespace namespace, String id) throws IOException {
		IFile springConfigFile = null;

		if (CXFCorePlugin.getDefault().getJava2WSContext().isUseSpringApplicationContext()) {
			springConfigFile = SpringUtils.getBeansFile(projectName);
		} else {
			springConfigFile = SpringUtils.getCXFServlet(projectName);
		}

		if (isSpringBeansFile(springConfigFile)) {
			SAXBuilder builder = new SAXBuilder();
			FileInputStream springConfigInputSteam = new FileInputStream(springConfigFile.getLocation()
					.toFile());
			try {
				Document doc = builder.build(springConfigInputSteam);
				Element beans = doc.getRootElement();

				List<Element> endpoints = beans.getChildren(elementName, namespace);
				for (Element element : endpoints) {
					if (element != null && element.getAttribute("id") != null) { //$NON-NLS-1$
						Attribute idAttribute = element.getAttribute("id"); //$NON-NLS-1$
						if (idAttribute.getValue().equals(id)) {
							return true;
						}
					}
				}
			} catch (JDOMException jdome) {
				CXFCorePlugin.log(jdome);
			} finally {
				springConfigInputSteam.close();
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static String getEndpointAddress(IProject project, String jaxwsEndpointId) throws IOException {
		IFile springConfigFile = null;
		if (CXFCorePlugin.getDefault().getJava2WSContext().isUseSpringApplicationContext()) {
			springConfigFile = SpringUtils.getBeansFile(project);
		} else {
			springConfigFile = SpringUtils.getCXFServlet(project);
		}
		if (isSpringBeansFile(springConfigFile)) {
			SAXBuilder builder = new SAXBuilder();
			FileInputStream springConfigInputSteam = new FileInputStream(springConfigFile.getLocation()
					.toFile());
			try {
				Document doc = builder.build(springConfigInputSteam);
				Element beans = doc.getRootElement();
				List<Element> endpoints = beans.getChildren("endpoint", JAXWS_NS); //$NON-NLS-1$
				for (Element element : endpoints) {
					if (element != null && element.getAttribute("id") != null) { //$NON-NLS-1$
						Attribute idAttribute = element.getAttribute("id"); //$NON-NLS-1$
						if (idAttribute.getValue().equals(jaxwsEndpointId)) {
							return element.getAttribute("address").getValue(); //$NON-NLS-1$
						}
					}
				}
			} catch (JDOMException jdome) {
				CXFCorePlugin.log(jdome);
			} finally {
				springConfigInputSteam.close();
			}
		}
		return ""; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public static void createConfigurationFromWSDL(WSDL2JavaDataModel model) throws IOException {
		String targetNamespace = model.getTargetNamespace();
		String packageName = model.getIncludedNamespaces().get(targetNamespace);

		Definition definition = model.getWsdlDefinition();
		Map servicesMap = definition.getServices();
		Set<Map.Entry> servicesSet = servicesMap.entrySet();
		for (Map.Entry serviceEntry : servicesSet) {
			Service service = (Service) serviceEntry.getValue();
			model.setServiceName(service.getQName().getLocalPart());
			Map portsMap = service.getPorts();
			Set<Map.Entry> portsSet = portsMap.entrySet();
			for (Map.Entry portEntry : portsSet) {
				Port port = (Port) portEntry.getValue();
				model.setEndpointName(port.getName());
				PortType portType = port.getBinding().getPortType();
				QName qName = portType.getQName();
				String portTypeName = qName.getLocalPart();
				String fullyQualifiedClassName = packageName + "." + //$NON-NLS-1$
				convertPortTypeName(portTypeName) + "Impl"; //$NON-NLS-1$
				model.setFullyQualifiedJavaClassName(fullyQualifiedClassName);
				SpringUtils.createJAXWSEndpoint(model);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadSpringConfigInformationFromWSDL(CXFDataModel model) {
		IFile wsdlFile = WSDLUtils.getWSDLFolder(model.getProjectName()).getFile(model.getWsdlFileName());
		if (wsdlFile.exists()) {
			try {
				model.setWsdlURL(wsdlFile.getLocationURI().toURL());
				Definition definition = WSDLUtils.readWSDL(model.getWsdlURL());
				Map servicesMap = definition.getServices();
				Set<Map.Entry> servicesSet = servicesMap.entrySet();
				for (Map.Entry serviceEntry : servicesSet) {
					Service service = (Service) serviceEntry.getValue();
					model.setServiceName(service.getQName().getLocalPart());
					Map portsMap = service.getPorts();
					Set<Map.Entry> portsSet = portsMap.entrySet();
					for (Map.Entry portEntry : portsSet) {
						Port port = (Port) portEntry.getValue();
						model.setEndpointName(port.getName());
					}
				}
				model.setWsdlDefinition(definition);
			} catch (MalformedURLException murle) {
				CXFCorePlugin.log(murle);
			} catch (IOException ioe) {
				CXFCorePlugin.log(ioe);
			}
		}
	}

	private static String convertPortTypeName(String portTypeName) {
		String[] segments = portTypeName.split("[\\-\\.\\:\\_\\u00b7\\u0387\\u06dd\\u06de]");

		StringBuilder stringBuilder = new StringBuilder();
		for (String segment : segments) {
			if (segment.length() == 0) {
				continue;
			}
			char firstCharacter = segment.charAt(0);
			if (!Character.isDigit(firstCharacter) && Character.isLowerCase(firstCharacter)) {
				segment = segment.substring(0, 1).toUpperCase() + segment.substring(1);
			}

			for (int i = 1; i < segment.length(); i++) {
				char currentChar = segment.charAt(i);
				char precedingChar = segment.charAt(i - 1);
				if (Character.isLetter(currentChar) && Character.isDigit(precedingChar)
						&& Character.isLowerCase(currentChar)) {
					segment = segment.substring(0, i) + segment.substring(i, i + 1).toUpperCase()
					+ segment.substring(i + 1, segment.length());
				}
			}
			stringBuilder.append(segment);
		}
		return stringBuilder.toString();
	}

	public static void createJAXWSEndpoint(CXFDataModel model) throws IOException {
		String projectName = model.getProjectName();

		IFile springConfigFile = null;
		if (CXFCorePlugin.getDefault().getJava2WSContext().isUseSpringApplicationContext()) {
			springConfigFile = SpringUtils.getBeansFile(projectName);
		} else {
			springConfigFile = SpringUtils.getCXFServlet(projectName);
		}

		if (isSpringBeansFile(springConfigFile)) {
			SAXBuilder builder = new SAXBuilder();
			FileInputStream springConfigInputSteam = new FileInputStream(springConfigFile.getLocation()
					.toFile());
			try {
				Document doc = builder.build(springConfigInputSteam);
				Element beans = doc.getRootElement();

				Element jaxwsEndpoint = new Element("endpoint", JAXWS_NS); //$NON-NLS-1$

				String id = getJAXWSEndpointID(model);
				model.setConfigId(id);
				jaxwsEndpoint.setAttribute("id", id); //$NON-NLS-1$

				jaxwsEndpoint.setAttribute("implementor", model.getFullyQualifiedJavaClassName()); //$NON-NLS-1$

				if (model.getConfigWsdlLocation() != null) {
					jaxwsEndpoint.setAttribute("wsdlLocation", model.getConfigWsdlLocation()); //$NON-NLS-1$

					if (model.getEndpointName() != null && model.getServiceName() != null) {
						jaxwsEndpoint.setAttribute("endpointName", "tns:" + model.getEndpointName()); //$NON-NLS-1$ //$NON-NLS-2$
						jaxwsEndpoint.setAttribute("serviceName", "tns:" + model.getServiceName()); //$NON-NLS-1$ //$NON-NLS-2$

						Namespace XMLNS_TNS = Namespace.getNamespace("tns", model.getTargetNamespace()); //$NON-NLS-1$
						jaxwsEndpoint.addNamespaceDeclaration(XMLNS_TNS);
					}
				}

				if (model.getEndpointName() != null) {
					jaxwsEndpoint.setAttribute("address", "/" + model.getEndpointName()); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					jaxwsEndpoint.setAttribute("address", "/" + id); //$NON-NLS-1$ //$NON-NLS-2$
				}


				Element jaxwsFeatures = new Element("features", JAXWS_NS); //$NON-NLS-1$
				Element bean = new Element("bean", SPRING_BEANS_NS); //$NON-NLS-1$
				bean.setAttribute("class", "org.apache.cxf.feature.LoggingFeature"); //$NON-NLS-1$ //$NON-NLS-2$
				jaxwsFeatures.addContent(bean);
				jaxwsEndpoint.addContent(jaxwsFeatures);

				if (!isBeanDefined(model, projectName, "endpoint", JAXWS_NS, id)) { //$NON-NLS-1$
					beans.addContent(jaxwsEndpoint);

					writeConfig(doc, springConfigFile);
				}
			} catch (JDOMException jdome) {
				CXFCorePlugin.log(jdome);
			} finally {
				springConfigInputSteam.close();
			}
		}
	}

	private static String getJAXWSEndpointID(CXFDataModel model) {
		String implementor = model.getFullyQualifiedJavaClassName();
		if (implementor.indexOf(".") != -1) { //$NON-NLS-1$
			implementor = implementor.substring(implementor.lastIndexOf(".") + 1, implementor.length());
		}
		if (!implementor.startsWith("Impl") && implementor.indexOf("Impl") != -1) {
			implementor = implementor.substring(0, implementor.indexOf("Impl")).toLowerCase(); //$NON-NLS-1$;
		} else {
			implementor = implementor.toLowerCase();
		}
		return implementor;
	}

	private static void writeConfig(Document document, IFile springConfigFile) throws IOException {
		OutputStream outputStream = new FileOutputStream(springConfigFile.getLocation().toFile());
		try {
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(document, outputStream);
			springConfigFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
			FileUtils.formatXMLFile(springConfigFile);
		} catch (CoreException ce) {
			CXFCorePlugin.log(ce.getStatus());
		} finally {
			outputStream.close();
		}
	}
}

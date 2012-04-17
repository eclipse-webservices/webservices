/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.utils.resources.StringInputStreamAdapter;

public class WsValidationTest extends ValidationTestsSetUp 
{
	private IType wsType;
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		wsType = testProject.createType(testPack, "Ws.java", "@javax.jws.WebService(serviceName=\"WsName\") public class Ws {}");
		testProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD);
		assertNotNull("Web service not found", findWs("test.Ws"));
	}
	
	public void testServiceNameIsNCName() throws CoreException
	{
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(serviceName=\"---\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 48);
		markerAttributes.put(IMarker.CHAR_END, 53);
		markerAttributes.put(IMarker.LINE_NUMBER, 2);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}
	
	public void testPortNameIsNCName() throws CoreException
	{
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(portName=\"---\") public class Ws {}");

		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 45);
		markerAttributes.put(IMarker.CHAR_END, 50);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}		
	
	public void testTargetNsIsUri() throws CoreException 
	{
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 52);
		markerAttributes.put(IMarker.CHAR_END, 57);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}

	public void testSEIExists() throws CoreException 
	{
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"com.sap.demo.Test\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 54);
		markerAttributes.put(IMarker.CHAR_END, 73);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}

	public void testNameMissingIfSeiReferenced() throws CoreException 
	{
		final IType sei = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
		assertNotNull(sei);
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"test.Sei\") public class Ws {}");
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", endpointInterface=\"test.Sei\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 41);
		markerAttributes.put(IMarker.CHAR_END, 47);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}	

	public void testWsdlLocationInProject() throws CoreException
	{
		IFile file = testProject.getProject().getFolder("WebContent").getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test.wsdl\") public class Ws {}");
		testProject.build(IncrementalProjectBuilder.CLEAN_BUILD);
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		// wrong location
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test1.wsdl\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 62);
		markerAttributes.put(IMarker.CHAR_END, 74);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE, "Test1.wsdl"));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}
	
	public void testWsdlLocationInWebInfCorrect() throws CoreException
	{
		final IFolder webInf = testProject.getProject().getFolder("WebContent").getFolder("WEB-INF");
		IFile file = webInf.getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(getWsdlContent()), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"WEB-INF/Test.wsdl\") public class Ws {}");
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}

    private String getWsdlContent()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<wsdl:definitions name=\"WsService\" targetNamespace=\"http://test/\"\n");
        builder.append("  xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:tns=\"http://test/\"\n");
        builder.append("  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\">\n");
        builder.append("  <wsdl:portType name=\"Ws\">\n");
        builder.append("  </wsdl:portType>\n");
        builder.append("  <wsdl:binding name=\"WsServiceSoapBinding\" type=\"tns:Ws\">\n");
        builder.append("    <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>\n");
        builder.append("  </wsdl:binding>\n");
        builder.append("  <wsdl:service name=\"WsService\">\n");
        builder.append("    <wsdl:port name=\"WsPort\" binding=\"tns:WsServiceSoapBinding\">\n");
        builder.append("      <soap:address location=\"http://localhost:9090/WsPort\"/>\n");
        builder.append("    </wsdl:port>\n");
        builder.append("  </wsdl:service>\n");
        builder.append("</wsdl:definitions>\n");
        return builder.toString();
    }

    public void testWarningReadingWsdlLocation() throws CoreException
    {
        final IFolder webInf = testProject.getProject().getFolder("WebContent").getFolder("WEB-INF");
        IFile file = webInf.getFile("Test.wsdl");
        file.create(new StringInputStreamAdapter(""), true, null);
        
        setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"WEB-INF/Test.wsdl\") public class Ws {}");

        final Map<String, Object> markerAttributes = new HashMap<String, Object>();
        markerAttributes.put(IMarker.CHAR_START, 62);
        markerAttributes.put(IMarker.CHAR_END, 81);
        markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
        markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_READ, file.getLocationURI().toString()));
        final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
        validateResourceMarkers(wsType.getResource(), markerData);
    }

    public void testWarningLocatingWsdlLocation() throws CoreException
    {
        final IFolder webInf = testProject.getProject().getFolder("WebContent").getFolder("WEB-INF");
        IFile file = webInf.getFile("Test.wsdl");
        file.create(new StringInputStreamAdapter(""), true, null);
        
        setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"WEB-INF/Test3.wsdl\") public class Ws {}");

        final Map<String, Object> markerAttributes = new HashMap<String, Object>();
        markerAttributes.put(IMarker.CHAR_START, 62);
        markerAttributes.put(IMarker.CHAR_END, 82);
        markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
        markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE, "WEB-INF/Test3.wsdl"));
        final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
        validateResourceMarkers(wsType.getResource(), markerData);
    }
	
	public void testWsdlLocationEmpty() throws CoreException
	{
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"\") public class Ws {}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 62);
		markerAttributes.put(IMarker.CHAR_END, 64);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.EMPTY_ATTRIBUTE_VALUE, "WebService", "wsdlLocation"));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);
	}
	
	public void testWsdlLocationAbsoluteUrl() throws CoreException, MalformedURLException
	{
		IFile file = testProject.getProject().getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		final String fileUrl = file.getLocation().toFile().toURI().toURL().toString();

		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"" + fileUrl + "\") public class Ws {}");
		testProject.build(IncrementalProjectBuilder.CLEAN_BUILD);
		assertNoValidationErrors(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}
	
	public void testEndpointImplementsSei() throws CoreException 
	{
		final IType seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService public interface Sei {" + 
				"public void test();" + 
				"public int second(int a, int b, String c);" + 
				"}"); 
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"test.Sei\") public class Ws {" + 
				"public void test() {}" + 
			"}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 54);
		markerAttributes.put(IMarker.CHAR_END, 64);
		markerAttributes.put(IMarker.LINE_NUMBER, 2);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT, "Sei.second(int, int, String)"));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(wsType.getResource(), markerData);

		final IServiceEndpointInterface sei = findSei("test.Sei");
		assertNotNull(sei);
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}
}

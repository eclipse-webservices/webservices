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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;
import org.eclipse.jst.ws.jaxws.utils.resources.StringInputStreamAdapter;
import org.jmock.core.Constraint;

public class WsValidationTest extends ValidationTestsSetUp 
{
	private IType wsType;
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		wsType = testProject.createType(testPack, "Ws.java", "@javax.jws.WebService(serviceName=\"WsName\") public class Ws {}");
	}
	
	public void testServiceNameIsNCName() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		assertNoValidationErrors(wsType.getResource(), WsProblemsReporter.MARKER_ID, ws);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(serviceName=\"---\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(47));
		markerAttributes.put(IMarker.CHAR_END, eq(52));
		markerAttributes.put(IMarker.LINE_NUMBER, eq(2));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}
	
	public void testPortNameIsNCName() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		assertNoValidationErrors(wsType.getResource(), WsProblemsReporter.MARKER_ID, ws);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(portName=\"---\") public class Ws {}");

		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(44));
		markerAttributes.put(IMarker.CHAR_END, eq(49));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}		
	
	public void testTargetNsIsUri() throws CoreException 
	{
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(51));
		markerAttributes.put(IMarker.CHAR_END, eq(56));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}

	public void testSEIExists() throws CoreException 
	{
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"com.sap.demo.Test\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(53));
		markerAttributes.put(IMarker.CHAR_END, eq(72));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}

	public void testNameMissingIfSeiReferenced() throws CoreException 
	{
		final IType sei = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
		assertNotNull(sei);
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"test.Sei\") public class Ws {}");
		assertNoValidationErrors(wsType.getResource(), WsProblemsReporter.MARKER_ID, ws);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", endpointInterface=\"test.Sei\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(40));
		markerAttributes.put(IMarker.CHAR_END, eq(46));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}	

	public void testWsdlLocationInProject() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		IFile file = testProject.getProject().getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test.wsdl\") public class Ws {}");
		assertNoValidationErrors(wsType.getResource(), WsProblemsReporter.MARKER_ID, ws);
		
		// wrong location
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test1.wsdl\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(61));
		markerAttributes.put(IMarker.CHAR_END, eq(73));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_WARNING));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}
	
	public void testWsdlLocationInMetaInfCorrect() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		final IFolder metaInf = ((IFolder)testProject.getSourceFolder().getResource()).getFolder("META-INF");
		metaInf.create(true, true, null);
		IFile file = metaInf.getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"META-INF/Test.wsdl\") public class Ws {}");
		assertNoValidationErrors(wsType.getResource(), WsProblemsReporter.MARKER_ID, ws);
	}
	
	public void testWsdlLocationIncorrect() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");		
		// empty location
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"\") public class Ws {}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(61));
		markerAttributes.put(IMarker.CHAR_END, eq(63));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_WARNING));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}
	
	public void testEndpointCorrect() throws CoreException
	{
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService class Ws {}");
		IWebService ws = findWs("test.Ws");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(13));
		markerAttributes.put(IMarker.CHAR_END, eq(34));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);
	}
	
	public void testEndpointImplementsSei() throws CoreException 
	{
		IType seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService public interface Sei {" + 
				"public void test();" + 
				"public int second(int a, int b, String c);" + 
				"}"); 
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"test.Sei\") public class Ws {" + 
				"public void test() {}" + 
			"}");
		
		IWebService ws = findWs("test.Ws"); 

		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(53));
		markerAttributes.put(IMarker.CHAR_END, eq(63));
		markerAttributes.put(IMarker.LINE_NUMBER, eq(2));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(wsType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(ws, markerData);

		final IServiceEndpointInterface sei = findSei("test.Sei");
		assertNotNull(sei);
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
	}
}

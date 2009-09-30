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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;
import org.eclipse.jst.ws.jaxws.utils.resources.StringInputStreamAdapter;

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
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(serviceName=\"---\") public class Ws {}");
		validator.validate(ws);
		
		markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(47, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(52, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(2, markers[0].getAttribute(IMarker.LINE_NUMBER));		
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));		
	}
	
	public void testPortNameIsNCName() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(portName=\"---\") public class Ws {}");
		validator.validate(ws);
		
		markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(44, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(49, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));		
	}		
	
	public void testTargetNsIsUri() throws CoreException 
	{
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public class Ws {}");
		validator.validate(ws);

		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(51, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(56, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));		
	}

	public void testSEIExists() throws CoreException 
	{
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"com.sap.demo.Test\") public class Ws {}");
		validator.validate(ws);

		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(53, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(72, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));			
	}

	public void testNameMissingIfSeiReferenced() throws CoreException 
	{
		final IType sei = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
		assertNotNull(sei);
		final IWebService ws = findWs("test.Ws");
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(endpointInterface=\"test.Sei\") public class Ws {}");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);		
		assertEquals(0, markers.length);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", endpointInterface=\"test.Sei\") public class Ws {}");
		validator.validate(ws);
		markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(40, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(46, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));		
	}	

	public void testWsdlLocationInProject() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		IFile file = testProject.getProject().getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test.wsdl\") public class Ws {}");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);		
		// wrong location
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"Test1.wsdl\") public class Ws {}");
		validator.validate(ws);
		markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(61, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(73, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_WARNING, markers[0].getAttribute(IMarker.SEVERITY));	
	}
	
	public void testWsdlLocationInMetaInfCorrect() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");
		final IFolder metaInf = ((IFolder)testProject.getSourceFolder().getResource()).getFolder("META-INF");
		metaInf.create(true, true, null);
		IFile file = metaInf.getFile("Test.wsdl");
		file.create(new StringInputStreamAdapter(""), true, null);
		
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"META-INF/Test.wsdl\") public class Ws {}");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
	
	public void testWsdlLocationIncorrect() throws CoreException
	{
		final IWebService ws = findWs("test.Ws");		
		// empty location
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"Test\", wsdlLocation=\"\") public class Ws {}");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(61, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(63, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_WARNING, markers[0].getAttribute(IMarker.SEVERITY));			
	}
	
	public void testEndpointCorrect() throws CoreException
	{
		setContents(wsType.getCompilationUnit(), "@javax.jws.WebService class Ws {}");
		IWebService ws = findWs("test.Ws");
		validator.validate(ws);
		IMarker [] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(13, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(34, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));	
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

		validator.validate(ws);
		IMarker[] markers = wsType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
		assertEquals(53, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(63, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(2, markers[0].getAttribute(IMarker.LINE_NUMBER));
		
		markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
}

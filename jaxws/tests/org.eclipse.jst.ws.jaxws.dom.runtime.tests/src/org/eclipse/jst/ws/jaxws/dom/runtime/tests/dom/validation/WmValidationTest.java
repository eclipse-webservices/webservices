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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;

public class WmValidationTest extends ValidationTestsSetUp
{
	private IType seiType;
	private IServiceEndpointInterface sei;
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"test\") public void test();" +
				"}");
	}
	
	public void testNameIsNCName() throws CoreException
	{
//		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(seiType.getCompilationUnit(),  "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"---\") public void test(); \n" + "}");
		
		sei = findSei("test.Sei");
		validator.validate(sei);
		waitUntilMarkersAppear(seiType.getResource(), WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(109, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(114, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(3, markers[0].getAttribute(IMarker.LINE_NUMBER));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testNameIsUnique() throws CoreException
	{
		setContents(seiType.getCompilationUnit(),  "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"OpName\") public void first(); \n" +
				"@javax.jws.WebMethod(operationName=\"OpName\") public void second(); \n" + 
				"}");
		
		sei = findSei("test.Sei");
		validator.validate(sei);
		waitUntilMarkersAppear(seiType.getResource(), WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(2, markers.length);
		
		assertTrue(((Integer)markers[0].getAttribute(IMarker.CHAR_START)) > 0);
		assertTrue(((Integer)markers[0].getAttribute(IMarker.CHAR_END)) > 0);
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
		assertTrue(((Integer)markers[1].getAttribute(IMarker.CHAR_START)) > 0);
		assertTrue(((Integer)markers[1].getAttribute(IMarker.CHAR_END)) > 0);
		assertEquals(IMarker.SEVERITY_ERROR, markers[1].getAttribute(IMarker.SEVERITY));		
	}
	
	public void testNameIsUniqueExcludedMethod() throws CoreException
	{	
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(exclude=true) public void first(); \n" +
				"@javax.jws.WebMethod(operationName=\"first\") public void second(); \n" + 
				"}");
		
		sei = findSei("test.Sei");
		validator.validate(sei);
		waitUntilMarkersAppear(seiType.getResource(), WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		// only the marker for incorrect 'excluded=true' should be available
		assertEquals(1, markers.length);
		assertEquals(103, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(107, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}

	/** 
	 * this test currently does nothing because excluded methods are not contained in 
	 * the model. 
	 * 
	 * @throws CoreException
	 */ 
	public void testRedundantAttributesForExcludedMethod() throws CoreException
	{
//		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public class Sei {\n" +
//				"@javax.jws.WebMethod(exclude=true, operationName=\"OpName\") public void first(); \n" +
//				"}");
//		
//		sei = findSei("test.Sei");
//		IWebService ws = findWs("test.Sei");
//		
//		validator.validate(sei);
//		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
	}
	
	public void testMethodCannotBeExcludedInSEI() throws CoreException
	{		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(exclude=true) public void first(); \n" +
				"}");
		
		sei = findSei("test.Sei");
		validator.validate(sei);
		waitUntilMarkersAppear(seiType.getResource(), WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(103, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(107, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
}

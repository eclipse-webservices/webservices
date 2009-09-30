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

/**
 * Tests for SEI validation constraints 
 * 
 * @author Georgi Vachkov
 */
public class SeiValidationTest extends ValidationTestsSetUp 
{
	protected IType seiType;

	public void setUp() throws Exception
	{
		super.setUp();
		seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
	}

	public void testServiceNameIsNCName() throws CoreException
	{		
		final IServiceEndpointInterface sei = findSei("test.Sei");
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"---\") public interface Sei {}");
		validator.validate(sei);
		markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);		
		assertEquals(1, markers.length);
		assertEquals(40, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(45, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(2, markers[0].getAttribute(IMarker.LINE_NUMBER));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testTargetNsIsValidUri() throws CoreException
	{
		final IServiceEndpointInterface sei = findSei("test.Sei");
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public interface Sei {}");
		validator.validate(sei);
		markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(51, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(56, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}

	public void testRedundandAttributesOnExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public interface Sei {}");
		IServiceEndpointInterface sei = findSei("test.Sei");		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(13, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(68, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testRedundandAttributesOnImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public class Ws {}");		
		IServiceEndpointInterface sei = findSei("test.Ws");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
	
	public void testSeiClassCorrectExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService interface Sei {}");
		IServiceEndpointInterface sei = findSei("test.Sei");		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(13, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(34, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testSeiClassCorrectImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService public class Ws {}");		
		IServiceEndpointInterface sei = findSei("test.Ws");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
}
	

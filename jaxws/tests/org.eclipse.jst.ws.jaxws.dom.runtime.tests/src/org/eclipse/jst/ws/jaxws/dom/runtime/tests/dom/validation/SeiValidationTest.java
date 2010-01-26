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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;
import org.jmock.core.Constraint;

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
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"---\") public interface Sei {}");
		
		final Map<Object, Constraint> markerProps = new HashMap<Object, Constraint>();
		markerProps.put(IMarker.CHAR_START, eq(40));
		markerProps.put(IMarker.CHAR_END, eq(45));
		markerProps.put(IMarker.LINE_NUMBER, eq(2));
		markerProps.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markersData = new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerProps);
		validate(sei, markersData);
	}
	
	public void testTargetNsIsValidUri() throws CoreException
	{
		final IServiceEndpointInterface sei = findSei("test.Sei");
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public interface Sei {}");

		final Map<Object, Constraint> markerProps = new HashMap<Object, Constraint>();
		markerProps.put(IMarker.CHAR_START, eq(51));
		markerProps.put(IMarker.CHAR_END, eq(56));
		markerProps.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markersData = new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerProps);
		validate(sei, markersData);
	}

	public void testRedundandAttributesOnExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public interface Sei {}");
		IServiceEndpointInterface sei = findSei("test.Sei");
		
		final Map<Object, Constraint> markerProps = new HashMap<Object, Constraint>();
		markerProps.put(IMarker.CHAR_START, eq(13));
		markerProps.put(IMarker.CHAR_END, eq(68));
		markerProps.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markersData = new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerProps);
		validate(sei, markersData);
	}
	
	public void testRedundandAttributesOnImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public class Ws {}");		
		IServiceEndpointInterface sei = findSei("test.Ws");
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
	}
	
	public void testSeiClassCorrectExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService interface Sei {}");
		IServiceEndpointInterface sei = findSei("test.Sei");
		
		final Map<Object, Constraint> markerProps = new HashMap<Object, Constraint>();
		markerProps.put(IMarker.CHAR_START, eq(13));
		markerProps.put(IMarker.CHAR_END, eq(34));
		markerProps.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markersData = new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerProps);
		validate(sei, markersData);
	}
	
	public void testSeiClassCorrectImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService public class Ws {}");		
		IServiceEndpointInterface sei = findSei("test.Ws");
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
	}
}
	

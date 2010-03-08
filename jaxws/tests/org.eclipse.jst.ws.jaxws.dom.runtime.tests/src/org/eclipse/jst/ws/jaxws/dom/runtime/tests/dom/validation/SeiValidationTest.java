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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

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
		assertNotNull("SEI not found", findSei("test.Sei"));
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"---\") public interface Sei {}");
		
		final Map<String, Object> markerProps = new HashMap<String, Object>();
		markerProps.put(IMarker.CHAR_START, 41);
		markerProps.put(IMarker.CHAR_END, 46);
		markerProps.put(IMarker.LINE_NUMBER, 2);
		markerProps.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerProps.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.INVALID_NCNAME_ATTRIBUTE, "WebService", "name", "---"));
		final MarkerData markersData = new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerProps);
		validateResourceMarkers(seiType.getResource(), markersData);
	}
	
	public void testTargetNsIsValidUri() throws CoreException
	{
		assertNotNull("SEI not found", findSei("test.Sei"));
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(targetNamespace=\"^^^\") public interface Sei {}");

		final Map<String, Object> markerProps = new HashMap<String, Object>();
		markerProps.put(IMarker.CHAR_START, 52);
		markerProps.put(IMarker.CHAR_END, 57);
		markerProps.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerProps.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.TARGET_NAMESPACE_URI_SYNTAX_ERROR, "0", "^^^", "Illegal character in path"));
		final MarkerData markersData = new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerProps);
		validateResourceMarkers(seiType.getResource(), markersData);
	}

	public void testRedundandAttributesOnExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public interface Sei {}");
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> markerProps = new HashMap<String, Object>();
		markerProps.put(IMarker.CHAR_START, 58);
		markerProps.put(IMarker.CHAR_END, 68);
		markerProps.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerProps.put(IMarker.MESSAGE, JAXWSCoreMessages.WEBSERVICE_PORTNAME_SEI);
		final MarkerData markersData = new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerProps);
		validateResourceMarkers(seiType.getResource(), markersData);
	}
	
	public void testRedundandAttributesOnImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService(name=\"Name\", portName=\"PortName\") public class Ws {}");		
		assertNotNull("SEI not found", findSei("test.Ws"));
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}
	
	public void testSeiClassCorrectExplicitSei() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService interface Sei {}");
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> markerProps = new HashMap<String, Object>();
		markerProps.put(IMarker.CHAR_START, 46);
		markerProps.put(IMarker.CHAR_END, 49);
		markerProps.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerProps.put(IMarker.MESSAGE, JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL);
		final MarkerData markersData = new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerProps);
		validateResourceMarkers(seiType.getResource(), markersData);
	}
	
	public void testSeiClassCorrectImplicitSei() throws CoreException
	{
		testProject.createType(testPack, "Ws.java", "@javax.jws.WebService public class Ws {}");		
		assertNotNull("SEI not found", findSei("test.Sei"));
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}
}
	

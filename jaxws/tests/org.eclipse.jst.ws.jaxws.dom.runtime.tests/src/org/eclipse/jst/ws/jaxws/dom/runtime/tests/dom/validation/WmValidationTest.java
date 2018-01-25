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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class WmValidationTest extends ValidationTestsSetUp
{
	private IType seiType;
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"test\") public void test();" +
				"}");
		testProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD);
	}
	
	public void testNameIsNCName() throws CoreException
	{
		IMarker [] markers = seiType.getResource().findMarkers(VALIDATION_PROBLEM_MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
		
		setContents(seiType.getCompilationUnit(),  "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"---\") public void test(); \n" + "}");
		
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 110);
		markerAttributes.put(IMarker.CHAR_END, 115);
		markerAttributes.put(IMarker.LINE_NUMBER, 3);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.INVALID_NCNAME_ATTRIBUTE, "WebMethod", "operationName", "---"));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}
	
	public void testNameIsUnique() throws CoreException
	{
		setContents(seiType.getCompilationUnit(),  "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(operationName=\"OpName\") public void first(); \n" +
				"@javax.jws.WebMethod(operationName=\"OpName\") public void second(); \n" + 
				"}");
		
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> marker1_Attributes = new HashMap<String, Object>();
		marker1_Attributes.put(IMarker.CHAR_START, 132);
		marker1_Attributes.put(IMarker.CHAR_END, 137);
		marker1_Attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker1_Attributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, "{http://test/}OpName"));
		final Map<String, Object> marker2_Attributes = new HashMap<String, Object>();
		marker2_Attributes.put(IMarker.CHAR_START, 199);
		marker2_Attributes.put(IMarker.CHAR_END, 205);
		marker2_Attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker2_Attributes.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, "{http://test/}OpName"));
		final MarkerData marker1_Data =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, marker1_Attributes);
		final MarkerData marker2_Data =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, marker2_Attributes);
		validateResourceMarkers(seiType.getResource(), marker1_Data, marker2_Data);
	}
	
	public void testNameIsUniqueExcludedMethod() throws CoreException
	{	
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(exclude=true) public void first(); \n" +
				"@javax.jws.WebMethod(operationName=\"first\") public void second(); \n" + 
				"}");
		
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> markerAttributes_1 = new HashMap<String, Object>();
		markerAttributes_1.put(IMarker.CHAR_START, 122);
		markerAttributes_1.put(IMarker.CHAR_END, 127);
		markerAttributes_1.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes_1.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, "{http://test/}first"));

		final Map<String, Object> markerAttributes_2 = new HashMap<String, Object>();
		markerAttributes_2.put(IMarker.CHAR_START, 104);
		markerAttributes_2.put(IMarker.CHAR_END, 108);
		markerAttributes_2.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes_2.put(IMarker.MESSAGE, JAXWSCoreMessages.WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI);
		
		final Map<String, Object> markerAttributes_3 = new HashMap<String, Object>();
		markerAttributes_3.put(IMarker.CHAR_START, 188);
		markerAttributes_3.put(IMarker.CHAR_END, 194);
		markerAttributes_3.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes_3.put(IMarker.MESSAGE, MessageFormat.format(JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, "{http://test/}first"));
		
		final MarkerData markerData_1 =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes_1);
		final MarkerData markerData_2 =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes_2);
		final MarkerData markerData_3 =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes_3);
		validateResourceMarkers(seiType.getResource(), markerData_1, markerData_2, markerData_3);
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
//		IMarker [] markers = seiType.getResource().findMarkers(VALIDATION_PROBLEM_MARKER_ID, false, IResource.DEPTH_ZERO);
	}
	
	public void testMethodCannotBeExcludedInSEI() throws CoreException
	{		
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {\n" +
				"@javax.jws.WebMethod(exclude=true) public void first(); \n" +
				"}");
		
		assertNotNull("SEI not found", findSei("test.Sei"));
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 104);
		markerAttributes.put(IMarker.CHAR_END, 108);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		markerAttributes.put(IMarker.MESSAGE, JAXWSCoreMessages.WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI);
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}
}

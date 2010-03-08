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

public class WpValidationTest extends ValidationTestsSetUp 
{
	protected IType seiType;
	//protected IServiceEndpointInterface sei;

	public void setUp() throws Exception
	{
		super.setUp();
		seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
		assertNotNull("Could not find SEI", findSei(seiType.getFullyQualifiedName()));
	}
	
	public void testNameIsNCName() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"---\")int a); \n" + 
				"}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 116);
		markerAttributes.put(IMarker.CHAR_END, 121);
		markerAttributes.put(IMarker.LINE_NUMBER, 2);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}
	
	public void testPartNameIsNCName() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(partName=\"---\")int a); \n" + 
				"}");

		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 180);
		markerAttributes.put(IMarker.CHAR_END, 185);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}	
	
	public void testNameIsUniqe() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"param1\")int a, @javax.jws.WebParam(name=\"param1\")int b); \n" + 
				"}");
		
		final Map<String, Object> marker1_Attributes = new HashMap<String, Object>();
		marker1_Attributes.put(IMarker.CHAR_START, 116);
		marker1_Attributes.put(IMarker.CHAR_END, 124);
		marker1_Attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final Map<String, Object> marker2_Attributes = new HashMap<String, Object>();
		marker2_Attributes.put(IMarker.CHAR_START, 157);
		marker2_Attributes.put(IMarker.CHAR_END, 165);
		marker2_Attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		
		final MarkerData marker1_Data =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, marker1_Attributes);
		final MarkerData marker2_Data =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, marker2_Attributes);
		validateResourceMarkers(seiType.getResource(), marker2_Data, marker1_Data);
	}
	
	public void testNameNotRedundant() throws CoreException
	{
		// in RPC style if partName is not present the name is not redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\")int a); \n" + 
				"}");
		assertNoValidationErrors(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}
	
	public void testNameRedundant() throws CoreException
	{
		// in RPC style if partName is present the name is redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\", partName=\"myPart\")int a); \n" + 
				"}");
		
		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 176);
		markerAttributes.put(IMarker.CHAR_END, 184);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}
	
	public void testTargetNsValidUri() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(targetNamespace=\"^^^\")int a); \n" + 
				"}");

		final Map<String, Object> markerAttributes = new HashMap<String, Object>();
		markerAttributes.put(IMarker.CHAR_START, 127);
		markerAttributes.put(IMarker.CHAR_END, 132);
		markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		final MarkerData markerData =  new MarkerData(seiType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markerAttributes);
		validateResourceMarkers(seiType.getResource(), markerData);
	}
}

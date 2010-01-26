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

public class WpValidationTest extends ValidationTestsSetUp 
{
	protected IType seiType;
	protected IServiceEndpointInterface sei;

	public void setUp() throws Exception
	{
		super.setUp();
		seiType = testProject.createType(testPack, "Sei.java", "@javax.jws.WebService(name=\"SeiName\") public interface Sei {}");
		sei = findSei(seiType.getFullyQualifiedName());
	}
	
	public void testNameIsNCName() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"---\")int a); \n" + 
				"}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(115));
		markerAttributes.put(IMarker.CHAR_END, eq(120));
		markerAttributes.put(IMarker.LINE_NUMBER, eq(2));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(sei, markerData);
	}
	
	public void testPartNameIsNCName() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(partName=\"---\")int a); \n" + 
				"}");

		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(179));
		markerAttributes.put(IMarker.CHAR_END, eq(184));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(sei, markerData);
	}	
	
	public void testNameIsUniqe() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"param1\")int a, @javax.jws.WebParam(name=\"param1\")int b); \n" + 
				"}");
		
		final Map<Object, Constraint> marker1_Attributes = new HashMap<Object, Constraint>();
		marker1_Attributes.put(IMarker.CHAR_START, eq(115));
		marker1_Attributes.put(IMarker.CHAR_END, eq(123));
		marker1_Attributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final Map<Object, Constraint> marker2_Attributes = new HashMap<Object, Constraint>();
		marker2_Attributes.put(IMarker.CHAR_START, eq(156));
		marker2_Attributes.put(IMarker.CHAR_END, eq(164));
		marker2_Attributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		
		final MarkerData marker1_Data =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, marker1_Attributes);
		final MarkerData marker2_Data =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, marker2_Attributes);
		validate(sei, marker2_Data, marker1_Data);
	}
	
	public void testNameNotRedundant() throws CoreException
	{
		// in RPC style if partName is not present the name is not redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\")int a); \n" + 
				"}");
		assertNoValidationErrors(seiType.getResource(), WsProblemsReporter.MARKER_ID, sei);
	}
	
	public void testNameRedundant() throws CoreException
	{
		// in RPC style if partName is present the name is redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\", partName=\"myPart\")int a); \n" + 
				"}");
		
		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(175));
		markerAttributes.put(IMarker.CHAR_END, eq(183));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_WARNING));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(sei, markerData);
	}
	
	public void testNameIsRequired() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)" +
				"@javax.jws.WebService public interface Sei {" +
				"public int test(@javax.jws.WebParam(mode=WebParam.Mode.INOUT) int a); \n" + 
				"}");

		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(168));
		markerAttributes.put(IMarker.CHAR_END, eq(213));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(sei, markerData);
	}	
	
	public void testTargetNsValidUri() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(targetNamespace=\"^^^\")int a); \n" + 
				"}");

		final Map<Object, Constraint> markerAttributes = new HashMap<Object, Constraint>();
		markerAttributes.put(IMarker.CHAR_START, eq(126));
		markerAttributes.put(IMarker.CHAR_END, eq(131));
		markerAttributes.put(IMarker.SEVERITY, eq(IMarker.SEVERITY_ERROR));
		final MarkerData markerData =  new MarkerData(seiType.getResource(), WsProblemsReporter.MARKER_ID, markerAttributes);
		validate(sei, markerData);
	}
}

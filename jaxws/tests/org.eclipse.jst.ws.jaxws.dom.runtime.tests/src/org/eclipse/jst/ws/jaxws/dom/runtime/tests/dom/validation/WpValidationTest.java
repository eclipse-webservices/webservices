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
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(115, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(120, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(2, markers[0].getAttribute(IMarker.LINE_NUMBER));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testPartNameIsNCName() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(partName=\"---\")int a); \n" + 
				"}");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(179, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(184, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}	
	
	public void testNameIsUniqe() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"param1\")int a, @javax.jws.WebParam(name=\"param1\")int b); \n" + 
				"}");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(2, markers.length);

		IMarker first;
		IMarker second;
		
		if (markers[0].getAttribute(IMarker.CHAR_START).equals(new Integer(115)) ) {
			first = markers[0];
			second = markers[1];
		} else {
			first = markers[1];
			second = markers[0];
		}
		
		assertEquals(115, first.getAttribute(IMarker.CHAR_START));
		assertEquals(123, first.getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, first.getAttribute(IMarker.SEVERITY));
		assertEquals(156, second.getAttribute(IMarker.CHAR_START));
		assertEquals(164, second.getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, second.getAttribute(IMarker.SEVERITY));			
	}
	
	public void testNameNotRedundant() throws CoreException
	{
		// in RPC style if partName is not present the name is not redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\")int a); \n" + 
				"}");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
	
	public void testNameRedundant() throws CoreException
	{
		// in RPC style if partName is present the name is redundant
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(style=SOAPBinding.Style.RPC)" +
				"@javax.jws.WebService public interface Sei {" +
				"public void test(@javax.jws.WebParam(name=\"myName\", partName=\"myPart\")int a); \n" + 
				"}");
		
		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(175, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(183, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_WARNING, markers[0].getAttribute(IMarker.SEVERITY));
	}
	
	public void testNameIsRequired() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "import javax.jws.soap.SOAPBinding;\n" +
				"@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)" +
				"@javax.jws.WebService public interface Sei {" +
				"public int test(@javax.jws.WebParam(mode=WebParam.Mode.INOUT) int a); \n" + 
				"}");

		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(168, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(213, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));
	}	
	
	public void testTargetNsValidUri() throws CoreException
	{
		setContents(seiType.getCompilationUnit(), "@javax.jws.WebService(name=\"SeiName\") public interface Sei {" +
				"public void test(@javax.jws.WebParam(targetNamespace=\"^^^\")int a); \n" + 
				"}");

		validator.validate(sei);
		IMarker [] markers = seiType.getResource().findMarkers(WsProblemsReporter.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		assertEquals(126, markers[0].getAttribute(IMarker.CHAR_START));
		assertEquals(131, markers[0].getAttribute(IMarker.CHAR_END));
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY));	
	}
}

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationInspectorImpl;

public class AnnotationsTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "org.eclipse.demo";

	private IType endpoint;

	public void setUp() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("Endpoint.src", "Endpoint");
		AnnotationFactory.removeAnnotationsFromJavaElement(endpoint);
	}

	public void testAddComplexAnnotationQNValue() throws JavaModelException, AnnotationGeneratorException
	{
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		IParamValuePair pv = AnnotationFactory.createParamValuePairValue("param", qv);
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();
		paramValues.add(pv);
		IAnnotation<IType> annotation = AnnotationFactory.createAnnotation("javax.jws.WebService", paramValues, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(annotation, endpoint);

		assertTrue(AnnotationFactory.createAnnotationInspector(endpoint).inspectType().size() > 0);
	}

//	public void testAddSingleElementAnnotationArrayValues() throws JavaModelException, AnnotationGeneratorException
//	{
//		IAnnotation annotation = AnnotationFactory.createSingleElementAnnotation("javax.jws.WebService", new String[] { "val1", "val2" });
//		annotation.addToClass(endpoint);
//
//		assertTrue(AnnotationFactory.getAnnotationsFromClass(endpoint).length > 0);
//	}
//
//	public void testAddSimpleAnnotation() throws JavaModelException, AnnotationGeneratorException
//	{
//		IAnnotation annotation = AnnotationFactory.createSimpleAnnotation("javax.jws.WebService");
//		annotation.addToClass(endpoint);
//
//		assertTrue(AnnotationFactory.getAnnotationsFromClass(endpoint).length > 0);
//	}

	/**
	 * Creates complex annotation and adds it to class. The annotation has the form:
	 * 
	 * <pre>
	 * 	 @WebService(serviceName=&quot;serviceName&quot;,
	 * 	 name=&quot;portTypeName&quot;,
	 * 	 targetNamespace=&quot;http://demo.sap.com/&quot;,
	 * 	 wsdlLocation=&quot;http://wsdl/url/&quot;,
	 * 	 portName=&quot;portName&quot;,
	 * 	 endpointInterface=&quot;org.eclipse.demo.ISei&quot;)
	 * </pre>
	 * 
	 * @throws JavaModelException
	 * @throws AnnotationGeneratorException
	 */
	public void testAddComplexClassAnnotation() throws JavaModelException, AnnotationGeneratorException
	{
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();

		addStringProperty(paramValues, "serviceName", "serviceName");
		addStringProperty(paramValues, "name", "portTypeName");
		addStringProperty(paramValues, "targetNamespace", "http://demo.sap.com/");
		addStringProperty(paramValues, "wsdlLocation", "http://wsdl/url/");
		addStringProperty(paramValues, "portName", "portName");
		addStringProperty(paramValues, "endpointInterface", "org.eclipse.demo.ISei");

		IAnnotation<IType> annotation = AnnotationFactory.createAnnotation("javax.jws.WebService", paramValues, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(annotation, endpoint);

		AnnotationInspectorImpl annotationInspector = new AnnotationInspectorImpl(endpoint);
		Collection<IAnnotation<IType>> typeAnnotations = annotationInspector.inspectType();
		 		
		boolean result = false;
		for (IAnnotation<IType> typeAnnotation : typeAnnotations)
		{
			if(typeAnnotation.getAnnotationName().equals("WebService") || typeAnnotation.getAnnotationName().equals("javax.jws.WebService"))
			{
				if(typeAnnotation.getParamValuePairs().size() != 6)
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("serviceName").equals("serviceName"))
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("name").equals("portTypeName"))
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("targetNamespace").equals("http://demo.sap.com/"))
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("wsdlLocation").equals("http://wsdl/url/"))
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("portName").equals("portName"))
				{
					break;
				}
				if(!typeAnnotation.getPropertyValue("endpointInterface").equals("org.eclipse.demo.ISei"))
				{
					break;
				}

				result = true;
			}
		}

		assertTrue("Class is not annotated correctly", result);
		}

	/**
	 * Creates complex annotation and adds it to method declaration. Annotation has the form:
	 * 
	 * <pre>
	 *  \@WebMethod(exclude=false,operationName=&quot;annotatedMethod&quot;,action=&quot;go&quot;)	
	 * </pre>
	 * 
	 * @throws JavaModelException
	 * @throws AnnotationGeneratorException
	 */
	public void testAddComplexMethodAnnotation() throws JavaModelException, AnnotationGeneratorException
	{
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();

		IValue emValue = AnnotationFactory.createBooleanValue(false);
		paramValues.add(AnnotationFactory.createParamValuePairValue("exclude", emValue));

		addStringProperty(paramValues, "operationName", "annotatedMethod");
		addStringProperty(paramValues, "action", "go");

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
				
		IAnnotation<IMethod> annotation = AnnotationFactory.createAnnotation("javax.jws.WebMethod", paramValues, method);
		AnnotationWriter.getInstance().setAppliedElement(annotation, method);

		AnnotationInspectorImpl annotationInspector = new AnnotationInspectorImpl(endpoint);
		Collection<IAnnotation<IMethod>> methodAnnotations = annotationInspector.inspectMethod(method);
		 		
		boolean result = false;
		for (IAnnotation<IMethod> methodAnnotation : methodAnnotations)
		{
			if(methodAnnotation.getAnnotationName().equals("javax.jws.WebMethod") || methodAnnotation.getAnnotationName().equals("WebMethod"))
			{
				if(methodAnnotation.getParamValuePairs().size() != 3)
				{
					break;
				}
				if(!methodAnnotation.getPropertyValue("exclude").equals("false"))
				{
					break;
				}
				if(!methodAnnotation.getPropertyValue("operationName").equals("annotatedMethod"))
				{
					break;
				}
				if(!methodAnnotation.getPropertyValue("action").equals("go"))
				{
					break;
				}
		 				
				result = true;
			}
		}

		assertTrue("Method is not annotated correctly", result);
	}

	/**
	 * Creates complex annotation and adds it to a method parameter. The annotation has the form:
	 * 
	 * <pre>
	 *  \@WebParam(operationName=&quot;annotatedParam&quot;, targetNamespace=&quot;http://demo.sap.com/params/&quot;, partName=&quot;partName&quot;)
	 * </pre>
	 * 
	 * @throws JavaModelException
	 * @throws AnnotationGeneratorException
	 */
	public void testAddComplexParamAnnotation() throws JavaModelException, AnnotationGeneratorException
	{
		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		ITypeParameter typeParameter = method.getTypeParameter("annotatedParam");

		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();

		addStringProperty(paramValues, "annotatedParam", "param");
		addStringProperty(paramValues, "targetNamespace", "http://demo.sap.com/params/");
		addStringProperty(paramValues, "partName", "partName");

		IAnnotation<ITypeParameter> annotation = AnnotationFactory.createAnnotation("javax.jws.WebParam", paramValues, typeParameter);
		AnnotationWriter.getInstance().setAppliedElement(annotation, typeParameter);
	}

	private void addStringProperty(Set<IParamValuePair> paramValues, String property, String value)
	{
		IValue pValue = AnnotationFactory.createStringValue(value);
		IParamValuePair pair = AnnotationFactory.createParamValuePairValue(property, pValue);
		paramValues.add(pair);
	}
}

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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.SeiImplementationValidator;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;

public class SEIImplementationValidatorTest extends ClassLoadingTest
{
	private static final String PCK = "org.eclipse.tests";

	@Override
	public void setUp() throws Exception
	{
		createJavaProject("src", PCK);
		createClasses("mock/SEITests.src");
	}

	public void testUseClassForSEI() throws Exception
	{
		try
		{
			IType seiType = getTestProject().getJavaProject().findType(PCK + ".SEIIsClass");
			new SeiImplementationValidator(seiType);
			assertTrue("IllegalArgumentException not thrown", false);
		} catch (IllegalArgumentException e)
		{
			assertTrue(true);
		}
	}

	/**
	 * Test wrong implementation via not same params and return type.
	 * 
	 * @throws JavaModelException
	 */
	public void testBeanDoesNotImplemetsSeiDifferentMethodParams() throws JavaModelException
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".INotImplemented");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".NotImplementedMethodParamsBean");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.ERROR, status.getSeverity());
	}

	/**
	 * Test wrong implementation different method exceptioins.
	 * 
	 * @throws JavaModelException
	 */
	public void testBeanDoesNotImplemetsSeiDifferentExceptions() throws JavaModelException
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".INotImplemented");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".NotImplementedMethodExceptionsBean");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.ERROR, status.getSeverity());
	}

	/**
	 * Tests the case when the bean implements the Sei by declaring the implementation.
	 * 
	 * @throws Exception
	 */
	public void testBeanImplementsSei() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IImplemented");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".BeanImplementsSei");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Staus not correct", IStatus.OK, status.getSeverity());
	}

	/**
	 * Tests the case when the bean implements methods from Sei withot directly implementing it via java declaration.
	 * 
	 * @throws Exception
	 */
	public void testSEIImplemented() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IImplemented");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".ImplementedBean");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Staus not correct", IStatus.OK, status.getSeverity());
	}

	public void testSEIImplementedExtended() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IImplementedExtended");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".ImplementedExtendedBean");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.OK, status.getSeverity());
	}

	public void testSEIImplementedNonPublicMethods() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IImplemented");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".ImplementedBean1");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.ERROR, status.getSeverity());
	}

	public void testSEIImplementedWrongExceptions() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IExceptionsUsed");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".ExceptionsUsedBean");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.ERROR, status.getSeverity());
	}

	public void testSEIImplementedRightExceptions() throws Exception
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IExceptionsUsed");
		IType beanType = getTestProject().getJavaProject().findType(PCK + ".ExceptionsUsedBean1");

		SeiImplementationValidator validator = new SeiImplementationValidator(seiType);
		IStatus status = validator.validate(beanType);
		assertEquals("Status not correct", IStatus.OK, status.getSeverity());
	}

}

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
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.SeiValidator;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;

/**
 * Class testing {@link SeiValidator}
 * 
 * @author Georgi Vachkov
 */
public class SeiValidatorTest extends ClassLoadingTest
{
	private static final String PCK = "org.eclipse.tests";

	private SeiValidator validator = new SeiValidator();

	@Override
	public void setUp() throws Exception
	{
		createJavaProject("src", PCK);
		createClasses("mock/SEITests.src");
	}

	/**
	 * Tests incorrect interface declarations
	 * 
	 * @throws Exception
	 */
	public void testUseIncorrectSEI() throws JavaModelException
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".SEIIsClass");
		IStatus status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);

		// testUseBinaryInterfaceForSEI
		seiType = getTestProject().getJavaProject().findType("java.util.List");
		status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);

		seiType = getTestProject().getJavaProject().findType(PCK + ".SEIIsClass.InternalSei");
		status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);

		seiType = getTestProject().getJavaProject().findType(PCK + ".SEINotPublic");
		status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);
	}

	/**
	 * Tests incorrect interface methds
	 * 
	 * @throws Exception
	 */
	public void testUseIncorrectSeiMethods() throws JavaModelException
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".SEIIncorrectMethods");
		IStatus status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);

		seiType = getTestProject().getJavaProject().findType(PCK + ".SEIExtendsSeiWithIncorrectMethods");
		status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.ERROR);
		assertTrue("Message is missing", status.getMessage().trim().length() > 0);
	}

	public void testUseCorrectSei() throws JavaModelException
	{
		IType seiType = getTestProject().getJavaProject().findType(PCK + ".IImplemented");
		IStatus status = validator.validate(seiType);

		assertEquals("Severity not correct", status.getSeverity(), IStatus.OK);
		assertTrue("Message is not empty", status.getMessage().trim().length() == 0);
	}

}

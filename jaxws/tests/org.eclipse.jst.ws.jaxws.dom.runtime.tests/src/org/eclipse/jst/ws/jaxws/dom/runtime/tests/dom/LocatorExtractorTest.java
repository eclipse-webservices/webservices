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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom;

import junit.framework.TestCase;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.LocatorExtractor;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;

/**
 * Tests for {@link LocatorExtractor}
 * 
 * @author Georgi Vachkov
 */
public class LocatorExtractorTest  extends TestCase
{
	private TestProject testProject;
	private IType wsType;
	private IWebService ws;
	
	public void setUp() throws Exception
	{
		testProject = new TestProject();
		
		IPackageFragment pack = testProject.createPackage("test.locator.extractor");
		wsType = testProject.createType(pack, "Ws.java", "public class Ws {\n" +
				"	public void test() {}\n " +
				"}");		
		
		IWebServiceProject wsProject = DomFactory.eINSTANCE.createIWebServiceProject();
		wsProject.eSet(DomPackage.Literals.IWEB_SERVICE_PROJECT__NAME, testProject.getProject().getName());
		
		ws = DomFactory.eINSTANCE.createIWebService();
		ws.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, wsType.getFullyQualifiedName());

		wsProject.getWebServices().add(ws);		
	}
	
	public void testFindForWs() throws JavaModelException, BadLocationException
	{
		ILocator locator = LocatorExtractor.getInstance().find(ws);
		
		assertEquals(3, locator.getLineNumber());
		assertEquals(46, locator.getStartPosition());
		assertEquals(2, locator.getLength());
	}
}

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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;


public class SeiSyncronizationTest extends SynchronizationTestFixture
{
	
	public void test_changedSeiNameSynched() throws Exception
	{
		target.startSynchronizing();
		final ICompilationUnit sei1CU = testPrj1.getJavaProject().findType(sei1.getImplementation()).getCompilationUnit();
		testUtil.setContents(sei1CU, "@javax.jws.WebService() public interface Sei1 {}");
		JobUtils.waitForJobs();
		assertEquals("Sei1", sei1.getName()); // check default value
		
		testUtil.setContents(sei1CU, "@javax.jws.WebService(name=\"Sei1NewName\") public interface Sei1 {}");
		JobUtils.waitForJobs();
		assertEquals("Sei1NewName", sei1.getName());
		assertTrue(sei1.getImplementingWebServices().contains(ws1));
		assertSame(sei1, domUtil.findSeiByImplName(wsPrj1, sei1.getImplementation()));
		
	}
	
	public void test_changedSeiTargetNamespaceSynched() throws Exception
	{
		target.startSynchronizing();
		assertEquals(JaxWsUtils.composeJaxWsTargetNamespaceByPackage(prj1PckName), sei1.getTargetNamespace()); // check default value
		
		final ICompilationUnit sei1CU = testPrj1.getJavaProject().findType(sei1.getImplementation()).getCompilationUnit();
		testUtil.setContents(sei1CU, "@javax.jws.WebService(name=\"Sei1NewName\", targetNamespace=\"http://com.sap/test\") public interface Sei1 {}");
		JobUtils.waitForJobs();
		assertEquals("Sei1NewName", sei1.getName());
		assertTrue(sei1.getImplementingWebServices().contains(ws1));
		assertSame(sei1, domUtil.findSeiByImplName(wsPrj1, sei1.getImplementation()));
		assertEquals("SEI targetNamespace not synched", "http://com.sap/test", sei1.getTargetNamespace());		
	}
	
	public void test_SoapBindingStyleChanged() throws Exception
	{
		target.startSynchronizing();
		assertEquals(SOAPBindingStyle.DOCUMENT, sei1.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.LITERAL, sei1.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.WRAPPED, sei1.getSoapBindingParameterStyle());

		final ICompilationUnit sei1CU = testPrj1.getJavaProject().findType(sei1.getImplementation()).getCompilationUnit();
		testUtil.setContents(sei1CU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)" + 
				"@javax.jws.WebService() public interface Sei1 {}");
		JobUtils.waitForJobs();
		assertEquals(SOAPBindingStyle.RPC, sei1.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.ENCODED, sei1.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.BARE, sei1.getSoapBindingParameterStyle());		
	}
//	
//	public void test_LocationInterfaceAdapted() throws Exception
//	{
//		final ICompilationUnit sei1CU = testPrj1.getJavaProject().findType(sei1.getImplementation()).getCompilationUnit();
//		testUtil.setContents(sei1CU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)" + 
//				"@javax.jws.WebService() public interface Sei1 {}");
//		
//		ILocatorAdapter adapter = (ILocatorAdapter)findAdapter(sei1, ILocatorAdapter.class);
//		assertNotNull(adapter);
//		IAnnotationLocator locator = adapter.getAnnotationLocator(WSAnnotationFeatures.WS_ANNOTATION);
//		printLocator("WS", locator.getLocation());
//		locator = adapter.getAnnotationLocator(SBAnnotationFeatures.SB_ANNOTATION);
//		printLocator("SB", locator.getLocation());
//		printLocator("use", locator.getAttributeNameLocation("use"));
//		printLocator("useVal", locator.getAttributeValueLocation("use"));
//	}
//	
//	private void printLocator(String name, ILocator locator)
//	{
//		System.out.println(name + "------");
//		System.out.println(locator.getStartPosition());
//		System.out.println(locator.getLength());		
//	}	
}

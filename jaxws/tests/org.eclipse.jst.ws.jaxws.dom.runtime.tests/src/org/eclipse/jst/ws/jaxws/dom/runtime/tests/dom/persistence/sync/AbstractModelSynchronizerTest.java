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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.AbstractModelSynchronizer;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

/**
 * Tests for {@link AbstractModelSynchronizer} class, methods not covered in this test
 * are covered implicitly by {@link ModelSynchronizationTests}.
 * 
 * @author Georgi Vachkov
 */
public class AbstractModelSynchronizerTest extends MockObjectTestCase
{
	private AbstractModelSynchronizerTester modelSync;
	private JaxWsWorkspaceResource resource;
	
	@Override
	public void setUp()
	{
		Mock<IJavaModel> javaModelMock = mock(IJavaModel.class);
		resource = new JaxWsWorkspaceResource(javaModelMock.proxy());
		
		modelSync = new AbstractModelSynchronizerTester(resource, resource.new ServiceModelData()){};
	}
	
	public void testProcessCompilationUnitEditEnabledDespiteNPE() throws JavaModelException 
	{
		assertTrue(resource.isSaveEnabled());
		try {
			modelSync.processCompilationUnit(null, null);
		} catch (NullPointerException _) {
		}
		assertTrue(resource.isSaveEnabled());
	}		
	
	public void testProcessCompilationUnitEditEnabledNormalExecution() throws JavaModelException 
	{
		assertTrue(resource.isSaveEnabled());
		Mock<ICompilationUnit> cuMock = mock(ICompilationUnit.class);
		cuMock.expects(once()).method("findPrimaryType").will(returnValue(null));			
		modelSync.processCompilationUnit(null, cuMock.proxy());
		assertTrue(resource.isSaveEnabled());
	}			
	
	public class AbstractModelSynchronizerTester extends AbstractModelSynchronizer
	{
		public AbstractModelSynchronizerTester(JaxWsWorkspaceResource resource, ServiceModelData serviceData) {
			super(resource, serviceData);
		}
		
		protected void processCompilationUnit(final IWebServiceProject wsProject, final ICompilationUnit cu) throws JavaModelException
		{
			super.processCompilationUnit(wsProject, cu);
		}		
	}
}

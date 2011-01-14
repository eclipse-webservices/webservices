/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomFactoryImpl;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.SeiSynchronizer;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

public class SeiSynchronizerTest extends MockObjectTestCase {
	public void testSEISynchronizeForImplementationNameValueNull() throws Exception {
		SeiSynchronizer seiSynchronizer = new SeiSynchronizer(null) {
			@Override
			public DomFactory domFactory() {
				DomFactoryImpl.init();
				return new DomFactoryImpl();
			}
		};
		final Mock<IWebServiceProject> webServiceProjectMock = mock(IWebServiceProject.class);
		final EList<IServiceEndpointInterface> testSEIList = new BasicEList<IServiceEndpointInterface>();
		webServiceProjectMock.stubs().method("getServiceEndpointInterfaces").withNoArguments().will(returnValue(testSEIList));
		Mock<IAnnotation<IType>> annotationMock = mock(IAnnotation.class);
		Mock<IType> typeMock = mock(IType.class);
		typeMock.stubs().method("getFullyQualifiedName").withNoArguments().will(returnValue(null));
		annotationMock.stubs().method("getAppliedElement").withNoArguments().will(returnValue(typeMock.proxy()));
		seiSynchronizer.synchronizeInterface(webServiceProjectMock.proxy(), annotationMock.proxy(), null);
		assertEquals("No SEI expected to be created when implementation name is null !", 0, testSEIList.size());
	}
}

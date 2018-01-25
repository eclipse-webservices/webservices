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

import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.WsSynchronizer;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

public class WsSynchronizerTest extends MockObjectTestCase {
	public void testWSSynchronizeForImplementationNameWithValueNull() throws Exception {
		WsSynchronizer wsSynchronizerToTest = new WsSynchronizer(null);
		Mock<IAnnotation<IType>> annotationMock = mock(IAnnotation.class);
		Mock<IType> typeMock = mock(IType.class);
		typeMock.stubs().method("getFullyQualifiedName").withNoArguments().will(returnValue(null));
		annotationMock.stubs().method("getAppliedElement").withNoArguments().will(returnValue(typeMock.proxy()));
		assertNull("WS instance gets created when the implementation name is null !", wsSynchronizerToTest.synchronizeWebService(null, annotationMock.proxy(), null));
	}
}

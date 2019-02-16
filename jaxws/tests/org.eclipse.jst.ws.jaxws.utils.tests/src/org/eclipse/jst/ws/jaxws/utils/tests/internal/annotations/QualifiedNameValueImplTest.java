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
package org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class QualifiedNameValueImplTest extends TestCase
{

	public void testHashCode()
	{
		IValue qv1 = AnnotationFactory.createQualifiedNameValue("full.qualified.Name1");
		IValue qv2 = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		assertFalse(qv1.hashCode() == qv2.hashCode());
	}

	public void testEqualsObject()
	{
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");
		IValue qv1 = AnnotationFactory.createQualifiedNameValue("full.qualified.Name1");
		IValue qv2 = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		assertFalse(qv.equals(qv1));
		assertTrue(qv.equals(qv2));
		assertTrue(qv.equals(qv));
		assertFalse(qv.equals(null));
		assertFalse(qv.equals(123));
	}
}

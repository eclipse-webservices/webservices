/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.tests.internal;

import org.eclipse.core.runtime.Platform;
import org.junit.Assert;

import junit.framework.TestCase;

public class ExistenceTest extends TestCase {

	public void testPluginExistence() {
		// Static plugin id is not public in org.eclipse.wst.wsi
		Assert.assertNotNull(Platform.getBundle("org.eclipse.wst.wsi"));
	}
}

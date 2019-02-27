/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.unittest;

import junit.framework.TestCase;

import org.eclipse.wst.ws.internal.common.BundleUtils;
import org.osgi.framework.Version;

public class TestBundleUtils extends TestCase {

	public void testGetVersion() {
		Version version = BundleUtils.getVersion("org.eclipse.wst.ws");
		assertNotNull("version should never be null", version);
	}

}

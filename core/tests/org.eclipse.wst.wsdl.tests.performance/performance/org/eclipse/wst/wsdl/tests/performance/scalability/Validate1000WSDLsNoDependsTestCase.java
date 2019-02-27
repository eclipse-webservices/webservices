/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;

public class Validate1000WSDLsNoDependsTestCase extends RunWSDLValidatorTestCase
{
	protected void setUp() throws Exception
	{
		WSPlugin.getDefault().getWSIAPContext().updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
		WSPlugin.getDefault().getWSISSBPContext().updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
	    IProject project = createProject("sp");
	    String bundleId = getBundleId();
	    for(int i = 1; i < 1000; i++)
	    {
	      copyFile(bundleId, "data/1000WSDLsNoDepends/sample" + i +".wsdl", project);
	    }
	    super.setUp();
	}

	protected String getFilePath()
	{
	    return "data/1000WSDLsNoDepends/sample0.wsdl";
	}
	
	public void testValidate1000WSDLsNoDepends()
	{
	    try
	    {
	      super.execute();
	    }
	    catch (Exception t)
	    {
	      fail(t.getMessage());
	    }
	}

}

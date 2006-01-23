/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.tests.performance.internal.scalability.RunValidatorTestCase;

public class Validate10WSDLsXSDDependsTestCase extends RunValidatorTestCase
{
	protected String getValidatorId()
	{
	    return "org.eclipse.wst.wsdl.ui.internal.validation.Validator";
	}

	protected String getBundleId()
	{
	    return "org.eclipse.wst.wsdl.tests.performance";
	}
	
	protected void setUp() throws Exception
	{
	    IProject project = createProject("sp");
	    String bundleId = getBundleId();
	    for(int i = 1; i < 10; i++)
	    {
	      copyFile(bundleId, "data/10WSDLsXSDDepends/sample" + i +".wsdl", project);
	    }
	    super.setUp();
	}

	protected String getFilePath()
	{
	    return "data/10WSDLsXSDDepends/sample0.xsd";
	}
	
	public void testValidate10WSDLsXSDDepends()
	{
	    try
	    {
	      super.execute();
	    }
	    catch (Throwable t)
	    {
	      fail(t.getMessage());
	    }
	}

}

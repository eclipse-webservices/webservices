/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060517   140832 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.core.locator;

import java.util.Vector;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

public class ServerConfigWSDDVisitor implements IResourceVisitor
{
	private static final String AXIS_SERVER_CONFIG_FILE_NAME = "Server-config.wsdd";
	Vector wsddFiles = new Vector();
	
	public boolean visit(IResource resource)
    {
		if (resource.getType() == IResource.FILE)
		{     
			if (resource.getName().equalsIgnoreCase(AXIS_SERVER_CONFIG_FILE_NAME))
			{
				wsddFiles.add((IFile)resource);
			}
        }
        return true;
      }
	public Vector getWsddFiles()
	{
		return wsddFiles; 
	}
}

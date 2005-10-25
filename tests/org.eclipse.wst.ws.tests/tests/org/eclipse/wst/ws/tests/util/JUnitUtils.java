/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.util;

import java.util.Enumeration;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.internal.provisional.env.core.context.TransientResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.tests.plugin.TestsPlugin;


public class JUnitUtils {
	
	// Begin: General Eclipse Utilities
	public static void syncBuildProject(IProject project,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		project.build(IncrementalProjectBuilder.FULL_BUILD,null);
		WaitForAutoBuildCommand cmd = new WaitForAutoBuildCommand();
    cmd.setEnvironment( env );
		cmd.execute( monitor, null );
	}
	
	private static void copyTestFiles(String pathString,int rootSegmentLength,IFolder destFolder,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		Enumeration e = TestsPlugin.getDefault().getBundle().getEntryPaths(pathString);
		while (e.hasMoreElements())
		{
			String filePath = (String)e.nextElement();
			if (filePath.endsWith("/"))
				copyTestFiles(filePath,rootSegmentLength,destFolder,env, monitor );
			else
			{ 
				IPath fileIPath = new Path(filePath);
				FileResourceUtils.copyFile(new TransientResourceContext(),
						                   TestsPlugin.getDefault(),
										   fileIPath.removeLastSegments(fileIPath.segmentCount()-rootSegmentLength), // /data/<subdir>
										   (new Path(filePath)).removeFirstSegments(rootSegmentLength), // files after /data/<subdir>
										   destFolder.getFullPath(),
										   monitor,
										   env.getStatusHandler());
			}
		}
	}
	
	public static void copyTestData(String dataSubdirectory,IFolder destFolder,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		String pathString = "/data/"+dataSubdirectory;
		copyTestFiles(pathString,new Path(pathString).segmentCount(),destFolder,env, monitor );
		
	}
	
}
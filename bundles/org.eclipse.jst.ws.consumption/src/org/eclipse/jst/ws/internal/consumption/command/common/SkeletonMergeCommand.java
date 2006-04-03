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
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.MergeUtils;

public class SkeletonMergeCommand extends AbstractDataModelOperation
{
  private String[] urls_;

  public SkeletonMergeCommand()
  {
  }

  public void setUrls(String[] urls)
  {
    this.urls_ = urls;
  }

  // Merge the content of the skeleton files (represented by urls_ with the previous version stored.	  
  // The Web service extensions triggers the storing of the content of the old skeleton file by 
  // calling WebServiceInfo.setImplURLs() before the new skeleton is generated in the extension's 
  // develop() method.
  
  public IStatus execute(IProgressMonitor monitor, IAdaptable info)
  {

	  IStatus status = Status.OK_STATUS;
	  IEnvironment environment = getEnvironment();
	  ResourceContext context = WebServicePlugin.getInstance().getResourceContext();
	  if (context.isSkeletonMergeEnabled()) {
		  // merge the skeleton implementation file with the model stored earlier

		  String mergedContent;
		  String filename;
		  InputStream inStream = null;
		  if (urls_ != null) {
			  for (int i = 0; i < urls_.length; i++) {
				  filename = MergeUtils.getFileFromURL(urls_[i]);
				  if (filename != null) {
					  mergedContent = MergeUtils.mergeFile(filename);
					  if (mergedContent != null) {
						  // write the merged content back into the file, respecting file overwrite preference
						  IPath targetPath = new Path(filename);
						  inStream = new ByteArrayInputStream(mergedContent.getBytes());

						  if (inStream != null) {
							  try {
								  FileResourceUtils.createFileAtLocation(context, targetPath.makeAbsolute(), inStream,
										  monitor, environment.getStatusHandler());
							  } catch (CoreException e) {
								  status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_FILE_MERGE, urls_), e);
								  environment.getStatusHandler().reportError( status );
							  }
						  }
					  }
				  }
			  }		
		  }			  
	  }

	  return status;
  }
}
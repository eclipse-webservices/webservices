/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.wse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;



public class LaunchWebServicesExplorerCommand extends SimpleCommand
{
  private boolean forceLaunchOutsideIDE_;
  private LaunchOption[] launchOptions;
  
  public LaunchWebServicesExplorerCommand()
  {
    super("LaunchWebServicesExplorerCommand", "LaunchWebServicesExplorerCommand");
  }


  private final WSExplorerType getWSExplorerType()
  {
    String wsexplorerTypeName = WebServicePlugin.getInstance().getScenarioContext().getNonJavaTestService();
    WSExplorerTypesRegistry wsexplorerReg = WSExplorerTypesRegistry.getInstance();
    WSExplorerType wsexplorerType = wsexplorerReg.getWSExplorerTypeByName(wsexplorerTypeName);
    if (wsexplorerType == null)
      wsexplorerType = wsexplorerReg.getWSExplorerTypeByRank(0);
    return wsexplorerType;
  }

  public void writeCategoryInfo(String inquiryURL, String categoriesDirectory)
  {
    try
    {
      Properties p = new Properties();
      p.setProperty(WSExplorerType.CATEGORIES_DIRECTORY, categoriesDirectory);
      StringBuffer propertiesFileName = new StringBuffer();
      propertiesFileName.append(getWSExplorerType().getMetadataDirectory());
      File metadataDirectoryFile = new File(propertiesFileName.toString());
      if (!metadataDirectoryFile.exists())
        metadataDirectoryFile.mkdirs();
      propertiesFileName.append(URLEncoder.encode(inquiryURL)).append(".properties");
      FileOutputStream fout = new FileOutputStream(propertiesFileName.toString());
      p.store(fout, null);
      fout.close();
    }
    catch (IOException e)
    {
    }
  }
  
  public IStatus execute()
  {
    return getWSExplorerType().launch(null, null, launchOptions, forceLaunchOutsideIDE_);
  }
  
  public Status execute(Environment env)
  {
    return EnvironmentUtils.convertIStatusToStatus(getWSExplorerType().launch(null, null, launchOptions, forceLaunchOutsideIDE_));
  }

  /**
   * @param forceLaunchOutsideIDE The forceLaunchOutsideIDE to set.
   */
  public void setForceLaunchOutsideIDE(boolean forceLaunchOutsideIDE)
  {
    this.forceLaunchOutsideIDE_ = forceLaunchOutsideIDE;
  }

  /**
   * @param launchOptions The launchOptions to set.
   */
  public void setLaunchOptions(LaunchOption[] launchOptions)
  {
    this.launchOptions = launchOptions;
  }

}

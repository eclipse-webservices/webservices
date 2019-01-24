/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import javax.servlet.ServletContext;

public abstract class Perspective extends MessageProvider
{
  protected Controller controller_;
  protected MessageQueue messageQueue_;

  public Perspective(String messageBundleFileName,Controller controller)
  {
    super(messageBundleFileName);
    controller_ = controller;
    messageQueue_ = new MessageQueue();
  }

  public MessageQueue getMessageQueue()
  {
    return messageQueue_;
  }

  public Controller getController()
  {
    return controller_;
  }

  public abstract String getPanesFile();
  public abstract String getFramesetsFile();
  public abstract String getProcessFramesetsForm();

  public abstract String getTreeContentVar();
  public abstract String getTreeContentPage();
  public abstract String getPropertiesContainerVar();
  public abstract String getPropertiesContainerPage();
  public abstract String getStatusContentVar();
  public abstract String getStatusContentPage();

  public abstract String getSwitchPerspectiveFormActionLink(int targetPerspectiveId,boolean forHistory);
  public abstract String getPerspectiveContentPage();
  public abstract int getPerspectiveId();

  // This should only be called after the perspective has been successfully instantiated.
  public abstract void initPerspective(ServletContext application);
  
  // Node manager for the navigator/tree content page
  public abstract NodeManager getNodeManager();
}

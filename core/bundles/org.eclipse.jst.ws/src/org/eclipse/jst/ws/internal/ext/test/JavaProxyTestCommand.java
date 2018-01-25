/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext.test;

import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;

public interface JavaProxyTestCommand extends WebServiceTestFinishCommand
{

 /**
  * This holds an info class that comes from user input or some calcilations that needs to be
  * used for the finish
  */
  
  public void setJspFolder(String jspFolder);
  
  public void setRunClientTest(boolean runClientTest);
  
  public void setSampleProject(String sampleProject);
  
  public void setProxyBean(String proxyBean);
  
  public void setSetEndpointMethod(String setEndpointMethod);
  
  public void setClientProject(String clientProject);
  
  public void setMethods(BooleanSelection[] methods);
  
  
}

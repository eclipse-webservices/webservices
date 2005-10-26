/**
* <copyright>
*
* Licensed Material - Property of IBM
* (C) Copyright IBM Corp. 2000, 2002 - All Rights Reserved.
* US Government Users Restricted Rights - Use, duplication or disclosure
* restricted by GSA ADP Schedule Contract with IBM Corp.
*
* </copyright>
*/

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

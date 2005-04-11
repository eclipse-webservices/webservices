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
package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.context.ResourceContext;


public class ServerWizardWidgetOutputCommand extends ClientWizardWidgetOutputCommand
{    
  private TypeRuntimeServer typeRuntimeServer_;
  private boolean           startService_;
  private boolean           testService_;
  private boolean           publishService_;
  private boolean           generateProxy_;
	private ResourceContext   resourceContext_;
    
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  { 
    return typeRuntimeServer_;
  }

  public boolean getStartService()
  {
    return startService_;  
  }

  public boolean getTestService()
  {
    return testService_;
  }
  
  public boolean getPublishService()
  {
    return publishService_;
  }

  public boolean getGenerateProxy()
  {
    return generateProxy_;  
  }
	
	public ResourceContext getResourceContext()
	{
		return resourceContext_;
	}
  
  /**
   * @param generateProxy_ The generateProxy_ to set.
   */
  public void setGenerateProxy(boolean generateProxy_)
  {
    this.generateProxy_ = generateProxy_;
  }
  /**
   * @param publishService_ The publishService_ to set.
   */
  public void setPublishService(boolean publishService_)
  {
    this.publishService_ = publishService_;
  }
  /**
   * @param startService_ The startService_ to set.
   */
  public void setStartService(boolean startService_)
  {
    this.startService_ = startService_;
  }
  /**
   * @param testService_ The testService_ to set.
   */
  public void setTestService(boolean testService_)
  {
    this.testService_ = testService_;
  }
  /**
   * @param typeRuntimeServer_ The typeRuntimeServer_ to set.
   */
  public void setServiceTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer_)
  {
    this.typeRuntimeServer_ = typeRuntimeServer_;
  }
	
	public void setResourceContext(ResourceContext rc)
	{
		resourceContext_ = rc;
	}
}
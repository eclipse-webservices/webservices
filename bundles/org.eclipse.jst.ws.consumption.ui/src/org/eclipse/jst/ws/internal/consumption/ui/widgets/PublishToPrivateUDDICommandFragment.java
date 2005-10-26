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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.consumption.ui.wizard.PrivateUDDIRegistryTypeRegistry;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.AbstractCommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;


public class PublishToPrivateUDDICommandFragment extends AbstractCommandFragment
{
  private DataMappingRegistry dataMappingRegistry;
  private CanFinishRegistry canFinishRegistry;
  private WidgetRegistry widgetRegistry;
  private boolean publishToPrivateUDDI;
  private CommandWidgetBinding privateUDDIBinding;
  
  public PublishToPrivateUDDICommandFragment()
  {
    super(null, "");
    privateUDDIBinding = PrivateUDDIRegistryTypeRegistry.getInstance().getPrivateUDDIRegistryType().getBinding();
  }

  /** 
   * Gets the first child fragment for this fragment.
   *
   * @return returns the first child fragment for this fragment.  Returns
   * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {
    if (privateUDDIBinding != null && publishToPrivateUDDI && !PrivateUDDIRegistryTypeRegistry.getInstance().getPrivateUDDIRegistryType().isPrivateUDDIRegistryInstalled())
    {
      privateUDDIBinding.registerCanFinish(canFinishRegistry);
      privateUDDIBinding.registerDataMappings(dataMappingRegistry);
      privateUDDIBinding.registerWidgetMappings(widgetRegistry);
      return privateUDDIBinding.create().create();
    }
    else
      return null;
  }

  /**
    * Gets the next child fragment for this fragment.
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/
  public CommandFragment getNextSubFragment(CommandFragment fragment)
  {
  	return null;
  }
  
  /*
   * This method is called to retrieve the data mappings for this command fragment.
   */
  public void registerDataMappings(DataMappingRegistry registry)
  {
  	dataMappingRegistry = registry;
  }

  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
    this.canFinishRegistry = canFinishRegistry;
  }

  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    this.widgetRegistry = widgetRegistry;
  }

  /**
    * All wizard fragments need to be cloneable.
  **/
  public Object clone()
  {
    PublishToPrivateUDDICommandFragment fragment = new PublishToPrivateUDDICommandFragment();
  	fragment.registerDataMappings(dataMappingRegistry);
  	fragment.registerCanFinish(canFinishRegistry);
  	fragment.registerWidgetMappings(widgetRegistry);
  	fragment.setPublishToPrivateUDDI(publishToPrivateUDDI);
  	return fragment;
  }

  public void setPublishToPrivateUDDI(boolean publish)
  {
    publishToPrivateUDDI = publish;
  }
}
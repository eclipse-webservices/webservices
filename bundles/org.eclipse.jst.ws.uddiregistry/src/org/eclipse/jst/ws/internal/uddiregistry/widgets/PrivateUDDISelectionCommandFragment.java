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
package org.eclipse.jst.ws.internal.uddiregistry.widgets;

import org.eclipse.jst.ws.internal.uddiregistry.wizard.PrivateUDDIRegistryType;
import org.eclipse.wst.command.env.core.CommandFactory;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.AbstractCommandFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.env.ui.widgets.WidgetRegistry;


public class PrivateUDDISelectionCommandFragment extends AbstractCommandFragment
{
  private PrivateUDDIRegistryType registryType;
  private DataMappingRegistry dataMappingRegistry;
  private CanFinishRegistry canFinishRegistry;
  private WidgetRegistry widgetRegistry;
  
  public PrivateUDDISelectionCommandFragment()
  {
  	super((CommandFactory)null, "");
  }

  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {
  	if (registryType != null)
  	{
      registryType.registerCanFinish(canFinishRegistry);
      registryType.registerDataMappings(dataMappingRegistry);
      registryType.registerWidgetMappings(widgetRegistry);
      return registryType.create().create();
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
  	PrivateUDDISelectionCommandFragment fragment = new PrivateUDDISelectionCommandFragment();
  	fragment.registerDataMappings(dataMappingRegistry);
  	fragment.registerCanFinish(canFinishRegistry);
  	fragment.registerWidgetMappings(widgetRegistry);
  	fragment.setPrivateUDDIRegistryType(registryType);
  	return fragment;
  }
  
  public void setPrivateUDDIRegistryType(PrivateUDDIRegistryType registryType)
  {
  	this.registryType = registryType;
  }
}
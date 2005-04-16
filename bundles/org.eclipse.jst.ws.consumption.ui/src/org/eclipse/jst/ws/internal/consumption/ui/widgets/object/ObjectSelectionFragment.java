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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceType;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;


public class ObjectSelectionFragment extends SequenceFragment implements Condition
{
  private TypeRuntimeServer typeRuntimeServer;
  private boolean hasObjectSelectionWidget;

  public ObjectSelectionFragment()
  {
    super();
    BooleanFragment boolFragment = new BooleanFragment(new SimpleFragment("ObjectSelectionWidget"), new SimpleFragment(""), this);
    add(boolFragment);
    add(new SimpleFragment(new ObjectSelectionOutputCommand(), ""));
    hasObjectSelectionWidget = false;
  }

  /**
   *  This method is called retrieve the data mappings for this command fragment.
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ObjectSelectionWidget.class );
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ObjectSelectionOutputCommand.class, "ObjectSelection", null);
    dataRegistry.addMapping(ObjectSelectionFragment.class, "TypeRuntimeServer", ObjectSelectionWidget.class);
    dataRegistry.addMapping(ObjectSelectionFragment.class, "TypeRuntimeServer", ObjectSelectionOutputCommand.class);
    
    dataRegistry.addMapping(ObjectSelectionWidget.class, "ObjectSelection", ObjectSelectionOutputCommand.class);
    dataRegistry.addMapping(ObjectSelectionWidget.class, "Project", ObjectSelectionOutputCommand.class);
  }

  public boolean evaluate()
  {
    return hasObjectSelectionWidget;
  }

  public TypeRuntimeServer getTypeRuntimeServer()
  {
    return typeRuntimeServer;
  }

  /**
   * @param typeRuntimeServer The typeRuntimeServer to set.
   */
  public void setTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer)
  {
    this.typeRuntimeServer = typeRuntimeServer;
    if (typeRuntimeServer != null)
    {
	  // rskreg
      //IWebServiceType wst = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceTypeById(typeRuntimeServer.getTypeId());
	  IWebServiceType wst = WebServiceRuntimeExtensionUtils.getWebServiceTypeById(typeRuntimeServer.getTypeId());
	  // rskreg
      if (wst != null)
      {
        String objectSelectionWidgetId = wst.getObjectSelectionWidget();
        hasObjectSelectionWidget = objectSelectionWidgetId != null && objectSelectionWidgetId.length() > 0;
      }
    }
  }
}
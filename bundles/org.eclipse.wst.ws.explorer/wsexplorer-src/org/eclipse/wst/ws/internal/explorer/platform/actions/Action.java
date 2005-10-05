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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.CurrentNodeSelectionTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;

// Abstract action class used for both Link and Form actions.
public abstract class Action
{
  protected Controller controller_;
  protected Hashtable propertyTable_;
  protected Vector removedProperties_;

  public Action()
  {
    this(null);
  }

  public Action(Controller controller)
  {
    controller_ = controller;
    propertyTable_ = new Hashtable();
    removedProperties_ = new Vector();
  }

  public final Controller getController()
  {
    return controller_;
  }

  /**
  * Get the property table.
  * @return Hashtable A hashtable containing the properties for this action.
  */
  public final Hashtable getPropertyTable()
  {
    return propertyTable_;
  }
  
  /**
   * Set the property table.
   * @return void
   */
  public void setPropertyTable(Hashtable propertyTable)
  {
   propertyTable_ = propertyTable;
  }
  
  /**
   * Add a property to the property table.
   * @param void
   */
  public final void addProperty(Object key, Object value)
  {
    propertyTable_.put(key, value);
  }

  /**
  * Remove a property from the property table. Track the deletions.
  * @param Object The key of the property being removed.
  */
  public final void removeProperty(Object key)
  {
    propertyTable_.remove(key);
    removedProperties_.addElement(key);
  }
  
  public final String[] getPropertyAsStringArray(Object key)
  {
    Object object = propertyTable_.get(key);
    if (object instanceof String[])
      return (String[])object;
    else if (object != null)
      return new String[] {object.toString()};
    else
      return new String[0];
  }

  public final boolean execute()
  {
    return execute(true);
  }

  public final boolean execute(boolean useActionEngine)
  {
    if (useActionEngine)
      return controller_.getActionEngine().executeAction(this);
    else
      return run();
  }

  // Abstract method for running this action.
  public abstract boolean run();
  
  /**
  * Return the transformers used to normalize the input/output parameters for this action
  * @return ITransformer[]
  */
  public ITransformer[] getTransformers()
  {
    return new ITransformer[] {new CurrentNodeSelectionTransformer(controller_)};
  }
  
  protected void handleUnexpectedException(Perspective perspective,MessageQueue messageQueue,String exceptionName,Throwable t)
  {
    messageQueue.addMessage(perspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
    messageQueue.addMessage(exceptionName);
    messageQueue.addMessage(t.getMessage());
  }  
}

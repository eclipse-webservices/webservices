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

package org.eclipse.wst.ws.internal.datamodel;

/**
* This is where we store all the state data that various UIs
* (such as the NewWSDLCreationWizard) capture from the user
* and then need to process.
*/
public interface ElementListener
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Called when a property is added to the model.
  * @param event The event containing the model, property and value.
  */
  public void propertyAdded ( PropertyAddEvent event );

  /**
  * Called when a property in the model changes.
  * @param event The event containing the model, property, old value
  * and new value.
  */
  public void propertyChanged ( PropertyChangeEvent event );

  /**
  * Called when a property in the model is removed.
  * @param event The event containing the model, property and old value.
  */
  public void propertyRemoved ( PropertyRemoveEvent event );

  /**
  * Called when a property is added to the model.
  * @param event The event containing the model, property and value.
  */
  public void relAdded ( RelAddEvent event );

  /**
  * Called when a property is added to the model.
  * @param event The event containing the model, property and value.
  */
  public void relRemoved ( RelRemoveEvent event );
}


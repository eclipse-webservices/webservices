/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.graph.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.DragEditPartsTracker;
                                   
public class WSDLGraphNodeDragTracker extends DragEditPartsTracker 
{                                     
  protected EditPart editPart; 
           
  public WSDLGraphNodeDragTracker(EditPart editPart)
  {
    super(editPart);
    this.editPart = editPart;
  } 
                                              
  protected Command getCommand() 
  { 
	  Request request = getTargetRequest();
    return editPart.getCommand(request); 
  }
} 

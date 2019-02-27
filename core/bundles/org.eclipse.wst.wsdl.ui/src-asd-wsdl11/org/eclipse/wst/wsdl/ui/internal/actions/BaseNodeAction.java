/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.actions; 
                      
import org.eclipse.jface.action.Action;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Node;

public abstract class BaseNodeAction extends Action
{ 
  public abstract Node getNode();
  public abstract String getUndoDescription();

  public void beginRecording()
  {    
    Node node = getNode();  
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().beginRecording(this, getUndoDescription());  
    }
  }

  public void endRecording()
  {                 
    Node node = getNode(); 
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().endRecording(this);  
    }
  }
}
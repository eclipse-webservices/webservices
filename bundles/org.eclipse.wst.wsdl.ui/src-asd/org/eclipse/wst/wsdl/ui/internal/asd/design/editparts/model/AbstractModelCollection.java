/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public abstract class AbstractModelCollection implements IASDObject, ITreeElement
{
  IASDObject model;
  String kind;
  
  public AbstractModelCollection(IASDObject model, String kind)
  {
    this.model = model;
    this.kind = kind;
  }

  public Object getModel()
  {
    return model;
  }

  public void setModel(IASDObject model)
  {
    this.model = model;
  }

  public String getKind()
  {
    return kind;
  }

  public void setKind(String kind)
  {
    this.kind = kind;
  }

  public Image getImage() {
	  return null;
  }

  public Image getSecondaryImage() {
	  return null;
  }
  
  public ITreeElement getParent() {
	  return null;
  }
  
  public boolean isReadOnly() {
	  return false;
  }
  
  public void registerListener(IASDObjectListener listener)
  {
    model.registerListener(listener);
  }
  
  public void unregisterListener(IASDObjectListener listener)
  {
    model.unregisterListener(listener);
  }

  public Command getDeleteCommand() {
    return null;
  }
  
  public List getDiagnosticMessages() {
	return new ArrayList();
  }
}

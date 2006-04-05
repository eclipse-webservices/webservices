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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;


public abstract class AbstractObjectSelectionWidget extends SimpleWidgetDataContributor implements IObjectSelectionWidget
{
  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    // subclass should override this method
  }

  public IStructuredSelection getObjectSelection()
  {
    // subclass should override this method
    return new StructuredSelection();
  }

  public IStatus validateSelection(IStructuredSelection objectSelection)
  {
    //  subclass should override this method
    return Status.OK_STATUS;
  }

  public IProject getProject()
  {
    //  subclass should override this method
    return null;
  }
  
   public  String getObjectSelectionDisplayableString()
   {
	    //  subclass should override this method
	  return "";
   }
  }

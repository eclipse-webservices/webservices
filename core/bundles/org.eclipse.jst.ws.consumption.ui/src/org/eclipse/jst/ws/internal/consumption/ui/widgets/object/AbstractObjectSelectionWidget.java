/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Point;
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
   
   public Point getWidgetSize()
   {
      // subclasses should override with an appropriate default dialog size for this widget	  
 	  return null;  
   }
   
   public boolean validate(String s) {
    // subclass should override this method
	return true;
    }  
  }

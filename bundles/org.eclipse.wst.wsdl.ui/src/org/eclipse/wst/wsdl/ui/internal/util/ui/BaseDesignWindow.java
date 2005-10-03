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
package org.eclipse.wst.wsdl.ui.internal.util.ui;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.wst.common.ui.internal.UIPlugin;

abstract public class BaseDesignWindow extends Viewer
{
  protected ScrolledComposite mainUIComponent;
  protected Composite controlsContainer;

  protected boolean pageComplete = true;
  protected Object input;

  private IStatusLineManager statusLine;

  public BaseDesignWindow(IStatusLineManager statusLine)
  {
    this.statusLine = statusLine;
  }

  protected void setStatusLine(IStatusLineManager statusLine)
  {
    this.statusLine = statusLine;
  }

  protected IStatusLineManager getStatusLine()
  {
    if (statusLine == null)
    {
      //statusLine = WorkbenchUtility.getStatusLineManager();

      IWorkbench workbench = UIPlugin.getDefault().getWorkbench();
      IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
      IEditorPart editorPart = workbenchWindow.getActivePage().getActiveEditor();
      
      try
      {                       
        EditorActionBarContributor contributor = (EditorActionBarContributor)editorPart.getEditorSite().getActionBarContributor();
        statusLine = contributor.getActionBars().getStatusLineManager();
      }
      catch (Exception e)
      {
      }  
    }
    return statusLine;
  }

  public Object getInput()
  {
    return input;
  }

  public void setInput(Object input)
  {
    if (this.input != input) 
    {
      setErrorMessage("");
    }
    
    this.input = input;
  }

  public ISelection getSelection()
  {
    return null;
  }

  public void setSelection(ISelection selection, boolean reveal)
  {
  }

  public void refresh()
  {
  }

  /**
   * A design view that has a main area for controls, and a message line at
   * the bottom
   */
  protected Control createDesignPane(Composite parent, int numColumns)
  {
    // Create the main UI container
    mainUIComponent= new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL)
    {
      public void setVisible(boolean visible)
      {
        super.setVisible(visible);
        if (visible == false && !currentMessage.equals(""))
        {
          showMessageInStatusLine(visible == true);
        }
      }
    };

//    mainUIComponent.setLayoutData(new GridData(GridData.FILL_BOTH));

    controlsContainer = new Composite(mainUIComponent, SWT.NONE);

    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    controlsContainer.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    controlsContainer.setLayoutData(data);
    
    // TODO: Remove above line and uncomment the following two when XSDEditor views
    // are rewritten.  Also refer do related TODO in DTDEditor's BaseWindow.
    // FlatViewUtility flatViewUtility = new FlatViewUtility();
    // controlsContainer = flatViewUtility.createComposite(mainUIComponent, numColumns);

    mainUIComponent.setContent(controlsContainer);

    return mainUIComponent;
  }

  public void setScrollComposite()
  {
    Point p = controlsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    mainUIComponent.setExpandHorizontal(true);
    mainUIComponent.setExpandVertical(true);
    mainUIComponent.setMinSize(p);
  }

  public Composite getControlsContainer()
  {
    return controlsContainer;
  }

  /**
   * Sets the complete state of the page.
   * If false, then client is not allowed to move to next page
   */
  public void setPageComplete(boolean complete)
  {
    pageComplete= complete;
  }

  /**
   * Returns whether the page is complete or not.
   */
  public boolean isPageComplete()
  {
    return pageComplete;
  }

  private String currentMessage = "";
  protected void showMessageInStatusLine(boolean show)
  {
    if (getStatusLine() == null)
      return;
    
    if (show) 
    {
      getStatusLine().setErrorMessage(currentMessage);
    }
    else 
    {
      getStatusLine().setErrorMessage("");
    }
    
    
    getStatusLine().update(false);
  }
  
  /**
   * Sets the message line
   */
  public void setMessage(String message)
  {
    currentMessage = message;
    if (getStatusLine() != null)
    {
      getStatusLine().setErrorMessage(currentMessage);
      getStatusLine().update(false);
    }
  }

  /**
   * Clears a message.
   */
  public void clearMessage()
  {
    currentMessage = "";
    if (getStatusLine() != null)
    {
      getStatusLine().setErrorMessage(currentMessage);
      getStatusLine().update(false);
    }
  }

  public void setErrorMessage(String message)
  {
    setMessage(message);
    setPageComplete(false);
  }

  public void clearErrorMessage()
  {
    clearMessage();
    setPageComplete(true);
  }

  public String getErrorMessage()
  {
    return currentMessage;
  }

}

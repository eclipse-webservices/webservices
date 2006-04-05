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
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class SimpleDialog extends Dialog implements DialogDataEvents
{
  private   WidgetContributor widget_;
  private   WidgetDataEvents  dataEvents_;
  private   Listener          statusListener_;
  protected WizardPageManager pageManager_;  
  	
  public SimpleDialog( Shell shell, PageInfo pageInfo)
  {
	super(shell);
	widget_  = pageInfo.getWidgetFactory().create();
  	statusListener_ = new StatusListener();
  } 	 
  
  public Control createDialogArea( Composite parent )
  {
	Composite control = (Composite) super.createDialogArea(parent);
	dataEvents_ = widget_.addControls( control, statusListener_ );
	callSetters();   
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(control);
    return control;
  }  

  //Implement this method to call all setters on the dialog widget after the controls are created
  protected abstract void callSetters();
  
  public WidgetDataEvents getDataEvents()
  {
    return dataEvents_;
  }
  
  protected WidgetContributor getWidget()
  {
	  return widget_;
  }
  	  
  public void validatePageToStatus()
  {
    /*IStatus status  = widget_.getStatus();
    
    if( status != null )
    {
      if( status.getSeverity() == IStatus.ERROR )
      {
        String message = status.getMessage();

        if( message.length() == 0 )
        {
          setErrorMessage( null );
          setMessage( getDescription() );
        }
        else
        {
          setErrorMessage( message );
        }
          
        // jvh - dialog equivalent ....disable OK? setPageComplete( false );
      }
      else if (status.getSeverity() == IStatus.WARNING )
      {
        setErrorMessage( null );
        setMessage(status.getMessage(), IStatus.WARNING );
        // jvh - dialog equivalent ....enable OK?setPageComplete( true );
      }
      else if( status.getSeverity() == IStatus.INFO )
      {
        setErrorMessage( null );
        setMessage( status.getMessage(), IStatus.INFO );
        // jvh - dialog equivalent ....enable OK?setPageComplete( true );
      }
      else
      {
        setErrorMessage( null );
        setMessage( getDescription() );
        // jvh - dialog equivalent ....enable OK?setPageComplete( true );
      }
    }
    else
    {
      setErrorMessage( null );
      setMessage( getDescription() );
      // jvh - dialog equivalent ....enable OK?setPageComplete( true );
    }
    
    // jvh - dialog equivalent - update dialog buttons?  getContainer().updateButtons();
*/  }
  
  private class StatusListener implements Listener
  {
	public void handleEvent( Event evt ) 
	{
      validatePageToStatus();
	}
  }
}  


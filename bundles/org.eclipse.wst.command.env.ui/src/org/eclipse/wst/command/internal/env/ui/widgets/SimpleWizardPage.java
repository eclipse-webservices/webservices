/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060731   120378 makandre@ca.ibm.com - Andrew Mak, Fields not visible in Large font settings
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;



public class SimpleWizardPage extends WizardPage implements PageWizardDataEvents
{
  private   WidgetContributor widget_;
  private   WidgetDataEvents  dataEvents_;
  private   Listener          statusListener_;
  protected WizardPageManager pageManager_;
  	
  public SimpleWizardPage( PageInfo pageInfo, WizardPageManager pageManager )
  {
  	super( "" );
  	
  	setTitle( pageInfo.getPageName() );
  	setDescription( pageInfo.getPageTitle());
  	  
  	widget_         = pageInfo.getWidgetFactory().create();
  	statusListener_ = new StatusListener();
  	pageManager_    = pageManager;
  } 	  
  	
  public void createControl( Composite parent ) 
  {	  
	ScrolledComposite control = new ScrolledComposite( parent, SWT.V_SCROLL );	
	Composite inner = internalCreateControl(control);
	
	control.setExpandHorizontal(true);
  	control.setExpandVertical(true);
	control.setContent(inner);  	
    control.setMinSize(inner.computeSize(SWT.DEFAULT, SWT.DEFAULT));	
	
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(control);
    setControl( control );
  }
	
  protected Composite internalCreateControl( Composite parent ) {

	Composite control = new Composite( parent, SWT.NONE );
	control.setLayout( new GridLayout() );
  	control.setLayoutData( new GridData( GridData.FILL_BOTH ));
	
	dataEvents_ = widget_.addControls( control, statusListener_ );	
	
	return control;
  }
  
  public WidgetDataEvents getDataEvents()
  {
    return dataEvents_;
  }
  	  
  public void validatePageToStatus()
  {
    IStatus status  = widget_.getStatus();
    
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
          
        setPageComplete( false );
      }
      else if (status.getSeverity() == IStatus.WARNING )
      {
        setErrorMessage( null );
        setMessage(status.getMessage(), IStatus.WARNING );
        setPageComplete( true );
      }
      else if( status.getSeverity() == IStatus.INFO )
      {
        setErrorMessage( null );
        setMessage( status.getMessage(), IStatus.INFO );
        setPageComplete( true );
      }
      else
      {
        setErrorMessage( null );
        setMessage( getDescription() );
        setPageComplete( true );
      }
    }
    else
    {
      setErrorMessage( null );
      setMessage( getDescription() );
      setPageComplete( true );
    }
    
    getContainer().updateButtons();
  }
    
  public boolean canFlipToNextPage() 
  {
	return isPageComplete() && pageManager_.hasNextPage();
  }

  public IWizardPage getNextPage() 
  {
	return pageManager_.getNextPage();
  }
  
  public void setVisible(boolean value) 
  {
  	pageManager_.handlePageVisible( this, value );
  	
    super.setVisible(value);
  }
  
  private class StatusListener implements Listener
  {
	public void handleEvent( Event evt ) 
	{
      validatePageToStatus();
	}
  }
}  

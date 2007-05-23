/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070513   186430 sandakith@wso2.com - Lahiru Sandakith, fix for 186430
 *										  Text not accessible on AXIS2 wizard pages.
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.creation.ui.widgets.bean;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class ServicesXMLSelectBeanWidget extends SimpleWidgetDataContributor 
{
	private DataModel 	model;
	private Button	 	browseButton;
	private Text 		servicesXMLPath;
	private Button 		generateServicesXML;
	private Button 		haveServicesXML;

	public ServicesXMLSelectBeanWidget( DataModel model )
	{
		this.model = model;  
	}

	public WidgetDataEvents addControls( Composite parent, final Listener statusListener )
	{
		final Composite  mainComp = new Composite( parent, SWT.NONE );
		GridLayout layout   = new GridLayout();
		layout.numColumns = 14;
		GridData   griddata = new GridData( GridData.FILL_HORIZONTAL );

		griddata = new GridData( GridData.FILL_HORIZONTAL );
		griddata.horizontalSpan = 14;
		mainComp.setLayout( layout );
		mainComp.setLayoutData( griddata );
		
		Text label = new Text(mainComp,SWT.BACKGROUND | SWT.READ_ONLY);
		label.setText( Axis2CreationUIMessages.LABEL_AXIS2_PREFERENCE_PAGE );
		
		model.setGenerateServicesXML(true);
		model.setServicesXML(false);

		griddata = new GridData( GridData.FILL_HORIZONTAL );
		griddata.horizontalSpan = 14;
		haveServicesXML = new Button( mainComp, SWT.CHECK );
		haveServicesXML.setText(Axis2CreationUIMessages.LABEL_HAVE_SERVICES_XML_FILE);	
		haveServicesXML.setLayoutData(griddata);
		haveServicesXML.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (haveServicesXML.getSelection()){
					haveServicesXML.setSelection(true);
					generateServicesXML.setSelection(false);
					enableServicesXMLBrowse();
				}else{
					if (!generateServicesXML.getSelection()){
						haveServicesXML.setSelection(true); 
						generateServicesXML.setSelection(false);
						enableServicesXMLBrowse();
					}else{
						haveServicesXML.setSelection(false);
						disableServicesXMLBrowse();
					}
				}
				model.setGenerateServicesXML(false);
				model.setServicesXML(true);

				// Need to trigger a validation at this point to ensure
				// that the next button is enabled properly just in case
				// this is the last page in the wizard.
				statusListener.handleEvent( null );
			}     
		});
		griddata = new GridData( GridData.FILL_HORIZONTAL );
		griddata.horizontalSpan = 13;

		servicesXMLPath = new Text( mainComp, SWT.BORDER );
		servicesXMLPath.setText("");
		servicesXMLPath.setLayoutData(griddata);
		servicesXMLPath.addModifyListener( new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				model.setPathToServicesXML( servicesXMLPath.getText() );
				// Need to trigger a validation at this point to ensure
				// that the next button is enabled properly just in case
				// this is the last page in the wizard.
				statusListener.handleEvent( null );
			}
		});

		griddata = new GridData( );
		griddata.horizontalSpan = 1;
		griddata.minimumWidth = 50;
		griddata.grabExcessHorizontalSpace = true;
		browseButton = new Button( mainComp, SWT.NULL );
		browseButton.setText(Axis2CreationUIMessages.LABEL_BROWSE);
		browseButton.setLayoutData(griddata);
		browseButton.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				handleBrowse(mainComp.getShell());
				// Need to trigger a validation at this point to ensure
				// that the next button is enabled properly just in case
				// this is the last page in the wizard.
				statusListener.handleEvent( null );
			}     
		}); 


		griddata = new GridData( GridData.FILL_HORIZONTAL );
		griddata.horizontalSpan = 14;
		generateServicesXML = new Button( mainComp, SWT.CHECK );
		generateServicesXML.setText( Axis2CreationUIMessages.LABEL_DEFAULT_SERVICES_XML_FILE );
		generateServicesXML.setLayoutData(griddata);
		generateServicesXML.setSelection(true);
		generateServicesXML.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (generateServicesXML.getSelection()){
					generateServicesXML.setSelection(true); 
					haveServicesXML.setSelection(false);
					disableServicesXMLBrowse();
				}else{
					if (!haveServicesXML.getSelection()){
						generateServicesXML.setSelection(true); 
						haveServicesXML.setSelection(false);
					}else{
						generateServicesXML.setSelection(false);
					}
				}
				model.setGenerateServicesXML(true);
				model.setServicesXML(false);
				// Need to trigger a validation at this point to ensure
				// that the next button is enabled properly just in case
				// this is the last page in the wizard.
				statusListener.handleEvent( null );
			}     
		}); 

		disableServicesXMLBrowse();

		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
	 */
	public IStatus getStatus() 
	{
		IStatus result = null;

		if( haveServicesXML.getSelection() && servicesXMLPath.getText().equals("") )
		{
			result = new Status( IStatus.ERROR, 
								"id", 
								0, 
								Axis2CreationUIMessages.ERROR_INVALID_SERVICES_XML, 
								null ); 
		}

		return result;
	}

	/**
	 * enable the services.xml text and browse button
	 */
	private void enableServicesXMLBrowse(){
		browseButton.setEnabled(true);
		servicesXMLPath.setEnabled(true);
	}

	/**
	 * disable the services.xml text and browse button
	 */
	private void disableServicesXMLBrowse(){
		browseButton.setEnabled(false);
		servicesXMLPath.setEnabled(false);
	}

	/**
	 * Pops up the file browse dialog box
	 */
	private void handleBrowse(Shell parent) {
		FileDialog fileDialog = new FileDialog(parent);
		fileDialog.setFilterExtensions(new String[] { Axis2CreationUIMessages.FILE_XML });
		String fileName = fileDialog.open();
		if (fileName != null) {
			servicesXMLPath.setText(fileName);
			model.setPathToServicesXML( servicesXMLPath.getText() );
		}
	}
}




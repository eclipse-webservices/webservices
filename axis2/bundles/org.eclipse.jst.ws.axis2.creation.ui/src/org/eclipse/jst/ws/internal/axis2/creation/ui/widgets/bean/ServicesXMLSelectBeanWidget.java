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
 * 20070529   188742 sandakith@wso2.com - Lahiru Sandakith, fix for 188742
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20071030	  207621 zina@ca.ibm.com - Zina Mostafia, Page GUI sequence using tab is not correct ( violates Accessibility)
 * 20080604   193371 samindaw@wso2.com - Saminda Wijeratne,  browsing only xml files in the workspace
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.creation.ui.widgets.bean;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.jst.ws.axis2.creation.core.utils.*;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.internal.axis2.consumption.ui.plugin.WebServiceAxis2ConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.*;
import org.eclipse.jst.ws.internal.ui.common.*;
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
		UIUtils uiUtils = new UIUtils(WebServiceAxis2ConsumptionUIPlugin.PLUGIN_ID);
		
		Text label = new Text(parent, SWT.READ_ONLY);
		label.setText( Axis2CreationUIMessages.LABEL_AXIS2_PREFERENCE_PAGE );
		
		model.setGenerateServicesXML(true);
		model.setServicesXML(false);

		final Composite radioComp = uiUtils.createComposite(parent, 1);
		
		haveServicesXML = uiUtils.createRadioButton(radioComp, 
				Axis2CreationUIMessages.LABEL_HAVE_SERVICES_XML_FILE, null, null );
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
		
		final Composite pathComp = uiUtils.createComposite(radioComp, 3);
		servicesXMLPath = uiUtils.createText(pathComp, "", null, null, SWT.BORDER );
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

		
		browseButton = uiUtils.createPushButton(pathComp, 
				Axis2CreationUIMessages.LABEL_BROWSE, null, null );
		browseButton.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				handleBrowse(pathComp.getShell());
				// Need to trigger a validation at this point to ensure
				// that the next button is enabled properly just in case
				// this is the last page in the wizard.
				statusListener.handleEvent( null );
			}     
		}); 


		generateServicesXML = uiUtils.createRadioButton(radioComp, 
				Axis2CreationUIMessages.LABEL_DEFAULT_SERVICES_XML_FILE, null, null);
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
		String serviceXMLPathLocation = servicesXMLPath.getText();
				if (haveServicesXML.getSelection()
						&& (serviceXMLPathLocation.equals("") 
								|| !(new File(serviceXMLPathLocation).exists()))) {
					result = new Status(IStatus.ERROR, "id", 0,
							Axis2CreationUIMessages.ERROR_INVALID_SERVICES_XML,
							null);
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
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		FileExtensionFilter fileExtensionFilter = new FileExtensionFilter(new String[] {"xml"});
		DialogResourceBrowser dialog = new DialogResourceBrowser(
				parent.getShell(), root, fileExtensionFilter);
		boolean isShowDialog = true;
		while (isShowDialog){
			if (dialog.open() == org.eclipse.jface.window.Window.CANCEL){
				break;
			}
			IResource res = dialog.getFirstSelection();
			if( res != null )
			{
				String fileLocation = res.getLocation().toOSString();
				
				//since the xml file exist now check whether it is a valid services.xsd file 
				if (ServicesXMLUtils.isServicesXMLValid(fileLocation)){
					servicesXMLPath.setText(fileLocation);
					model.setPathToServicesXML(fileLocation);
					isShowDialog = false;
				}else{
					MessageBox messageBox = new MessageBox(parent,SWT.OK);
					messageBox.setMessage(Axis2CreationUIMessages.ERROR_INVALID_SERVICES_XML);
					messageBox.open();
				}
			}
		}
	}
}




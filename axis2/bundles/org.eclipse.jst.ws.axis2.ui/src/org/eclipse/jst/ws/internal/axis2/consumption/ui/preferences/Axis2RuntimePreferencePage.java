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
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070425   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070511   186440 sandakith@wso2.com - Lahiru Sandakith fix 186440
 * 20070513   186430 sandakith@wso2.com - Lahiru Sandakith, fix for 186430
 *										  Text not accessible on AXIS2 wizard pages.
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.preferences;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.RuntimePropertyUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Axis2RuntimePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Button browseButton; 
	private Text axis2Path; 
	private Text statusLabel;
	@SuppressWarnings("unused")
	private IStatus status = Status.OK_STATUS;
	private boolean webappExist = false;
	private boolean isWar = false;
	private String statusBanner = null;
	Axis2EmitterContext context;
	

	  
	protected Control createContents(Composite superparent) {
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		status = Status.OK_STATUS;
		
		final Composite  mainComp = new Composite( superparent, SWT.NONE );
		
		TabFolder axis2PreferenceTab = new TabFolder(mainComp, SWT.NONE);
		TabItem runtimeInstalLocationItem = new TabItem(axis2PreferenceTab, SWT.NONE);
		runtimeInstalLocationItem.setText(Axis2CoreUIMessages.AXIS2_RUNTIME);
		runtimeInstalLocationItem.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);
		
		//-----------------------------Axis2 Rintume Location Group------------------------------//
		Group runtimeGroup = new Group(axis2PreferenceTab, SWT.NONE);
		runtimeGroup.setText(Axis2CoreUIMessages.AXIS2_RUNTIME_LOCATION);
		runtimeInstalLocationItem.setControl(runtimeGroup);
		runtimeGroup.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);
		
		Label label = new Label( runtimeGroup, SWT.NONE );
		label.setText( Axis2CoreUIMessages.AXIS2_LOCATION );
		label.setLocation(10,30);
		label.setSize(100,20);
		
		axis2Path = new Text( runtimeGroup, SWT.BORDER );
		String serverPath = null;
		if (!(context.getAxis2RuntimeLocation()==null)){
		    	serverPath = context.getAxis2RuntimeLocation();
		    	axis2Path.setText(serverPath);
		}else{
			//never come here
		}

		webappExist =runtimeExist(serverPath);
		if(isWar){
			updateWarStatus(true);
		}else{
			updateWarStatus(false);
		}
		axis2Path.setLocation(110,30);
		axis2Path.setSize(400, 20);
		axis2Path.addModifyListener( new ModifyListener(){
			public void modifyText(ModifyEvent e){
				context.setAxis2RuntimeLocation( axis2Path.getText() );
				webappExist =runtimeExist(axis2Path.getText());
				storeValues();
				status = RuntimePropertyUtils.writeServerPathToPropertiesFile(
						axis2Path.getText());
				if (webappExist) {
					status = Status.OK_STATUS;
					statusUpdate(true);
				}else{
					status = Status.CANCEL_STATUS;
					statusUpdate(false);
				}
			}
		});
		browseButton = new Button( runtimeGroup, SWT.NONE );
		browseButton.setText(Axis2CoreUIMessages.LABEL_BROUSE);
		browseButton.setLocation(520,30);
		browseButton.setSize(70, 20);
		browseButton.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				handleBrowse(mainComp.getShell());
			}     
		}); 
	
		if(axis2Path.getText().equals(Axis2CoreUIMessages.NULL)){
			status = new Status( IStatus.ERROR, 
					"id", 
					0, 
					Axis2CoreUIMessages.ERROR_INVALID_AXIS2_SERVER_LOCATION, 
					null ); 
		}
		
		
		statusLabel = new Text(runtimeGroup,SWT.BACKGROUND | SWT.READ_ONLY | SWT.CENTER);
		statusLabel.setLocation(20,100);
		statusLabel.setSize(560,40);
		
		if (axis2Path.getText().equals("")) {
			statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_NOT_EXIT;
		} else if ( !axis2Path.getText().equals("") && (!webappExist)) {
			statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR;
		}else{
			statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD;
		}
		statusLabel.setText(statusBanner);
		
		webappExist =runtimeExist(axis2Path.getText());
		
		TabItem codegenPreferencesItem = new TabItem(axis2PreferenceTab, SWT.NONE);
		codegenPreferencesItem.setText(Axis2CoreUIMessages.AXIS2_PREFERENCES);
		codegenPreferencesItem.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);
		
		//--------------------------------Axis2 Runtime Preferences------------------------------//
		
		Group codegenGroup = new Group(axis2PreferenceTab, SWT.NONE);
		codegenGroup.setText(Axis2CoreUIMessages.AXIS2_RUNTIME_PREFERENCES);
		codegenPreferencesItem.setControl(codegenGroup);
		codegenGroup.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);
		
		//Service Codegen Options
		Text serviceCodegenLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
		serviceCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CODEGEN);
		serviceCodegenLabel.setLocation(10,30);
		serviceCodegenLabel.setSize(220,20);
		
		//Data binding
		Label databindingLabel = new Label( codegenGroup, SWT.NONE );
		databindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
		databindingLabel.setLocation(10,60);
		databindingLabel.setSize(200,20);
		
		final Text databindingText = new Text( codegenGroup,SWT.BORDER );
		databindingText.setText(context.getServiceDatabinding());
		databindingText.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				context.setServiceDatabinding(databindingText.getText() );
			}
		});
		databindingText.setLocation(220,60);
		databindingText.setSize(100,20);
		
		//model.setServerXMLCheck(true);

		//the server side interface option
		final Button generateServerSideInterfaceCheckBoxButton = 
							new Button(codegenGroup, SWT.CHECK);
		generateServerSideInterfaceCheckBoxButton.setText(
				Axis2CoreUIMessages.LABEL_GENERATE_SERVERSIDE_INTERFACE);
		generateServerSideInterfaceCheckBoxButton.setSelection(
									context.isServiceInterfaceSkeleton());
		generateServerSideInterfaceCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setServiceInterfaceSkeleton(
						generateServerSideInterfaceCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		generateServerSideInterfaceCheckBoxButton.setLocation(10, 90);
		generateServerSideInterfaceCheckBoxButton.setSize(300, 15);

		// generate all
		final Button generateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		generateAllCheckBoxButton.setSelection(context.isServiceGenerateAll());
		generateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
		generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setServiceGenerateAll(generateAllCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		generateAllCheckBoxButton.setLocation(10, 120);
		generateAllCheckBoxButton.setSize(350, 15);
		
		///////////////////////////////////////////////////////////////////////////////////////////
		
		//seperator
		Label seperatorLabel0 = new Label( codegenGroup, SWT.SEPARATOR|SWT.BORDER);
		seperatorLabel0.setLocation(10,155);
		seperatorLabel0.setSize(570,1);
		
		///Client Codegen Options
		Text clientCodegenLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
		clientCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CLIENT_CODEGEN);
		clientCodegenLabel.setLocation(10,170);
		clientCodegenLabel.setSize(220,20);
		
		//Client type label 
		Label clientLabel = new Label(codegenGroup, SWT.HORIZONTAL | SWT.NULL);
		clientLabel.setText(Axis2CoreUIMessages.LABEL_CLIENT_SIDE);
		clientLabel.setLocation(10,210);
		clientLabel.setSize(70,20); 
		
		//client side buttons
		final Button syncAndAsyncRadioButton = new Button(codegenGroup, SWT.RADIO);
		syncAndAsyncRadioButton.setText(Axis2CoreUIMessages.LABEL_SYNC_AND_ASYNC);
		syncAndAsyncRadioButton.setVisible(true);
		syncAndAsyncRadioButton.setSelection(
				((context.isSync() || context.isAsync())==false)
				?true
				:(context.isSync()) && context.isAsync());
		//context.setAsync(syncAndAsyncRadioButton.getSelection());
		syncAndAsyncRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setAsync(syncAndAsyncRadioButton.getSelection());
				context.setSync(syncAndAsyncRadioButton.getSelection());
				
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		syncAndAsyncRadioButton.setLocation(80,210);
		syncAndAsyncRadioButton.setSize(190,20); 
		
		final Button syncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
		syncOnlyRadioButton.setText(Axis2CoreUIMessages.LABEL_SYNC);
		syncOnlyRadioButton.setSelection(context.isSync() && !context.isAsync() );
		syncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setAsync(!syncOnlyRadioButton.getSelection());
				context.setSync(syncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		syncOnlyRadioButton.setLocation(280,210);
		syncOnlyRadioButton.setSize(170,20); 

		final Button asyncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
		asyncOnlyRadioButton.setText(Axis2CoreUIMessages.LABEL_ASYNC);
		asyncOnlyRadioButton.setSelection(context.isAsync() && !context.isSync());
		asyncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setAsync(asyncOnlyRadioButton.getSelection());
				context.setSync(!asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		asyncOnlyRadioButton.setLocation(460,210);
		asyncOnlyRadioButton.setSize(170,20);
		
		//Data binding
		Label clientDatabindingLabel = new Label( codegenGroup, SWT.NONE );
		clientDatabindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
		clientDatabindingLabel.setLocation(10,240);
		clientDatabindingLabel.setSize(200,20);
		
		final Text databindingText1 = new Text( codegenGroup, SWT.BORDER );
		databindingText1.setText(context.getClientDatabinding());
		databindingText1.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				context.setClientDatabinding(databindingText1.getText());
			}
		});
		databindingText1.setLocation(220,240);
		databindingText1.setSize(100,20);
		
		
		// generate test case option
		final Button clientTestCaseCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		clientTestCaseCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_TESTCASE_CAPTION);
		clientTestCaseCheckBoxButton.setSelection(context.isClientTestCase());
		clientTestCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setClientTestCase(clientTestCaseCheckBoxButton.getSelection());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		clientTestCaseCheckBoxButton.setLocation(10,270);
		clientTestCaseCheckBoxButton.setSize(300, 15);


		// generate all
		final Button clientGenerateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		clientGenerateAllCheckBoxButton.setSelection(context.isClientGenerateAll());
		clientGenerateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
		clientGenerateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				context.setClientGenerateAll(clientGenerateAllCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		clientGenerateAllCheckBoxButton.setLocation(10, 300);
		clientGenerateAllCheckBoxButton.setSize(400, 15);
		
		///////////////////////////////////////////////////////////////////////////////////////////

		//seperator
		Label seperatorLabel1 = new Label( codegenGroup, SWT.SEPARATOR|SWT.BORDER);
		seperatorLabel1.setLocation(10,335);
		seperatorLabel1.setSize(570,1);
		
		///AAR Options
		Text aarLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
		aarLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_AAR);
		aarLabel.setLocation(10,350);
		aarLabel.setSize(220,20);
		
		//aar extention 
		Label aarExtentionLabel = new Label( codegenGroup, SWT.NONE );
		aarExtentionLabel.setText( Axis2CoreUIMessages.LABEL_AAR_EXTENTION);
		aarExtentionLabel.setLocation(10,390);
		aarExtentionLabel.setSize(200,20);
		
		final Text aarExtentionText = new Text( codegenGroup, SWT.BORDER);
		aarExtentionText.setText(context.getAarExtention());
		aarExtentionText.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				context.setAarExtention(aarExtentionText.getText());
			}
		});
		aarExtentionText.setLocation(220,390);
		aarExtentionText.setSize(100,20);
		

		axis2PreferenceTab.setSize(640, 500);
		
	    return mainComp;
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}
	
	  
	/**
	 * Pops up the file browse dialog box
	 */
	private void handleBrowse(Shell parent) {
		DirectoryDialog fileDialog = new DirectoryDialog(parent);
		String fileName = fileDialog.open();
		if (fileName != null) {
			axis2Path.setText(fileName);
			context.setAxis2RuntimeLocation( axis2Path.getText() );
			if(isWar){
				updateWarStatus(true);
			}else{
				updateWarStatus(false);
			}
		}
	}
	
	private void statusUpdate(boolean status){
		if(statusLabel != null){
			if(!axis2Path.getText().equals("")){
		if (status) {
			statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD);
			this.setErrorMessage(null);
		} else {
			statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR);
					//this.setErrorMessage(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR);
		}
			}else{
				statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_NOT_EXIT);
			}
		}
	}
	
	private boolean runtimeExist(String path){
		isWar=false;
		File axis2HomeDir = new File(path);
		if (axis2HomeDir.isDirectory()) {
			String axis2LibPath = Axis2CoreUtils.addAnotherNodeToPath(
													axis2HomeDir.getAbsolutePath(),
													"lib");
			String axis2WebappPath = Axis2CoreUtils.addAnotherNodeToPath(
					axis2HomeDir.getAbsolutePath(),
					"webapp");
			if (new File(axis2LibPath).isDirectory() && new File(axis2WebappPath).isDirectory()) {
				statusUpdate(true);
				return true;
			} else {
				String axis2WarPath = Axis2CoreUtils.addAnotherNodeToPath(
						path,
						"axis2.war");
				if (new File(axis2WarPath).isFile()) {
					isWar = true;
					statusUpdate(true);
					return true;
				} else {
					statusUpdate(false);
					return false;
				}
			}
		}else{
			statusUpdate(false);
			return false;
		}
	}
	
	private void updateWarStatus(boolean status){
		RuntimePropertyUtils.writeWarStausToPropertiesFile(status);
	}
	
	private void storeValues(){
	    // get the persistent context from the plugin
	    context.setAxis2RuntimeLocation( axis2Path.getText() );
	}
	
}

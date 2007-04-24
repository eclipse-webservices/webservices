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
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.preferences;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.axis2.core.plugin.data.ServerModel;
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
	private Label statusLabel;
	@SuppressWarnings("unused")
	private IStatus status = Status.OK_STATUS;
	private boolean warExist = false;

	  
	protected Control createContents(Composite superparent) {
		status = Status.OK_STATUS;
		
		final Composite  mainComp = new Composite( superparent, SWT.NONE );
		
		TabFolder axis2PreferenceTab = new TabFolder(mainComp, SWT.NONE);
		TabItem runtimeInstalLocationItem = new TabItem(axis2PreferenceTab, SWT.NONE);
		runtimeInstalLocationItem.setText(Axis2CoreUIMessages.AXIS2_RUNTIME);
		
		//---------------------------------------------------Axis2 Rintume Location Group------------------------------//
		Group runtimeGroup = new Group(axis2PreferenceTab, SWT.NONE);
		runtimeGroup.setText("Axis2 Rintume Location");
		runtimeInstalLocationItem.setControl(runtimeGroup);
		
		Label label = new Label( runtimeGroup, SWT.NONE );
		label.setText( Axis2CoreUIMessages.AXIS2_LOCATION );
		label.setLocation(10,30);
		label.setSize(100,20);
		
		axis2Path = new Text( runtimeGroup, SWT.BORDER );
		axis2Path.setText(
					(RuntimePropertyUtils.getServerPathFromPropertiesFile() == null) ? "" 
					: RuntimePropertyUtils.getServerPathFromPropertiesFile()
				);
		axis2Path.setLocation(110,30);
		axis2Path.setSize(400, 20);
		axis2Path.addModifyListener( new ModifyListener(){
			public void modifyText(ModifyEvent e){
				ServerModel.setAxis2ServerPath( axis2Path.getText() );
				warExist =runtimeExist(axis2Path.getText());
				if (warExist) {
					status = RuntimePropertyUtils.writeServerPathToPropertiesFile(
																axis2Path.getText());
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
		
		
		statusLabel = new Label( runtimeGroup, SWT.NONE );
		statusLabel.setLocation(20,100);
		statusLabel.setSize(560,40);
		statusLabel.setAlignment(SWT.CENTER);
		statusLabel.setText(
				(axis2Path.getText().equals("") || !warExist) ?Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR
						: Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD
		);
		
		warExist =runtimeExist(axis2Path.getText());
		
		TabItem codegenPreferencesItem = new TabItem(axis2PreferenceTab, SWT.NONE);
		codegenPreferencesItem.setText(Axis2CoreUIMessages.AXIS2_PREFERENCES);
		
		//---------------------------------------------------Axis2 Runtime Preferences------------------------------//
		
		Group codegenGroup = new Group(axis2PreferenceTab, SWT.NONE);
		codegenGroup.setText("Axis2 Runtime Preferences");
		codegenPreferencesItem.setControl(codegenGroup);
		
		//Service Codegen Options
		Label serviceCodegenLabel = new Label( codegenGroup, SWT.NONE );
		serviceCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CODEGEN);
		serviceCodegenLabel.setLocation(10,30);
		serviceCodegenLabel.setSize(200,20);
		
		//Data binding
		Label databindingLabel = new Label( codegenGroup, SWT.NONE );
		databindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
		databindingLabel.setLocation(10,60);
		databindingLabel.setSize(200,20);
		
		final Text databindingText = new Text( codegenGroup,SWT.BORDER );
		databindingText.setText(Axis2CoreUIMessages.ADB);
		databindingText.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				ServerModel.setAxis2ServerPath( databindingText.getText() );
			}
		});
		databindingText.setLocation(220,60);
		databindingText.setSize(100,20);
		
		// generate test case option
		Button testCaseCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		testCaseCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_TESTCASE_CAPTION);
		//model.setTestCaseCheck(false);
		testCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setTestCaseCheck(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		testCaseCheckBoxButton.setLocation(10, 90);
		testCaseCheckBoxButton.setSize(300, 15);

		//model.setServerXMLCheck(true);

		//the server side interface option
		Button generateServerSideInterfaceCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		generateServerSideInterfaceCheckBoxButton.setSelection(false);
		generateServerSideInterfaceCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_SERVERSIDE_INTERFACE);
		//model.setGenerateAllCheck(false);
		generateServerSideInterfaceCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setGenerateServerSideInterface(true);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		generateServerSideInterfaceCheckBoxButton.setLocation(10, 120);
		generateServerSideInterfaceCheckBoxButton.setSize(300, 15);

		// generate all
		Button generateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		generateAllCheckBoxButton.setSelection(false);
		generateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
		generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setGenerateAllCheck(true);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		generateAllCheckBoxButton.setLocation(10, 150);
		generateAllCheckBoxButton.setSize(350, 15);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		//seperator
		Label seperatorLabel0 = new Label( codegenGroup, SWT.SEPARATOR|SWT.BORDER);
		seperatorLabel0.setLocation(10,185);
		seperatorLabel0.setSize(570,1);
		
		///Client Codegen Options
		Label clientCodegenLabel = new Label( codegenGroup, SWT.NONE );
		clientCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CLIENT_CODEGEN);
		clientCodegenLabel.setLocation(10,200);
		clientCodegenLabel.setSize(200,20);
		
		//Client type label 
		Label clientLabel = new Label(codegenGroup, SWT.HORIZONTAL | SWT.NULL);
		clientLabel.setText(Axis2CoreUIMessages.LABEL_CLIENT_SIDE);
		clientLabel.setLocation(10,240);
		clientLabel.setSize(70,20); 
		
		//client side buttons
		Button syncAndAsyncRadioButton = new Button(codegenGroup, SWT.RADIO);
		syncAndAsyncRadioButton.setText(Axis2CoreUIMessages.LABEL_SYNC_AND_ASYNC);
		syncAndAsyncRadioButton.setVisible(true);
		syncAndAsyncRadioButton.setSelection(true);
		syncAndAsyncRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setSync(syncOnlyRadioButton.getSelection());
				//model.setSync(asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		syncAndAsyncRadioButton.setLocation(80,240);
		syncAndAsyncRadioButton.setSize(190,20); 
		
		Button syncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
		syncOnlyRadioButton.setText(Axis2CoreUIMessages.LABEL_SYNC);
		syncOnlyRadioButton.setSelection(false);
		syncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setSync(syncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		syncOnlyRadioButton.setLocation(280,240);
		syncOnlyRadioButton.setSize(170,20); 

		Button asyncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
		asyncOnlyRadioButton.setText(Axis2CoreUIMessages.LABEL_ASYNC);
		asyncOnlyRadioButton.setSelection(false);
		asyncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setSync(asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		asyncOnlyRadioButton.setLocation(460,240);
		asyncOnlyRadioButton.setSize(170,20);
		
		//Data binding
		Label clientDatabindingLabel = new Label( codegenGroup, SWT.NONE );
		clientDatabindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
		clientDatabindingLabel.setLocation(10,270);
		clientDatabindingLabel.setSize(200,20);
		
		final Text databindingText1 = new Text( codegenGroup, SWT.BORDER );
		databindingText1.setText(Axis2CoreUIMessages.ADB);
		databindingText1.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				ServerModel.setAxis2ServerPath( databindingText1.getText() );
			}
		});
		databindingText1.setLocation(220,270);
		databindingText1.setSize(100,20);
		
		
		// generate test case option
		Button clientTestCaseCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		clientTestCaseCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_TESTCASE_CAPTION);
		//model.setTestCaseCheck(false);
		clientTestCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setTestCaseCheck(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		clientTestCaseCheckBoxButton.setLocation(10,300);
		clientTestCaseCheckBoxButton.setSize(300, 15);

		//model.setServerXMLCheck(true);

		// generate all
		Button clientGenerateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
		clientGenerateAllCheckBoxButton.setSelection(false);
		clientGenerateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
		clientGenerateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//model.setGenerateAllCheck(true);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		clientGenerateAllCheckBoxButton.setLocation(10, 330);
		clientGenerateAllCheckBoxButton.setSize(400, 15);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////

		//seperator
		Label seperatorLabel1 = new Label( codegenGroup, SWT.SEPARATOR|SWT.BORDER);
		seperatorLabel1.setLocation(10,365);
		seperatorLabel1.setSize(570,1);
		
		///AAR Options
		Label aarLabel = new Label( codegenGroup, SWT.NONE );
		aarLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_AAR);
		aarLabel.setLocation(10,380);
		aarLabel.setSize(200,20);
		
		//aar extention 
		Label aarExtentionLabel = new Label( codegenGroup, SWT.NONE );
		aarExtentionLabel.setText( Axis2CoreUIMessages.LABEL_AAR_EXTENTION);
		aarExtentionLabel.setLocation(10,420);
		aarExtentionLabel.setSize(200,20);
		
		final Text aarExtentionText = new Text( codegenGroup, SWT.BORDER);
		aarExtentionText.setText(Axis2CoreUIMessages.AAR);
		aarExtentionText.addModifyListener( new ModifyListener() {
			public void modifyText(ModifyEvent e){
				ServerModel.setAxis2ServerPath( databindingText1.getText() );
			}
		});
		aarExtentionText.setLocation(220,420);
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
			ServerModel.setAxis2ServerPath( axis2Path.getText() );
		}
	}
	
	private void statusUpdate(boolean status){
		if (status) {
			statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD);
			this.setErrorMessage(null);
		} else {
			statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR);
			this.setErrorMessage(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR);
		}
		
	}
	
	private boolean runtimeExist(String path){
		File axis2HomeDir = new File(path);
		if (axis2HomeDir.isDirectory()) {
			String axis2DistPath = Axis2CoreUtils.addAnotherNodeToPath(
													axis2HomeDir.getAbsolutePath(),
													"dist");
			if (new File(axis2DistPath).isDirectory()) {
				String axis2WarPath = Axis2CoreUtils.addAnotherNodeToPath(
						axis2DistPath,
						"axis2.war");
				if (new File(axis2WarPath).isFile()) {
					statusUpdate(true);
					return true;
				} else {
					statusUpdate(false);
					return false;
				}
			} else {
				statusUpdate(false);
				return false;
			}
		}else{
			statusUpdate(false);
			return false;
		}
	}
	
}

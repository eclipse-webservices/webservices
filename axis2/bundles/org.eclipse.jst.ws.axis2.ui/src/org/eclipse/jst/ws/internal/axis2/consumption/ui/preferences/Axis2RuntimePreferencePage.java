/*******************************************************************************
 * Copyright (c) 2007, 2010 WSO2 Inc. and others.
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
 *                      runtime to the framework for 168762
 * 20070425   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070511   186440 sandakith@wso2.com - Lahiru Sandakith fix 186440
 * 20070513   186430 sandakith@wso2.com - Lahiru Sandakith, fix for 186430
 *                      Text not accessible on AXIS2 wizard pages.
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070603   188740 sandakith@wso2.com - Lahiru Sandakith, 
 * 20070604   190505 sandakith@wso2.com - Lahiru Sandakith, 
 * 20070604   190067 pmoogk@ca.ibm.com - Peter Moogk
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20070827   188732 sandakith@wso2.com - Lahiru Sandakith, Restore defaults for preferences
 * 20071030	  207618 zina@ca.ibm.com - Zina Mostafia, Page GUI sequence using tab is not correct ( violates Accessibility)
 * 20080522   233154 samindaw@wso2.com - Saminda Wijeratne, UI rendering problem where textboxes used as labels had white background
 * 20090307   196954 samindaw@wso2.com - Saminda Wijeratne, Support XMLBeans data binding
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.preferences;

import java.io.File;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterDefaults;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.RuntimePropertyUtils;
import org.eclipse.jst.ws.axis2.ui.plugin.WebServiceAxis2UIPlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Axis2RuntimePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Text axis2Path; 
	private Text statusLabel;
	private Combo aarExtensionCombo; 
	private Combo serviceDatabindingCombo;
	private Combo clientDatabindingCombo;
	private Button generateServerSideInterfaceCheckBoxButton;
	private Button generateAllCheckBoxButton;
	private Button syncAndAsyncRadioButton;
	private Button syncOnlyRadioButton;
	private Button asyncOnlyRadioButton;
	private Button clientTestCaseCheckBoxButton;
	private Button clientGenerateAllCheckBoxButton;

	protected Control createContents(Composite superparent) {
		
		UIUtils uiUtils = new UIUtils(WebServiceAxis2UIPlugin.PLUGIN_ID);
		final Composite  mainComp = uiUtils.createComposite(superparent, 1);
		
		TabFolder axis2PreferenceTab = new TabFolder(mainComp, SWT.WRAP);
		axis2PreferenceTab.setLayoutData( new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH) );
		axis2PreferenceTab.setBackground(mainComp.getBackground());
		
		//-----------------------------Axis2 Runtime Location Group------------------------------//
		TabItem runtimeInstalLocationItem = new TabItem(axis2PreferenceTab, SWT.WRAP);
		runtimeInstalLocationItem.setText(Axis2CoreUIMessages.AXIS2_RUNTIME);
		runtimeInstalLocationItem.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);
		
		Composite runtimeTab = uiUtils.createComposite(axis2PreferenceTab, 1);
		runtimeTab.setLayoutData( new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH ) );
		Composite runtimeGroup = uiUtils.createComposite(runtimeTab, 3);
		runtimeTab.setBackground(axis2PreferenceTab.getBackground());
		 
		runtimeInstalLocationItem.setControl(runtimeTab);
		runtimeTab.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);

		axis2Path = uiUtils.createText(runtimeGroup, Axis2CoreUIMessages.AXIS2_RUNTIME_LOCATION , null, null , SWT.BORDER);
		
		Button browseButton = uiUtils.createPushButton(runtimeGroup, Axis2CoreUIMessages.LABEL_BROUSE, null, null);
		browseButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse(mainComp.getShell());
			}     
		}); 

		axis2Path.addModifyListener( new ModifyListener(){
			public void modifyText(ModifyEvent e){
				statusUpdate(runtimeExist(axis2Path.getText()));
				// runtimeTab.layout();
			}
		});
		new org.eclipse.swt.widgets.Label(runtimeTab, SWT.HORIZONTAL);  // Leave some vertical space.
		statusLabel = new Text(runtimeTab, SWT.BACKGROUND | SWT.READ_ONLY | SWT.CENTER | SWT.WRAP | SWT.H_SCROLL);
		statusLabel.setLayoutData( new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH) );
		statusLabel.setBackground(axis2PreferenceTab.getBackground());

		//--------------------------------Axis2 Runtime Preferences------------------------------//

		TabItem codegenPreferencesItem = new TabItem(axis2PreferenceTab, SWT.WRAP);
		codegenPreferencesItem.setText(Axis2CoreUIMessages.AXIS2_PREFERENCES);
		codegenPreferencesItem.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);

		Composite codegenGroup = uiUtils.createComposite(axis2PreferenceTab, 1);
		codegenPreferencesItem.setControl(codegenGroup);
		codegenGroup.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);
		codegenGroup.setBackground(mainComp.getBackground());
		
		///////////////////////////////////////////////////////////////////////////////////////////

		//Service Codegen Options
		Composite serviceCodegenGroup = uiUtils.createComposite(codegenGroup, 1);

		Text serviceCodegenGroupLabel= new Text(serviceCodegenGroup, SWT.READ_ONLY |SWT.WRAP);
		serviceCodegenGroupLabel.setText(Axis2CoreUIMessages.LABEL_WEB_SERVICE_CODEGEN);
		serviceCodegenGroupLabel.setBackground(axis2PreferenceTab.getBackground());
		
		Composite dataBindComp = uiUtils.createComposite(serviceCodegenGroup, 2);
		//Data binding
		serviceDatabindingCombo = uiUtils.createCombo(dataBindComp, Axis2CoreUIMessages.LABEL_DATABINDING, null, null, SWT.READ_ONLY);

		//the server side interface option
		generateServerSideInterfaceCheckBoxButton = uiUtils.createCheckbox(serviceCodegenGroup, Axis2CoreUIMessages.LABEL_GENERATE_SERVERSIDE_INTERFACE, null, null);

		// generate all
		generateAllCheckBoxButton = uiUtils.createCheckbox(serviceCodegenGroup, Axis2CoreUIMessages.LABEL_GENERATE_ALL, null,null);

		uiUtils.createHorizontalSeparator(codegenGroup,2);
		///////////////////////////////////////////////////////////////////////////////////////////

		///Client Codegen Options
		Composite clientCodegenGroup = uiUtils.createComposite(codegenGroup, 1);
		Text clientCodegenGroupLabel= new Text(clientCodegenGroup, SWT.READ_ONLY);
		clientCodegenGroupLabel.setText(Axis2CoreUIMessages.LABEL_WEB_SERVICE_CLIENT_CODEGEN);
		clientCodegenGroupLabel.setBackground(axis2PreferenceTab.getBackground());
		
		Group clientModeRadioComp = uiUtils.createGroup(clientCodegenGroup, Axis2CoreUIMessages.LABEL_CLIENT_SIDE, null, null);

		//client side buttons
		syncAndAsyncRadioButton = uiUtils.createRadioButton(clientModeRadioComp, Axis2CoreUIMessages.LABEL_SYNC_AND_ASYNC, null, null);
		syncOnlyRadioButton 	= uiUtils.createRadioButton(clientModeRadioComp,Axis2CoreUIMessages.LABEL_SYNC, null, null);
		asyncOnlyRadioButton 	= uiUtils.createRadioButton(clientModeRadioComp, Axis2CoreUIMessages.LABEL_ASYNC, null, null);

		Composite dataBind = uiUtils.createComposite(clientCodegenGroup, 2);
		clientDatabindingCombo = uiUtils.createCombo(dataBind, Axis2CoreUIMessages.LABEL_DATABINDING, null, null, SWT.READ_ONLY);

		// generate test case option
		clientTestCaseCheckBoxButton = uiUtils.createCheckbox(clientCodegenGroup, Axis2CoreUIMessages.LABEL_GENERATE_TESTCASE_CAPTION, null, null);

		// generate all
		clientGenerateAllCheckBoxButton = uiUtils.createCheckbox(clientCodegenGroup, Axis2CoreUIMessages.LABEL_GENERATE_ALL, null, null);


		uiUtils.createHorizontalSeparator(codegenGroup,2);

		///////////////////////////////////////////////////////////////////////////////////////////

		///AAR Options
		Composite aarGroup = uiUtils.createComposite(codegenGroup,1);

		Text arrGroupLabel= new Text(aarGroup, SWT.READ_ONLY);
		arrGroupLabel.setText(Axis2CoreUIMessages.LABEL_WEB_SERVICE_AAR);
		arrGroupLabel.setBackground(axis2PreferenceTab.getBackground());
		
		Composite aarExtGroup = uiUtils.createComposite(aarGroup,2);

		//aar extention 
		aarExtensionCombo = uiUtils.createCombo(aarExtGroup, Axis2CoreUIMessages.LABEL_AAR_EXTENTION, null, null, SWT.READ_ONLY );

		initializeValues();
		axis2PreferenceTab.setEnabled(true);
		axis2PreferenceTab.setVisible(true);
		return mainComp;
	}

	public void init(IWorkbench workbench) {
	}


	/**
	 * Pops up the file browse dialog box
	 */
	private void handleBrowse(Shell parent) {
		DirectoryDialog fileDialog = new DirectoryDialog(parent);
		String fileName = fileDialog.open();
		if (fileName != null) {
			axis2Path.setText(fileName);
		}
	}

	private void statusUpdate(boolean status){
		if(statusLabel != null){
			if(!axis2Path.getText().equals("")){
				if (status) {
					statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD);
				} else {
					statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR);
				}
			}else{
				statusLabel.setText(Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_NOT_EXIT);
			}
		}
	}

	private boolean runtimeExist(String path){

		File axis2HomeDir = new File(path);
		if (!axis2HomeDir.isDirectory()) 
			return false;

		String axis2LibPath = Axis2CoreUtils.addAnotherNodeToPath(axis2HomeDir.getAbsolutePath(), "lib");
		String axis2WebappPath = Axis2CoreUtils.addAnotherNodeToPath(axis2HomeDir.getAbsolutePath(), "webapp");
		if (new File(axis2LibPath).isDirectory() && new File(axis2WebappPath).isDirectory()) 
			return true;
		else {
			String axis2WarPath = Axis2CoreUtils.addAnotherNodeToPath( path, "axis2.war");
			if (new File(axis2WarPath).isFile()) {
				RuntimePropertyUtils.writeWarStausToPropertiesFile(true);
				return true;
			} else 				
				return false;
		}
	}

	private void storeValues(){
		// set values in the persistent context 
		Axis2EmitterContext context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		context.setAxis2RuntimeLocation( axis2Path.getText());
		RuntimePropertyUtils.writeServerPathToPropertiesFile(axis2Path.getText());
		context.setServiceDatabinding(serviceDatabindingCombo.getItem(serviceDatabindingCombo.getSelectionIndex()));
		context.setServiceInterfaceSkeleton( generateServerSideInterfaceCheckBoxButton.getSelection());
		context.setServiceGenerateAll(generateAllCheckBoxButton.getSelection());
		context.setAsync(asyncOnlyRadioButton.getSelection());
		context.setSync(syncOnlyRadioButton.getSelection());
		context.setClientDatabinding(clientDatabindingCombo.getItem(clientDatabindingCombo.getSelectionIndex()));
		context.setClientTestCase(clientTestCaseCheckBoxButton.getSelection());
		context.setClientGenerateAll(clientGenerateAllCheckBoxButton.getSelection());
		context.setAarExtention(aarExtensionCombo.getText());
	}

	/**
	 * Initializes states of the controls using default values
	 * in the preference store.
	 */
	private void initializeDefaults() {
		aarExtensionCombo.select(0);
		serviceDatabindingCombo.select(0);
		clientDatabindingCombo.select(0);
		generateServerSideInterfaceCheckBoxButton.setSelection(Axis2EmitterDefaults.isServiceInterfaceSkeleton());
		generateAllCheckBoxButton.setSelection(Axis2EmitterDefaults.isServiceGenerateAll());
		syncAndAsyncRadioButton.setSelection(((Axis2EmitterDefaults.isClientSync() || Axis2EmitterDefaults.isClientAsync())==false)?true:
			(Axis2EmitterDefaults.isClientSync()) && Axis2EmitterDefaults.isClientAsync());
		syncOnlyRadioButton.setSelection( Axis2EmitterDefaults.isClientSync() && !Axis2EmitterDefaults.isClientAsync());
		asyncOnlyRadioButton.setSelection(
				Axis2EmitterDefaults.isClientAsync() && !Axis2EmitterDefaults.isClientSync());

		clientTestCaseCheckBoxButton.setSelection(Axis2EmitterDefaults.isClientTestCase());
		clientGenerateAllCheckBoxButton.setSelection(Axis2EmitterDefaults.isClientGenerateAll());
		axis2Path.setText(Axis2EmitterDefaults.getAxis2RuntimeLocation());
	}

	private void initializeValues()
	{
		Axis2EmitterContext context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();

		String[] databindingItems = {Axis2Constants.DATA_BINDING_ADB,Axis2Constants.DATA_BINDING_XMLBEANS};
		serviceDatabindingCombo.setItems(databindingItems);
		int selected = serviceDatabindingCombo.indexOf(context.getServiceDatabinding().toUpperCase());
		if (selected==-1)
			serviceDatabindingCombo.select(0);
		else
			serviceDatabindingCombo.select(selected);
		clientDatabindingCombo.setItems(databindingItems);
		selected = clientDatabindingCombo.indexOf(context.getClientDatabinding().toUpperCase());
		if (selected==-1)
			clientDatabindingCombo.select(0);
		else
			clientDatabindingCombo.select(selected);

		generateServerSideInterfaceCheckBoxButton.setSelection( context.isServiceInterfaceSkeleton());
		generateAllCheckBoxButton.setSelection(context.isServiceGenerateAll());

		syncAndAsyncRadioButton.setSelection(((context.isSync() || context.isAsync())==false) ?true
				:(context.isSync()) && context.isAsync());
		syncOnlyRadioButton.setSelection(context.isSync() && !context.isAsync() );
		asyncOnlyRadioButton.setSelection(context.isAsync() && !context.isSync());

		clientTestCaseCheckBoxButton.setSelection(context.isClientTestCase());
		clientGenerateAllCheckBoxButton.setSelection(context.isClientGenerateAll());

		String[] aarExtentionItems = { Axis2Constants.AAR };
		aarExtensionCombo.setItems(aarExtentionItems);
		aarExtensionCombo.select(0);
		
		String serverPath = context.getAxis2RuntimeLocation();
		if ( serverPath != null){
			axis2Path.setText(serverPath);
			statusUpdate(runtimeExist(serverPath));
			RuntimePropertyUtils.writeWarStausToPropertiesFile(false);
		}
		else
			statusUpdate(false);
	}

	/**
	 * Default button has been pressed.
	 */
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}

	/**
	 * Apply button has been pressed.
	 */
	protected void performApply() {
		performOk();
	}

	/**
	 * OK button has been pressed.
	 */	
	public boolean performOk() {
		storeValues();
		return true;
	}

}

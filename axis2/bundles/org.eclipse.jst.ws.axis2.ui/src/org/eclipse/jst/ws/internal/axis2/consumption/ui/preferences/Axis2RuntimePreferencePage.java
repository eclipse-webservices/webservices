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
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.preferences;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterDefaults;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
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
    context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
    status = Status.OK_STATUS;
    
    final Composite  mainComp = new Composite( superparent, SWT.NONE );
    
    mainComp.setLayout( new GridLayout() );
    mainComp.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    
    TabFolder axis2PreferenceTab = new TabFolder(mainComp, SWT.NONE);
    
    axis2PreferenceTab.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    
    //-----------------------------Axis2 Runtime Location Group------------------------------//
    TabItem runtimeInstalLocationItem = new TabItem(axis2PreferenceTab, SWT.NONE);
    runtimeInstalLocationItem.setText(Axis2CoreUIMessages.AXIS2_RUNTIME);
    runtimeInstalLocationItem.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);
    
    final Composite runtimeGroup = new Composite(axis2PreferenceTab, SWT.NONE);
    
    runtimeInstalLocationItem.setControl(runtimeGroup);
    runtimeGroup.setToolTipText(Axis2CoreUIMessages.AXIS2_RUNTIME_TOOLTIP);
    
    GridLayout layout = new GridLayout();
    
    layout.numColumns = 3;
    layout.marginHeight = 10;
    runtimeGroup.setLayout( layout );
    runtimeGroup.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        
    Label label = new Label( runtimeGroup, SWT.NONE );
    label.setText( Axis2CoreUIMessages.AXIS2_RUNTIME_LOCATION );
    
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
    
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    
    gd.minimumWidth = 40;
    axis2Path.setLayoutData(gd);
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
        
        runtimeGroup.layout();
      }
    });
    
    browseButton = new Button( runtimeGroup, SWT.NONE );
    browseButton.setText(Axis2CoreUIMessages.LABEL_BROUSE);
    
    browseButton.addSelectionListener( new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        handleBrowse(mainComp.getShell());
      }     
    }); 
  
    if(axis2Path.getText().equals(Axis2Constants.NULL)){
      status = new Status( IStatus.ERROR, 
          "id", 
          0, 
          Axis2CoreUIMessages.ERROR_INVALID_AXIS2_SERVER_LOCATION, 
          null ); 
    }
    
    Label separator = new Label( runtimeGroup, SWT.NONE);  // Leave some vertical space.
    
    gd = new GridData();
    gd.horizontalSpan = 3;
    separator.setText( " " );
    separator.setLayoutData( gd );
    
    statusLabel = new Text(runtimeGroup,SWT.BACKGROUND | SWT.READ_ONLY | SWT.CENTER);
    gd = new GridData();
    gd.horizontalSpan = 3;
    gd.horizontalAlignment = GridData.CENTER;
    statusLabel.setLayoutData( gd );
    
    if (axis2Path.getText().equals("")) {
      statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_NOT_EXIT;
    } else if ( !axis2Path.getText().equals("") && (!webappExist)) {
      statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD_ERROR;
    }else{
      statusBanner = Axis2CoreUIMessages.LABEL_AXIS2_RUNTIME_LOAD;
    }
    statusLabel.setText(statusBanner);
    
    webappExist =runtimeExist(axis2Path.getText());
    
    //--------------------------------Axis2 Runtime Preferences------------------------------//
    
    TabItem codegenPreferencesItem = new TabItem(axis2PreferenceTab, SWT.NONE);
    codegenPreferencesItem.setText(Axis2CoreUIMessages.AXIS2_PREFERENCES);
    codegenPreferencesItem.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);
    
    Composite codegenGroup = new Composite(axis2PreferenceTab, SWT.NONE);
    codegenPreferencesItem.setControl(codegenGroup);
    codegenGroup.setToolTipText(Axis2CoreUIMessages.AXIS2_PREFERENCES_TOOLTIP);
    
    layout = new GridLayout();
    codegenGroup.setLayout(layout);
    layout.numColumns = 2;
    layout.marginHeight = 10;
    gd = new GridData(GridData.FILL_BOTH);
    codegenGroup.setLayoutData( gd );
    
    //Service Codegen Options
    Text serviceCodegenLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
    serviceCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CODEGEN);
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    serviceCodegenLabel.setLayoutData(gd);

    //Data binding
    Label databindingLabel = new Label( codegenGroup, SWT.NONE );
    databindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
  
    //Data binding items
    final String[] databindingItems = {context.getServiceDatabinding().toUpperCase()};
    
    serviceDatabindingCombo = new Combo(codegenGroup,SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
    serviceDatabindingCombo.setItems(databindingItems);
    serviceDatabindingCombo.select(0);
    context.setServiceDatabinding(serviceDatabindingCombo.getItem(0));
    serviceDatabindingCombo.addSelectionListener(new SelectionAdapter(){
        public void widgetSelected(SelectionEvent e) {
          context.setServiceDatabinding(serviceDatabindingCombo
              .getItem(serviceDatabindingCombo.getSelectionIndex()));
        }
    });
    
    //the server side interface option
    generateServerSideInterfaceCheckBoxButton = 
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
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    generateServerSideInterfaceCheckBoxButton.setLayoutData(gd);

    // generate all
    generateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
    generateAllCheckBoxButton.setSelection(context.isServiceGenerateAll());
    generateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
    generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        context.setServiceGenerateAll(generateAllCheckBoxButton.getSelection());
      }
      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    generateAllCheckBoxButton.setLayoutData(gd);

    ///////////////////////////////////////////////////////////////////////////////////////////
    
    //seperator
    Label seperatorLabel0 = new Label( codegenGroup, SWT.SEPARATOR | SWT.HORIZONTAL );
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    gd.verticalIndent=5;
    seperatorLabel0.setLayoutData(gd);
            
    ///Client Codegen Options
    Text clientCodegenLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
    clientCodegenLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_CLIENT_CODEGEN);
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    gd.verticalIndent=5;
    clientCodegenLabel.setLayoutData(gd);
    
    //Client type label 
    Label clientLabel = new Label(codegenGroup, SWT.HORIZONTAL | SWT.NULL);
    clientLabel.setText(Axis2CoreUIMessages.LABEL_CLIENT_SIDE);
    
    //client side buttons
    syncAndAsyncRadioButton = new Button(codegenGroup, SWT.RADIO);
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

    // Skip a column
    new Label( codegenGroup, SWT.NONE );
    
    syncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
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
    
    // Skip a column
    new Label( codegenGroup, SWT.NONE );

    asyncOnlyRadioButton = new Button(codegenGroup, SWT.RADIO);
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
    
    //Data binding
    Label clientDatabindingLabel = new Label( codegenGroup, SWT.NONE );
    clientDatabindingLabel.setText( Axis2CoreUIMessages.LABEL_DATABINDING);
    
    clientDatabindingCombo = new Combo(codegenGroup,SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
    clientDatabindingCombo.setItems(databindingItems);
    clientDatabindingCombo.select(0);
    context.setClientDatabinding(clientDatabindingCombo.getItem(0));
    clientDatabindingCombo.addSelectionListener(new SelectionAdapter(){
        public void widgetSelected(SelectionEvent e) {
            context.setClientDatabinding(clientDatabindingCombo
                .getItem(clientDatabindingCombo.getSelectionIndex()));
        }
    });
    
    // generate test case option
    clientTestCaseCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
    clientTestCaseCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_TESTCASE_CAPTION);
    clientTestCaseCheckBoxButton.setSelection(context.isClientTestCase());
    clientTestCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        context.setClientTestCase(clientTestCaseCheckBoxButton.getSelection());
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    clientTestCaseCheckBoxButton.setLayoutData(gd);

    // generate all
    clientGenerateAllCheckBoxButton = new Button(codegenGroup, SWT.CHECK);
    clientGenerateAllCheckBoxButton.setSelection(context.isClientGenerateAll());
    clientGenerateAllCheckBoxButton.setText(Axis2CoreUIMessages.LABEL_GENERATE_ALL);
    clientGenerateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        context.setClientGenerateAll(clientGenerateAllCheckBoxButton.getSelection());
      }
      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    clientGenerateAllCheckBoxButton.setLayoutData(gd);
    
    ///////////////////////////////////////////////////////////////////////////////////////////

    //seperator
    Label seperatorLabel11 = new Label( codegenGroup, SWT.SEPARATOR | SWT.HORIZONTAL );
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    gd.verticalIndent=5;
    seperatorLabel11.setLayoutData(gd);

    ///AAR Options
    Text aarLabel = new Text(codegenGroup,SWT.BACKGROUND | SWT.READ_ONLY);
    aarLabel.setText( Axis2CoreUIMessages.LABEL_WEB_SERVICE_AAR);
    
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    gd.verticalIndent=5;
    aarLabel.setLayoutData(gd);
    
    //aar extention 
    Label aarExtentionLabel = new Label( codegenGroup, SWT.NONE );
    aarExtentionLabel.setText( Axis2CoreUIMessages.LABEL_AAR_EXTENTION);
    
    //AAR extention items
    final String[] aarExtentionItems = { Axis2Constants.AAR };
    
    aarExtensionCombo = new Combo(codegenGroup,SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
    aarExtensionCombo.setItems(aarExtentionItems);
    aarExtensionCombo.select(0);
    context.setAarExtention( aarExtensionCombo.getItem(0) );
    aarExtensionCombo.addSelectionListener(new SelectionAdapter(){
        public void widgetSelected(SelectionEvent e) {
            int index = aarExtensionCombo.getSelectionIndex();
            context.setAarExtention(aarExtensionCombo.getItem(index));
        }
    });
    
        
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
  
  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults() {
	  aarExtensionCombo.select(0);
	  serviceDatabindingCombo.select(0);
	  clientDatabindingCombo.select(0);
	  generateServerSideInterfaceCheckBoxButton.setSelection(Axis2EmitterDefaults.isServiceInterfaceSkeleton());
	  context.setServiceInterfaceSkeleton(Axis2EmitterDefaults.isServiceInterfaceSkeleton());
	  generateAllCheckBoxButton.setSelection(Axis2EmitterDefaults.isServiceGenerateAll());
	  context.setServiceGenerateAll(Axis2EmitterDefaults.isServiceGenerateAll());
	  syncAndAsyncRadioButton.setSelection(
			  ((Axis2EmitterDefaults.isClientSync() || Axis2EmitterDefaults.isClientAsync())==false)?true:
				  (Axis2EmitterDefaults.isClientSync()) && Axis2EmitterDefaults.isClientAsync());
	  syncOnlyRadioButton.setSelection(
			  Axis2EmitterDefaults.isClientSync() && !Axis2EmitterDefaults.isClientAsync());
	  asyncOnlyRadioButton.setSelection(
			  Axis2EmitterDefaults.isClientAsync() && !Axis2EmitterDefaults.isClientSync());
	  context.setSync(Axis2EmitterDefaults.isClientSync());
	  context.setAsync(Axis2EmitterDefaults.isClientAsync());
	  clientTestCaseCheckBoxButton.setSelection(Axis2EmitterDefaults.isClientTestCase());
	  context.setClientTestCase(Axis2EmitterDefaults.isClientTestCase());
	  clientGenerateAllCheckBoxButton.setSelection(Axis2EmitterDefaults.isClientGenerateAll());
	  context.setClientGenerateAll(Axis2EmitterDefaults.isClientGenerateAll());
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
	  super.performApply();
  }

  /**
   * Cancel button has been pressed.
   */	
  public boolean performCancel() {
	  return super.performCancel();
  }

  /**
   * OK button has been pressed.
   */	
  public boolean performOk() {
	  return super.performOk();
  }
  
}

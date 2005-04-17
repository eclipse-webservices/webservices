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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.ClientProjectTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;


public class ClientRuntimeSelectionWidget extends SimpleWidgetDataContributor
{
  private String    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  
  /* CONTEXT_ID PWRS0003 for the Wizard Scenario Client configuration of the Runtime Selection Page */
  private String INFOPOP_PWRS_GROUP_CLIENT = pluginId_ + ".PWRS0003";
  
  private String                       clientScenarioId;
  private Group                        clientGroup_;
  private RuntimeServerSelectionWidget runtimeWidget_;
  private SelectionListChoices         runtime2ClientTypes_;
  private Combo                        clientType_;
  private SelectionAdapter             clientTypeSelListener;
  private ProjectSelectionWidget       projectWidget_;
  private boolean 					   isVisible_;
  
  // rsk revisit - Putting this in temporarily. The RuntimeSelectionDialog needs the server
  // information even when it is launched for the client half of the configuration.
  private String                       serviceScenarioId;
  //
  
  
  public WidgetDataEvents addControls( Composite parent, final Listener statusListener )
  {
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, pluginId_ ); 
    
    clientGroup_ = uiUtils.createGroup( parent, "LABEL_CLIENT_SELECTION_VIEW_TITLE",
                                        null, INFOPOP_PWRS_GROUP_CLIENT, 2, 5, 5 );
    
    runtimeWidget_ = new RuntimeServerSelectionWidget( true );
    runtimeWidget_.addControls( clientGroup_, statusListener );
    runtimeWidget_.addModifyListener( new ModifyListener()
                                      {
                                        public void modifyText(ModifyEvent e)
                                        {
                                          handleRuntime2ClientTypesEvent();
                                          handleUpdateProjectWidget();
                                        }
                                      });
    
    clientType_ = uiUtils.createCombo( clientGroup_, "LABEL_CLIENT_TYPE",  
                                       "TOOLTIP_PWCR_COMBO_CLIENT_TYPE",
                                       INFOPOP_PWRS_GROUP_CLIENT, 
                                       SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    clientTypeSelListener = new SelectionAdapter()
                                {
                                  public void widgetSelected( SelectionEvent evt )
                                  {
                                    handleSetProjects( clientType_.getSelectionIndex() );
                                    statusListener.handleEvent(null);
                                  }
                                };
                                
    clientType_.addSelectionListener(clientTypeSelListener);
    
    projectWidget_ = new ProjectSelectionWidget(true);
    projectWidget_.addControls( clientGroup_, statusListener );
    
    //projectWidget_.addModifyListener(new ModifyListener(){
    //  								public void modifyText(ModifyEvent e){
    //  								  getProjectSelections();
    //  								}
    //								});
    
    return this;
  }
  
  // This method is called by the creator of this widget to control
  // whether it is visible or not.
  public void setVisible( boolean value )
  {
    clientGroup_.setVisible( value ); 
    isVisible_ = value;
  }
  
  public boolean isVisible(){
  	return this.isVisible_;
  }

  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {     
    runtimeWidget_.setTypeRuntimeServer( ids );
    projectWidget_.setTypeRuntimeServer(ids);
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return runtimeWidget_.getTypeRuntimeServer();  
  }
  
  public String getJ2EEVersion()
  {
    return runtimeWidget_.getJ2EEVersion();
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
    runtimeWidget_.setJ2EEVersion(j2eeVersion);
    projectWidget_.setJ2EEVersion(j2eeVersion);
  }
    
  public boolean getClientNeedEAR()
  {
    return projectWidget_.getNeedEAR();
  }
  
  public void setClientNeedEAR(boolean b)
  {
    projectWidget_.setNeedEAR(b);
  }  
  
  /**
   * @param runtime2ClientTypes The runtime2ClientTypes to set.
   */
  public void setRuntime2ClientTypes(SelectionListChoices runtime2ClientTypes)
  {
    runtime2ClientTypes_ = runtime2ClientTypes;
    handleRuntime2ClientTypesEvent();
  }

  public SelectionListChoices getRuntime2ClientTypes()
  {
    ClientProjectTypeRegistry registry = ClientProjectTypeRegistry.getInstance();
    
    String runtime       = runtimeWidget_.getTypeRuntimeServer().getRuntimeId();
    String clientProject = projectWidget_.getProjectChoices().getList().getSelection();
    String earProject    = projectWidget_.getProjectChoices().getChoice().getList().getSelection();
    
    SelectionList runtimeList  = runtime2ClientTypes_.getList();
    runtimeList.setSelectionValue( runtime );
    
    SelectionList projTypeList = runtime2ClientTypes_.getChoice().getList();
    projTypeList.setIndex( clientType_.getSelectionIndex() );
    
    SelectionListChoices clientProjChoice = runtime2ClientTypes_.getChoice().getChoice();
    SelectionList        clientProjList   = clientProjChoice.getList();
    clientProjList.setSelectionValue( clientProject );    
    
    SelectionList        earProjList      = clientProjChoice.getChoice().getList();   
    earProjList.setSelectionValue( earProject );
    
    return runtime2ClientTypes_; 
  }
  
  private void handleRuntime2ClientTypesEvent()
  {
    if (runtime2ClientTypes_ != null)
    {  
      //If the runtimeId from runtimeWidget_ is already selected in
      //in runtime2ClientTypes_, do nothing. 
      String currentRuntimeId = runtime2ClientTypes_.getList().getSelection();
      String newRuntimeId = runtimeWidget_.getTypeRuntimeServer().getRuntimeId();
      if (currentRuntimeId != newRuntimeId)
      {
        SelectionList runtimes = runtime2ClientTypes_.getList();
        runtimes.setSelectionValue( runtimeWidget_.getTypeRuntimeServer().getRuntimeId() );
      }
      
      String[] clientTypeIds = runtime2ClientTypes_.getChoice().getList().getList();
      int selectedClientTypeIdx = runtime2ClientTypes_.getChoice().getList().getIndex();
      
      clientType_.removeSelectionListener(clientTypeSelListener);
      clientType_.setItems( getClientTypeLabels( clientTypeIds ));
      clientType_.select(selectedClientTypeIdx);
      clientType_.addSelectionListener(clientTypeSelListener);
      handleSetProjects(selectedClientTypeIdx);
      
    }
  }


  private void handleUpdateProjectWidget()
  {
    projectWidget_.setTypeRuntimeServer(runtimeWidget_.getTypeRuntimeServer());
    projectWidget_.setJ2EEVersion(runtimeWidget_.getJ2EEVersion());
  }

  
  private void handleSetProjects( int clientTypeIndex )
  {    
    SelectionListChoices clientType2Projects = runtime2ClientTypes_.getChoice();
    clientType2Projects.getList().setIndex( clientTypeIndex );
    
    // Make sure that the project name and EAR name are kept the same
    // even if the client type is changed.  Note: the project and EAR lists
    // will be updated according to what is set by the client type.
    SelectionListChoices currentProjects = projectWidget_.getProjectChoices();
    
    if( currentProjects != null )
    {
      String projectName = currentProjects.getList().getSelection();
      String earName     = currentProjects.getChoice().getList().getSelection();
      
      // Set the project to it's original state.
      clientType2Projects.getChoice().getList().setSelectionValue( projectName );
	
	  // Set the EAR name to it's original state.
      clientType2Projects.getChoice().getChoice().getList().setSelectionValue( earName );
    }
	
    projectWidget_.setProjectTypeId(clientType2Projects.getList().getSelection());
    projectWidget_.setProjectChoices( clientType2Projects.getChoice() );    
  }
  
  private String[] getClientTypeLabels( String[] types )
  {
    ClientProjectTypeRegistry registry         = ClientProjectTypeRegistry.getInstance();
    String[]                  clientTypeLabels = new String[types.length];
    
    for( int index = 0; index < types.length; index++ )
    {
      clientTypeLabels[index] = registry.getElementById(types[index]).getAttribute("label");
    }
    
    return clientTypeLabels;
  }
  
  public ProjectSelectionWidget getProjectSelectionWidget() {
    return this.projectWidget_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public Status getStatus() 
  {
    Status finalStatus   = new SimpleStatus( "" );
    Status projectStatus = projectWidget_.getStatus();
    Status runtimeStatus = runtimeWidget_.getStatus();
    
    if( runtimeStatus.getSeverity() == Status.ERROR )
    {
      finalStatus = runtimeStatus;
    }
    else if( projectStatus.getSeverity() == Status.ERROR )
    {
      finalStatus = projectStatus;
    }
    else
    {
      SelectionListChoices clientProjects = getProjectSelectionWidget().getProjectChoices();
      ValidationUtils valUtils = new ValidationUtils();
      if (clientProjects != null)
      {
        String clientEARName = clientProjects.getChoice().getList().getSelection();
        String clientProjName = clientProjects.getList().getSelection();

        //Validate that the selected client project is of the type indicated by client project type.
        Status clientProjectTypeStatus = valUtils.validateProjectType(clientProjName, runtime2ClientTypes_);
        if (clientProjectTypeStatus.getSeverity() == Status.ERROR)
        {
          finalStatus = clientProjectTypeStatus;
        }
        
        //Validate client side server targets and J2EE levels
        String clientServerFactoryId = getClientTypeRuntimeServer().getServerId();
        String clientJ2EElevel = getJ2EEVersion();
        Status clientProjectStatus = valUtils.validateProjectTargetAndJ2EE(clientProjName, clientEARName, clientServerFactoryId,
            clientJ2EElevel);
        if (clientProjectStatus.getSeverity() == Status.ERROR)
        {
          finalStatus = clientProjectStatus;
        }
      }
    }
    return finalStatus;    
  }
}

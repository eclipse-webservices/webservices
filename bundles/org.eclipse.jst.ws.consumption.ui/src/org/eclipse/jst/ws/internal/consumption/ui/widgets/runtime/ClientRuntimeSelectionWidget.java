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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
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
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;


public class ClientRuntimeSelectionWidget extends SimpleWidgetDataContributor
{
  private String    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  
  /* CONTEXT_ID PWRS0003 for the Wizard Scenario Client configuration of the Runtime Selection Page */
  private String INFOPOP_PWRS_GROUP_CLIENT = pluginId_ + ".PWRS0003";
  
  private Group                        clientGroup_;
  private RuntimeServerSelectionWidget runtimeWidget_;
  private SelectionListChoices         runtime2ClientTypes_;
  //private Combo                        clientType_;
  private SelectionAdapter             clientTypeSelListener;
  private ProjectSelectionWidget       projectWidget_;
  private boolean 					           isVisible_;
  private String                        clientRuntimeId_;
  
  
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
                                          //handleRuntime2ClientTypesEvent();
                                          handleUpdateProjectWidget();
                                        }
                                      });
                                         
    
    projectWidget_ = new ProjectSelectionWidget(true);
    projectWidget_.addControls( clientGroup_, statusListener );
    
    //clientType_ = uiUtils.createCombo(clientGroup_, "LABEL_CLIENT_TYPE", "TOOLTIP_PWCR_COMBO_CLIENT_TYPE",
    //    INFOPOP_PWRS_GROUP_CLIENT, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);

    //Temporarily remove the listeners
    /*
    clientTypeSelListener = new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent evt)
      {
        handleSetProjects(clientType_.getSelectionIndex());
        statusListener.handleEvent(null);
      }
    };

    clientType_.addSelectionListener(clientTypeSelListener);
    */
    
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
  
  public void setClientRuntimeId(String id)
  {
    clientRuntimeId_ = id;
  }
  
  public String getClientRuntimeId()
  {
    //calculate the most appropriate clientRuntimeId based on current settings.
    String projectName = projectWidget_.getProjectName();
    String templateId = projectWidget_.getComponentType();
    
    //Find the client runtime that fits this profile best.
    return WebServiceRuntimeExtensionUtils2.getClientRuntimeId(runtimeWidget_.getTypeRuntimeServer(), projectName, templateId);
  } 
    
  public boolean getClientNeedEAR()
  {
    return projectWidget_.getNeedEAR();
  }
  
  public void setClientNeedEAR(boolean b)
  {
    projectWidget_.setNeedEAR(b);
  }  
  
  public String getClientProjectName()
  {
	return projectWidget_.getProjectName();  
  }
  
  public void setClientProjectName( String name )
  {
    projectWidget_.setProjectName( name );
  }
  
  public String getClientEarProjectName()
  {
    return projectWidget_.getEarProjectName();	  
  }
  
  public void setClientEarProjectName( String name )
  {
	projectWidget_.setEarProjectName( name );  
  }
  
  public String getClientComponentType()
  {
    return projectWidget_.getComponentType();
  }
  
  public void setClientComponentType( String type )
  {
	projectWidget_.setComponentType( type );  
  }
  
  /*
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
      // Temp remove listeners and event handling 
      //handleSetProjects(selectedClientTypeIdx);
      
    }
  }
  */


  private void handleUpdateProjectWidget()
  {
    projectWidget_.setTypeRuntimeServer(runtimeWidget_.getTypeRuntimeServer());    

    //Update the list of projects shown to the user.
    projectWidget_.refreshProjectItems();    
  }

  
  /*
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
	
    //projectWidget_.setProjectTypeId(clientType2Projects.getList().getSelection());
	projectWidget_.setComponentType(clientType2Projects.getList().getSelection());
    projectWidget_.setProjectChoices( clientType2Projects.getChoice() );    
  }
  */
  
  private String[] getClientTypeLabels( String[] types )
  {
	MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );	  
    //ClientProjectTypeRegistry registry         = ClientProjectTypeRegistry.getInstance();
    String[]                  clientTypeLabels = new String[types.length];
    
    for( int index = 0; index < types.length; index++ )
    {
      //clientTypeLabels[index] = registry.getElementById(types[index]).getAttribute("label");
	  String type = types[index];
	  if (type.equals(IModuleConstants.JST_WEB_MODULE))
	  {
		  clientTypeLabels[index] = msgUtils.getMessage("LABEL_CLIENT_COMP_TYPE_WEB");
	  }
	  else if (type.equals(IModuleConstants.JST_EJB_MODULE))
	  {
		  clientTypeLabels[index] = msgUtils.getMessage("LABEL_CLIENT_COMP_TYPE_EJB");
	  }
	  else if (type.equals(IModuleConstants.JST_APPCLIENT_MODULE))
	  {
		  clientTypeLabels[index] = msgUtils.getMessage("LABEL_CLIENT_COMP_TYPE_APP_CLIENT");
	  }
	  else if (type.equals(IModuleConstants.JST_UTILITY_MODULE))
	  {
		  clientTypeLabels[index] = msgUtils.getMessage("LABEL_CLIENT_COMP_TYPE_CONTAINERLESS");
	  }
	  else
	  {
		  clientTypeLabels[index] = type;
	  }
    }
    
    return clientTypeLabels;
  }
  
  public ProjectSelectionWidget getProjectSelectionWidget() {
    return this.projectWidget_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() 
  {
    //Return OK all the time for now.
    IStatus finalStatus   = Status.OK_STATUS;
    
    /*
    IStatus projectStatus = projectWidget_.getStatus();
    IStatus runtimeStatus = runtimeWidget_.getStatus();
    
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
          String clientEARName = projectWidget_.getEarProjectName();
          if (clientEARName == null){
        	  clientEARName = clientProjects.getChoice().getList().getSelection();
          }
          String clientProjName = projectWidget_.getProjectName();
          if (clientProjName == null){
            clientProjName = clientProjects.getList().getSelection();
          }
        
        String clientCompName = projectWidget_.getComponentName();
        String clientEARCompName = projectWidget_.getEarComponentName();

        //Validate that the selected client project is of the type indicated by client project type.
        IStatus clientProjectTypeStatus = valUtils.validateProjectType(clientProjName, runtime2ClientTypes_);
        if (clientProjectTypeStatus.getSeverity() == Status.ERROR)
        {
          finalStatus = clientProjectTypeStatus;
        }
        
        //Validate client side server targets and J2EE levels
        String clientServerFactoryId = getClientTypeRuntimeServer().getServerId();
        // rm j2ee
        String clientJ2EElevel = "14";
        IStatus clientProjectStatus = valUtils.validateProjectTargetAndJ2EE(clientProjName, clientCompName, clientEARName, clientEARCompName, clientServerFactoryId,
            clientJ2EElevel);
        if (clientProjectStatus.getSeverity() == Status.ERROR)
        {
          finalStatus = clientProjectStatus;
        }
      }
    }
    */
    return finalStatus;    
  }
}

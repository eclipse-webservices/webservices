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
package org.eclipse.jst.ws.internal.creation.ui.widgets.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.RuntimeServerSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;

/*
 *
 * ServerRuntimeSelectionWidget
 * This widget is the parent widget for server and client deployment/environment settings 
 * 
 */
public class ServerRuntimeSelectionWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private String createPluginId_ = "org.eclipse.jst.ws.creation.ui";
  
  /* CONTEXT_ID PWRS0002 for the Wizard Scenario Service configuration of the Runtime Selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE =  "PWRS0002";
  
  private RuntimeServerSelectionWidget runtimeWidget_;
  private ProjectSelectionWidget       projectWidget_;
  private ClientRuntimeSelectionWidget clientWidget_;
  private SelectionListChoices         serviceChoices_;
  private TextModifyListener           textListener_;
  private MessageUtils msgUtils_;
  
  private boolean isClientWidgetVisible_ = true;
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils_, createPluginId_ ); 
    
    Composite root = uiUtils.createComposite( parent, 1 );
    
    Text messageText = new Text( root, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY );
    
    GridData gd = new GridData(); 
    gd.horizontalIndent = 10;
    
    messageText.setLayoutData(gd);
    messageText.setText(
        msgUtils_.getMessage("MSG_GENERAL_PROJECT_AND_EAR", new String[] { msgUtils_.getMessage("MSG_SERVICE_SUB")})
        + "\n"
        + msgUtils_.getMessage("MSG_EAR_PROJECT_WILL_BE_CREATED"));
    
    Composite serverComp = uiUtils.createComposite( root, 1, 5, 0 );
    
    Group serverGroup = uiUtils.createGroup( serverComp, "LABEL_SELECTION_VIEW_TITLE",
                                             null, INFOPOP_PWRS_GROUP_SERVICE,
											 2, 5, 5);
    
    runtimeWidget_ = new RuntimeServerSelectionWidget( false );
    runtimeWidget_.addControls( serverGroup, statusListener );
    textListener_ = new TextModifyListener();
    runtimeWidget_.addModifyListener( textListener_ );
             
    projectWidget_ = new ProjectSelectionWidget();
    projectWidget_.addControls( serverGroup, statusListener );
    
    clientWidget_ = new ClientRuntimeSelectionWidget();
    clientWidget_.addControls( root, statusListener );
    
    return this;
  }
  
  //If generate proxy is not selected, don't show the client portion of the page.
  public void setGenerateProxy( Boolean value )
  {
    clientWidget_.setVisible( value.booleanValue() ); 
    isClientWidgetVisible_ = value.booleanValue();
  }
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  {
    return runtimeWidget_.getTypeRuntimeServer();  
  }
   
  public void setServiceTypeRuntimeServer( TypeRuntimeServer ids )
  {
    runtimeWidget_.removeModifyListener( textListener_ );
    runtimeWidget_.setTypeRuntimeServer( ids );  
    projectWidget_.setTypeRuntimeServer(ids);
    runtimeWidget_.addModifyListener( textListener_ );
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientWidget_.getClientTypeRuntimeServer();  
  }
   
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {
    clientWidget_.setClientTypeRuntimeServer( ids );  
  }
  
  public SelectionListChoices getServiceProject2EARProject()
  {
    return projectWidget_.getProjectChoices();
  }
  
  public void setServiceProject2EARProject(SelectionListChoices serviceProject2EARProject)
  {
    projectWidget_.setProjectChoices(serviceProject2EARProject);
  }
  
  public SelectionListChoices getRuntime2ClientTypes()
  {
    return clientWidget_.getRuntime2ClientTypes();
  }  
  
  public void setRuntime2ClientTypes(SelectionListChoices runtime2ClientTypes)
  {
    clientWidget_.setRuntime2ClientTypes(runtime2ClientTypes);
  }
  
  public String getServiceJ2EEVersion()
  {
    return runtimeWidget_.getJ2EEVersion();
  }
  
  public void setServiceJ2EEVersion(String j2eeVersion)
  {
    runtimeWidget_.setJ2EEVersion(j2eeVersion);
    projectWidget_.setJ2EEVersion(j2eeVersion);
  }
  
  public String getClientJ2EEVersion()
  {
    return clientWidget_.getJ2EEVersion();
  }
  
  public void setClientJ2EEVersion(String j2eeVersion)
  {
    clientWidget_.setJ2EEVersion(j2eeVersion);
  }
  
  public boolean getServiceNeedEAR()
  {
    return projectWidget_.getNeedEAR();
  }
  
  public void setServiceNeedEAR(boolean b)
  {
    projectWidget_.setNeedEAR(b);
  }
  
  public boolean getClientNeedEAR()
  {
    return clientWidget_.getClientNeedEAR();
  }
  
  public void setClientNeedEAR(boolean b)
  {
    clientWidget_.setClientNeedEAR(b);
  }  
  
  private class TextModifyListener implements ModifyListener 
  {
  	public void modifyText(ModifyEvent e)
  	{
		if( clientWidget_.isVisible() )
  		{  	  
		  TypeRuntimeServer serviceIds = runtimeWidget_.getTypeRuntimeServer();
  		  TypeRuntimeServer clientIds  = clientWidget_.getClientTypeRuntimeServer();
  		
  		  clientIds.setRuntimeId( serviceIds.getRuntimeId() );
  		  clientIds.setServerId( serviceIds.getServerId() );
  		  clientIds.setServerInstanceId( serviceIds.getServerInstanceId() );

  		  clientWidget_.setClientTypeRuntimeServer( clientIds );
  		  clientWidget_.setJ2EEVersion(runtimeWidget_.getJ2EEVersion());  	
  		}
		
		//Set the current server selection and J2EE level on the ProjectSelectionWidget
		projectWidget_.setTypeRuntimeServer(runtimeWidget_.getTypeRuntimeServer());
		projectWidget_.setJ2EEVersion(runtimeWidget_.getJ2EEVersion());
  	}
  }
 
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public Status getStatus() 
  {
    Status serviceStatus = runtimeWidget_.getStatus();
    Status clientStatus  = clientWidget_.getStatus();
    Status projectStatus = projectWidget_.getStatus();
    
    Status finalStatus   = new SimpleStatus( "" );
    
    // call child widgets' getStatus()
    if( serviceStatus.getSeverity() == Status.ERROR )
    {
      finalStatus = serviceStatus;
    }
    else if( clientStatus.getSeverity() == Status.ERROR )
    {
      if (isClientWidgetVisible_)
        finalStatus = clientStatus;
    }
    else if ( projectStatus.getSeverity()== Status.ERROR) {
      finalStatus = projectStatus;
    }
    
    //Validate service side server target and J2EE level
    SelectionListChoices serviceProjects = projectWidget_.getProjectChoices();
    String serviceEARName = null;
    String serviceProjName = null;
    String serviceServerFactoryId = null;
    ValidationUtils valUtils = new ValidationUtils();
    if (serviceProjects!=null)
    {
      serviceEARName  = serviceProjects.getChoice().getList().getSelection();
      serviceProjName = serviceProjects.getList().getSelection();
      serviceServerFactoryId = runtimeWidget_.getTypeRuntimeServer().getServerId();
      String serviceJ2EElevel = runtimeWidget_.getJ2EEVersion();
      Status serviceProjectStatus = valUtils.validateProjectTargetAndJ2EE(serviceProjName, serviceEARName, serviceServerFactoryId, serviceJ2EElevel);
      if(serviceProjectStatus.getSeverity()==Status.ERROR)
      {
        finalStatus = serviceProjectStatus;
      }
    }
    
    //Ensure the service project type (Web/EJB) is valid
    if (serviceProjName!=null && serviceProjName.length()>0)
    {
      IProject serviceProj = (IProject)((new StringToIProjectTransformer().transform(serviceProjName)));
      if (serviceProj.exists())
      {
        //Determine whether an EJB project is required
        String webServiceRuntimeId = runtimeWidget_.getTypeRuntimeServer().getRuntimeId();
        String webServiceTypeId = runtimeWidget_.getTypeRuntimeServer().getTypeId();
        WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
        String serverTypeId = wssrtRegistry.getWebServiceServerByFactoryId(serviceServerFactoryId).getId();
        boolean isEJBRequired = wssrtRegistry.requiresEJBModuleFor(serverTypeId, webServiceRuntimeId, webServiceTypeId);
        if (!isEJBRequired)
        {
          //Check the Web service type to see if an EJB project is required
          isEJBRequired = wssrtRegistry.requiresEJBProject(webServiceTypeId);
        }
        
        if (isEJBRequired && !ResourceUtils.isEJBProject(serviceProj))
        {
          finalStatus = new SimpleStatus("",msgUtils_.getMessage("MSG_INVALID_EJB_PROJECT",new String[]{serviceProjName}),Status.ERROR);          
        }
        if (!isEJBRequired && !ResourceUtils.isWebProject(serviceProj))
        {
          finalStatus = new SimpleStatus("",msgUtils_.getMessage("MSG_INVALID_WEB_PROJECT",new String[]{serviceProjName}),Status.ERROR);
        }
      }
    }
    
    if (isClientWidgetVisible_) 
    {
      SelectionListChoices clientProjects = clientWidget_.getProjectSelectionWidget().getProjectChoices();      
      if (clientProjects!=null && serviceProjects!=null) 
      {
        String clientEARName   = clientProjects.getChoice().getList().getSelection();
        String clientProjName  = clientProjects.getList().getSelection();

        // check same EAR-ness -----
        String warning_msg = getEARProjectWarningMessage(serviceEARName, clientEARName);
        
        if( clientProjName != null && serviceProjName != null && 
            clientProjName.equalsIgnoreCase( serviceProjName ))
        {
          if (finalStatus.getSeverity()!=Status.ERROR)
          {
            warning_msg = msgUtils_.getMessage( "MSG_SAME_CLIENT_AND_SERVICE_EARS", new String[]{ "WEB" } );
            finalStatus = new SimpleStatus( "", warning_msg, Status.WARNING );
          }
        }
        else if (warning_msg != null)
        {
          if (finalStatus.getSeverity()!=Status.ERROR)
          	return new SimpleStatus( "", warning_msg, Status.WARNING );          
        }         
      }      
    }
    
    // TODO: validate projects (i.e 1.2 vs 1.3 Web Projects) against servers
    
    
    
    return finalStatus;
  }
  
  private String getEARProjectWarningMessage(String serviceEARName, String clientEARName ) {

    // check if service and client share the same EAR
    if (serviceEARName!=null && clientEARName!=null) {
    
      if (clientEARName.equalsIgnoreCase(serviceEARName) && clientEARName.length()>0) {
        return msgUtils_.getMessage("MSG_SAME_CLIENT_AND_SERVICE_EARS", new String[]{ "EAR" });
      }
    }

    return null;

  }  
}

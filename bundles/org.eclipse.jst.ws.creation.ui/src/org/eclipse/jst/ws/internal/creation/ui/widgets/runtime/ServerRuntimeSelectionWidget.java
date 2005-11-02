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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.RuntimeServerSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;

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
  private TextModifyListener           textListener_;
  private MessageUtils msgUtils_;
  private String serviceRuntimeId_;
  
  private boolean isClientWidgetVisible_ = true;
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils_, createPluginId_ ); 
    
    ScrolledComposite scroller = new ScrolledComposite( parent, SWT.H_SCROLL | SWT.V_SCROLL );
    scroller.setExpandHorizontal(true);
    scroller.setExpandVertical(true);
    
    Composite root = uiUtils.createComposite( scroller, 1 );
    scroller.setContent( root );
    
    Composite textComposite = uiUtils.createComposite( root, 1, 0, 0 );
    createMessageText( textComposite, msgUtils_.getMessage("MSG_GENERAL_PROJECT_AND_EAR", new String[] { msgUtils_.getMessage("MSG_SERVICE_SUB")}));
    createMessageText( textComposite, msgUtils_.getMessage("MSG_EAR_PROJECT_WILL_BE_CREATED") );
    
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
    
    Point size = root.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    scroller.setMinSize( size ); 
    root.setSize( size );
     
    scroller.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    
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
  
  public void setServiceRuntimeId(String id)
  {
    serviceRuntimeId_ = id;
  }
  
  public String getServiceRuntimeId()
  {
    //calculate the most appropriate clientRuntimeId based on current settings.
    String projectName = projectWidget_.getProjectName();
    String templateId = projectWidget_.getComponentType();
    
    //Find the service runtime that fits this profile best.
    return WebServiceRuntimeExtensionUtils2.getServiceRuntimeId(runtimeWidget_.getTypeRuntimeServer(), projectName, templateId);    
  }  
  
  public void setClientRuntimeId(String id)
  {
    clientWidget_.setClientRuntimeId(id);
  }
  
  public String getClientRuntimeId()
  {
    //calculate the most appropriate clientRuntimeId based on current settings.
    return clientWidget_.getClientRuntimeId();
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
  
  public String getServiceComponentType()
  {
    return projectWidget_.getComponentType();
  }
  
  public void setServiceComponentType( String type )
  {
	projectWidget_.setComponentType( type );  
  }
  
  public String getClientComponentType()
  {
    return clientWidget_.getClientComponentType();
  }  
  
  public void setClientComponentType( String type )
  {
	clientWidget_.setClientComponentType( type );  
  }
  
  public String getServiceProjectName()
  {
    return projectWidget_.getProjectName();  
  }
  
  public void setServiceProjectName(String name)
  {
    projectWidget_.setProjectName(name);  
  }
  
  public String getServiceEarProjectName()
  {
	return projectWidget_.getEarProjectName();  
  }
  
  public void setServiceEarProjectName(String name)
  {
    projectWidget_.setEarProjectName(name);  
  }  
  
  public String getClientProjectName()
  {
    return clientWidget_.getClientProjectName();  
  }
  
  public void setClientProjectName(String name)
  {
    clientWidget_.setClientProjectName(name);  
  }  
  
  public String getClientEarProjectName()
  {
	return clientWidget_.getClientEarProjectName();  
  }
  
  public void setClientEarProjectName(String name)
  {
    clientWidget_.setClientEarProjectName(name);  
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
  		  //clientWidget_.setJ2EEVersion(runtimeWidget_.getJ2EEVersion());  	
  		}
		
		//Set the current server selection and J2EE level on the ProjectSelectionWidget
		projectWidget_.setTypeRuntimeServer(runtimeWidget_.getTypeRuntimeServer());
  	}
  }
 
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() 
  {
    IStatus serviceStatus = runtimeWidget_.getStatus();
    IStatus projectStatus = projectWidget_.getStatus();
    IStatus clientStatus  = clientWidget_.getStatus();    
    IStatus finalStatus   = Status.OK_STATUS;
    /*
    
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
	ValidationUtils valUtils = new ValidationUtils();
	String serviceEARName  = projectWidget_.getEarProjectName();
	String serviceProjName = projectWidget_.getProjectName();
	String serviceServerFactoryId = runtimeWidget_.getTypeRuntimeServer().getServerId();
	String serviceJ2EElevel = runtimeWidget_.getJ2EEVersion();
  String serviceComponentName = projectWidget_.getComponentName();
  String serviceEARComponentName = projectWidget_.getEarComponentName();
	IStatus serviceProjectStatus = valUtils.validateProjectTargetAndJ2EE(serviceProjName,serviceComponentName, serviceEARName, serviceEARComponentName, serviceServerFactoryId, serviceJ2EElevel);
	if(serviceProjectStatus.getSeverity()==Status.ERROR)
	{
		finalStatus = serviceProjectStatus;
	}
    
    //Ensure the service project type (Web/EJB) is valid
    if (serviceProjName!=null && serviceProjName.length()>0)
    {
      IProject serviceProj = ProjectUtilities.getProject(serviceProjName);
      if (serviceProj.exists())
      {
        if (serviceComponentName!=null && serviceComponentName.length()>0)
        {
          String compTypeId = J2EEUtils.getComponentTypeId(serviceProj);
          if (!compTypeId.equals(projectWidget_.getComponentType()))
          {
        	//Construct the error message
        	String compTypeLabel = getCompTypeLabel(projectWidget_.getComponentType()); 
        	finalStatus = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_INVALID_PROJECT_TYPE",new String[]{serviceProjName, compTypeLabel}) );        	        	
          }
        }
      }
    }
    
    if (isClientWidgetVisible_) 
    {
	    String clientEARName   = clientWidget_.getClientEarProjectName();
	    String clientProjName  = clientWidget_.getClientProjectName();
	
  		String clientComponentName = clientWidget_.getClientComponentName();
		
	    // check same EAR-ness -----
	    String warning_msg = getEARProjectWarningMessage(serviceEARName, clientEARName);
	    
		if (serviceComponentName.equalsIgnoreCase(clientComponentName)){
			  String err_msg = msgUtils_.getMessage( "MSG_SAME_CLIENT_AND_SERVICE_COMPONENTS", new String[]{ "WEB" } );
			  finalStatus = StatusUtils.errorStatus( err_msg );				
		}
		
	    if( clientProjName != null && serviceProjName != null && 
	        clientProjName.equalsIgnoreCase( serviceProjName ))
	    {
		  String error_msg = msgUtils_.getMessage("MSG_SAME_CLIENT_AND_SERVICE_PROJECTS");
		  finalStatus = StatusUtils.errorStatus( error_msg );
	    }
	    
		if (warning_msg != null)
	    {
	      if (finalStatus.getSeverity()!=Status.ERROR)
	      	return StatusUtils.warningStatus( warning_msg );          
	    }         
      
    }
    */
    return finalStatus;
  }
  
  private void createMessageText( Composite parent, String value )
  {
    Text     messageText = new Text( parent, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY );    
	GridData gridData    = new GridData( GridData.FILL_BOTH );
	
	gridData.horizontalIndent = 10;    
	messageText.setLayoutData( gridData );
	messageText.setText( value );
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
  
  //Returns a label corresponding to the componentTypeId. This is hard-coded for now.
  //This is something the flexible project framework should provide. Enhancement 106785 has been
  //opened.
  private String getCompTypeLabel(String typeId)
  {
	  if (typeId.equals(IModuleConstants.JST_WEB_MODULE))
	  {
		  return msgUtils_.getMessage("LABEL_CLIENT_COMP_TYPE_WEB");
	  }
	  else if (typeId.equals(IModuleConstants.JST_EJB_MODULE))
	  {
		  return msgUtils_.getMessage("LABEL_CLIENT_COMP_TYPE_EJB");
	  }
	  else if (typeId.equals(IModuleConstants.JST_APPCLIENT_MODULE))
	  {
		  return msgUtils_.getMessage("LABEL_CLIENT_COMP_TYPE_APP_CLIENT");
	  }
	  else if (typeId.equals(IModuleConstants.JST_UTILITY_MODULE))
	  {
		  return msgUtils_.getMessage("LABEL_CLIENT_COMP_TYPE_CONTAINERLESS");
	  }
	  else
	  {
		  //No known label, return the typeId itself. 
		  return typeId;
	  }	  
  }
}

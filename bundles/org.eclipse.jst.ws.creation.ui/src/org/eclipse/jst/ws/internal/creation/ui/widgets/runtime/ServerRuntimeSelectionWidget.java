/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner 
 * 20060221   100190 pmoogk@ca.ibm.com - Peter Moogk         
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.RuntimeServerSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
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
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

/*
 *
 * ServerRuntimeSelectionWidget
 * This widget is the parent widget for server and client deployment/environment settings 
 * 
 */
public class ServerRuntimeSelectionWidget extends SimpleWidgetDataContributor
{
  private String createPluginId_ = "org.eclipse.jst.ws.creation.ui";
  
  /* CONTEXT_ID PWRS0002 for the Wizard Scenario Service configuration of the Runtime Selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE =  "PWRS0002";
  
  private RuntimeServerSelectionWidget runtimeWidget_;
  private ProjectSelectionWidget       projectWidget_;
  private ClientRuntimeSelectionWidget clientWidget_;
  private TextModifyListener           textListener_;

  private boolean isClientWidgetVisible_ = true;
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    UIUtils      uiUtils  = new UIUtils( createPluginId_ ); 
    
    ScrolledComposite scroller = new ScrolledComposite( parent, SWT.H_SCROLL | SWT.V_SCROLL );
    scroller.setExpandHorizontal(true);
    scroller.setExpandVertical(true);
    
    Composite root = uiUtils.createComposite( scroller, 1 );
    scroller.setContent( root );
    
    Composite textComposite = uiUtils.createComposite( root, 1, 0, 0 );
    createMessageText( textComposite, NLS.bind(ConsumptionUIMessages.MSG_GENERAL_PROJECT_AND_EAR, new String[] {ConsumptionUIMessages.MSG_SERVICE_SUB}));
    createMessageText( textComposite, ConsumptionUIMessages.MSG_EAR_PROJECT_WILL_BE_CREATED );
    
    Composite serverComp = uiUtils.createComposite( root, 1, 5, 0 );
    
    Group serverGroup = uiUtils.createGroup( serverComp, ConsumptionUIMessages.LABEL_SELECTION_VIEW_TITLE,
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
    projectWidget_.refreshProjectItems();
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
	//TODO This method and any mappings to it
	// should be deleted if no longer required.
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
    //TODO This method and any mappings to it
	// should be deleted if no longer required.
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
  
  public void setInstallService(boolean b)
  {
    runtimeWidget_.setInstall(b);
  }  
  
  public void setInstallClient(boolean b)
  {
    clientWidget_.setInstallClient(b);
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
        projectWidget_.refreshProjectItems();
  	}
  }
 
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() 
  {
    IStatus serviceStatus = runtimeWidget_.getStatus();
    IStatus projectStatus = projectWidget_.getStatus();
    IStatus clientStatus  = isClientWidgetVisible_ ? clientWidget_.getStatus() : Status.OK_STATUS;    
    IStatus finalStatus   = Status.OK_STATUS;
    
    
    // call child widgets' getStatus()
    if( serviceStatus.getSeverity() == Status.ERROR )
    {
      finalStatus = serviceStatus;
    }
    else if( clientStatus.getSeverity() == Status.ERROR )
    {
      finalStatus = clientStatus;
    }
    else if ( projectStatus.getSeverity()== Status.ERROR) {
      finalStatus = projectStatus;
    }    
    
    String projectName = projectWidget_.getProjectName();
    if (projectName != null && projectName.length()>0)
    {
      //If the project exists, ensure that it is suitable for the selected runtime
      //and server.
      ValidationUtils valUtils = new ValidationUtils();
      IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
      String typeId = getServiceTypeRuntimeServer().getTypeId();
      String runtimeId = getServiceTypeRuntimeServer().getRuntimeId();
      String serverFactoryId = getServiceTypeRuntimeServer().getServerId();
      
      if (project.exists())
      {
        //Check if the runtime supports it.
        if (!WebServiceRuntimeExtensionUtils2.doesServiceTypeAndRuntimeSupportProject(typeId, runtimeId, projectName))
        {
          String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
          finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_PROJECT, new String[]{runtimeLabel, projectName}));
        }
        
        //Check if the server supports it.

        if (serverFactoryId!=null && serverFactoryId.length()>0)
        {
          if (!valUtils.doesServerSupportProject(serverFactoryId, projectName))
          {
            String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverFactoryId);
            finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_PROJECT, new String[]{serverLabel, projectName}));
          }
        }          
      }
      else
      {
        //Non-existing project is only permitted if there is a server selected.
        if (serverFactoryId==null || serverFactoryId.length()==0)
        {
          finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_PROJECT_MUST_EXIST, new String[]{projectName}));
        }
        else
        {
          // Look at the project type to ensure that it is suitable for the
          // selected runtime and server.
          String templateId = getServiceComponentType();

          if (templateId != null && templateId.length() > 0)
          {
            // Check if the runtime supports it.
            if (!WebServiceRuntimeExtensionUtils2.doesServiceTypeAndRuntimeSupportTemplate(typeId, runtimeId, templateId))
            {
              String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
              String templateLabel = FacetUtils.getTemplateLabelById(templateId);
              finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE,
                  new String[] { runtimeLabel, templateLabel }));
            }

            // Check if the server supports it.
            if (serverFactoryId != null && serverFactoryId.length() > 0)
            {
              if (!valUtils.doesServerSupportTemplate(serverFactoryId, templateId))
              {
                String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverFactoryId);
                String templateLabel = FacetUtils.getTemplateLabelById(templateId);
                finalStatus = StatusUtils.errorStatus(NLS.bind(
                    ConsumptionUIMessages.MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_TEMPLATE,
                    new String[] { serverLabel, templateLabel }));
              }
            }
          }
        }
        
        
      }
    }    

    if (isClientWidgetVisible_) 
    {
	    String clientEARName   = clientWidget_.getClientEarProjectName();
	    String clientProjName  = clientWidget_.getClientProjectName();
        String serviceProjName = projectWidget_.getProjectName();
	    String serviceEarName  = projectWidget_.getEarProjectName();
  		//String clientComponentName = clientWidget_.getClientComponentName();
		
	    // check same EAR-ness -----
	    String warning_msg = getEARProjectWarningMessage(serviceEarName, clientEARName);
	    
		//if (serviceComponentName.equalsIgnoreCase(clientComponentName)){
			  //String err_msg = NLS.bind(ConsumptionUIMessages.MSG_SAME_CLIENT_AND_SERVICE_COMPONENTS, new String[]{ "WEB" } );
			  //finalStatus = StatusUtils.errorStatus( err_msg );				
		//}
		
	    if( clientProjName != null && serviceProjName != null && 
	        clientProjName.equalsIgnoreCase( serviceProjName ))
	    {
		  String error_msg =ConsumptionUIMessages.MSG_SAME_CLIENT_AND_SERVICE_PROJECTS;
		  finalStatus = StatusUtils.errorStatus( error_msg );
	    }
	    
		if (warning_msg != null)
	    {
	      if (finalStatus.getSeverity()!=Status.ERROR)
	      	return StatusUtils.warningStatus( warning_msg );          
	    }         
      
    }

    //If finalStatus is still OK, check if there are any warnings.
    if (finalStatus.getSeverity()!=Status.ERROR)
    {
      if( serviceStatus.getSeverity() == Status.WARNING )
      {
        finalStatus = serviceStatus;
      }
      else if (clientStatus.getSeverity() == Status.WARNING)
      {
        finalStatus = serviceStatus;
      }        
    }

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
        return NLS.bind(ConsumptionUIMessages.MSG_SAME_CLIENT_AND_SERVICE_EARS, new String[]{ "EAR" });
      }
    }

    return null;

  }
}

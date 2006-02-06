/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class ClientRuntimeSelectionWidget extends SimpleWidgetDataContributor
{
  private String    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  
  /* CONTEXT_ID PWRS0003 for the Wizard Scenario Client configuration of the Runtime Selection Page */
  private String INFOPOP_PWRS_GROUP_CLIENT = pluginId_ + ".PWRS0003";
  
  private Group                        clientGroup_;
  private RuntimeServerSelectionWidget runtimeWidget_;
  private ProjectSelectionWidget       projectWidget_;
  private boolean 					           isVisible_;
  
  
  public WidgetDataEvents addControls( Composite parent, final Listener statusListener )
  {
    UIUtils      uiUtils  = new UIUtils( pluginId_ ); 
    
    clientGroup_ = uiUtils.createGroup( parent, ConsumptionUIMessages.LABEL_CLIENT_SELECTION_VIEW_TITLE,
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
  
  public void setInstallClient(boolean b)
  {
    runtimeWidget_.setInstall(b);
  }

  private void handleUpdateProjectWidget()
  {
    projectWidget_.setTypeRuntimeServer(runtimeWidget_.getTypeRuntimeServer());    

    //Update the list of projects shown to the user.
    projectWidget_.refreshProjectItems();    
  }
  
  public ProjectSelectionWidget getProjectSelectionWidget() {
    return this.projectWidget_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() 
  {
    //MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    ValidationUtils valUtils = new ValidationUtils();
    //Return OK all the time for now.
    IStatus finalStatus   = Status.OK_STATUS;
    
    
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
      String projectName = projectWidget_.getProjectName();
      if (projectName != null && projectName.length()>0)
      {
        //If the project exists, ensure that it is suitable for the selected runtime
        //and server.
        
        IProject project = ProjectUtilities.getProject(projectName);
        String typeId = getClientTypeRuntimeServer().getTypeId();
        String runtimeId = getClientTypeRuntimeServer().getRuntimeId();
        String serverFactoryId = getClientTypeRuntimeServer().getServerId();
        
        if (project.exists())
        {
          //Check if the runtime supports it.
          if (!WebServiceRuntimeExtensionUtils2.doesClientTypeAndRuntimeSupportProject(typeId, runtimeId, projectName))
          {
            String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
            finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_PROJECT, new String[]{runtimeLabel, projectName}));
          }
          
          //Check if the server supports it.

          if (serverFactoryId!=null && serverFactoryId.length()>0)
          {
            if (!valUtils.doesServerSupportProject(serverFactoryId, projectName))
            {
              String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverFactoryId);
              finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_PROJECT, new String[]{serverLabel, projectName}));
            }
          }          
        }
        else
        {
          //Look at the project type to ensure that it is suitable for the selected runtime
          //and server.
          
          String templateId = getClientComponentType();

          if (templateId != null && templateId.length()>0)
          {
            //Check if the runtime supports it.            
            if (!WebServiceRuntimeExtensionUtils2.doesClientTypeAndRuntimeSupportTemplate(typeId, runtimeId, templateId))
            {
              String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
              String templateLabel = FacetUtils.getTemplateLabelById(templateId);
              finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE, new String[]{runtimeLabel, templateLabel}));
            }
            
            //Check if the server supports it.
            if (serverFactoryId!=null && serverFactoryId.length()>0)
            {
              if (!valUtils.doesServerSupportTemplate(serverFactoryId, templateId))
              {
                String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverFactoryId);
                String templateLabel = FacetUtils.getTemplateLabelById(templateId);
                finalStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_TEMPLATE, new String[]{serverLabel, templateLabel}));
              }
            }
          }
          
          
          
        }
      }
      
      //If finalStatus is still OK, check if there are any warnings.
      if (finalStatus.getSeverity()!=Status.ERROR)
      {
        if( runtimeStatus.getSeverity() == Status.WARNING )
        {
          finalStatus = runtimeStatus;
        }
      }
    }
        
    return finalStatus;    
  }
}

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
 * 20060204 124143   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060221   122661 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060223   129020 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060510   140868 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060524   141024 joan@ca.ibm.com - Joan Haggarty
 * 20060829   145449 makandre@ca.ibm.com - Andrew Mak, Web service client project type dropdown disabled after selecting client project
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class ProjectSelectionWidget extends SimpleWidgetDataContributor {


  
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  
  //private SelectionListChoices projects_;
  
  private boolean needEAR_;
  
  private TypeRuntimeServer trsIds_;

  private Listener statusListener_;

  private Text messageText_;

  private boolean isClient_ = false;

  private byte CREATE_PROJECT = (byte) 1;

  private byte CREATE_EAR = (byte) 2;

  private byte ADD_EAR_ASSOCIATION = (byte) 4;
  
  private Composite parent_;
  private Combo moduleProject_;
  private Combo earProject_;
  private Combo projectType_;
  
  private String[] templates_;

  //private String   componentType_;
  
  private ModifyListener projectTypeListener_;
  private ModifyListener moduleProjectListener_;
  private ModifyListener earProjectListener_;

  /*
   * CONTEXT_ID PWRS0006 for the service-side Web project combo box of the
   * runtime selection Page
   */
  private String INFOPOP_PWRS_COMBO_PROJECT = pluginId_ + ".PWRS0006";

  //private Combo module_;

  /* CONTEXT_ID PWRS0018 for the client project type combo box of the runtime selection Page */
  private String INFOPOP_PWRS_COMBO_CLIENT_PROJECT_TYPE = pluginId_ + ".PWRS0018";
  
  /* CONTEXT_ID PWRS0024 for the service project type combo box of the runtime selection Page */
  private String INFOPOP_PWRS_COMBO_SERVICE_PROJECT_TYPE = pluginId_ + ".PWRS0018";    

  //private Combo earModule_;

  /*
   * Default Constructor
   */
  public ProjectSelectionWidget() {
  }

  /*
   * ProjectSelectionWidget @param isClient
   */
  public ProjectSelectionWidget(boolean isClient) {
    this.isClient_ = isClient;
  }
    
  public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
	  
    parent_ = parent;
    UIUtils uiUtils = new UIUtils(pluginId_);

    statusListener_ = statusListener;
		
    if (isClient_)
	{
	  moduleProject_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_CLIENT_PROJECT, ConsumptionUIMessages.LABEL_CLIENT_PROJECT, INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
      projectType_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_CLIENT_TYPE, ConsumptionUIMessages.TOOLTIP_PWCR_COMBO_CLIENT_TYPE, INFOPOP_PWRS_COMBO_CLIENT_PROJECT_TYPE, SWT.SINGLE | SWT.BORDER |SWT.READ_ONLY);      
	  earProject_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_CLIENT_EAR_PROJECT, ConsumptionUIMessages.LABEL_CLIENT_EAR_PROJECT, INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
    }
    else 
	{      
	  moduleProject_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_SERVICE_PROJECT, ConsumptionUIMessages.LABEL_SERVICE_PROJECT, INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
      projectType_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_SERVICE_TYPE, ConsumptionUIMessages.TOOLTIP_PWCR_COMBO_SERVICE_TYPE, INFOPOP_PWRS_COMBO_SERVICE_PROJECT_TYPE, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);      
	  earProject_ = uiUtils.createCombo(parent, ConsumptionUIMessages.LABEL_SERVICE_EAR_PROJECT, ConsumptionUIMessages.LABEL_SERVICE_EAR_PROJECT, INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
    }
    
    //Temporarily remove the listeners
    
    projectTypeListener_ = new ModifyListener()
    {
      public void modifyText(ModifyEvent evt) 
      {
        handleProjectTypeChanged();
        statusListener_.handleEvent( null );
      }
    };    
    
	moduleProjectListener_ = new ModifyListener()
	                         {
	                           public void modifyText(ModifyEvent evt) 
					           {
					             handleProjectChanged();
					             statusListener_.handleEvent( null );
		                       }
	                         };
    
	earProjectListener_ = new ModifyListener()
                          {
                            public void modifyText(ModifyEvent evt) 
				            {
				              statusListener_.handleEvent( null );
	                        }
                          };
  					  
    // message area
    messageText_ = uiUtils.createText(parent, ConsumptionUIMessages.LABEL_NO_LABEL, ConsumptionUIMessages.LABEL_NO_LABEL, null, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
    setEarProjectItems();
  
    return this;
  }
  
  private void listenersOn()
  {
	projectType_.addModifyListener(projectTypeListener_);
	moduleProject_.addModifyListener( moduleProjectListener_ );
	earProject_.addModifyListener( earProjectListener_ );
  }
  
  private void listenersOff()
  {
    projectType_.removeModifyListener(projectTypeListener_);
    moduleProject_.removeModifyListener( moduleProjectListener_ );
    earProject_.removeModifyListener( earProjectListener_ );  
  }
    
  public String getProjectName()
  {
	return moduleProject_.getText();
  }
  
  public void setProjectName( String name )
  {
	listenersOff();
    moduleProject_.setText( name );
    handleProjectChanged();
    updateEARState();
	listenersOn();
  }
  
  public String getEarProjectName()
  {
    return earProject_.getText();
  }
  
  public void setEarProjectName( String name )
  { 
	listenersOff();
	earProject_.setText( name );
	listenersOn();
  }
  
  public void setComponentType( String type )
  {
    listenersOff();
    if (type != null && type.length()>0)
    {
      String label = FacetUtils.getTemplateLabelById(type);
	  projectType_.setText(label);
    }
    else
      projectType_.setText("");
    
    handleProjectTypeChanged();
    listenersOn();
  }
  
  public String getComponentType()
  {
    int idx = projectType_.getSelectionIndex();
    if (templates_ != null && idx > -1)
    {
      return templates_[idx];
    }
    else
    {
      String templateLabel = projectType_.getText();
      String templateId = FacetUtils.getTemplateIdByLabel(templateLabel);
      return templateId;
    }

  }
  
  public boolean getNeedEAR()
  {
    return needEAR_;
  }
  
  public void setNeedEAR(boolean b)
  {
    listenersOff();
    needEAR_ = b;
    if (needEAR_)
    {
     earProject_.setEnabled(true);
     populateEARCombos();
    }
    else
    {
      earProject_.setEnabled(false);
      earProject_.setText("");
    }
    listenersOn();
  }    

  public void setTypeRuntimeServer(TypeRuntimeServer trs)
  {
    trsIds_ = trs;
    if (earProject_ != null)
    {
    	listenersOff();
        updateEARState();
        listenersOn();	
    }	
  }
  
  public void refreshProjectItems()
  {
    listenersOff();
    String selectedModuleProject = moduleProject_.getText();    
    String runtimeId = trsIds_.getRuntimeId();
    String typeId = trsIds_.getTypeId();
    
    //Get all the projects that are compatible with the type and runtime
    String[] projectNames = null;
    if (isClient_)
    {
      projectNames = WebServiceRuntimeExtensionUtils2.getProjectsForClientTypeAndRuntime(typeId, runtimeId);
    }
    else
    {
      projectNames = WebServiceRuntimeExtensionUtils2.getProjectsForServiceTypeAndRuntime(typeId, runtimeId);
    }
    
    moduleProject_.setItems(projectNames);
    moduleProject_.setText(selectedModuleProject);
    handleProjectChanged();
    updateEARState();
    listenersOn();
  }
  
  public void setEarProjectItems()
  {
    IVirtualComponent[] ears = J2EEUtils.getAllEARComponents();
    String[] earProjectNames = new String[ears.length];
    for (int i=0; i<earProjectNames.length; i++)
    {
      earProjectNames[i]=ears[i].getProject().getName();
    }
    earProject_.setItems(earProjectNames);
    
    if (earProjectNames.length > 0)
      earProject_.select(0);
  }
  
  private void handleProjectTypeChanged()
  {
    updateEARState();    
  }
  
  private void handleProjectChanged()
  { 
    updateTemplates(); 
  }
  
  private void updateTemplates()
  {
    String projectName = moduleProject_.getText();
    if (projectName != null && projectName.length()>0)
    {
      IProject project = ProjectUtilities.getProject(projectName);
      if (project.exists())
      {
        projectType_.setEnabled(false);
        projectType_.deselectAll();        
      }
      else
      {
        populateProjectTypeCombo();
        projectType_.setEnabled(true);
      }      
    }
  }
  
  private void populateProjectTypeCombo()
  {
    //Get the old value if there was one.
    String oldTemplateId = getComponentType();
    
    String[] templates = null;
    if (trsIds_ != null)
    {
      if (isClient_)
      {
        templates = WebServiceRuntimeExtensionUtils2.getClientProjectTemplates(trsIds_.getTypeId(), trsIds_.getRuntimeId());
      }
      else
      {
        templates = WebServiceRuntimeExtensionUtils2.getServiceProjectTemplates(trsIds_.getTypeId(), trsIds_.getRuntimeId());      
      }
    
      String[] templateLabels = FacetUtils.getTemplateLabels(templates);
      projectType_.setItems(templateLabels);
      templates_ = templates;
      
      if (templates.length > 0)
      {
      
        //Select the previous template selection if that one is in the list.
        int idx = getIndexOfTemplateId(templates, oldTemplateId);
        if (idx > -1)        
        {
          projectType_.select(idx);
        }
        else
        {
          // Select the preferred client project type.
          ProjectTopologyContext ptc = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
          String[] preferredTemplateIds = null;
          if (isClient_)
          {
            preferredTemplateIds = ptc.getClientTypes();
          }
          else
          {
            preferredTemplateIds = ptc.getServiceTypes();
          }
          
          boolean selected = false;
          outer: for (int j = 0; j < preferredTemplateIds.length; j++)
          {
            for (int i = 0; i < templates.length; i++)
            {
              String templateId = templates[i];
              if (templateId.equals(preferredTemplateIds[j]))
              {
                projectType_.select(i);
                selected = true;
                break outer;
              }
            }
          }

          if (!selected)
          {
            projectType_.select(0);
          }
        }
      }
    }
  }
  

  private int getIndexOfTemplateId(String[] templateIds, String templateId)
  {
    for (int i=0; i<templateIds.length; i++)
    {
      if (templateIds[i].equals(templateId))
      {
        return i;
      }
    }
    
    return -1;    
  }
  
  private void updateEARState()
  {
      if(!projectNeedsEAR(moduleProject_.getText()))
      {
	    earProject_.setEnabled(false);   
        earProject_.setText("");
        needEAR_ = false;
      }
      else
      {
        needEAR_ = true;
        earProject_.setEnabled(true);
        populateEARCombos();
      }
    
  }

  private void populateEARCombos()
  {
    earProject_.removeAll();    
    setEarProjectItems(); 
    
    String earName = DefaultingUtils.getDefaultEARProjectName(moduleProject_.getText());
    earProject_.setText(earName);
  }

  private boolean projectNeedsEAR(String projectName)
  {
    //If the project is a simple Java project or the project type is 
    //Java utility return false.
    if (projectName != null && projectName.length()>0)
    {
      IProject project = ProjectUtilities.getProject(projectName);
      if (project.exists())
      {
        if (FacetUtils.isJavaProject(project))
        {
          return false;
        }
      }
    }

    //Project didn't rule out the need for an EAR
    //so check the proect type
    String templateId = getComponentType();
    if (templateId != null && templateId.length()>0)
    {
      if (FacetUtils.isUtilityTemplate(templateId))
      {
        return false;
      }
    }
    
        
	//Project or project type didn't rule out the need for an EAR
    //so check the server type.
    if (trsIds_ != null && trsIds_.getServerId() != null)
    {
      String targetId = ServerUtils.getRuntimeTargetIdFromFactoryId(trsIds_.getServerId());
  	  if (targetId!=null && targetId.length()>0)
  	  {
  	    if (!ServerUtils.isTargetValidForEAR(targetId,"13")) //rm j2ee
  	    {
  	      return false;
  	    }
  	  }
    }
  	 
  	return true;    
  }
  
  
  private IStatus handleSetMessageText() {
    IStatus status = Status.OK_STATUS;
    try {
      byte result = (byte) 0;
      if (moduleProject_.getText().length() != 0 && earProject_.getText().length() != 0) {
        String projectText = moduleProject_.getText();
        String earText = earProject_.getText();
        IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectText);
        IProject ear = ResourceUtils.getWorkspaceRoot().getProject(earText);
        if (project != null) {
          if (!project.exists()) {
            result = (byte) (result | CREATE_PROJECT);
          }
          if (!ear.exists()) {
            result = (byte) (result | CREATE_EAR);
          }

          if (project.exists() && J2EEUtils.exists(project) && ear.exists() && J2EEUtils.exists(ear)) {
            if (!J2EEUtils.isComponentAssociated(ear, project)) result = (byte) (result | ADD_EAR_ASSOCIATION);
          }
        }
      }
      if (isClient_) {
        messageText_.setText(getValidationMessage(result, ConsumptionUIMessages.MSG_CLIENT_SUB));
      }
      else {
        messageText_.setText(getValidationMessage(result, ConsumptionUIMessages.MSG_SERVICE_SUB));
      }
      parent_.layout();  //force message area resize/wrap
    }
    catch (Exception e) {
      return StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_VALIDATION_INTERNAL_ERROR, e );
    }
    return status;
  }
  
  private String getValidationMessage(byte result, String serviceOrClient) {
    String msg = null;
    switch (result) {
    case 0:
      return "";
    case 1:
    case 5:
      msg = ConsumptionUIMessages.MSG_PROJECT_WILL_BE_CREATED;
      break;
    case 2:
    case 6:
      msg = ConsumptionUIMessages.MSG_EAR_WILL_BE_CREATED;
      break;
    case 3:
    case 7:
      msg = ConsumptionUIMessages.MSG_PROJECT_AND_EAR_CREATED;
      break;
    case 4:
      msg = ConsumptionUIMessages.MSG_EAR_WILL_BE_ASSOCIATED;
      break;
    }
    return msg != null ? NLS.bind(msg, new Object[] { serviceOrClient}) : "";
  }

  public IStatus getStatus() 
  {
    IStatus finalStatus = Status.OK_STATUS;
    handleSetMessageText();
    String projectText = moduleProject_.getText();
    String earText = earProject_.getText();
    
    if (projectText==null || projectText.length()==0)
    {
      if (isClient_)
        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_CLIENT_PROJECT_EMPTY, new String[]{""} ) );
      else
        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_SERVICE_PROJECT_EMPTY, new String[]{""} ) );
    }
    
    if (needEAR_ && (earText==null || earText.length()==0))
    {
      if (isClient_)
      {
        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_CLIENT_EAR_EMPTY, new String[]{""} ) );
      }
      else
      {
        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_SERVICE_EAR_EMPTY, new String[]{""} ) );
      }      
    }
    
    //If the project and EAR both exist and the project is not already associated with the EAR, ensure
    //they can be associated.
    if (needEAR_)
    {
      IProject p = ProjectUtilities.getProject(projectText.trim());
      IProject ep = ProjectUtilities.getProject(earText.trim());
      if (p.exists() && ep.exists())
      {
        if (!J2EEUtils.isComponentAssociated(ep,p))
        {
          IStatus associateStatus = J2EEUtils.canAssociateProjectToEAR(p, ep);
          if (associateStatus.getSeverity() == IStatus.ERROR)
          {
            if (isClient_)
            {
              return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_CLIENT_CANNOT_ASSOCIATE, new String[]{p.getName(), ep.getName(), associateStatus.getMessage()} ) );
            }
            else
            {
              return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_SERVICE_CANNOT_ASSOCIATE, new String[]{p.getName(), ep.getName(), associateStatus.getMessage()} ) );
            }                  
          }
        }
      }
    }
    
    return finalStatus;
  }
}

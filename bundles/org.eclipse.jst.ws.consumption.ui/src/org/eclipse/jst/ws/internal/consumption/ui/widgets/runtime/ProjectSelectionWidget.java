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

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionList;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.server.core.IRuntime;

public class ProjectSelectionWidget extends SimpleWidgetDataContributor {

  private final String EAR_PERMITTED_PROJECT_TYPE = "EAR_PERMITTED_PROJECT_TYPE";
  private final String JAVA_PROJECT_TYPE_ID = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Containerless";
  
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  private SelectionListChoices projects_;
  
  private boolean needEAR_;
  
  private TypeRuntimeServer trsIds_;
  
  private String j2eeVersion_;
  
  private String projectTypeId_ = EAR_PERMITTED_PROJECT_TYPE;

  private ModifyListener projectListener_;
  private ModifyListener earListener_;

  private Listener statusListener_;

  private Text messageText_;

  private boolean isClient_ = false;

  private MessageUtils msgUtils;

  private byte CREATE_PROJECT = (byte) 1;

  private byte CREATE_EAR = (byte) 2;

  private byte ADD_EAR_ASSOCIATION = (byte) 4;
  
  private Text moduleProject_;
  private Text earProject_;

  /*
   * CONTEXT_ID PWRS0006 for the service-side Web project combo box of the
   * runtime selection Page
   */
  private String INFOPOP_PWRS_COMBO_PROJECT = pluginId_ + ".PWRS0006";

  private Combo project_;

  /* CONTEXT_ID PWRS0012 for the EAR combo box of the runtime selection Page */
  private String INFOPOP_PWRS_COMBO_EAR = pluginId_ + ".PWRS0012";

  private Combo ear_;

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
    msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
    UIUtils uiUtils = new UIUtils(msgUtils, pluginId_);

    statusListener_ = statusListener;
    // project
    projectListener_ = new ModifyListener() 
                       {
                         public void modifyText(ModifyEvent evt) 
	                     {
                           handleSetEarProjects();
		                   updateModuleProject();
                          }
                       };
    
	earListener_ = new ModifyListener()
	               {
	                 public void modifyText(ModifyEvent evt) 
					 {
					   updateEarProject();
		             }
	               };
				   
	
    if (isClient_)
	{
	  moduleProject_ = uiUtils.createText(parent, "LABEL_CLIENT_PROJECT", "LABEL_CLIENT_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.READ_ONLY | SWT.BORDER );
      project_ = uiUtils.createCombo(parent, "LABEL_CLIENT_MODULE", "LABEL_CLIENT_MODULE", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER);
	  earProject_ = uiUtils.createText(parent, "LABEL_CLIENT_EAR_PROJECT", "LABEL_CLIENT_EAR_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.READ_ONLY | SWT.BORDER );
	  ear_ = uiUtils.createCombo(parent, "LABEL_CLIENT_EAR_MODULE", "LABEL_CLIENT_EAR_MODULE", INFOPOP_PWRS_COMBO_EAR, SWT.SINGLE | SWT.BORDER);
    }
    else 
	{
	  moduleProject_ = uiUtils.createText(parent, "LABEL_SERVICE_PROJECT", "LABEL_SERVICE_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.READ_ONLY | SWT.BORDER );
      project_ = uiUtils.createCombo(parent, "LABEL_SERVICE_MODULE", "LABEL_SERVICE_MODULE", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER);
	  earProject_ = uiUtils.createText(parent, "LABEL_SERVICE_EAR_PROJECT", "LABEL_SERVICE_EAR_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.READ_ONLY | SWT.BORDER );
	  ear_ = uiUtils.createCombo(parent, "LABEL_SERVICE_EAR_MODULE", "LABEL_SERVICE_EAR_MODULE", INFOPOP_PWRS_COMBO_EAR, SWT.SINGLE | SWT.BORDER);
    }
	
    project_.addModifyListener(projectListener_);
	
    ear_.addModifyListener(earListener_);
    ear_.addListener(SWT.Modify, statusListener);
	
    // message area
    messageText_ = uiUtils.createText(parent, "LABEL_NO_LABEL", "LABEL_NO_LABEL", null, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
    return this;
  }

  private void updateEarProject() 
  {
    String earModule = ear_.getText();
	String earProject = earModule; // Some J2ee call here.
	earProject_.setText( earProject );
  }

  private void updateModuleProject() 
  {
	String module        = project_.getText();
	String moduleProject = module; // Some J2ee call here.
	moduleProject_.setText( moduleProject );
  }

  public void setProjectChoices(SelectionListChoices projects) 
  {
    projects_ = projects;
    SelectionList projectList = projects_.getList();
    // We remove listener here so that modifications to the Combo will
    // NOT trigger a
    // modifcation event.
    project_.removeModifyListener(projectListener_);
    project_.removeListener(SWT.Modify, statusListener_);
    project_.setItems(projectList.getList());
    project_.setText(projectList.getSelection());
    project_.addModifyListener(projectListener_);
    project_.addListener(SWT.Modify, statusListener_);

    handleSetEarProjects();
    updateEarProject();
	updateModuleProject();
  }

  public SelectionListChoices getProjectChoices() {
    if (projects_ != null) {
      SelectionList projectList = projects_.getList();
      projectList.setSelectionValue(project_.getText());
      SelectionListChoices ears = projects_.getChoice();
      if (ears != null) ears.getList().setSelectionValue(ear_.getText());
    }
    return projects_;
  }
  
  public boolean getNeedEAR()
  {
    return needEAR_;
  }
  
  public void setNeedEAR(boolean b)
  {
    needEAR_ = b;
  }    

  
  public void setTypeRuntimeServer(TypeRuntimeServer trs)
  {
    trsIds_ = trs;
    updateEAREnabledState();
    
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
    j2eeVersion_ = j2eeVersion;
    updateEAREnabledState();
  }
  
  public void setProjectTypeId(String id)
  {
    projectTypeId_ = id;
  }
  
  private void handleSetEarProjects() {
    SelectionList projectList = projects_.getList();
    projectList.setSelectionValue(project_.getText());
    SelectionList earList = projects_.getChoice().getList();
    ear_.removeListener(SWT.Modify, statusListener_);
    ear_.setItems(earList.getList());
    ear_.setText(earList.getSelection());
    ear_.addListener(SWT.Modify, statusListener_);
    updateEAREnabledState();
    handleSetMessageText();
  }

  private void updateEAREnabledState()
  {
    ear_.removeListener(SWT.Modify, statusListener_);
    if (projects_ != null)
    {
      SelectionList earList = projects_.getChoice().getList();
      if(!projectNeedsEAR(project_.getText()))
      {
        earList.setIndex(-1);        
        ear_.setText("");
        needEAR_ = false;
        ear_.setEnabled(false);        
      }
      else
      {
        needEAR_ = true;
        ear_.setEnabled(true);
      }
    }
    ear_.addListener(SWT.Modify, statusListener_);
  }

  private boolean projectNeedsEAR(String projectName)
  {
    if (projectTypeId_.equals(JAVA_PROJECT_TYPE_ID))
      return false;
    
    if (projectName == null || projectName.length()==0)
      return true;
    
    //IProject project = (IProject)((new StringToIProjectTransformer()).transform(projectName));
	IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
  	if (project != null && project.exists())
  	{
  	  //Get the runtime target on the project
  	  IRuntime target = ServerSelectionUtils.getRuntimeTarget(projectName);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(project));
  	  if (target != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(target.getRuntimeType().getId(),j2eeVersion))
  	  	{
          return false;
  	  	}
  	  		
  	  }
  	}
  	else
  	{
  		//Use the server type
  	    if (trsIds_ != null)
  	    {
  		  String targetId = ServerUtils.getRuntimeTargetIdFromFactoryId(trsIds_.getServerId());
  		  if (targetId!=null && targetId.length()>0)
  		  {
  		    if (!ServerUtils.isTargetValidForEAR(targetId,j2eeVersion_))
  		    {
  		      return false;
  	  	    }
  		  }
  	    }
  	}      	
  	
  	return true;    
  }
  
  private Status handleSetMessageText() {
    Status status = new SimpleStatus("");
    try {
      byte result = (byte) 0;
      if (project_.getText().length() != 0 && ear_.getText().length() != 0) {
        String projectText = project_.getText();
        String earText = ear_.getText();
        IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectText);
        IProject ear = ResourceUtils.getWorkspaceRoot().getProject(earText);
        if (project != null) {
          if (!project.exists()) {
            result = (byte) (result | CREATE_PROJECT);
          }
          if (!ear.exists()) {
            result = (byte) (result | CREATE_EAR);
          }
          if (project.exists() && ear.exists()) {
            if (!J2EEUtils.isEARAssociated(project, ear)) result = (byte) (result | ADD_EAR_ASSOCIATION);
          }
        }
      }
      if (isClient_) {
        messageText_.setText(getValidationMessage(result, msgUtils.getMessage("MSG_CLIENT_SUB")));
      }
      else {
        messageText_.setText(getValidationMessage(result, msgUtils.getMessage("MSG_SERVICE_SUB")));
      }
    }
    catch (Exception e) {
      return new SimpleStatus("", msgUtils.getMessage("PAGE_MSG_VALIDATION_INTERNAL_ERROR"), Status.ERROR);
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
      msg = "MSG_PROJECT_WILL_BE_CREATED";
      break;
    case 2:
    case 6:
      msg = "MSG_EAR_WILL_BE_CREATED";
      break;
    case 3:
    case 7:
      msg = "MSG_PROJECT_AND_EAR_CREATED";
      break;
    case 4:
      msg = "MSG_EAR_WILL_BE_ASSOCIATED";
      break;
    }
    return msg != null ? msgUtils.getMessage(msg, new Object[] { serviceOrClient}) : "";
  }

  public Status getStatus() {
    Status finalStatus = new SimpleStatus("");
    handleSetMessageText();
    String projectText = project_.getText();
    String earText = ear_.getText();
    if (projectText==null || projectText.length()==0)
    {
      if (isClient_)
        return new SimpleStatus("",msgUtils.getMessage("MSG_CLIENT_PROJECT_EMPTY"),Status.ERROR);
      else
        return new SimpleStatus("",msgUtils.getMessage("MSG_SERVICE_PROJECT_EMPTY"),Status.ERROR);
    }
    
    if (needEAR_ && (earText==null || earText.length()==0))
    {
      if (isClient_)
        return new SimpleStatus("",msgUtils.getMessage("MSG_CLIENT_EAR_EMPTY"),Status.ERROR);
      else
        return new SimpleStatus("",msgUtils.getMessage("MSG_SERVICE_EAR_EMPTY"),Status.ERROR);      
    }
    
    return finalStatus;
  }

}
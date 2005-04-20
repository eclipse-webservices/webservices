package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.application.internal.operations.EARComponentCreationOperation;
import org.eclipse.jst.j2ee.application.internal.operations.FlexibleJavaProjectCreationDataModel;
import org.eclipse.jst.j2ee.application.internal.operations.FlexibleProjectCreationDataModel;
import org.eclipse.jst.j2ee.application.internal.operations.FlexibleProjectCreationOperation;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.AppClientComponentCreationDataModel;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.AppClientComponentCreationOperation;
import org.eclipse.jst.j2ee.internal.earcreation.EARComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EjbComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EjbComponentCreationOperation;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentCreationOperation;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.frameworks.internal.operations.WTPOperation;

public class CreateModuleCommand extends SimpleCommand
{
	
	public final static int WEB = J2EEUtils.WEB;
	public final static int EJB = J2EEUtils.EJB;
	public final static int APPCLIENT = J2EEUtils.APPCLIENT;
	public final static int EAR = J2EEUtils.EAR;
	
	private String   projectName;
	private String   moduleName;
	private int      moduleType;;
	private String   j2eeLevel;
	private String   serverFactoryId;
	private Environment env;
	
	private MessageUtils msgUtils;
	
	public CreateModuleCommand(){
		msgUtils = new MessageUtils(WebServiceConsumptionPlugin.ID + ".plugin", this);
	}
	
	public Status execute(Environment env)
	{
		this.env = env;
		Status status = new SimpleStatus("");
		
		// check if data ready
		status = checkDataReady();
		if (status.getSeverity()==Status.ERROR){
			return status;
		}	
		
		// check if flexible project exists
		IProject project = ProjectUtilities.getProject(projectName);
		if (project==null || !project.exists()){
			status = createFlexibleJavaProject();
			if (status.getSeverity()==Status.ERROR){
				return status;
			}			
		}

		// check if component exists
		if (projectName!=null && moduleName!=null){
			if (J2EEUtils.exists(projectName, moduleName))
				return status;
		}
		else {
			return new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_COMPONENT_CREATION", new String[]{projectName, moduleName}),Status.ERROR, null);
		}
		
		// create the component according to the component type specified
		int type = getModuleType();
		switch (type) {
		case WEB:
			status = createWebComponent();
			break;
		case EJB:
			status = createEJBComponent();
			break;
		case APPCLIENT:
			status = createAppClientComponent();
			break;
		case EAR:
			status = createEARComponent();
			break;

		default:
			// determine an error status message?
			break;
		}
		return status;
	}

	private Status checkDataReady(){
		
		if (projectName==null || moduleName==null ||
				serverFactoryId==null){
			return new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_COMPONENT_CREATION", new String[]{projectName, moduleName}),Status.ERROR,null);
		}
		
		return new SimpleStatus("");
	}
	
	
	/**
	 * Create a Web component
	 * @return
	 */
	public Status createWebComponent(){
		Status status = new SimpleStatus("");
		try
		{
		  WebComponentCreationDataModel projectInfo = new WebComponentCreationDataModel();
		  projectInfo.setProperty(WebComponentCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(WebComponentCreationDataModel.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(WebComponentCreationDataModel.J2EE_VERSION, Integer.valueOf(j2eeLevel));
		  WebComponentCreationOperation op = new WebComponentCreationOperation(projectInfo);
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",e.getMessage(),Status.ERROR,e);
		}
		return status;		
	}
	
	/**
	 * Create an EAR component
	 * @return
	 */
	public Status createEARComponent(){
		Status status = new SimpleStatus("");
		try
		{
		  EARComponentCreationDataModel projectInfo = new EARComponentCreationDataModel();
		  projectInfo.setProperty(EARComponentCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(EARComponentCreationDataModel.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(EARComponentCreationDataModel.J2EE_VERSION, Integer.valueOf(j2eeLevel));
		  EARComponentCreationOperation op = new EARComponentCreationOperation(projectInfo);
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",e.getMessage(),Status.ERROR,e);
		}
		return status;				
	}
	
	/**
	 * Create an EJB Component
	 * @return
	 */
	public Status createEJBComponent(){
		Status status = new SimpleStatus("");
		try
		{
		  EjbComponentCreationDataModel projectInfo = new EjbComponentCreationDataModel();
		  projectInfo.setProperty(EjbComponentCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(EjbComponentCreationDataModel.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(EjbComponentCreationDataModel.J2EE_VERSION, Integer.valueOf(j2eeLevel));
		  EjbComponentCreationOperation op = new EjbComponentCreationOperation(projectInfo);
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_CREATE_EJB_COMPONENT", new String[]{projectName}),Status.ERROR,e);
		}
		return status;		
	}
	
	/**
	 * Create an Application Client component
	 * @return
	 */
	public Status createAppClientComponent(){
		Status status = new SimpleStatus("");
		try
		{
		  AppClientComponentCreationDataModel projectInfo = new AppClientComponentCreationDataModel();
		  projectInfo.setProperty(AppClientComponentCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(AppClientComponentCreationDataModel.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)		  
			  projectInfo.setProperty(AppClientComponentCreationDataModel.J2EE_VERSION, Integer.valueOf(j2eeLevel));
		  AppClientComponentCreationOperation op = new AppClientComponentCreationOperation(projectInfo);
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_CREATE_APPCIENT_PROJET", new String[]{projectName}),Status.ERROR,e);
		}
		return status;		
	}
	
	/**
	 * Creates Flexible Project structure
	 * This project is required if it doesn't already exist in order to create the component 
	 * @return
	 * 
	 * Note: This call may not be necessary once J2EE implements creating a flex project automatically
	 * 		with the creation of components.
	 */
	public Status createFlexibleProject(){
		Status status = new SimpleStatus("");
		try
		{
		  FlexibleProjectCreationDataModel projectInfo = new FlexibleProjectCreationDataModel();
		  projectInfo.setProperty(FlexibleProjectCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(FlexibleProjectCreationDataModel.SERVER_TARGET_ID,serverFactoryId);
		  projectInfo.setProperty(FlexibleProjectCreationDataModel.ADD_SERVER_TARGET,Boolean.TRUE);
		  FlexibleProjectCreationOperation op = new FlexibleProjectCreationOperation(projectInfo);
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_CREATE_FLEX_PROJET", new String[]{projectName}),Status.ERROR,e);
		}
		return status;		
	}
	
	/**
	 * Creates Flexible Java Project structure
	 * This project is required if it doesn't already exist in order to create the component 
	 * @return
	 * 
	 * Note: This call may not be necessary once J2EE implements creating a flex project automatically
	 * 		with the creation of components.
	 */
	public Status createFlexibleJavaProject(){
		Status status = new SimpleStatus("");
		try
		{
		  FlexibleJavaProjectCreationDataModel projectInfo = new FlexibleJavaProjectCreationDataModel();
		  projectInfo.setProperty(FlexibleJavaProjectCreationDataModel.PROJECT_NAME,projectName);
		  projectInfo.setProperty(FlexibleJavaProjectCreationDataModel.SERVER_TARGET_ID,serverFactoryId);
		  projectInfo.setProperty(FlexibleJavaProjectCreationDataModel.ADD_SERVER_TARGET,Boolean.TRUE);
		  WTPOperation op = projectInfo.getDefaultOperation();
		  if (env!=null)
			  op.run(EnvironmentUtils.getIProgressMonitor(env));
		  else 
			  op.run(new NullProgressMonitor());

		}
		catch (Exception e)
		{
			status = new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_CREATE_FLEX_PROJET", new String[]{projectName}),Status.ERROR,e);
		}
		return status;		
	}
	
	
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}	

	public int getModuleType()
	{
		return moduleType;
	}

	public void setModuleType(int moduleType)
	{
		this.moduleType = moduleType;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * Expecting 12,13,14 etc.
	 * @param level
	 */
	public void setJ2eeLevel(String level)
	{
		if (level !=null && level.indexOf(".")!=-1)
			j2eeLevel = J2EEUtils.getJ2EEIntVersionAsString(level);
		else
			j2eeLevel = level;
	}

	public void setServerFactoryId(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}

}

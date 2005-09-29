package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.application.internal.operations.FlexibleJavaProjectCreationDataModelProvider;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.AppClientComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.datamodel.properties.IAppClientComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.datamodel.properties.IEarComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.ejb.datamodel.properties.IEjbComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.internal.earcreation.EarComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EjbComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.project.datamodel.properties.IFlexibleJavaProjectCreationDataModelProperties;
import org.eclipse.jst.j2ee.web.datamodel.properties.IWebComponentCreationDataModelProperties;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.datamodel.properties.IComponentCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class CreateModuleCommand extends EnvironmentalOperation
{
	
	public final static int WEB = J2EEUtils.WEB;
	public final static int EJB = J2EEUtils.EJB;
	public final static int APPCLIENT = J2EEUtils.APPCLIENT;
	public final static int EAR = J2EEUtils.EAR;
	
	private String   projectName;
	private String   moduleName;  // may be null for non-flexible project
	private int      moduleType;;
	private String   j2eeLevel;
	private String   serverFactoryId;
	private String   serverInstanceId_;
	private Environment env;
	private boolean supportMultipleModules;
  private IProgressMonitor monitor_;
	
	private MessageUtils msgUtils;
	
	public CreateModuleCommand(){
		msgUtils = new MessageUtils(WebServiceConsumptionPlugin.ID + ".plugin", this);
	}
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    monitor_ = monitor;
		this.env = getEnvironment();
		IStatus status = Status.OK_STATUS;
		
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

		// check if project and/or component exists
        if (projectName!=null) {
          if (moduleName==null){
            if (project.exists())
              return status;
          }
          else {
			if (J2EEUtils.exists(projectName, moduleName))
				return status;
          }
        }
		else {
			return StatusUtils.errorStatus(msgUtils.getMessage("MSG_ERROR_COMPONENT_CREATION", new String[]{projectName, moduleName}) );
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
			return StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_COMPONENT_CREATION", new String[]{moduleName}) );			
		}
		
	
		return status;
	}

	private IStatus checkDataReady(){
		
		if (projectName==null || serverFactoryId==null){
			return StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_COMPONENT_CREATION", new String[]{projectName, moduleName}) );
		}
		
		return Status.OK_STATUS;
	}
	
	
	/**
	 * Create a Web component
	 * @return
	 */
	public IStatus createWebComponent(){
		IStatus status = Status.OK_STATUS;
		try
		{
		  IDataModel projectInfo = DataModelFactory.createDataModel(new WebComponentCreationDataModelProvider());

		  if (supportMultipleModules){
			  projectInfo.setProperty(IComponentCreationDataModelProperties.SUPPORT_MULTIPLE_MODULES, Boolean.TRUE);
		  }		  
		  
		  projectInfo.setProperty(IWebComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)
            projectInfo.setProperty(IWebComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
          
          // get the Web servlet level
          Integer servletLevel = J2EEUtils.getServletVersionForJ2EEVersion(j2eeLevel);
          
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IWebComponentCreationDataModelProperties.COMPONENT_VERSION, servletLevel);
		  IDataModelOperation op = projectInfo.getDefaultOperation();
			op.execute(monitor_, null);
		}
		catch (Exception e)
		{
			status = StatusUtils.errorStatus( e );
		}
		return status;		
	}
	
	/**
	 * Create an EAR component
	 * @return
	 */
	public IStatus createEARComponent(){
		IStatus status = Status.OK_STATUS;
		try
		{
		  IDataModel projectInfo = DataModelFactory.createDataModel(new EarComponentCreationDataModelProvider());
		  if (supportMultipleModules){
			  projectInfo.setProperty(IComponentCreationDataModelProperties.SUPPORT_MULTIPLE_MODULES, Boolean.TRUE);
		  }
		  projectInfo.setProperty(IEarComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)
              projectInfo.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  IDataModelOperation op =projectInfo.getDefaultOperation();
			op.execute(monitor_, null);
		}
		catch (Exception e)
		{
			status = StatusUtils.errorStatus( e );
		}
		return status;				
	}
	
	/**
	 * Create an EJB Component
	 * @return
	 */
	public IStatus createEJBComponent(){
		IStatus status = Status.OK_STATUS;
    
		try
		{
		  IDataModel projectInfo = DataModelFactory.createDataModel(new EjbComponentCreationDataModelProvider());
		  if (supportMultipleModules){
			  projectInfo.setProperty(IComponentCreationDataModelProperties.SUPPORT_MULTIPLE_MODULES, Boolean.TRUE);
		  }
		  projectInfo.setProperty(IEjbComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)  
              projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
          
          // get the EJB spec level
          Integer ejbLevel = J2EEUtils.getEJBVersionForJ2EEVersion(j2eeLevel);
         
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, ejbLevel);
		  
		  //Don't create an EAR. The ADD_TO_EAR property gets defaulted to TRUE for everything except Web projects.
		  projectInfo.setProperty(IEjbComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.FALSE);
		  
		  IDataModelOperation op = projectInfo.getDefaultOperation();
			op.execute( monitor_, null);
		}
		catch (Exception e)
		{
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_EJB_COMPONENT", new String[]{projectName}), e);
		}
		return status;		
	}
	
	/**
	 * Create an Application Client component
	 * @return
	 */
	public IStatus createAppClientComponent()
  {
		IStatus status = Status.OK_STATUS;
		try
		{
		  IDataModel projectInfo = DataModelFactory.createDataModel(new AppClientComponentCreationDataModelProvider());
		  if (supportMultipleModules){
			  projectInfo.setProperty(IComponentCreationDataModelProperties.SUPPORT_MULTIPLE_MODULES, Boolean.TRUE);
		  }
		  projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)      
		      projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)		  
			  projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  
		  //Don't create an EAR. The ADD_TO_EAR property gets defaulted to TRUE for everything except Web projects.
		  projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.FALSE);
		  
		  IDataModelOperation op = projectInfo.getDefaultOperation();

			op.execute( monitor_, null);
		}
		catch (Exception e)
		{
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_APPCIENT_PROJET", new String[]{projectName}), e);
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
	public IStatus createFlexibleJavaProject(){
		IStatus status = Status.OK_STATUS;
		try
		{
		  IDataModel projectInfo = DataModelFactory.createDataModel(new FlexibleJavaProjectCreationDataModelProvider());
		  projectInfo.setProperty(IFlexibleJavaProjectCreationDataModelProperties.PROJECT_NAME,projectName);
  
		  String runtimeTargetId = null;
		  if( serverInstanceId_ == null )
		  {
			// We don't have a server instance so we will get the first runtimeTarget from the factory ID.
			runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId, ServerUtils.getServerTargetModuleType(moduleType), j2eeLevel);		
		  }
		  else
		  {
			// We have a server instance so we will just get it's runtimeTargetId.
			IServer server = ServerCore.findServer( serverInstanceId_ );
			runtimeTargetId = server.getRuntime().getId();  
		  }
		  
		  projectInfo.setProperty(IFlexibleJavaProjectCreationDataModelProperties.RUNTIME_TARGET_ID,runtimeTargetId);
		  projectInfo.setProperty(IFlexibleJavaProjectCreationDataModelProperties.ADD_SERVER_TARGET,Boolean.TRUE);
		  IDataModelOperation op = projectInfo.getDefaultOperation();
		  if (env!=null)
			  op.execute( monitor_, null);
		  else 
			  op.execute(new NullProgressMonitor(), null);

		}
		catch (Exception e)
		{
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_FLEX_PROJET", new String[]{projectName}), e);
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
	
	public void setServerInstanceId( String serverInstanceId )
	{
	  serverInstanceId_ = serverInstanceId;
	}

	public boolean getSupportMultipleModules() {
		return supportMultipleModules;
	}

	public void setSupportMultipleModules(boolean supportMultipleModules) {
		this.supportMultipleModules = supportMultipleModules;
	}

}

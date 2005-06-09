package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.AppClientComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.datamodel.properties.IAppClientComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.datamodel.properties.IEarComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.ejb.datamodel.properties.IEjbComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.internal.earcreation.EarComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EjbComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.web.datamodel.properties.IWebComponentCreationDataModelProperties;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

public class CreateModuleCommand extends SimpleCommand
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

		// check if project and/or component exists
        if (projectName!=null) {
          if (moduleName==null){
            IProject project = ProjectUtilities.getProject(projectName);
            if (project.exists())
              return status;
          }
          else {
			if (J2EEUtils.exists(projectName, moduleName))
				return status;
          }
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
		
		if (projectName==null || serverFactoryId==null){
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
		  IDataModel projectInfo = DataModelFactory.createDataModel(new WebComponentCreationDataModelProvider());
		  projectInfo.setProperty(IWebComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)
            projectInfo.setProperty(IWebComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IWebComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  IDataModelOperation op = projectInfo.getDefaultOperation();
		  if (env!=null)
			  op.execute(EnvironmentUtils.getIProgressMonitor(env), null);
		  else 
			  op.execute(new NullProgressMonitor(), null);
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
		  IDataModel projectInfo = DataModelFactory.createDataModel(new EarComponentCreationDataModelProvider());
		  projectInfo.setProperty(IEarComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)
              projectInfo.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  IDataModelOperation op =projectInfo.getDefaultOperation();
		  if (env!=null)
			  op.execute(EnvironmentUtils.getIProgressMonitor(env), null);
		  else 
			  op.execute(new NullProgressMonitor(), null);
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
		  IDataModel projectInfo = DataModelFactory.createDataModel(new EjbComponentCreationDataModelProvider());
		  projectInfo.setProperty(IEjbComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)  
              projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)
			  projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  IDataModelOperation op = projectInfo.getDefaultOperation();
		  if (env!=null)
			  op.execute(EnvironmentUtils.getIProgressMonitor(env), null);
		  else 
			  op.execute(new NullProgressMonitor(), null);
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
		  IDataModel projectInfo = DataModelFactory.createDataModel(new AppClientComponentCreationDataModelProvider());
		  projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.PROJECT_NAME,projectName);
          if (moduleName!=null)      
		      projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_NAME, moduleName);
		  if (j2eeLevel!=null)		  
			  projectInfo.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_VERSION, Integer.valueOf(j2eeLevel));
		  IDataModelOperation op = projectInfo.getDefaultOperation();

		  if (env!=null)
			  op.execute(EnvironmentUtils.getIProgressMonitor(env), null);
		  else 
			  op.execute(new NullProgressMonitor(), null);
		}
		catch (Exception e)
		{
			status = new SimpleStatus("",msgUtils.getMessage("MSG_ERROR_CREATE_APPCIENT_PROJET", new String[]{projectName}),Status.ERROR,e);
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

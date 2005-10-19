package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.InputFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.MethodFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.ResultFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.TestClientFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.GeneratePageCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.JavaToModelCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.CopyWebServiceUtilsJarCommand;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.EnvironmentUtils;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class GSTCGenerateCommand extends AbstractDataModelOperation 
{

  public static String INPUT       = "Input.jsp";
  public static String TEST_CLIENT = "TestClient.jsp";
  public static String RESULT      = "Result.jsp";
  public static String METHOD      = "Method.jsp";
	
  private TestInfo testInfo;
  private Model proxyModel;
  private String jspfolder;
  
  public GSTCGenerateCommand(TestInfo testInfo){
  	this.testInfo = testInfo;
  }
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    IStatus status = Status.OK_STATUS;
	CopyWebServiceUtilsJarCommand copy = new CopyWebServiceUtilsJarCommand();    
	copy.setSampleProject(testInfo.getGenerationProject());
    copy.setEnvironment( env );
	status = copy.execute( monitor, null);
	if (status.getSeverity() == Status.ERROR) return status;
	setJSPFolder();
	status = createModel(env, monitor);
	if (status.getSeverity() == Status.ERROR) return status;
	status = generatePages(env);
	if (status.getSeverity() == Status.ERROR) return status;
	return status;   
  }

  private void setJSPFolder(){
    //if the client is not a webcomponent then the 
	//sample must have been created, we must now factor in 
	//flexible projects  
	  
	IProject clientIProject = ProjectUtilities.getProject(testInfo.getClientProject());
    if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject)){   
	  IProject project = ProjectUtilities.getProject(testInfo.getGenerationProject());
	  IPath path = J2EEUtils.getWebContentPath(project);
	  int index = testInfo.getJspFolder().lastIndexOf("/");
	  String jsp = testInfo.getJspFolder().substring(index + 1);
	  StringBuffer sb = new StringBuffer();	
	  sb.append("/").append(path.toString()).append("/").append(jsp);	  
	  jspfolder = sb.toString();
	} 
    else
	  jspfolder = testInfo.getJspFolder();	
  
  
  }
  
  //create the model from the resource
  private IStatus createModel(Environment env, IProgressMonitor monitor ) {
    JavaToModelCommand jtmc = new JavaToModelCommand();
	jtmc.setMethods(testInfo.getMethods());
	jtmc.setClientProject(testInfo.getClientProject());
	jtmc.setProxyBean(testInfo.getProxyBean());
	jtmc.setEnvironment( env );
	IStatus status = jtmc.execute( monitor, null);
	if (status.getSeverity() == Status.ERROR) return status;
    proxyModel = jtmc.getJavaDataModel();
	return status;
  } 
  
   /**
   * Generate the four jsps that make up this
   * sample app.
   */
   private IStatus generatePages(Environment env)
   {
   	IStatus status = Status.OK_STATUS;
	IPath fDestinationFolderPath = new Path(jspfolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    IWorkspaceRoot fWorkspace = ResourcesPlugin.getWorkspace().getRoot();

     IPath pathTest = fDestinationFolderPath.append(TEST_CLIENT);
     IFile fileTest = fWorkspace.getFile(pathTest);
     GeneratePageCommand gpcTest = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       new TestClientFileGenerator(INPUT,METHOD,RESULT),fileTest);
     //gpcTest.setStatusMonitor(getStatusMonitor());
     gpcTest.setEnvironment( env );
     status = gpcTest.execute( null, null );
     if (status.getSeverity() == Status.ERROR )
     	return status;
     

     //input codegen
     IPath pathInput = fDestinationFolderPath.append(INPUT);
     IFile fileInput = fWorkspace.getFile(pathInput);
     InputFileGenerator inputGenerator = new InputFileGenerator(RESULT);
     GeneratePageCommand gpcInput = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       inputGenerator,fileInput);
     //gpcInput.setStatusMonitor(getStatusMonitor());
     gpcInput.setEnvironment( env );
     status = gpcInput.execute( null, null );
     if (status.getSeverity() == Status.ERROR )
     	return status;

     //method codegen
     IPath pathMethod = fDestinationFolderPath.append(METHOD);
     IFile fileMethod = fWorkspace.getFile(pathMethod);
     MethodFileGenerator methodGenerator = new MethodFileGenerator(INPUT);
     methodGenerator.setClientFolderPath(jspfolder);
     GeneratePageCommand gpcMethod = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       methodGenerator,fileMethod);
     //gpcMethod.setStatusMonitor(getStatusMonitor());
     gpcMethod.setEnvironment( env );
     status = gpcMethod.execute( null, null );
     if (status.getSeverity() == Status.ERROR )
     	return status;    


     //result codegen
     IPath pathResult = fDestinationFolderPath.append(RESULT);
     IFile fileResult = fWorkspace.getFile(pathResult);
     ResultFileGenerator rfg = new ResultFileGenerator();
     rfg.setClientFolderPath(jspfolder);
     rfg.setSetEndpointMethod(testInfo.getSetEndpointMethod());
     GeneratePageCommand gpcResult = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       rfg,fileResult);
     gpcResult.setEnvironment( env );
     status = gpcResult.execute( null, null );
     
     return status;
   }

   



}

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.InputFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.MethodFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.ResultFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.TestClientFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.GeneratePageCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.JavaToModelCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.CopyWebServiceUtilsJarCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class GSTCGenerateCommand extends SimpleCommand {

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
	
  public Status execute(Environment env)
  {
    Status status = new SimpleStatus( "" );
	CopyWebServiceUtilsJarCommand copy = new CopyWebServiceUtilsJarCommand();    
	copy.setSampleProject(testInfo.getGenerationProject());
    copy.setSampleComponent(testInfo.getGenerationModule());
	status = copy.execute(env);
	if (status.getSeverity() == Status.ERROR) return status;
	setJSPFolder();
	status = createModel(env);
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
    if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject, testInfo.getClientModule())){   
	  IProject project = ProjectUtilities.getProject(testInfo.getGenerationProject());
	  IPath path = J2EEUtils.getWebContentPath(project,testInfo.getGenerationModule());
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
  private Status createModel(Environment env) {
    JavaToModelCommand jtmc = new JavaToModelCommand();
	jtmc.setMethods(testInfo.getMethods());
	jtmc.setClientProject(testInfo.getClientProject());
	jtmc.setProxyBean(testInfo.getProxyBean());
	Status status = jtmc.execute(env);
	if (status.getSeverity() == Status.ERROR) return status;
    proxyModel = jtmc.getDataModel();
	return status;
  } 
  
   /**
   * Generate the four jsps that make up this
   * sample app.
   */
   private Status generatePages(Environment env)
   {
   	Status status = new SimpleStatus( "" );
	IProject sampleIProject = ProjectUtilities.getProject(testInfo.getClientProject()); 
	IPath fDestinationFolderPath = new Path(jspfolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    IWorkspaceRoot fWorkspace = ResourcesPlugin.getWorkspace().getRoot();

     IPath pathTest = fDestinationFolderPath.append(TEST_CLIENT);
     IFile fileTest = fWorkspace.getFile(pathTest);
     GeneratePageCommand gpcTest = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       new TestClientFileGenerator(INPUT,METHOD,RESULT),fileTest);
     //gpcTest.setStatusMonitor(getStatusMonitor());
     status = gpcTest.execute(env);
     if (status.getSeverity() == Status.ERROR )
     	return status;
     

     //input codegen
     IPath pathInput = fDestinationFolderPath.append(INPUT);
     IFile fileInput = fWorkspace.getFile(pathInput);
     InputFileGenerator inputGenerator = new InputFileGenerator(RESULT);
     GeneratePageCommand gpcInput = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
       inputGenerator,fileInput);
     //gpcInput.setStatusMonitor(getStatusMonitor());
     status = gpcInput.execute(env);
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
     status = gpcMethod.execute(env);
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
     status = gpcResult.execute(env);
     
     return status;
   }

   



}

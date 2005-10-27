/**
 * 
 */
package org.eclipse.jst.ws.tests.unittest;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.etools.common.test.apitools.ProjectUnzipUtil;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.jst.ws.tests.plugin.TestsPlugin;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * Tests the various Component creation commands
 */
public class ComponentCreationTests extends TestCase implements WSJUnitConstants {

	public static Test suite(){
		return new TestSuite(ComponentCreationTests.class);
	}
	
	public void testWebComponentCreation(){

        createServerRuntime();
        createProjects();
        //createWebModule(projectName, projectName, J2EEVersionConstants.J2EE_1_4_ID);
        //createWebModule(project2Name, project2Name, J2EEVersionConstants.J2EE_1_3_ID);

	}
  
	// Creates projects from the provided ZIP file.
	public static boolean createProjects() {
		IPath localZipPath = getLocalPath();
		ProjectUnzipUtil util = new ProjectUnzipUtil(localZipPath, projectNames);
		return util.createProjects();
	}
	
	private static IPath getLocalPath() {
		URL url = TestsPlugin.getDefault().find(zipFilePath);
		try {
			url = Platform.asLocalURL(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Path(url.getPath());
	}	
  
    public void createServerRuntime(){
      
      try {
        JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH);
      }
      catch(Exception e){
        e.printStackTrace();
      }      
      
    }
  
    private void createWebModule(String projectNm, String componentName, int j2eeVersion){

      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(j2eeVersion).toString());
      cmc.setModuleName(componentName);
      cmc.setModuleType(CreateModuleCommand.WEB);
      cmc.setProjectName(projectNm);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null, null );
      
      System.out.println("Done creating Web component..."+projectNm);      
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(projectNm);
      IVirtualComponent vc = ComponentCore.createComponent(p);
      assertTrue(vc.exists());      
      
    }
    
    public void dtestCreateEJBModule(){
     
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(ejbComponentName);
      cmc.setModuleType(CreateModuleCommand.EJB);
      cmc.setProjectName(ejbProjectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null, null );
      
      System.out.println("Done creating EJB component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(ejbProjectName);
      IVirtualComponent vc = ComponentCore.createComponent(p);
      assertTrue(vc.exists());      
    }
    
    public void dtestCreateAppClientModule(){
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(appClientCompName);
      cmc.setModuleType(CreateModuleCommand.APPCLIENT);
      cmc.setProjectName(appClientProjectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null, null);
      
      System.out.println("Done creating App client component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(appClientProjectName);
      IVirtualComponent vc = ComponentCore.createComponent(p);
      assertTrue(vc.exists());       
    }

    public void dtestCreateEARModule(){
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(earCompName);
      cmc.setModuleType(CreateModuleCommand.EAR);
      cmc.setProjectName(projectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null, null);
      
      System.out.println("Done creating EAR component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(projectName);
      IVirtualComponent vc = ComponentCore.createComponent(p);
      assertTrue(vc.exists());       
    }
}

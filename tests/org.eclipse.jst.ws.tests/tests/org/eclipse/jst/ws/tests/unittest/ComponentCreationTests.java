/**
 * 
 */
package org.eclipse.jst.ws.tests.unittest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
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
        createWebModule(webProjectName, webComponentName, J2EEVersionConstants.J2EE_1_4_ID);
        createWebModule(webProjectName, webComponent2Name, J2EEVersionConstants.J2EE_1_3_ID);
        
        createWebModule(webProject2Name, webComp3Name, J2EEVersionConstants.J2EE_1_3_ID);
        createWebModule(webProject2Name, webComp4Name, J2EEVersionConstants.J2EE_1_3_ID);
        
	}
  
  
    public void createServerRuntime(){
      
      try {
        JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH);
      }
      catch(Exception e){
        e.printStackTrace();
      }      
      
    }
  
    private void createWebModule(String projectName, String componentName, int j2eeVersion){

      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(j2eeVersion).toString());
      cmc.setModuleName(componentName);
      cmc.setModuleType(CreateModuleCommand.WEB);
      cmc.setProjectName(projectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null);
      
      System.out.println("Done creating Web component...");      
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(projectName);
      IVirtualComponent vc = ComponentCore.createComponent(p, componentName);
      assertTrue(vc.exists());      
      
    }
    
    public void testCreateEJBModule(){
     
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(ejbComponentName);
      cmc.setModuleType(CreateModuleCommand.EJB);
      cmc.setProjectName(ejbProjectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null);
      
      System.out.println("Done creating EJB component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(ejbProjectName);
      IVirtualComponent vc = ComponentCore.createComponent(p, ejbComponentName);
      assertTrue(vc.exists());      
    }
    
    public void testCreateAppClientModule(){
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(appClientCompName);
      cmc.setModuleType(CreateModuleCommand.APPCLIENT);
      cmc.setProjectName(appClientProjectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null);
      
      System.out.println("Done creating App client component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(appClientProjectName);
      IVirtualComponent vc = ComponentCore.createComponent(p, appClientCompName);
      assertTrue(vc.exists());       
    }

    public void testCreateEARModule(){
      CreateModuleCommand cmc = new CreateModuleCommand();
      cmc.setJ2eeLevel(new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString());
      cmc.setModuleName(earCompName);
      cmc.setModuleType(CreateModuleCommand.EAR);
      cmc.setProjectName(webProjectName);
      cmc.setServerFactoryId(SERVERTYPEID_TC50);
      cmc.execute(null);
      
      System.out.println("Done creating EAR component.");
      IProject p = ResourceUtils.getWorkspaceRoot().getProject(webProjectName);
      IVirtualComponent vc = ComponentCore.createComponent(p, earCompName);
      assertTrue(vc.exists());       
    }
}

/**
 * 
 */
package org.eclipse.jst.ws.tests.unittest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * @author sengpl
 *
 */
public class J2EEUtilsTests extends TestCase implements WSJUnitConstants{

    private IProject project1 = null;
    private IProject project2 = null;
    private IProject ejbProject = null;
    
    private String comp1 = webComponentName;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		// Project and component names initialized here!
		project1 = ProjectUtilities.getProject(projectName);
		assertNotNull(project1);
		
		project2 = ProjectUtilities.getProject(project2Name);
		assertNotNull(project2);
    
        ejbProject = ProjectUtilities.getProject(ejbProjectName);
        assertNotNull(ejbProject);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// TODO Clear the workspace
		
	}

	 public static Test suite()
	  {
	    return new TestSuite(J2EEUtilsTests.class);
	  }
	 
	  public void testComponentsExist(){
		  System.out.println("< BEGIN: testComponentExists ...");
		  IVirtualComponent vc1 = J2EEUtils.getVirtualComponent(project1);
		  IVirtualComponent vc2 = J2EEUtils.getVirtualComponent(project2.getName());
		  
		  assertTrue(vc1.exists());
		  assertTrue(J2EEUtils.exists(project1));
		  assertTrue(vc2.exists());
		  assertTrue(J2EEUtils.exists(project2.getName(), project2Name));
		  System.out.println("< END: testComponentExists ...");
	  }
	  
	  public void testLocationGetterMethods(){
		  
		  System.out.println("< BEGIN: testLocationGetterMethods ...");
		  IPath loc1 = J2EEUtils.getWebContentPath(project1);
		  System.out.println("WebContentPath of project1 = "+loc1);
		  IResource res = ResourceUtils.getWorkspaceRoot().findMember(loc1);
		  assertTrue(res.exists());
		  assertTrue(res.toString().endsWith("WebContent"));
		  
		  IPath loc2 = J2EEUtils.getWebContentPath(project2);
		  System.out.println("WebContentPath of project2 = "+loc2);
		  IResource res2 = ResourceUtils.getWorkspaceRoot().findMember(loc2);
		  assertTrue(res2.exists());
		  assertTrue(res2.toString().endsWith("WebContent"));		  
		  
		  IPath loc3 = J2EEUtils.getWebInfPath(project1);
		  System.out.println("Web-INF path = "+loc3);
		  IResource res3 = ResourceUtils.getWorkspaceRoot().findMember(loc3);
		  assertTrue(res3.exists());
		  assertTrue(res3.toString().endsWith("WEB-INF"));
		  
		  IContainer container = J2EEUtils.getWebContentContainer(project2);
		  IResource res4 = ResourceUtils.getWorkspaceRoot().findMember(container.getFullPath());
		  assertEquals(res2, res4);
		  
		  System.out.println("< END: testLocationGetterMethods ...");
		  
	  }
	  
	  public void testJ2EEVersionMethods(){
		  
		  System.out.println("< BEGIN: testJ2EEVersionMethods ...");
	  		  
		  IVirtualComponent vc2 = J2EEUtils.getVirtualComponent(project2);
		  int j2 = J2EEUtils.getJ2EEVersion(vc2);
		  System.out.println("J2EEVersions p1: "+j2);		  
		  assertEquals(j2, J2EEVersionConstants.VERSION_1_3);
		  
		  String j3 = J2EEUtils.getJ2EEVersionAsString(project1);
		  System.out.println("J2EEVersion p1 as String: "+j3);
		  assertEquals(j3, J2EEVersionConstants.VERSION_1_4_TEXT);
		  
		  System.out.println("< ENG: testJ2EEVersionMethods ...");
	  }

	  public void testComponentGetterMethods(){
		  
		  System.out.println("< BEGIN: testComponentGetterMethods ...");
		  
		  IVirtualComponent[] vcs = J2EEUtils.getAllComponents();
		  checkVirtualComponentsExists(vcs);
		  
		  IVirtualComponent[] vcs2 = J2EEUtils.getAllEARComponents();
		  checkVirtualComponentsExists(vcs2);
		  
		  IVirtualComponent[] vcs3 = J2EEUtils.getAllWebComponents();
		  checkVirtualComponentsExists(vcs3);
		  
		  IVirtualComponent[] vcs4 = J2EEUtils.getWebComponents(project1);
		  checkVirtualComponentsExists(vcs4);
		  
		  String[] names = J2EEUtils.getWebComponentNames(project2);
		  printNameStrings(names);
		  
		  String[] names2 = J2EEUtils.toComponentNamesArray(J2EEUtils.getComponentsByType(project1, J2EEUtils.WEB | J2EEUtils.EJB));
		  printNameStrings(names2);
		  
		  IProject[] projects = J2EEUtils.getAllFlexibleProjects();
		  for (int i=0;i<projects.length;i++){
			  System.out.println("Flex project "+i+" = "+projects[i]);
		  }
		  
		  System.out.println("< END: testComponentGetterMethods ...");
	  }
	  
	  private void checkVirtualComponentsExists(IVirtualComponent[] vcs){
		  for (int i=0; i<vcs.length;i++){
			  IVirtualComponent vc = vcs[i];
			  System.out.println("VC: Project = "+vc.getProject()+" Name = "+vc.getName());
			  assertTrue(vc.exists());
			  
		  }
	  }
	  
	  private void printNameStrings(String[] names){
		  for (int i=0;i<names.length;i++){
			  System.out.println("Name"+i+" = "+names[i]);
		  }
	  }
	  
      public void dtestAssociateComponentCommand(){
        
        System.out.println("< BEGIN: testAssociateComponentCommand ...");
        
        AssociateModuleWithEARCommand amwe = new AssociateModuleWithEARCommand();
        amwe.setEar(earCompName);
        amwe.setEARProject(projectName);
        amwe.setModule(comp1);
        amwe.setProject(projectName);
        amwe.execute(null, null);
        
        System.out.println("< END: testAssociateComponentCommand ...");     
      }
    
	  public void dtestReferencingGetterMethods(){
		  
		  System.out.println("< BEGIN: testReferencingGetterMethods ...");
		  
		  System.out.println("isComponentAssociated ..");
		  J2EEUtils.isComponentAssociated(project1, project1);
		
		  String[] names = J2EEUtils.toComponentNamesArray(J2EEUtils.getReferencingEARComponents(project1));
		  printNameStrings(names);
		  String[] names2 = J2EEUtils.toComponentNamesArray(J2EEUtils.getReferencingEARComponents(project2));
		  printNameStrings(names2);
		  
		  String[] names3 = J2EEUtils.toComponentNamesArray(J2EEUtils.getReferencingWebComponentsFromEAR(project1));
		  printNameStrings(names3);
		  
		  System.out.println("< END: testReferencingGetterMethods ...");
	  }
	  
	  public void dtestIsComponentMethods(){
		
		  System.out.println("< BEGIN: testIsComponentMethods ...");
		  
		  //assertFalse(J2EEUtils.isEJB20Component(ejbProject, ejbComponentName));
		  assertTrue(J2EEUtils.isEJB20Component(ejbProject));
		  
		  System.out.println("< END: testIsComponentMethods ...");
	  }
	  

	  
}

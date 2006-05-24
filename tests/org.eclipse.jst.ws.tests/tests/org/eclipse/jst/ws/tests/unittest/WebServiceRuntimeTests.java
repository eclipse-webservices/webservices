/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060518   127189 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.tests.unittest;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.debug.ui.jres.JREsUpdater;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.server.tomcat.core.internal.ITomcatRuntimeWorkingCopy;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.FacetMatchCache;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

/**
 * WebServiceRuntimeTests contains the JUnit tests which cover project
 * creation, project filtering, and project type filtering for the Axis
 * Web service runtime. In order for the test to run successfully:
 * <ol>
 * <li>The Eclipse test environment must be launched with JDK1.4.2</li>
 * <li>The following VM arguments must be specified:
 * <ul>
 *   <li>org.eclipse.jst.server.tomcat.55: the install location of a Tomcat 5.5 server,<br> 
 *   for example, use -Dorg.eclipse.jst.server.tomcat.55=d:\jakarta-tomcat-5.5.9
 *   <li>org.eclipse.jst.server.tomcat.50: the install location of a Tomcat 5.0 server,<br>
 *   for example, use -Dorg.eclipse.jst.server.tomcat.50=d:\jakarta-tomcat-5.0.28   
 *   <li>org.eclipse.jst.server.tomcat.41: the install location of a Tomcat 4.1 server,<br>
 *   for example, use -Dorg.eclipse.jst.server.tomcat.41=d:\jakarta-tomcat-4.1.29
 *   <li>java.15.install.path: the install location of a Java 1.5 JDK,<br>
 *   for example, use -Djava.15.install.path=d:\jdk1.5.0_02
 * </ul></li>
 * </ol>
 * 
 * 
 */
public class WebServiceRuntimeTests extends TestCase
{
  
  //Server install paths
  private final String SERVER_INSTALL_PATH_TC55 = System.getProperty("org.eclipse.jst.server.tomcat.55");  
  private final String SERVER_INSTALL_PATH_TC50 = System.getProperty("org.eclipse.jst.server.tomcat.50");  
  private final String SERVER_INSTALL_PATH_TC41 = System.getProperty("org.eclipse.jst.server.tomcat.41");
  
  //JDK 1.5 install info
  private final String JAVA15_VM_INSTALL_PATH = System.getProperty("java.15.install.path");
  private final String JAVA15_VM_INSTALL_TYPE = "org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType";
  private final String JAVA15_VM_INSTALL_NAME = "Java15";
  private final String JAVA15_VM_INSTALL_JAVADOC = "http://java.sun.com/j2se/1.5.0/docs/api/";
  
  //Runtime type ids ans server type ids
  private final String RUNTIMETYPEID_TC55 = "org.eclipse.jst.server.tomcat.runtime.55";
  private final String SERVERTYPEID_TC55 = "org.eclipse.jst.server.tomcat.55";
  private final String RUNTIMETYPEID_TC50 = "org.eclipse.jst.server.tomcat.runtime.50";
  private final String SERVERTYPEID_TC50 = "org.eclipse.jst.server.tomcat.50";
  private final String RUNTIMETYPEID_TC41 = "org.eclipse.jst.server.tomcat.runtime.41";
  private final String SERVERTYPEID_TC41 = "org.eclipse.jst.server.tomcat.41";  
  
  //Runtime ids for runtimes that will be created
  private final String RUNTIMEID_TC55 = "Tomcat55Runtime";
  private final String RUNTIMEID_TC50 = "Tomcat50Runtime";
  private final String RUNTIMEID_TC41 = "Tomcat41Runtime";
  
  //Web service runtime extension ids for Axis
  private final String AXIS_RUNTIME = "org.eclipse.jst.ws.axis.creation.axisWebServiceRT";
  private final String SERVICE_IMPL_JAVA = "org.eclipse.jst.ws.wsImpl.java";
  private final String AXIS_SERVICE_RUNTIME = "org.eclipse.jst.ws.axis.creation.java";
  private final String CLIENT_IMPL_JAVA = "org.eclipse.jst.ws.client.type.java";
  private final String AXIS_CLIENT_RUNTIME_WEB = "org.eclipse.jst.ws.axis.consumption.web";
  private final String AXIS_CLIENT_RUNTIME_JAVA = "org.eclipse.jst.ws.axis.consumption.java";
  
  //Various constants used by the test case for identifying service/client side and 
  //for setting names on created projects.
  private final String SERVICE_SIDE = "s";
  private final String CLIENT_SIDE = "c";  
  private final String PROJECT_NAME_PREFIX = "tc";
  private final String PROJECT_NAME_SUFFIX = "project";
  
  //IProjectFacetVersions used in the expected results
  private final IProjectFacetVersion WEB23 = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_WEB_MODULE).getVersion(J2EEVersionConstants.VERSION_2_3_TEXT);
  private final IProjectFacetVersion WEB24 = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_WEB_MODULE).getVersion(J2EEVersionConstants.VERSION_2_4_TEXT);
  private final IProjectFacetVersion JAVA14 = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_JAVA).getVersion(J2EEVersionConstants.VERSION_1_4_TEXT);
  private final IProjectFacetVersion JAVA50 = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_JAVA).getVersion("5.0");
  private final IProjectFacetVersion UTILITY10 = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_UTILITY_MODULE).getVersion(J2EEVersionConstants.VERSION_1_0_TEXT);  
  
  private final IFacetedProjectTemplate TEMPLATE_WEB = ProjectFacetsManager.getTemplate(IJ2EEModuleConstants.JST_WEB_TEMPLATE);
  private final IFacetedProjectTemplate TEMPLATE_UTILITY = ProjectFacetsManager.getTemplate(IJ2EEModuleConstants.JST_UTILITY_TEMPLATE);
  
  private int multiplesForProjectCreation;
  
  public static Test suite()
  {
    return new TestSuite(WebServiceRuntimeTests.class);
  }
  
  private void init()
  {
    try
    {
      assertNotNull(SERVER_INSTALL_PATH_TC55);
      assertNotNull(SERVER_INSTALL_PATH_TC50);
      assertNotNull(SERVER_INSTALL_PATH_TC41);
      assertNotNull(JAVA15_VM_INSTALL_PATH);
      
      //Create a server runtime: Tomcat 4.1 with JDK 1.4.2
      createTomcatServerRuntime(RUNTIMEID_TC41, RUNTIMETYPEID_TC41, SERVER_INSTALL_PATH_TC41);
      
      //Create a server runtime: Tomcat 5.0 with JDK 1.4.2
      createTomcatServerRuntime(RUNTIMEID_TC50, RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH_TC50);
      
      //Create a server runtime: Tomcat 5.5 with JDK 1.5
      createTomcatServerRuntime(RUNTIMEID_TC55, RUNTIMETYPEID_TC55, SERVER_INSTALL_PATH_TC55);
      
      
      
      String multiples = System.getProperty("multiples"); //this is an optional propery - defaulted to 1
      if (multiples != null && (Integer.parseInt(multiples) > 0))
      {
        multiplesForProjectCreation = Integer.parseInt(multiples);
      }
      else
      {
        multiplesForProjectCreation = 1;
      }
      
    } catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }  
  
  private IRuntime createTomcatServerRuntime(String runtimeId, String runtimeTypeId, String serverInstallPath) throws Exception
  {
    IRuntimeType rt = ServerCore.findRuntimeType(runtimeTypeId);
    IRuntimeWorkingCopy wc = rt.createRuntime(runtimeId, null);
    wc.setLocation(new Path(serverInstallPath));
    IRuntime runtime = wc.save(true, null);
    
    if (runtimeTypeId.equals(RUNTIMETYPEID_TC55))
    {
      //Create a new IVMInstall
      IVMInstallType type = JavaRuntime.getVMInstallType(JAVA15_VM_INSTALL_TYPE);
      IVMInstall newVM = new VMStandin(type, createUniqueId(type)); 
      newVM.setInstallLocation(new File(JAVA15_VM_INSTALL_PATH).getAbsoluteFile());
      newVM.setName(JAVA15_VM_INSTALL_NAME);
      newVM.setJavadocLocation(new URL(JAVA15_VM_INSTALL_JAVADOC));
      if (newVM instanceof IVMInstall2)
      {
        IVMInstall2 newVM2 = (IVMInstall2) newVM;
        newVM2.setVMArgs(null);
      }
      newVM.setLibraryLocations(null);      

      //Add the new IVMInstall to the master list.
      IVMInstall[] vms = new IVMInstall[]{newVM};
      //TODO Replace use of the internal class org.eclipse.jdt.internal.debug.ui.jres.JREsUpdater
      //with something public. Not sure if something public is available at this time. Need to check.
      JREsUpdater updater = new JREsUpdater();
      IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();
      updater.updateJRESettings(vms, defaultVM);
      
      //Get the new IVMInstall
      IVMInstallType vmType = JavaRuntime.getVMInstallType(JAVA15_VM_INSTALL_TYPE);
      IVMInstall java15VM = vmType.findVMInstallByName(JAVA15_VM_INSTALL_NAME);
    
      //Set the tomcat runtime to use the new Java 15 JDK.
      IRuntimeWorkingCopy wc2 = runtime.createWorkingCopy();
      //TODO Replace use of the internal class 
      //org.eclipse.jst.server.tomcat.core.internal.ITomcatRuntimeWorkingCopy
      //with something public. Enhancement 127884 has been opened to request API
      //for this.
      ITomcatRuntimeWorkingCopy tcwc = (ITomcatRuntimeWorkingCopy)wc2.loadAdapter(ITomcatRuntimeWorkingCopy.class, new NullProgressMonitor());
      tcwc.setVMInstall(java15VM);
      wc2.save(true, null);      
    }
    
    return ServerCore.findRuntime(runtimeId);
  }  
  
  private String createUniqueId(IVMInstallType vmType)
  {
    String id = null;
    do
    {
      id = String.valueOf(System.currentTimeMillis());
    } while (vmType.findVMInstall(id) != null);
    return id;
  }
  
  /**
   * Tests project creation and filtering for the Axis Web service
   * runtime. Tests cover all valid combinations of ServiceRuntimes, 
   * Tomcat servers, and J2EE project types and well as all valid  
   * combinations of ClientRuntimes, Tomcat servers, and J2EE project types.
   */  
  public void testProjectCreationAndFiltering()
  {
    init();
    
    String[] templateIds = new String[]{IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_WEB_TEMPLATE,
        IJ2EEModuleConstants.JST_UTILITY_TEMPLATE,
        IJ2EEModuleConstants.JST_UTILITY_TEMPLATE,
        IJ2EEModuleConstants.JST_UTILITY_TEMPLATE};
    String[] scenarios = new String[]{SERVICE_SIDE,
        SERVICE_SIDE,
        SERVICE_SIDE,
        CLIENT_SIDE,
        CLIENT_SIDE,
        CLIENT_SIDE,
        CLIENT_SIDE,
        CLIENT_SIDE,
        CLIENT_SIDE};
    String[] serviceClientRuntimeIds = new String[]{AXIS_SERVICE_RUNTIME,
        AXIS_SERVICE_RUNTIME,
        AXIS_SERVICE_RUNTIME,
        AXIS_CLIENT_RUNTIME_WEB,
        AXIS_CLIENT_RUNTIME_WEB,
        AXIS_CLIENT_RUNTIME_WEB,
        AXIS_CLIENT_RUNTIME_JAVA,
        AXIS_CLIENT_RUNTIME_JAVA,
        AXIS_CLIENT_RUNTIME_JAVA};
    String[] serverTypes = new String[]{SERVERTYPEID_TC41,
        SERVERTYPEID_TC50,
        SERVERTYPEID_TC55,
        SERVERTYPEID_TC41,
        SERVERTYPEID_TC50,
        SERVERTYPEID_TC55,
        SERVERTYPEID_TC41,
        SERVERTYPEID_TC50,
        SERVERTYPEID_TC55};
    
    
    //Expected Results:
    
    IProjectFacetVersion[][] expectedFacets = new IProjectFacetVersion[][]{ new IProjectFacetVersion[]{WEB23, JAVA14},
        new IProjectFacetVersion[]{WEB24, JAVA14},
        new IProjectFacetVersion[]{WEB24, JAVA50},
        new IProjectFacetVersion[]{WEB23, JAVA14},
        new IProjectFacetVersion[]{WEB24, JAVA14},
        new IProjectFacetVersion[]{WEB24, JAVA50},
        new IProjectFacetVersion[]{UTILITY10, JAVA14},
        new IProjectFacetVersion[]{UTILITY10, JAVA14},
        new IProjectFacetVersion[]{UTILITY10, JAVA50},
    };
    
    String[] expectedRuntimes = new String[] {RUNTIMEID_TC41,
        RUNTIMEID_TC50,
        RUNTIMEID_TC55,
        RUNTIMEID_TC41,
        RUNTIMEID_TC50,
        RUNTIMEID_TC55,
        RUNTIMEID_TC41,
        RUNTIMEID_TC50,
        RUNTIMEID_TC55        
    };
    
    String bigPrefix = PROJECT_NAME_PREFIX+"0"+PROJECT_NAME_SUFFIX;
    String[] expectedProjectsForAxisService = new String[] { bigPrefix+"0", 
        bigPrefix+"1",
        bigPrefix+"2",
        bigPrefix+"3",
        bigPrefix+"4",
        bigPrefix+"5"        
    };
    
    String[] expectedProjectsForAxisClient = new String[] { bigPrefix+"0", 
        bigPrefix+"1",
        bigPrefix+"2",
        bigPrefix+"3",
        bigPrefix+"4",
        bigPrefix+"5",
        bigPrefix+"6",
        bigPrefix+"7",
        bigPrefix+"8",
    };
    
    boolean[] expectedBooleansForAxisService = new boolean[] {true, true, true, true, true, true, false, false, false};
    boolean[] expectedBooleansForAxisClientWeb = new boolean[] {true, true, true, true, true, true, false, false, false};
    boolean[] expectedBooleansForAxisClientJava = new boolean[] {false, false, false, false, false, false, true, true, true};
    
    
    //Turn auto-build off
    ValidationManager manager = new ValidationManager();
    manager.disableAutoBuild();
    
    //Create the projects
    for (int i=0; i<multiplesForProjectCreation; i++)
    {
      createProjects(templateIds, scenarios, serviceClientRuntimeIds, serverTypes, PROJECT_NAME_PREFIX+i);
    }  
    
    //Restore auto-build
    manager.restoreAutoBuild();
    
    //Check Expected Results:
    
    //Check facets and runtime on the first "0"th set of created projects. Ignore higher multiples since they
    //are duplicates.    
    for (int j=0; j<expectedFacets.length; j++)
    {
      String projectName = bigPrefix+j;
      IProject project = ProjectUtilities.getProject(projectName);
      assertNotNull(project);
      assertTrue(project.exists());
      try
      {
        IFacetedProject fProject = ProjectFacetsManager.create(project);
        assertNotNull(fProject);

        //Check facets
        //begin debug
        Iterator pfacets = fProject.getProjectFacets().iterator();
        while (pfacets.hasNext())
        {
        	IProjectFacetVersion pfv = (IProjectFacetVersion)pfacets.next();
        	System.out.println("facet="+pfv.getProjectFacet().getId()+", version="+pfv.getVersionString());
        }
        
        for (int k=0; k<expectedFacets[j].length; k++)
        {
          assertTrue(fProject.hasProjectFacet(expectedFacets[j][k]));
          
        }
        
        //Check runtime
        org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = fProject.getRuntime();
        assertNotNull(fRuntime);
        IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
        assertNotNull(sRuntime);
        assertTrue(sRuntime.getId().equals(expectedRuntimes[j]));
        
      } catch (CoreException ce)
      {
        fail();
      }
      
    }
    
    //Check that WebServiceRuntimeExtensionUtils2.getProjectsForServiceTypeAndRuntime(..) returns
    //the right set of projects
    String serviceType = String.valueOf(WebServiceScenario.BOTTOMUP) + "/" + SERVICE_IMPL_JAVA;
    String[] projectsForAxisService = WebServiceRuntimeExtensionUtils2.getProjectsForServiceTypeAndRuntime(serviceType, AXIS_RUNTIME);
    assertEquals(expectedProjectsForAxisService.length, projectsForAxisService.length);
    List projectsForAxisServiceList = new ArrayList();
    for (int i=0; i<projectsForAxisService.length; i++)
    {
      projectsForAxisServiceList.add(projectsForAxisService[i]);      
    }
    for (int i=0; i<expectedProjectsForAxisService.length; i++)
    {
      assertTrue(projectsForAxisServiceList.contains(expectedProjectsForAxisService[i]));
    }
    
    //Check that WebServiceRuntimeExtensionUtils2.getProjectsForClientTypeAndRuntime(..) returns
    //the right set of projects    
    String[] projectsForAxisClient = WebServiceRuntimeExtensionUtils2.getProjectsForClientTypeAndRuntime(CLIENT_IMPL_JAVA, AXIS_RUNTIME);
    assertEquals(expectedProjectsForAxisClient.length, projectsForAxisClient.length);
    List projectsForAxisClientList = new ArrayList();
    for (int i=0; i<projectsForAxisClient.length; i++)
    {
      projectsForAxisClientList.add(projectsForAxisClient[i]);      
    }
    for (int i=0; i<expectedProjectsForAxisClient.length; i++)
    {
      assertTrue(projectsForAxisClientList.contains(expectedProjectsForAxisClient[i]));
    }    
    
    //Check that the Axis service and client runtimes' project compatibility checking is working
    for (int i=0; i<expectedBooleansForAxisService.length; i++)
    {
      String projectName = bigPrefix+i;      
      assertEquals(expectedBooleansForAxisService[i], WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportProject(AXIS_SERVICE_RUNTIME, projectName));    
      assertEquals(expectedBooleansForAxisClientWeb[i], WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportProject(AXIS_CLIENT_RUNTIME_WEB, projectName));
      assertEquals(expectedBooleansForAxisClientJava[i], WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportProject(AXIS_CLIENT_RUNTIME_JAVA, projectName));
    } 
    
    //Turn auto-build off
    manager.disableAutoBuild();
        
    //Delete the projects
    for (int i=0; i<multiplesForProjectCreation; i++)
    {
      deleteProjects(templateIds.length, PROJECT_NAME_PREFIX+i);
    }      

    //Restore auto-build
    manager.restoreAutoBuild();    
  }  
  
  private void deleteProjects(int n, String namePrefix)
  {
    for (int i=0; i<n; i++)
    {
      String name = namePrefix + PROJECT_NAME_SUFFIX + i;
      IProject project = ProjectUtilities.getProject(name);
      try
      {
        project.delete(true, null);
      }
      catch (CoreException ce)
      {
        //Don't fail the test case if project deletion fails.
      }
    }
  }  
  
  private void createProjects(String[] templateIds, String[] scenarios, String[] serviceClientRuntimeIds, String[] serverTypes, String namePrefix )
  {
    int n = templateIds.length;
    for (int i=0; i<n; i++)
    {
      String name = namePrefix + PROJECT_NAME_SUFFIX + i;
      RequiredFacetVersion[] rfvs = null;
      if (scenarios[i].equals(CLIENT_SIDE))
      {

          rfvs = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(serviceClientRuntimeIds[i]).getRequiredFacetVersions();
        
      }
      else
      {
        if (scenarios[i].equals(SERVICE_SIDE))
        {
          rfvs = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceClientRuntimeIds[i]).getRequiredFacetVersions();
        }
        else
        {
          rfvs = new RequiredFacetVersion[0];
        }
      }
      
      CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
      command.setProjectName(name);
      command.setTemplateId(templateIds[i]);                    
      command.setRequiredFacetVersions(rfvs);           
      command.setServerFactoryId(serverTypes[i]);
      IStatus status = command.execute( new NullProgressMonitor(), null );    
      if (status.getSeverity() == Status.ERROR)
      {
        fail();
      }
      
      IProject project = ProjectUtilities.getProject(name);
      FacetUtils.addRequiredFacetsToProject(project, rfvs, new NullProgressMonitor());
    }    
  }  
  
  /**
   * Tests project type filtering for the Axis Web service runtime.
   * Tests cover all ServiceRuntimes and ClientRuntimes.
   */
  public void testProjectTypeFiltering()
  {  
    IFacetedProjectTemplate[] expectedTemplatesForAxisService = new IFacetedProjectTemplate[] {TEMPLATE_WEB};
    IFacetedProjectTemplate[] expectedTemplatesForAxisClientWeb = new IFacetedProjectTemplate[] {TEMPLATE_WEB};
    IFacetedProjectTemplate[] expectedTemplatesForAxisClientJava = new IFacetedProjectTemplate[] {TEMPLATE_UTILITY};
    
    Set templatesForAxisService = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(AXIS_SERVICE_RUNTIME);
    assertEquals(expectedTemplatesForAxisService.length,templatesForAxisService.size());
    for (int i=0; i<expectedTemplatesForAxisService.length; i++)
    {
      assertTrue(templatesForAxisService.contains(expectedTemplatesForAxisService[i]));
    }
    Set templatesForAxisClientWeb = FacetMatchCache.getInstance().getTemplatesForClientRuntime(AXIS_CLIENT_RUNTIME_WEB);
    assertEquals(expectedTemplatesForAxisClientWeb.length, templatesForAxisClientWeb.size());
    for (int i=0; i<expectedTemplatesForAxisClientWeb.length; i++)
    {
      assertTrue(templatesForAxisClientWeb.contains(expectedTemplatesForAxisClientWeb[i]));
    }    
    Set templatesForAxisClientJava = FacetMatchCache.getInstance().getTemplatesForClientRuntime(AXIS_CLIENT_RUNTIME_JAVA);
    assertEquals(expectedTemplatesForAxisClientJava.length, templatesForAxisClientJava.size());
    for (int i=0; i<expectedTemplatesForAxisClientJava.length; i++)
    {
      assertTrue(templatesForAxisClientJava.contains(expectedTemplatesForAxisClientJava[i]));
    }
    
    
  }  
}

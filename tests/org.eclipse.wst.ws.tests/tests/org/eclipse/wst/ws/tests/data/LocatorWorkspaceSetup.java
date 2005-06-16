package org.eclipse.wst.ws.tests.data;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.wst.ws.tests.plugin.TestsPlugin;

/**
 * @author joan
 *  Sets up locator test data - uses same data file but renames it for each use
 *  Workspace structure created looks like this:
 *  
 *  root
 *  --project1
 *    |----folder1
 *         |-----folder2
 *               |------one.wsdl
 *         |-----two.wsdl
 *    |----folder3
 *         |-----three.wsdl
 *         |-----four.wsdl 
 *  --project2
 *    |----five.wsdl
 *    |----folder4
 *    |----folder5
 *         |-----six.wsdl
 *  --project3
 *    |----one.xml
 */

public class LocatorWorkspaceSetup extends TestCase {

	  public static String PROJECT1_NAME = "Project1";
	  public static String FOLDER1_NAME = "Folder1";  //contains WSDL file and one folder 
	  public static String FOLDER2_NAME = "Folder2";  //nested in folder 1 and contains WSDL file
	  public static String FOLDER3_NAME = "Folder3";  //contains two WSDL files
	  public static String PROJECT2_NAME = "Project2"; //contains WSDL file and two folders
	  public static String FOLDER4_NAME = "Folder4";  //contains no WSDL
	  public static String FOLDER5_NAME = "Folder5";  //contains one WSDL file
	  public static String PROJECT3_NAME = "Project3";  //contains no WSDL files but one XML file
	  
	  public static String WSDLFILE_ONE = "one.wsdl";
	  public static String WSDLFILE_TWO = "two.wsdl";
	  public static String WSDLFILE_THREE = "three.wsdl";
	  public static String WSDLFILE_FOUR = "four.wsdl";
	  public static String WSDLFILE_FIVE = "five.wsdl";
	  public static String WSDLFILE_SIX = "six.wsdl";
	  public static String XMLFILE_ONE = "one.XML";
	  
	  public static String ACTUAL_TESTFILEPATH = "data/locator/test.wsdl";
	  	  
	  private IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	  
	  public static Test suite()
	  {
	    return new TestSuite(LocatorWorkspaceSetup.class, "LocatorWorkspaceSetup");
	  }

	  protected void closeIntro()
	  {
	    IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
	    IIntroPart introPart = introManager.getIntro();
	    if (introPart != null)
	      introManager.closeIntro(introPart);
	  }

	  protected IProject createSimpleProject(String name) throws CoreException
	  {
	    IProject simpleProject = root.getProject(name);
	    simpleProject.create(null);
	    simpleProject.open(null);
	    return simpleProject;
	  }

	  protected IFolder createFolder(IContainer parent, String folderName) throws CoreException
	  {
		IFolder folder1 = parent.getFolder(new Path(folderName));
        folder1.create(false, true, null);
	    return folder1;
	  }

	  
	  protected void copyFile(IContainer project, String source, String dest) throws IOException, CoreException
	  {
		  
	    IFile file = project.getFile(new Path(dest));
	    file.create(TestsPlugin.getDefault().getBundle().getEntry(source).openStream(), true, null);
	    Assert.assertTrue(file.exists());
	  }

	  protected void joinAutoBuild() throws CoreException
	  {
	    boolean interrupted = true;
	    while (interrupted)
	    {
	      try
	      {
	        Platform.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
	        interrupted = false;
	      }
	      catch (InterruptedException e)
	      {
	        interrupted = true;
	      }
	    }
	  }

	  public void testSetup() throws Exception
	  {
		  try{
		System.out.println("------ locator data setup starting");
	    closeIntro();

	    IProject project1 = createSimpleProject(PROJECT1_NAME);
	    IFolder folder1 = createFolder(project1, FOLDER1_NAME);
	    copyFile(folder1, ACTUAL_TESTFILEPATH, WSDLFILE_TWO);
	    IFolder folder2 = createFolder(folder1, FOLDER2_NAME);
	    copyFile(folder2, ACTUAL_TESTFILEPATH, WSDLFILE_ONE);
	    IFolder folder3 = createFolder(project1, FOLDER3_NAME);
	    copyFile(folder3, ACTUAL_TESTFILEPATH, WSDLFILE_THREE);
	    copyFile(folder3, ACTUAL_TESTFILEPATH, WSDLFILE_FOUR);
	    
	    IProject project2 = createSimpleProject(PROJECT2_NAME);
	    copyFile(project2, ACTUAL_TESTFILEPATH, WSDLFILE_FIVE);
	    createFolder(project2, FOLDER4_NAME);  //folder with no content
	    IFolder folder5 = createFolder(project2, FOLDER5_NAME);
	    copyFile(folder5, ACTUAL_TESTFILEPATH, WSDLFILE_SIX);
	    IProject project3 = createSimpleProject(PROJECT3_NAME);
	    copyFile(project3, ACTUAL_TESTFILEPATH, XMLFILE_ONE);
		  }
		  catch (Exception e)
		  {
			  e.printStackTrace();
		  }
	 
		//code to check workspace set up - debug
		/*DataResourceVisitor visitor = new DataResourceVisitor();
	    root.accept(visitor);
	    visitor.visit(root);*/
	    
	    System.out.println("------ locator data setup complete");
	  }
	
	protected class DataResourceVisitor implements IResourceVisitor
	{
    	public boolean visit(IResource resource) throws CoreException {
    		if ((resource instanceof IProject) || (resource instanceof IFolder))
    		{
    		  System.out.println("visiting " + resource.getName());    		  
    		}
    		else if (resource instanceof IFile)
    		{
    			System.out.println("found file " + resource.getName());    			
    		}
    		return true;
    	}
    }
}

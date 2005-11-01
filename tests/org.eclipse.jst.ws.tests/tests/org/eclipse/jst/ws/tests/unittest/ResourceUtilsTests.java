package org.eclipse.jst.ws.tests.unittest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * JUnit tests for selective ResourceUtils methods.  Calls which return a simple String or rely on a IResource
 * API need not be tested.  The focus is mainly to exercise Component and Flexible project APIs. 
 * 
 */
public class ResourceUtilsTests extends TestCase implements WSJUnitConstants{


	public static Test suite(){
		return new TestSuite(ResourceUtilsTests.class);
	}
	
	public void testComponentType(){
		IProject project = ProjectUtilities.getProject(projectName);
		String componentType = ResourceUtils.getComponentType(project);
		assertEquals(IModuleConstants.JST_WEB_MODULE, componentType);
	}
	
	public void testComponentOf(){
		IProject project = ProjectUtilities.getProject(projectName);
        IPath destPath = ResourceUtils.getJavaSourceLocation(project);
        IFolder folder = (IFolder)ResourceUtils.findResource(destPath);
        try {
            PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
            EclipseStatusHandler       handler         = new EclipseStatusHandler();
            EclipseEnvironment         env     = new EclipseEnvironment( null, resourceContext, handler );

            IFile file = folder.getFile(new Path("foo/Echo.java"));
            if (!file.exists()) {
              JUnitUtils.copyTestData("BUJava/src", folder, env, null );
            }
        }
        catch (Exception ex){
        	ex.printStackTrace();
        }

        IPath javaFilePath = folder.getFile(new Path("foo/Echo.java")).getFullPath();
        IVirtualComponent vc = ResourceUtils.getComponentOf(javaFilePath);
        assertTrue(vc.exists());
        
		
	}
	
	public void testJavaSourceLocation(){
		IProject project = ProjectUtilities.getProject(projectName);
		String javaSourceLoc = ResourceUtils.getJavaSourceLocation(project).toString();
		System.out.println("Java source location = "+javaSourceLoc);
		assertTrue(javaSourceLoc.endsWith("JavaSource"));
		
		// get all the JavaSourceLocations via the VirtualComponent
		IVirtualComponent vc1 = ComponentCore.createComponent(project);
		IVirtualComponent vc2 = ComponentCore.createComponent(project);
		IVirtualComponent[] vcs = new IVirtualComponent[]{vc1, vc2};
		IPath[] paths = ResourceUtils.getAllJavaSourceLocations(vcs);
		
		// verify first folder exists
		IPath path1 = (IPath)paths[0];
		IResource resource  = ResourceUtils.findResource(path1);
		if (resource.exists()){
			assertEquals(IResource.FOLDER, resource.getType());
		}

		// verify second folder exists
		IPath path2 = (IPath)paths[1];
		IResource resource2  = ResourceUtils.findResource(path2);
		if (resource2.exists()){
			assertEquals(IResource.FOLDER, resource2.getType());
		}		
	}
	
	public void testWebComponentServerRoot() {
		IProject project = ProjectUtilities.getProject(projectName);
		IFolder folder = ResourceUtils.getWebComponentServerRoot(project);
		assertTrue(folder.exists());
	
	}
	
}

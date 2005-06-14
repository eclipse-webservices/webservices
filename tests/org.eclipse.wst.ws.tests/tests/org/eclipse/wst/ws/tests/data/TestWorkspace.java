package org.eclipse.wst.ws.tests.data;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseProgressMonitor;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.ws.tests.util.JUnitUtils;

public class TestWorkspace extends Assert {

	protected Environment env_;
	/**
	 * 
	 * @throws Exception
	 */
	public void installData()throws Exception
	{
		PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
		EclipseStatusHandler handler = new EclipseStatusHandler();
		EclipseProgressMonitor monitor = new EclipseProgressMonitor();
		EclipseEnvironment environment = new EclipseEnvironment(null, resourceContext, monitor, handler );

		env_ = environment; 
		assertTrue(env_ != null);
		
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IProject locatorProjectA = root.getProject("testProjA");
		locatorProjectA.create(null);
		locatorProjectA.open(null);
		
        IFolder folder1 = locatorProjectA.getFolder("f1");
        folder1.create(false, true, null);
           
        // copy wsdl files into project folders...
		createFile("locator", folder1);
		
		DataResourceVisitor drv = new DataResourceVisitor();
        root.accept(drv);
        
        drv.visit(root); 
	}

	private void createFile(String location, IFolder destFolder)
	{
		try {
		JUnitUtils.copyTestData(location, destFolder, env_);
		}
		catch (Exception ex){
				System.out.println("exception creating file " + ex.toString());
		}
	}
	
	// class to show the workspace structure 
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

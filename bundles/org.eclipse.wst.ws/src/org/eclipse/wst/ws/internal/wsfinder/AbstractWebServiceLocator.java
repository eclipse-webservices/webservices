package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author joan
 *
 */
public abstract class AbstractWebServiceLocator implements IWebServiceLocator {

	public AbstractWebServiceLocator()
	{
		super();
	}
	
	public abstract List getWebServices();
	
	public IProject[] getWorkspaceProjects()
	{
	  IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      return root.getProjects();
	}
	
}

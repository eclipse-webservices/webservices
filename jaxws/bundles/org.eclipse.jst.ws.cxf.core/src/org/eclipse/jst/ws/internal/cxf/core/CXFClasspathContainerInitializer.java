package org.eclipse.jst.ws.internal.cxf.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class CXFClasspathContainerInitializer extends ClasspathContainerInitializer {

	@Override
	public void initialize(IPath containerPath, IJavaProject javaProject) throws CoreException {
		CXFClasspathContainer cxfClasspathContainer = new CXFClasspathContainer(containerPath, javaProject);
		
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {javaProject}, 
				new IClasspathContainer[] {cxfClasspathContainer}, null);  
	}

	@Override
	public String getDescription(IPath containerPath, IJavaProject project) {
		String description = super.getDescription(containerPath, project);
		return description;
	}

	@Override
	public IStatus getAttributeStatus(IPath containerPath, IJavaProject project, String attributeKey) {
		// TODO Auto-generated method stub
		return super.getAttributeStatus(containerPath, project, attributeKey);
	}
	
	
	
	
	

}

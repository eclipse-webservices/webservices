/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * Transforms a string value representing a workspace resource path into a StructuredSelection object
 * 
 * @author joan
 *
 */

public class String2SelectionTransformer implements Transformer {

	public Object transform(Object value) {
		
		StructuredSelection selection = null;
		IResource resource = FileResourceUtils.getWorkspaceRoot().findMember(new Path(value.toString()));
		IProject resProject = resource.getProject();
		
		// check if resource is part of an EJB
		if (J2EEUtils.isEJBComponent(resProject))
		{
			// if resource is part of EJB need to get the EnterpriseBean as the selection
			IVirtualComponent[] ejbComponents = J2EEUtils.getEJBComponents(resProject);
			EJBArtifactEdit  ejbEdit = null;			
			try{				
				ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(ejbComponents[0]);				
	            EJBResource ejbRes = ejbEdit.getEJBJarXmiResource();
	            EJBJar ejbJar = ejbRes.getEJBJar();
	            String resourceFilename =resource.getName(); 
	            String javaClassName = resourceFilename.substring(resourceFilename.indexOf('.'));
	            JavaClass javaClass = JavaMOFUtils.getJavaClass(javaClassName, resProject);
	            EnterpriseBean ejb = ejbJar.getEnterpriseBeanWithReference(javaClass);
	            selection = new StructuredSelection(ejb.getName());
			}
			catch (Exception exc)
			{
				
			}
			finally {
	              if (ejbEdit!=null)
	                ejbEdit.dispose();
	            }
		}		
		else if (resource != null)
		{
			selection = new StructuredSelection(resource);			
		}		
		return selection;
	}

}

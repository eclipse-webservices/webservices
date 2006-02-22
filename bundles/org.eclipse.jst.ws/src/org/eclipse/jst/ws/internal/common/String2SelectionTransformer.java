/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060222   128094 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.ws.internal.WSPluginMessages;
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
		if (resource != null)
		{
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
		            IPath path = resource.getFullPath();
		            String resourcePath = ResourceUtils.getJavaResourcePackageName(path);
		            String javaClassName = resource.getName();
		            // strip off file extension if necessary
		            if (javaClassName.lastIndexOf('.')>-1)
		            {	
		              javaClassName = javaClassName.substring(0, javaClassName.lastIndexOf('.'));	              
		            } 
		            JavaClass javaClass = JavaMOFUtils.getJavaClass(resourcePath, javaClassName, resProject);
		            EnterpriseBean ejb = ejbJar.getEnterpriseBeanWithReference(javaClass);
		            if (ejb != null)
		            {
		            	selection = new StructuredSelection(ejb.getName());	
		            }
				}
				catch (Exception exc)
				{
					
				}
				finally {
		              if (ejbEdit!=null)
		                ejbEdit.dispose();
		        }
			}
			// if selection not already set - create it from the resource object
			if (selection == null)
			{
			  selection = new StructuredSelection(resource);
			}
		}
		else
		{
			// string doesn't return resource - error condition
			throw new IllegalArgumentException(WSPluginMessages.ERROR_SELECTION_TRANSFORM);
		}
		return selection;
	}

}

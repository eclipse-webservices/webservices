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
package org.eclipse.wst.command.internal.env.ant;

import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.StructuredSelection;

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
			selection = new StructuredSelection(resource);			
		}		
		return selection;
	}

}

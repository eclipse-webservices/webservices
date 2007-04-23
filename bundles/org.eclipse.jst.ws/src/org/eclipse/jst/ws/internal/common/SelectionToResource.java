/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class SelectionToResource implements Transformer
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.data.Transformer#transform(java.lang.Object)
   */
  public Object transform(Object value)
  {
    IStructuredSelection selection = (IStructuredSelection)value;
    IResource            resource  = null;
    
    if( selection != null )
    {
      Object object = selection.getFirstElement();
      
      if( object != null )
      {
        if( object instanceof IResource)
        {
          resource = (IResource)object;
        }
        else if( object instanceof ICompilationUnit )
        {
          ICompilationUnit compUnit = (ICompilationUnit)object;
          
          try
          {
            resource = compUnit.getCorrespondingResource();
          }
          catch( JavaModelException exc)
          {
          }
        }
        else if( object instanceof EnterpriseBean ) 
        {
          EnterpriseBean ejbBean    = (EnterpriseBean)object;
          IProject       ejbProject = ProjectUtilities.getProject( ejbBean );
          
          if( ejbProject != null )
          {
            resource = ejbProject;
          }
        }
      }
    }

    return resource;
  }
}

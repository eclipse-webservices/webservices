/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;

public class JavaBeanSelectionTransformer implements Transformer
{
  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
      Object sel = ((IStructuredSelection)value).getFirstElement();
      if (sel instanceof IFile)
      {
        return new StructuredSelection(computeFullyQualifiedJavaName((IFile)sel));
      }
      else if (sel instanceof ICompilationUnit)
      {
        IResource res = ((ICompilationUnit)sel).getResource();
        if (res instanceof IFile)
          return new StructuredSelection(computeFullyQualifiedJavaName((IFile)res));
      }
    }
    return value;
  }

  private String computeFullyQualifiedJavaName(IFile resource)
  {
    IPath path = resource.getFullPath();
    String basename = path.lastSegment();
    String beanClass = "";
    if (basename != null && basename.length() > 0)
    {
      String beanPackage = org.eclipse.jst.ws.internal.common.ResourceUtils.getJavaResourcePackageName(path);
      beanClass = (beanPackage == null ? basename : (beanPackage + "." + basename));
      if (beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class"))
        beanClass = beanClass.substring(0, beanClass.lastIndexOf('.'));
    }
    return beanClass;
  }
}
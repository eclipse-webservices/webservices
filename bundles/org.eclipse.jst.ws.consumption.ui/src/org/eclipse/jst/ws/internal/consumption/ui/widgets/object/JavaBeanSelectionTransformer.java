/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070116   159618 makandre@ca.ibm.com - Andrew Mak, Project and EAR not defaulted properly when wizard launched from JSR-109 Web services branch in J2EE Project Explorer
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsdd.ServiceImplBean;
import org.eclipse.jst.j2ee.webservice.wsdd.ServletLink;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.PortComponentImpl;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

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
      else if (sel instanceof ServiceImplBean)
      {
        return new StructuredSelection(getBeanName((ServiceImplBean) sel));
      }
      else if(sel instanceof ServletLink)
      {
        return new StructuredSelection(getBeanName((ServletLink) sel));
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
      beanClass = (beanPackage == null || beanPackage.length() == 0 ? basename : (beanPackage + "." + basename));
      if (beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class"))
        beanClass = beanClass.substring(0, beanClass.lastIndexOf('.'));
    }
    return beanClass;
  }
  
  private String getBeanName(ServiceImplBean bean) {
    EObject eObject = bean.eContainer();
    if (eObject instanceof PortComponentImpl) {
      PortComponentImpl pci = (PortComponentImpl) eObject;
      return pci.getServiceEndpointInterface();	      	
    }
    return "";
  }
  
  private String getBeanName(ServletLink link) {    
    EObject eObject = link.eContainer();
    if (eObject instanceof ServiceImplBean)
      return getBeanName((ServiceImplBean) eObject);
    return "";
  }
}

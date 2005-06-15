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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class WSDLSelectionWidgetDefaultingCommand extends SimpleCommand
{
  private IStructuredSelection selection_;
  
  public String getWebServiceURI()
  {
  	if (selection_ != null && !selection_.isEmpty())
  	{
  	  Object firstSel = selection_.getFirstElement();
  	  if (firstSel instanceof IFile)
  	  {
  	    IFile ifile = (IFile)firstSel;
  	    String ext = ifile.getFileExtension();
  	    if (ext != null && (ext.equals("wsdl") || ext.equals("wsil") || ext.equals("html")))
  	    {
  	      return ifile.getFullPath().toString();
  	    }
  	  }
  	  if (firstSel instanceof ServiceImpl)
      {
        ServiceImpl serviceImpl = (ServiceImpl)firstSel;
        return J2EEActionAdapterFactory.getWSDLURI(serviceImpl);
      }
  	  if (firstSel instanceof ServiceRef)
      {
  	    ServiceRef serviceRef = (ServiceRef)firstSel;
        return J2EEActionAdapterFactory.getWSDLURI(serviceRef);
      }
  	  if (firstSel instanceof WSDLResourceImpl)
  	  {
  	    WSDLResourceImpl wsdlRI = (WSDLResourceImpl)firstSel;
  	    return J2EEActionAdapterFactory.getWSDLURI(wsdlRI);
  	  }
  	  if (firstSel instanceof String)
  	    return (String)firstSel;
  	}
    return "";
  }
 
  public void setInitialSelection( IStructuredSelection selection )
  {
    selection_ = selection;   
  }
  
  public boolean getGenWSIL()
  {
    return false;
  }
  
  public String getWsilURI()
  {
  	String wsURI = getWebServiceURI();
  	if (wsURI != null && wsURI.length() > 0 && wsURI.endsWith("wsdl"))
  	{
      StringBuffer sb = new StringBuffer(wsURI.substring(0, wsURI.length()-4));
      sb.append("wsil");
      return sb.toString();
  	}
    return "";
  }
  
  public IProject getProject()
  {
  	IProject p = getProjectFromInitialSelection(selection_);
  	return p;
  }
  
  public String getComponentName()
  {
    String cname = getComponentNameFromInitialSelection(selection_);
    return cname;
  }
  
  private IProject getProjectFromInitialSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          if (resource==null) 
            return null;
          IProject p = ResourceUtils.getProjectOf(resource.getFullPath());
          return p;
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }
  
  private String getComponentNameFromInitialSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          if (resource==null) 
            return null;
          
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
          if (comp!=null)
          {
            return comp.getName();
          }
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }  
}
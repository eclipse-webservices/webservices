/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070116   159618 makandre@ca.ibm.com - Andrew Mak, Project and EAR not defaulted properly when wizard launched from JSR-109 Web services branch in J2EE Project Explorer
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files
 * 20070410   181827 kathy@ca.ibm.com - Kathy Chan
 * 20080220   219537 makandre@ca.ibm.com - Andrew Mak
 * 20080501   229728 makandre@ca.ibm.com - Andrew Mak, uppercase .WSDL cannot be found by the Web Service Client wizard
 * 20081208   257618 mahutch@ca.ibm.com - Mark Hutchinson, Add Mechanism for Adopters to map Services to WSDL URLs
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu, Pluggable IFile to URI Converter
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.converter.IIFile2UriConverter;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;
import org.eclipse.wst.ws.internal.util.UniversalPathTransformer;
import org.eclipse.wst.ws.internal.wsfinder.WSDLURLStringWrapper;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class WSDLSelectionWidgetDefaultingCommand extends AbstractDataModelOperation
{
  private IStructuredSelection selection_;
  private String uri_;
  
  public String getWebServiceURI()
  {
	if (uri_ != null)
		return uri_;
	  
    uri_ = "";
    
  	if (selection_ != null && !selection_.isEmpty())
  	{
  	  Object firstSel = selection_.getFirstElement();
  	  if (firstSel instanceof IFile)
  	  {
  	    IFile ifile = (IFile)firstSel;
  	    String ext = ifile.getFileExtension();
  	    if (ext != null && (ext.equalsIgnoreCase("wsdl") || ext.equalsIgnoreCase("wsil") || ext.equalsIgnoreCase("html")))
  	    {  	    
  	      IIFile2UriConverter converter = WSPlugin.getInstance().getIFile2UriConverter();
  	      boolean allowBaseConversionOnFailure = true;
  	      if (converter != null)
  	      {
  	    	  uri_ = converter.convert(ifile);
  	    	  if (uri_ == null)
  	    		  allowBaseConversionOnFailure = converter.allowBaseConversionOnFailure();
  	    		  
  	      }
  	      if ((uri_ == null || uri_.length() == 0) && allowBaseConversionOnFailure)
  	        uri_ = ifile.getFullPath().toString();
  	    }
  	  } 
  	  else if (Platform.getAdapterManager().hasAdapter(firstSel, WSDLURLStringWrapper.class.getName())) {
		Object adaptedObject = Platform.getAdapterManager().loadAdapter(firstSel, WSDLURLStringWrapper.class.getName());
  		WSDLURLStringWrapper wrapper = (WSDLURLStringWrapper)adaptedObject;
  		uri_ = wrapper.getWSDLURLString();  		
  	  }
  	  else if (firstSel instanceof ServiceImpl)
      {
        ServiceImpl serviceImpl = (ServiceImpl)firstSel;
        uri_ = J2EEActionAdapterFactory.getWSDLURI(serviceImpl);
      } else if (firstSel instanceof ServiceRef)
      {
  	    ServiceRef serviceRef = (ServiceRef)firstSel;
        uri_ = J2EEActionAdapterFactory.getWSDLURI(serviceRef);
      } else if (firstSel instanceof WSDLResourceImpl)
  	  {
  	    WSDLResourceImpl wsdlRI = (WSDLResourceImpl)firstSel;
  	    uri_ = J2EEActionAdapterFactory.getWSDLURI(wsdlRI);
  	  } else if (firstSel instanceof String)
  	  {
  	    uri_ = (String)firstSel;
  	  } else {
  		String adaptedUri = AdapterUtils.getAdaptedWSDL(firstSel);
  		if (adaptedUri != null) {
  			uri_ = adaptedUri;
  		}
  	  }
  	  
  	  uri_ = UniversalPathTransformer.toPath(uri_);
  	}
    return uri_;
  }
 
  public void setInitialSelection( IStructuredSelection selection )
  {
    selection_ = selection;   
    uri_ = null;
  }
  
  public boolean getGenWSIL()
  {
    return false;
  }
  
  public String getWsilURI()
  {
  	String wsURI = getWebServiceURI();
  	if (wsURI != null && wsURI.length() > 0 && wsURI.toLowerCase().endsWith("wsdl"))
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
  
  private boolean hasProtocol(String url) {	  
    return url.indexOf(":") != -1;	  
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
          if (resource==null) {
            String uri = getWebServiceURI();
            if (hasProtocol(uri)) return null;
            return ResourceUtils.getProjectOf(new Path(uri));
          }
          else
            return ResourceUtils.getProjectOf(resource.getFullPath());
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
          IVirtualComponent comp;
          if (resource==null) {
            String uri = getWebServiceURI();
            if (hasProtocol(uri)) return null;              
            comp = ResourceUtils.getComponentOf(new Path(uri)) ;
          }
          else 
            comp = ResourceUtils.getComponentOf(resource);
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

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
	  return Status.OK_STATUS;
  }    
     
}

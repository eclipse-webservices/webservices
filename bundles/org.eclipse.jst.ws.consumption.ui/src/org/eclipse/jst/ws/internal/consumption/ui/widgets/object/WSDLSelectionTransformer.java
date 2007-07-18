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
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 * 20070713   191357 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.net.MalformedURLException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.internal.impl.ServiceRefImpl;
import org.eclipse.jst.j2ee.webservice.wsdd.BeanLink;
import org.eclipse.jst.j2ee.webservice.wsdd.ServiceImplBean;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class WSDLSelectionTransformer implements Transformer
{
  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
     IStructuredSelection selection = (IStructuredSelection)value;
     if (selection != null && !selection.isEmpty()) {
     
      Object sel = selection.getFirstElement();
      if (sel instanceof IResource)
      {
        try
        {
          return new StructuredSelection(((IResource)sel).getLocation().toFile().toURL().toString());
        }
        catch (MalformedURLException murle)
        {
        }
      }
      else if (sel instanceof ServiceImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((ServiceImpl)sel));
      }
      else if (sel instanceof ServiceRefImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((ServiceRefImpl)sel));
      }
      else if (sel instanceof WSDLResourceImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((WSDLResourceImpl)sel));
      }
      /*
       * TODO - Remove the cases below after bug 170834 is fixed
       * 
       * WSDLSelectionTransformer should not need to handle ServiceImplBean and BeanLink which
       * represent service classes.  These catches are here for now since we incorrectly allow
       * the wizard to default to top-down sceanrio when launch from these objects in the JSR-109
       * branch. 
       */
      else if (sel instanceof ServiceImplBean)
      {
        return new StructuredSelection(getWSDLURI((ServiceImplBean) sel));
      }
      else if (sel instanceof BeanLink)
      {
        return new StructuredSelection(getWSDLURI((BeanLink) sel));
      } else {
    	  String wsdlURI = AdapterUtils.getAdaptedWSDL(sel);
    	  if (wsdlURI != null) {
    		  return new StructuredSelection(wsdlURI);
    	  }
      }
     }
    }
    return value;
  }
  
  private String getWSDLURI(ServiceImplBean bean) {
	  EObject eObject = bean.eContainer();
	  if (eObject == null)
		  return "";
          
	  eObject = eObject.eContainer();
      
	  if (eObject instanceof WebServiceDescription) {
		  WebServiceDescription wsd = (WebServiceDescription) eObject;
		  IProject project = ProjectUtilities.getProject(wsd);    	  
    	  
		  // getWebContentPath returns the "WebContent" folder path for Web projects
		  // for EJB projects, it returns the "ebjModule" folder path
		  IPath path = J2EEUtils.getWebContentPath(project);    	 
		  path = path.append(wsd.getWsdlFile()); 
    	  
		  return path.toString();    	    
	  }
	  return "";
  }
  
  private String getWSDLURI(BeanLink link) {
	  EObject eObject = link.eContainer();
	  if (eObject instanceof ServiceImplBean)
		  return getWSDLURI((ServiceImplBean) eObject);
	  return "";
  }
}

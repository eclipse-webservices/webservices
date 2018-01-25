/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
 * 20080212   208795 ericdp@ca.ibm.com - Eric D. Peters, WS wizard framework should support EJB 3.0
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.webservice.wsdd.EJBLink;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.ServiceImplBean;
import org.eclipse.jst.javaee.ejb.SessionBean;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class EJBSelectionTransformer implements Transformer
{

  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
      Object sel = ((IStructuredSelection)value).getFirstElement();
      if (sel instanceof EnterpriseBean)
      {
        return new StructuredSelection(((EnterpriseBean)sel).getName());
      } 
      else if (sel instanceof SessionBean) 
      {
    	  return new StructuredSelection(((SessionBean)sel).getEjbName());
      }
      else if (sel instanceof ServiceImplBean)
      {
        return new StructuredSelection(getBeanName((ServiceImplBean) sel));
      }
      else if (sel instanceof EJBLink)
      {
        return new StructuredSelection(getBeanName((EJBLink) sel));
      }
    }
    return value;
  }
  
  private String getBeanName(ServiceImplBean bean) {
	  EObject eObject = bean.eContainer();
	  if (eObject instanceof PortComponent) {
		  PortComponent pc = (PortComponent) eObject;
		  return pc.getPortComponentName();	      	
	  }
	  return "";
  }
  
  private String getBeanName(EJBLink link) {
	  EObject eObject = link.eContainer();
	  if (eObject instanceof ServiceImplBean)
		  return getBeanName((ServiceImplBean) eObject);
	  return "";
  }
}

/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.label.providers;

import java.util.HashMap;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.w3c.dom.Node;


public class SOAPLabelProvider extends LabelProvider
{                                                     
  protected HashMap map = new HashMap();
                         
  public SOAPLabelProvider()
  {
    map.put(SOAPConstants.ADDRESS_ELEMENT_TAG, "icons/soapaddress_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.BINDING_ELEMENT_TAG, "icons/soapbinding_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.BODY_ELEMENT_TAG, "icons/soapbody_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.FAULT_ELEMENT_TAG, "icons/soapfault_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.HEADER_ELEMENT_TAG, "icons/soapheader_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.HEADER_FAULT_ELEMENT_TAG, "icons/soapheaderfault_obj.gif"); //$NON-NLS-1$
    map.put(SOAPConstants.OPERATION_ELEMENT_TAG, "icons/soapoperation_obj.gif"); //$NON-NLS-1$
  }

  public Image getImage(Object object) 
  {    
    Node node = (Node)object;
    String imageName = (String)map.get(node.getLocalName());                        
	  return imageName != null ? WSDLEditorPlugin.getInstance().getImage(imageName) : null; 
  }
  
  public String getText(Object object)  
  {
    Node node = (Node)object;
    return node.getNodeName();
  }
}
/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.label.providers;

import java.util.HashMap;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.w3c.dom.Node;


public class HTTPLabelProvider extends LabelProvider
{                                                   
  protected HashMap map = new HashMap();
                         
  public HTTPLabelProvider()
  {                    
    map.put(HTTPConstants.ADDRESS_ELEMENT_TAG, "icons/httpaddress_obj.gif"); //$NON-NLS-1$
    map.put(HTTPConstants.BINDING_ELEMENT_TAG, "icons/httpbinding_obj.gif"); //$NON-NLS-1$
    map.put(HTTPConstants.OPERATION_ELEMENT_TAG, "icons/httpoperation_obj.gif");     //$NON-NLS-1$
    map.put(HTTPConstants.URL_ENCODED_ELEMENT_TAG, "icons/httpurlencoded_obj.gif"); //$NON-NLS-1$
    map.put(HTTPConstants.URL_REPLACEMENT_ELEMENT_TAG, "icons/httpurlreplacement_obj.gif"); //$NON-NLS-1$
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
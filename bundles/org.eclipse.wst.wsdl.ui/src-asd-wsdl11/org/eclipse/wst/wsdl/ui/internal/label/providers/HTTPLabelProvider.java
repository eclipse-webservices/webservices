/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.w3c.dom.Node;


public class HTTPLabelProvider extends LabelProvider
{                                                   
  protected final static String ADDRESS         = "address";
  protected final static String BINDING         = "binding";
  protected final static String OPERATION       = "operation";
  protected final static String URL_ENCODED     = "urlEncoded";
  protected final static String URL_REPLACEMENT = "urlReplacement";

  protected HashMap map = new HashMap();
                         
  public HTTPLabelProvider()
  {                    
    map.put(ADDRESS, "icons/httpaddress_obj.gif");
    map.put(BINDING, "icons/httpbinding_obj.gif");
    map.put(OPERATION, "icons/httpoperation_obj.gif");    
    map.put(URL_ENCODED, "icons/httpurlencoded_obj.gif");
    map.put(URL_REPLACEMENT, "icons/httpurlreplacement_obj.gif");
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
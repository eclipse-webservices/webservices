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

package org.eclipse.jst.ws.util;

/**
 * @author gilberta
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoapElementHelper {
  public static javax.xml.soap.SOAPElement createSOAPElementFromXMLString(String xmlString) throws SoapElementRuntimeException
  {
    throw new SoapElementRuntimeException("SoapElement is not supported by the sample on the current runtime. Please use WebSphere v5.1 or higher.");    
  }

  public static java.lang.String soapElementWriter(javax.xml.soap.SOAPElement node,java.lang.StringBuffer buffer)
  {
    if (node == null ) {
      return "";
    }
    
      buffer.append(JspUtils.markup("<" + node.getElementName().getLocalName()));
      java.util.Iterator attrs = node.getAllAttributes();
      while(attrs.hasNext()) {
        javax.xml.soap.Name attr = (javax.xml.soap.Name)attrs.next();
        buffer.append(" " + attr.getQualifiedName() + "=\"" + JspUtils.markup(node.getAttributeValue(attr)) + "\"");
      }
      buffer.append(JspUtils.markup(">"));
      java.util.Iterator children = node.getChildElements();
      if ( children != null ) {
        while(children.hasNext()){
          javax.xml.soap.Node childNode = (javax.xml.soap.Node)children.next();
          if(childNode instanceof javax.xml.soap.SOAPElement){
            buffer.append("<br>");
            soapElementWriter((javax.xml.soap.SOAPElement)childNode,buffer);
          }
          else
            buffer.append(JspUtils.markup(((javax.xml.soap.Text)childNode).getValue())); 
        }
        buffer.append(JspUtils.markup("</" + node.getElementName().getLocalName() + ">"));
	  }
      return buffer.toString();
	}
}

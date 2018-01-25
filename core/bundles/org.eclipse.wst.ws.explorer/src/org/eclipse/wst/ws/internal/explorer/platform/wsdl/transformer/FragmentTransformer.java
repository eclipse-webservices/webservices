/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transformer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.wsdl.Part;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FragmentTransformer implements ITransformer
{
  protected Controller controller;

  public FragmentTransformer(Controller controller)
  {
    this.controller = controller;
  }

  public Hashtable normalize(Hashtable properties)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("<root>");
    WSDLOperationElement operElement = (WSDLOperationElement)controller.getWSDLPerspective().getNodeManager().getSelectedNode().getTreeElement();
    Iterator it = operElement.getOrderedBodyParts().iterator();
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = (IXSDFragment)operElement.getFragment(part);
      Element[] elements = new Element[0];
      try
      {
        elements = frag.genInstanceDocumentsFromParameterValues(!operElement.isUseLiteral(), new Hashtable(), XMLUtils.createNewDocument(null));
      }
      catch (ParserConfigurationException pce)
      {
      }
      for (int i = 0; i < elements.length; i++)
        sb.append(XMLUtils.serialize(elements[i], true));
    }
    sb.append("</root>");
    properties.put(FragmentConstants.SOURCE_CONTENT, sb.toString());
    return properties;
  }

  public Hashtable deNormalize(Hashtable properties)
  {
    String source = (String) properties.get(FragmentConstants.SOURCE_CONTENT);
    if (source != null)
    {
      try
      {
        Element root = XMLUtils.stringToElement(source);
        if (root != null)
        {
          NodeList childNodes = root.getChildNodes();
          Vector childrenVector = new Vector();
          for (int i = 0; i < childNodes.getLength(); i++)
          {
            Node child = childNodes.item(i);
            if (child instanceof Element)
              childrenVector.add(child);
          }
          Element[] children = (Element[])childrenVector.toArray(new Element[0]);
          WSDLOperationElement operElement = (WSDLOperationElement) controller.getWSDLPerspective().getNodeManager().getSelectedNode().getTreeElement();
          Iterator it = operElement.getOrderedBodyParts().iterator();
          while (it.hasNext())
          {
            Part part = (Part)it.next();
            IXSDFragment frag = (IXSDFragment)operElement.getFragment(part);
            frag.setParameterValuesFromInstanceDocuments(children);
          }
        }
      }
      catch (Throwable t)
      {
      }
    }
    return properties;
  }
}

/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.ISOAPEncodingWrapperFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.xsd.XSDTypeDefinition;

import org.w3c.dom.*;
import java.util.Vector;
import java.util.Hashtable;

public class SOAPEncodingWrapperFragment extends XSDDelegationFragment implements ISOAPEncodingWrapperFragment
{
  private static final String ID = "id";
  private static final String HREF = "href";
  private static final String POUND = "#";

  private Hashtable uriReferenceTable_;

  public SOAPEncodingWrapperFragment(String id, String name, XSDToFragmentConfiguration config)
  {
    super(id, name, null);
    uriReferenceTable_ = null;
  }

  public void setXSDToFragmentConfiguration(XSDToFragmentConfiguration config)
  {
    getXSDDelegationFragment().setXSDToFragmentConfiguration(config);
  }

  public XSDToFragmentConfiguration getXSDToFragmentConfiguration()
  {
    return getXSDDelegationFragment().getXSDToFragmentConfiguration();
  }

  public void setXSDTypeDefinition(XSDTypeDefinition typeDef)
  {
    getXSDDelegationFragment().setXSDTypeDefinition(typeDef);
  }

  public XSDTypeDefinition getXSDTypeDefinition()
  {
    return getXSDDelegationFragment().getXSDTypeDefinition();
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    for (int i = 0; i < instanceDocuments.length; i++)
      instanceDocuments[i] = resolveURIReferences(uriReferenceTable_, instanceDocuments[i]);
    return getXSDDelegationFragment().setParameterValuesFromInstanceDocuments(instanceDocuments);
  }

  public void setURIReferences(Hashtable uriReferences)
  {
    uriReferenceTable_ = uriReferences;
  }

  public static Hashtable parseURIReferences(Element element, boolean overwrites)
  {
    Hashtable uriReferences = new Hashtable();
    parseURIReferences(uriReferences, element, overwrites);
    return uriReferences;
  }

  private static boolean parseURIReferences(Hashtable uriReferences, Element element, boolean overwrites)
  {
    boolean resolved = true;
    if (element != null)
    {
      URIReference ref = null;
      String id = element.getAttribute(ID);
      resolved = !element.hasAttribute(HREF);
      if (id != null && id.length() > 0 && (overwrites || !uriReferences.contains(id)))
      {
        ref = new URIReference(element, true);
        uriReferences.put(id, ref);
      }
      NodeList nl = element.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node node = nl.item(i);
        if (node instanceof Element)
          resolved = parseURIReferences(uriReferences, (Element)node, overwrites) && resolved;
      }
      if (ref != null)
        ref.setResolved(resolved);
    }
    return resolved;
  }

  public static Element resolveURIReferences(Hashtable uriReferences, Element element)
  {
    if (uriReferences != null && !uriReferences.isEmpty())
      return resolveURIReferences(uriReferences, element.getOwnerDocument(), element, new Vector());
    else
      return element;
  }

  private static Element resolveURIReferences(Hashtable uriReferences, Document doc, Element element, Vector usedIds)
  {
    String href = element.getAttribute(HREF);
    Element resolvedElement = element;
    boolean resolved = false;
    if (href != null && href.length() > 0)
    {
      URIReference ref = null;
      if (href.indexOf(POUND) != -1)
        href = href.substring(1, href.length());
      if (!usedIds.contains(href))
      {
        ref = (URIReference)uriReferences.get(href);
        if (ref != null)
        {
          usedIds.add(href);
          Element elementRef = ref.getRefElement();
          resolved = ref.getResolved();
          resolvedElement = doc.createElement(element.getTagName());
          NodeList nl = elementRef.getChildNodes();
          for (int i = 0; i < nl.getLength(); i++)
          {
            Node node = nl.item(i);
            if (node != null)
              resolvedElement.appendChild(doc.importNode(node, true));
          }
        }
      }
    }
    if (!resolved)
    {
      NodeList nl = resolvedElement.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node node = nl.item(i);
        if (node instanceof Element)
        {
          Element child = resolveURIReferences(uriReferences, resolvedElement.getOwnerDocument(), (Element)node, usedIds);
		  if (child != node)
            resolvedElement.replaceChild(child, node);
        }
      }
    }
    if (href != null)
      usedIds.remove(href);
    return resolvedElement;
  }

  private static class URIReference
  {
    private Element ref_;
    private boolean resolved_;

    public URIReference(Element ref, boolean resolved)
    {
      ref_ = ref;
      resolved_ = resolved;
    }

    public Element getRefElement()
    {
      return ref_;
    }

    public void setRefElement(Element ref)
    {
      ref_ = ref;
    }

    public boolean getResolved()
    {
      return resolved_;
    }

    public void setResolved(boolean resolved)
    {
      resolved_ = resolved;
    }
  }
}

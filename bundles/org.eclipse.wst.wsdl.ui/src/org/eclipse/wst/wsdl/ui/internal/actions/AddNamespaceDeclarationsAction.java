/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.util.Hashtable;

import org.eclipse.jface.action.Action;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class AddNamespaceDeclarationsAction extends Action
{
  protected Element ownerElement;
  protected String[] namespaceNames;
  protected String[] preferredPrefixes;

  public AddNamespaceDeclarationsAction(Element ownerElement, String[] namespaceNames, String[] preferredPrefixes)
  {
    this.ownerElement = ownerElement;
    this.namespaceNames = namespaceNames;
    this.preferredPrefixes = preferredPrefixes;
  }

  public AddNamespaceDeclarationsAction(Element ownerElement, String namespaceName, String preferredPrefix)
  {
    this.ownerElement = ownerElement;
    namespaceNames = new String[1];
    namespaceNames[0] = namespaceName;

    preferredPrefixes = new String[1];
    preferredPrefixes[0] = preferredPrefix;
  }

  public void run()
  {
    if (ownerElement != null)
    {
      NamedNodeMap map = ownerElement.getAttributes();
      Hashtable table = new Hashtable();
      Hashtable prefixTable = new Hashtable();

      int mapLength = map.getLength();
      for (int i = 0; i < mapLength; i++)
      {
        Attr attribute = (Attr) map.item(i);
        String attributeName = attribute.getName();
        if (attributeName.startsWith("xmlns:"))
        {
          table.put(attribute.getValue(), Boolean.TRUE);
          prefixTable.put(attributeName.substring(6), Boolean.TRUE);
        }
        else if (attributeName.equals("xmlns"))
        {
          table.put(attribute.getValue(), Boolean.TRUE);
          prefixTable.put("", Boolean.TRUE);
        }
      }

      for (int i = 0; i < namespaceNames.length; i++)
      {
        String namespace = namespaceNames[i];
        if (table.get(namespace) == null)
        {
          String prefix = (i < preferredPrefixes.length) ? preferredPrefixes[i] : "p0";
          if (prefixTable.get(prefix) != null)
          {
            prefix = computeUniquePrefix("p", prefixTable);
          }

          String attributeName = prefix.length() > 0 ? "xmlns:" + prefix : "xmlns";
          ownerElement.setAttribute(attributeName, namespace);
        }
      }
    }
  }

  protected String computeUniquePrefix(String base, Hashtable table)
  {
    int i = 0;
    String prefix = base;
    while (true)
    {
      if (table.get(prefix) == null)
      {
        break;
      }
      else
      {
        prefix = base + i;
        i++;
      }
    }
    return prefix;
  }
}
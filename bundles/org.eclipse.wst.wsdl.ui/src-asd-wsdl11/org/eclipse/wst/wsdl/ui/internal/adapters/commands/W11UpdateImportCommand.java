/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.adapters.commands;


import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.w3c.dom.Element;


/**
 * Updates a import's properties: namespaceURI, locationURI and potentially creates a unique prefix.
 */
public class W11UpdateImportCommand extends W11TopLevelElementCommand
{
  private Import theImport;

  private String prefix;

  private String locationURI;

  private String namespaceURI;

  public W11UpdateImportCommand(Import theImport, String locationURI, String namespaceURI, String prefix)
  {
    super(Messages._UI_ACTION_UPDATE_IMPORT, theImport.getEnclosingDefinition());
    this.theImport = theImport;
    this.namespaceURI = namespaceURI;
    this.prefix = prefix;
    this.locationURI = locationURI;
  }

  public void execute()
  {
    super.execute();
    Element definitionElement = definition.getElement();
    try
    {
      // TODO vb Refactor all commands to push the begin/end recording into the base class
      // and implement the base class' method as a template method doExecute to do all the work.

      beginRecording(definitionElement);

      theImport.setLocationURI(locationURI);
      
      // TODO vb There's a limitation in the WSDL EMF model where the namespaces map is not
      // properly reconciling with the DOM. Because of this, the code here updates the 
      // underlying DOM directly.
      
      // The fact that the prefix is on not a property of the import object also causes
      // some issues with how the properties import section is refreshed on undo/redo. 
      // Because of this, the prefix updating is done between the location and namespace update. 
      
      Definition definition = theImport.getEnclosingDefinition();
      if (definition != null)
      {
        String existingPrefix = definition.getPrefix(namespaceURI);
        if (existingPrefix == null)
        {
          String uniquePrefix = getImportPrefix();
          definitionElement.setAttribute("xmlns:" + uniquePrefix, namespaceURI); //$NON-NLS-1$
        }
      }
      
      theImport.setNamespaceURI(namespaceURI);

      ((ImportImpl)theImport).importDefinitionOrSchema();
    }
    finally
    {
      endRecording(definitionElement);
    }

  }

  private String getImportPrefix()
  {
    String uniquePrefix;

    if (prefix != null && prefix.trim().equals("")) //$NON-NLS-1$
    {
      URI uri = URI.createPlatformResourceURI(locationURI, false);
      uniquePrefix = getUniquePrefix(definition, uri.fileExtension());
    }
    else
    {
      uniquePrefix = prefix;
    }
    return uniquePrefix;
  }

  private String getUniquePrefix(Definition definition, String initPrefix)
  {
    String uniquePrefix;
    Map map = definition.getNamespaces();

    if (definition.getNamespace(initPrefix) == null)
    {
      uniquePrefix = initPrefix;
    }
    else
    // if used, then try to create a unique one
    {
      String tempPrefix = initPrefix;
      int i = 1;
      while (map.containsKey(tempPrefix + i))
      {
        i++;
      }
      uniquePrefix = tempPrefix + i;
    }
    return uniquePrefix;
  }
}

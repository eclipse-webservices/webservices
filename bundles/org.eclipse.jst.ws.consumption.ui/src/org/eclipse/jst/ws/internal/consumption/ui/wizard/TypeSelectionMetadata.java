/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.StringTokenizer;

  //
  // This class holds the set of distinguishing factors in determining 
  // the default Web service type.  The default Web Service type will be determined
  // by pattern matching the initial selection with the supported extension 
  // and the resource type metadata specified in the manifest file.
  //
  public class TypeSelectionMetadata
  {
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

    public String[] extensionMeta;
    public String[] resourceTypeMeta;

    public TypeSelectionMetadata(String extensionMD, String resourceTypeMD)
    {
      setExtensionMetadata(extensionMD);
      setResourceTypeMetadata(resourceTypeMD);
    }
    
    public void setExtensionMetadata(String extensionMetadata)
    {
      StringTokenizer st = new StringTokenizer(extensionMetadata);
      extensionMeta = new String[st.countTokens()];
      int i=0;
      while (st.hasMoreElements())
      {
        String exten = (String)st.nextToken();
        if (exten!=null)
        {
          extensionMeta[i]=exten;
        }
        i++;
      }

    }

    public String[] getExtensionMetadata()
    {
      return extensionMeta;
    }
    
    public void setResourceTypeMetadata(String resourceTypeMetadata)
    {
      StringTokenizer st = new StringTokenizer(resourceTypeMetadata);
      resourceTypeMeta = new String[st.countTokens()];
      int i=0;
      while (st.hasMoreElements())
      {
        String resourceType = (String)st.nextToken();
        if (resourceType!=null)
        {
          resourceTypeMeta[i]=resourceType;
        }
        i++;
      }
    }

    public String[] getResourceTypeMetadata()
    {
      return resourceTypeMeta;
    }

  }


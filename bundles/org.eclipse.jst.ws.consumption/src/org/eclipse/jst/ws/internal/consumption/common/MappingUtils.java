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

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.common.ResourceUtils;


/**
* This class contains useful methods for working with Server plugin functions
*/
public final class MappingUtils
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private static final String DEFAULT_SERVICE_EXT = "Service.wsdl";
  private static final String DEFAULT_BINDING_EXT = "Binding.wsdl";
  private static final String DEFAULT_V4_SERVICE_EXT = "-service.wsdl";
  private static final String DEFAULT_V4_BINDING_EXT = "-binding.wsdl";
  private static final String WSDL_EXTENSION = ".wsdl";
  private static final String WSDL_FOLDER = "wsdl";
  public static final String DEFAULT_SKELETON_PACKAGENAME = "service";

  /**
  * Returns the WSDL folder path
  * @param project the project
  */
  public static IPath getWSDLFolderPath (IProject project)
  {
    IPath wsdlFolder=null;
  	IPath webModuleServerRoot = ResourceUtils.getWebModuleServerRoot( project ).getFullPath();
  	if (webModuleServerRoot != null )
  		wsdlFolder = webModuleServerRoot.append(WSDL_FOLDER);
    return wsdlFolder;
  }
    

   /**
  * Returns the base name based on WSDL file name
  * @param wsdlName WSDL file name
  */
  public static String getBaseName (String wsdlLocation )
  {
  	String wsdlName=wsdlLocation;
  	
  	int index = wsdlLocation.lastIndexOf('/');
    if (index!= -1) {
      wsdlName = wsdlLocation.substring(index+1,wsdlName.length());
    }
    
    String baseName=wsdlName;
    if ( wsdlName.endsWith(DEFAULT_SERVICE_EXT) ) {
      baseName = wsdlName.substring(0,(wsdlName.length() - DEFAULT_SERVICE_EXT.length()));
    } else if ( wsdlName.toLowerCase().endsWith(DEFAULT_V4_SERVICE_EXT) ) {
      baseName = wsdlName.substring(0,(wsdlName.length() - DEFAULT_V4_SERVICE_EXT.length()));
    } else if (wsdlName.endsWith(DEFAULT_BINDING_EXT)) {
      baseName = wsdlName.substring(0,(wsdlName.length()-DEFAULT_BINDING_EXT.length()));
    } else if (wsdlName.toLowerCase().endsWith(DEFAULT_V4_BINDING_EXT)) {
      baseName = wsdlName.substring(0,(wsdlName.length()-DEFAULT_V4_BINDING_EXT.length()));
    } else if (wsdlName.toLowerCase().endsWith(WSDL_EXTENSION)) {
    	baseName = wsdlName.substring(0,(wsdlName.length() - WSDL_EXTENSION.length()));
    } else {
      // no recognizable suffix, just remove extension, if any
      int index4 = baseName.lastIndexOf('.');
      if (index4!=-1)
        baseName = baseName.substring(0, index4);
    }
    return baseName;
  }

}

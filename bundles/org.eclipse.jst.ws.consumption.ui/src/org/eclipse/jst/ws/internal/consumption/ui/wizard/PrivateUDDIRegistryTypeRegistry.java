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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.ui.uddi.PrivateUDDIRegistryType;


public class PrivateUDDIRegistryTypeRegistry
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private static PrivateUDDIRegistryTypeRegistry instance_;

  private PrivateUDDIRegistryTypeRegistry() {
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceClientTypeRegistry object.
  */
  public static PrivateUDDIRegistryTypeRegistry getInstance() {
    if (instance_ == null) {
      instance_ = new PrivateUDDIRegistryTypeRegistry();
    }
    
    return instance_;
  }

  public PrivateUDDIRegistryType getPrivateUDDIRegistryType() {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "privateUDDIRegistryType");

    try {
      Object configElement = ((config.length > 0) ? config[0].createExecutableExtension("class") : null);
      if(configElement != null && configElement instanceof PrivateUDDIRegistryType) 
        return (PrivateUDDIRegistryType)configElement; 
      else
        return null;
    }
    catch (CoreException e) {
      return null;
    }
  }
}

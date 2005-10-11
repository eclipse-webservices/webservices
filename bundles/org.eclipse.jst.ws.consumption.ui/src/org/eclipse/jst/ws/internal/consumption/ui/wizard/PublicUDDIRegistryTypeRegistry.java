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

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.Vector;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.uddi.PublicUDDIRegistryType;


public class PublicUDDIRegistryTypeRegistry
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";

  private static PublicUDDIRegistryTypeRegistry instance_;
  private Vector types_;
  private Vector configElements_;

  private PublicUDDIRegistryTypeRegistry() {
    types_ = new Vector();
    configElements_ = new Vector();

    init();
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceClientTypeRegistry object.
  */
  public static PublicUDDIRegistryTypeRegistry getInstance() {
    if (instance_ == null) {
      instance_ = new PublicUDDIRegistryTypeRegistry();
    }
    
    return instance_;
  }

  public void init() {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "publicUDDIRegistryType");

    for (int i = 0; i < config.length; i++) {
      try {
        Object configElement = config[i].createExecutableExtension("class");

        if( configElement instanceof PublicUDDIRegistryType)  {
          types_.add(configElement);
          configElements_.add(config[i]);
        }
      }
      catch (CoreException e) {}
    }
  }

  public PublicUDDIRegistryType[] getAllPublicUDDIRegistryTypes() {
    PublicUDDIRegistryType[] types = new PublicUDDIRegistryType[types_.size()];
    for (int i = 0; i < types_.size(); i++) {
      types[i] = (PublicUDDIRegistryType)types_.elementAt(i);
    }
    return types;
  }

  public PublicUDDIRegistryType getPublicUDDIRegistryTypeByID(String id) {
    for (int i = 0; i < configElements_.size(); i++) {
      IConfigurationElement c = (IConfigurationElement)configElements_.elementAt(i);
      if (c.getAttribute("id").equals(id))
        return (PublicUDDIRegistryType)types_.elementAt(i);
    }
    return null;
  }

  public String getPublicUDDIRegistryTypeIDByName(String name) {
    for (int i = 0; i < types_.size(); i++) {
      PublicUDDIRegistryType type = (PublicUDDIRegistryType)types_.elementAt(i);
      if (type.getName().equals(name))
        return ((IConfigurationElement)configElements_.elementAt(i)).getAttribute("id");
    }
    return null;
  }

}

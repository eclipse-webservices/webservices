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

package org.eclipse.jst.ws.internal.ui.wse;

import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class WSExplorerTypesRegistry {

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private static WSExplorerTypesRegistry instance_;
  private Vector configElements_;
  private Vector wsexplorerTypes_;

  private WSExplorerTypesRegistry() {
    configElements_ = new Vector();
    wsexplorerTypes_ = new Vector();
    init();
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceClientTypeRegistry object.
  */
  public static WSExplorerTypesRegistry getInstance() {
    if (instance_ == null) {
      instance_ = new WSExplorerTypesRegistry();
    }
    return instance_;
  }

  public void init() {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor("org.eclipse.jst.ws.explorer", "wsexplorerType");
    for (int i = 0; i < config.length; i++) {
      try {
        Object configElement = config[i].createExecutableExtension("class");
        if (configElement instanceof WSExplorerType) {
          configElements_.add(config[i]);
          wsexplorerTypes_.add(configElement);
        }
      }
      catch (CoreException e) {
      }
    }
  }

  public int getNumOfTypes() {
    return wsexplorerTypes_.size();
  }

  public WSExplorerType[] getAllWSExplorerTypes() {
    WSExplorerType[] types = new WSExplorerType[wsexplorerTypes_.size()];
    wsexplorerTypes_.copyInto(types);
    return types;
  }

  public IConfigurationElement[] getAllConfigElements() {
    IConfigurationElement[] configElements = new IConfigurationElement[configElements_.size()];
    configElements_.copyInto(configElements);
    return configElements;
  }

  private WSExplorerType getWSExplorerTypeByAttribute(String attribute, String value) {
    for (int i = 0; i < configElements_.size(); i++) {
      IConfigurationElement configElement = (IConfigurationElement)configElements_.elementAt(i);
      if (configElement.getAttribute(attribute).equals(value))
        return (WSExplorerType)wsexplorerTypes_.elementAt(i);
    }
    return null;
  }

  public WSExplorerType getWSExplorerTypeByRank(int rank) {
    return getWSExplorerTypeByAttribute("rank", String.valueOf(rank));
  }

  public WSExplorerType getWSExplorerTypeByID(String id) {
    return getWSExplorerTypeByAttribute("id", id);
  }

  public WSExplorerType getWSExplorerTypeByName(String name) {
    return getWSExplorerTypeByAttribute("name", name);
  }
}

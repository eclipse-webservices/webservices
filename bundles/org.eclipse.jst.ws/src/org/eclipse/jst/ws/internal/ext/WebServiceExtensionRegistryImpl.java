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

package org.eclipse.jst.ws.internal.ext;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;


/**
* This is a generic registry which sets up general code in handling   
* IConfigElements for an extension
*/
public abstract class WebServiceExtensionRegistryImpl implements WebServiceExtensionRegistry
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  
  private Hashtable nameExtensionTable_;
  private Vector id_;
  private Vector label_;
  private LabelsAndIds labelsandids_;
    
  public WebServiceExtensionRegistryImpl()
  {
    nameExtensionTable_ = new Hashtable();
    label_ = new Vector();
    id_ = new Vector();
    loadExtensions();
  }
  
  /**
  * Children registries will have different extension types 
  * @return WebserviceExtension holds a config elem
  * for that extension capable of creating an executable file
  */
  public abstract WebServiceExtension createWebServiceExtension(IConfigurationElement configElement);

  /**
  * Children must implement how they get the IConfigurationElement[] 
  * @return IConfigurationElement[] an array of elements particular to that
  * extension
  */
  public abstract IConfigurationElement[] getConfigElements();

  /*
  * Loads WebServiceExtensions for WebServiceClientTest into this registry.
  * This is done by querying the plugin registry for all extensions
  * that extend webServiceClientTest  
  */
  private void loadExtensions ()
  {    
    IConfigurationElement[] config = getConfigElements();

    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      String label = elem.getAttribute( "label" );
      String id =   elem.getAttribute( "id" );      
      WebServiceExtension webServiceExtension = createWebServiceExtension(elem);	
      nameExtensionTable_.put(label,webServiceExtension);
      label_.add(label);
      id_.add(id);
    }
  }

  /**
  * Returns the names of all registered extensions
  * @return The names of all registered extensions.
  */
  public String[] getWebServiceExtensionNames ()
  {
    return (String[])nameExtensionTable_.keySet().toArray( new String[0] );
  }

  /**
   * Return the names and Ids 
   */
  public LabelsAndIds getLabelsAndIDs()
  {
  	labelsandids_ = new LabelsAndIds();
  	labelsandids_.setLabels_((String[])label_.toArray( new String[0] ));
  	labelsandids_.setIds_((String[])id_.toArray( new String[0] ));
  	return labelsandids_;
  }
  
  /**
  * Returns the extension object of the given name
  * @return WebServiceExtension object
  */
  public WebServiceExtension getWebServiceExtensionsByName( String name)
  {
    return (WebServiceExtension)nameExtensionTable_.get(name);
  }

  /**
  * Returns All extention objects in this registry
  *@return WebServiceExtension objects
  */
  public WebServiceExtension[] getWebServiceExtensions()
  {
    return (WebServiceExtension[])nameExtensionTable_.values().toArray( new WebServiceExtension[0] );
  }
  
}

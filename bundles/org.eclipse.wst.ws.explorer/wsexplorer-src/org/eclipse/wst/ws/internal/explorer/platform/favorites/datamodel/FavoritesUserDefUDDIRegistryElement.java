/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060912   141796 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.registry.IRegistryManager;
import org.eclipse.wst.ws.internal.registry.RegistryService;
import org.eclipse.wst.ws.internal.registry.UDDIRegistryService;

public class FavoritesUserDefUDDIRegistryElement extends FavoritesElement
{
  private List names;
  private List descs;
  private String version;
  private String defaultLogin;
  private String defaultPassword;
  private String inquiryURL;
  private String publishURL;
  private String secureInquiryURL;
  private String securePublishURL;
  private Taxonomy[] taxonomies;

  public String getDefaultLogin()
  {
    return defaultLogin;
  }

  public void setDefaultLogin(String defaultLogin)
  {
    this.defaultLogin = defaultLogin;
  }

  public String getDefaultPassword()
  {
    return defaultPassword;
  }

  public void setDefaultPassword(String defaultPassword)
  {
    this.defaultPassword = defaultPassword;
  }

  public Taxonomy[] getTaxonomies()
  {
    return taxonomies;
  }

  public void setTaxonomies(Taxonomy[] taxonomies)
  {
    this.taxonomies = taxonomies;
  }

  public FavoritesUserDefUDDIRegistryElement(String name, Model model)
  {
    super(name, model);
  }

  public List getDescs()
  {
    return descs;
  }

  public void setDescs(List descs)
  {
    this.descs = descs;
  }

  public String getInquiryURL()
  {
    return inquiryURL;
  }

  public void setInquiryURL(String inquiryURL)
  {
    this.inquiryURL = inquiryURL;
  }

  public List getNames()
  {
    return names;
  }

  public void setNames(List names)
  {
    this.names = names;
  }

  public String getPublishURL()
  {
    return publishURL;
  }

  public void setPublishURL(String publishURL)
  {
    this.publishURL = publishURL;
  }

  public String getSecureInquiryURL()
  {
    return secureInquiryURL;
  }

  public void setSecureInquiryURL(String secureInquiryURL)
  {
    this.secureInquiryURL = secureInquiryURL;
  }

  public String getSecurePublishURL()
  {
    return securePublishURL;
  }

  public void setSecurePublishURL(String securePublishURL)
  {
    this.securePublishURL = securePublishURL;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public String toString()
  {
    return getName();
  }
  
  public void refreshMeta(){
	    
	    RegistryService regService = RegistryService.instance();
	    IRegistryManager regManager = regService.getDefaultRegistryManager();
	    try
	    {
	      
	    	
	      regManager.refreshManager();
	      String[] regURIs = regManager.getRegistryURIs();
	      for (int i = 0; i < regURIs.length; i++)
	      {
	        Registry reg = regManager.loadRegistry(regURIs[i]);
	        if (reg instanceof UDDIRegistry)
	        {
	          UDDIRegistry uddiReg = (UDDIRegistry)reg;
	          List names = uddiReg.getName();
	          
	          if(uddiReg.getDiscoveryURL().equals(getInquiryURL())){
	          
	        	  setNames(names);
	        	  setDescs(uddiReg.getDescription());
	        	  setVersion(uddiReg.getVersion());
	        	  setDefaultLogin(uddiReg.getDefaultLogin());
	        	  setDefaultPassword(uddiReg.getDefaultPassword());
	        	  setInquiryURL(uddiReg.getDiscoveryURL());
	        	  setPublishURL(uddiReg.getPublicationURL());
	        	  setSecureInquiryURL(uddiReg.getSecuredDiscoveryURL());
	        	  setSecurePublishURL(uddiReg.getSecuredPublicationURL());
	        	  Taxonomy[] taxonomies = regManager.loadTaxonomies(UDDIRegistryService.instance().getTaxonomyURIs(uddiReg));
	        	  setTaxonomies(taxonomies);
	          }
	        }
	      }
	    }
	    catch (CoreException ce)
	    {
	      // TODO: Better error reporting
	      ce.printStackTrace();
	    }  
  }
}

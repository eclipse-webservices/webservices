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

package org.eclipse.wst.wsdl.validation.internal.ui.eclipse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.wsdl.validation.internal.Constants;
import org.eclipse.wst.wsdl.validation.internal.ui.WSDLConfigurator;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalogEntityHolder;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class required for eclipse.
 */
public class ValidateWSDLPlugin extends AbstractUIPlugin
{
  protected final String PROPERTIES_FILE = "validatewsdlui";
  protected static ValidateWSDLPlugin instance;
  protected ResourceBundle resourcebundle = null;
  protected ResourceBundle wsdlValidatorResourceBundle = null;

  /**
   * Constructor.
   */
  public ValidateWSDLPlugin(IPluginDescriptor descriptor)
  {
    super(descriptor);
//    instance = this;
//    wsdlValidatorResourceBundle = ResourceBundle.getBundle(Constants.WSDL_VALIDATOR_PROPERTIES_FILE);
//    resourcebundle = ResourceBundle.getBundle(PROPERTIES_FILE);
//
//    // Configure the XML catalog.
//    new ExtXMLCatalogPluginRegistryReader().readRegistry();
//    new WSDLValidatorPluginRegistryReader(
//      "extvalidator",
//      "extvalidator",
//      WSDLValidatorPluginRegistryReader.EXT_VALIDATOR)
//      .readRegistry();
//
//    // register any WSDL 1.1 validators defined
//    new WSDL11ValidatorPluginRegistryReader("wsdl11validator", "validator").readRegistry();
//
//    WSDLConfigurator.registerDefaultValidators(wsdlValidatorResourceBundle);
  }

  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    instance = this;
    wsdlValidatorResourceBundle = ResourceBundle.getBundle(Constants.WSDL_VALIDATOR_PROPERTIES_FILE);
    resourcebundle = ResourceBundle.getBundle(PROPERTIES_FILE);

    // Configure the XML catalog.
    new ExtXMLCatalogPluginRegistryReader().readRegistry();
    new WSDLValidatorPluginRegistryReader(
      "extvalidator",
      "extvalidator",
      WSDLValidatorPluginRegistryReader.EXT_VALIDATOR)
      .readRegistry();

    // register any WSDL 1.1 validators defined
    new WSDL11ValidatorPluginRegistryReader("wsdl11validator", "validator").readRegistry();

    WSDLConfigurator.registerDefaultValidators(wsdlValidatorResourceBundle);
  }
  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception
  {
    super.stop(context);
    XMLCatalog.reset();
  }
  
  /**
   * Return the instance of this plugin object.
   * 
   * @return the instance of this plugin object
   */
  public static ValidateWSDLPlugin getInstance()
  {
    return instance;
  }

  /**
   * Get the install URL of this plugin.
   * 
   * @return the install url of this plugin
   */
  public String getInstallURL()
  {
    try
    {
      return Platform.resolve(getBundle().getEntry("/")).getFile();
    }
    catch (IOException e)
    {
      return null;
    }
  }

  /*************************************************************
   * ResourceBundle helper methods
   * 
   *************************************************************/
  /**
   * Returns the resource bundle for this plugin.
   * 
   * @return the resource bundle for this plugin
   */
  public ResourceBundle getResourceBundle()
  {
    return resourcebundle;
  }

  /**
   * Returns the resource bundle for the WSDL validator.
   * 
   * @return the resource bundle for the WSDL validator
   */
  public ResourceBundle getWSDLValidatorResourceBundle()
  {
    return wsdlValidatorResourceBundle;
  }

  /**
   * Returns the string for the given id.
   * 
   * @param stringID - the id for the string
   * @return the string for the given id
   */
  public String getString(String stringID)
  {
    return getResourceBundle().getString(stringID);
  }
}

/**
 * This class reads the plugin manifests and registers each WSDLExtensionValidator
 */
class WSDLValidatorPluginRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.validation";
  protected static final String ATT_CLASS = "class";
  protected static final String ATT_NAMESPACE = "namespace";
  protected static final String ATT_RESOURCEBUNDLE = "resourcebundle";
  protected static final int WSDL_VALIDATOR = 0;
  protected static final int EXT_VALIDATOR = 1;
  protected String extensionPointId;
  protected String tagName;
  protected int validatorType;

  /**
   * 
   */
  public WSDLValidatorPluginRegistryReader(String extensionPointId, String tagName, int validatorType)
  {
    this.extensionPointId = extensionPointId;
    this.tagName = tagName;
    this.validatorType = validatorType;
  }

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
    IExtensionPoint point = pluginRegistry.getExtensionPoint(PLUGIN_ID, extensionPointId);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        readElement(elements[i]);
      }
    }
  }

  /**
   * readElement() - parse and deal with an extension like:
   *
   * <extension point="org.eclipse.validate.wsdl.WSDLExtensionValidator"
   *            id="soapValidator"
   *            name="SOAP Validator">>
   *   <validator>
   *        <run class=" org.eclipse.validate.wsdl.soap.SOAPValidator"/>
   *   </validator>
   *   <attribute name="namespace" value="http://schemas.xmlsoap.org/wsdl/soap/"/>
   * </extension>
   */
  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(tagName))
    {
      String validatorClass = element.getAttribute(ATT_CLASS);
      String namespace = element.getAttribute(ATT_NAMESPACE);
      String resourceBundle = element.getAttribute(ATT_RESOURCEBUNDLE);

      if (validatorClass != null)
      {
        try
        {
          //          ClassLoader pluginLoader =
          //            element.getDeclaringExtension().getDeclaringPluginDescriptor().getPlugin().getClass().getClassLoader();
          //				modified to resolve certain situations where the plugin has not been initialized

          ClassLoader pluginLoader =
            element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
          
          if (validatorType == WSDL_VALIDATOR)
           {
            WSDLConfigurator.registerWSDLValidator(namespace, validatorClass, resourceBundle, pluginLoader);
          }
          else if (validatorType == EXT_VALIDATOR)
           {
            WSDLConfigurator.registerExtensionValidator(namespace, validatorClass, resourceBundle, pluginLoader);
          }
//          registerWSDLValidatorPluginExtensionWithClassName(
//            pluginLoader,
//            WSDLValidatorExtensionClass,
//            WSDLValidatorExtensionNamespace);
        }
        catch (Exception e)
        {
        }
      }
    }
  }

  /**
   * Register the extension validator with the given class name and namespaces.
   * 
   * @param classLoader - the class loader to create the validator
   * @param className - the name of the extension validator
   * @param namespace - the namespace of the extension validator
   * @throws Exception
   */
//  protected void registerWSDLValidatorPluginExtensionWithClassName(
//    ClassLoader classLoader,
//    String className,
//    String namespace)
//    throws Exception
//  {
//    try
//    {
//      Class validatorExtensionClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
//
//      
//      //IValidatorExtensionPlugin validatorHandler = (IValidatorExtensionPlugin)validatorExtensionClass.newInstance();
//      //add(namespace, validatorHandler.getValidator());
//    }
//    catch (Exception e)
//    {
//      //System.out.println(e.getMessage());
//      //TODO: write the error message to the log file - use custom log writer class
//      //ValidateWSDLPlugin.getInstance().getMsgLogger().write("WSDL Validator could not register the extension validator." + e.getMessage());	
//    }
//  }

  /**
   * Register the loaded validator.
   * 
   * @param namespace - the namespace of the validator
   * @param validatorExtension - the extension validator
   */
//  protected void add(String namespace, IWSDLValidator validatorExtension)
//  {
//    if (validatorType == WSDL_VALIDATOR)
//    {
//      WSDLConfigurator.registerWSDLValidator(namespace, validatorExtension);
//    }
//    else if (validatorType == WSI_VALIDATOR)
//    {
//      WSDLConfigurator.registerWSIValidator(namespace, validatorExtension);
//    }
//  }
}

/**
 * Read WSDl 1.1 extension validators.
 * 
 *  <extension
 *     point="com.ibm.etools.validation.validator"
 *     id="wsdlValidator"
 *     name="%_UI_WSDL_VALIDATOR">
 *    <wsdl11validator
 *       namespace="http://schemas.xmlsoap.org/wsdl/soap/"
 *       class="org.eclipse.wsdl.validate.soap.wsdl11.SOAPValidator"
 *       resourcebundle="validatewsdlsoap"/>
 *   </extension>
 *  
 */
class WSDL11ValidatorPluginRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.validation";
  protected static final String ATT_CLASS = "class";
  protected static final String ATT_NAMESPACE = "namespace";
  protected static final String ATT_RESOURCEBUNDLE = "resourcebundle";
  protected String extensionPointId;
  protected String tagName;

  /**
   * Constructor.
   * 
   * @param extensionPointId - the id of the extension point
   * @param tagName - the tag name of the extension point
   */
  public WSDL11ValidatorPluginRegistryReader(String extensionPointId, String tagName)
  {
    this.extensionPointId = extensionPointId;
    this.tagName = tagName;
  }

  /**
   * Read from plugin registry and handle the configuration elements that match
   * the spedified elements.
   */
  public void readRegistry()
  {
    IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
    IExtensionPoint point = pluginRegistry.getExtensionPoint(PLUGIN_ID, extensionPointId);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        readElement(elements[i]);
      }
    }
  }

  /**
   * Parse and deal with the extension points.
   * 
   * @param element The extension point element.
   */
  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(tagName))
    {
      String validatorClass = element.getAttribute(ATT_CLASS);
      String namespace = element.getAttribute(ATT_NAMESPACE);
      String resourceBundle = element.getAttribute(ATT_RESOURCEBUNDLE);

      if (validatorClass != null && namespace != null)
      {
        try
        {
          ClassLoader pluginLoader =
            element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
            
          WSDLConfigurator.registerWSDL11Validator(namespace, validatorClass, resourceBundle, pluginLoader);
        }
        catch (Exception e)
        {
        }
      }
    }
  }
}

/**
 * This class reads the plugin manifests and registers each WSDLExtensionValidator
 */
class ExtXMLCatalogPluginRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.validation";
  protected static final String EXTENSION_POINT_ID = "xmlcatalog";
  
  /**
   * The xmlcatalog element allow adding an extension XML Catalog such as
   * <xmlcatalog class="org.eclipse.wsdl.validate.ExtXMLCatalog">
   */
  protected static final String EXT_CATALOG_TAG_NAME = "xmlcatalog";
  protected static final String ATT_CLASS = "class";
  
  /**
   * The entity element allows adding specific XML catalog entities such as
   * <entity
   *   publicid="http://schemas.xmlsoap.org/wsdl/" 
   *   systemid="xsd/wsdl.xsd" />
   */
  protected static final String ENTITY_TAG_NAME = "entity";
  protected static final String ATT_PUBLIC_ID = "publicId";
  protected static final String ATT_SYSTEM_ID = "location";
  
  /**
   * The schemadir element allows adding a director of schemas to the XML catalog such as
   * <schemadir location="c:\myschemadir" />
   * Note: It is more expensive to use this method then the entity method
   * of adding schemas to the catalog as this method requires that all of
   * the schemas be read.
   */
  protected static final String SCHEMA_DIR_TAG_NAME = "schemadir";
  protected static final String ATT_LOCATION = "location";
  protected String pluginId, extensionPointId;

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    IExtensionPoint point = extensionRegistry.getExtensionPoint(PLUGIN_ID, EXTENSION_POINT_ID);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        readElement(elements[i]);
      }
    }
  }

  /**
   * readElement() - parse and deal with an extension like:
   *
   * <extension point="com.ibm.etools.validate.wsdl.WSDLExtensionValidator"
   *            id="soapValidator"
   *            name="SOAP Validator">>
   *   <validator>
   *        <run class=" com.ibm.etools.validate.wsdl.soap.SOAPValidator"/>
   *   </validator>
   *   <attribute name="namespace" value="http://schemas.xmlsoap.org/wsdl/soap/"/>
   * </extension>
   */
  protected void readElement(IConfigurationElement element)
  {
    String elementname = element.getName();
    // Extension XML Catalogs.
    if (elementname.equals(EXT_CATALOG_TAG_NAME))
    {
      String xmlCatalogClass = element.getAttribute(ATT_CLASS);

      if (xmlCatalogClass != null)
      {
        try
        {
          // modified to resolve certain situations where the plugin has not been initialized
          ClassLoader pluginLoader =
            element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
          //					ClassLoader pluginLoader =
          //						element.getDeclaringExtension().getDeclaringPluginDescriptor().getPlugin().getClass().getClassLoader();
          XMLCatalog.setExtensionXMLCatalog(xmlCatalogClass, pluginLoader);
        }
        catch (Exception e)
        {
          System.out.println(e);
        }
      }
    }
    // XML Catalog entites.
    else if(elementname.equals(ENTITY_TAG_NAME))
    {
      String publicid = element.getAttribute(ATT_PUBLIC_ID);
      String systemid = element.getAttribute(ATT_SYSTEM_ID);
      if(publicid == null || systemid == null)
      {
        return;
      }
      Bundle bundle = Platform.getBundle(element.getDeclaringExtension().getNamespace());
      systemid = getAbsoluteLocation(systemid, bundle);
      
      XMLCatalog.addEntity(new XMLCatalogEntityHolder(publicid, systemid));
    }
    // Schema directories for the XML Catalog.
    else if(elementname.equals(SCHEMA_DIR_TAG_NAME))
    {
      String location = element.getAttribute(ATT_LOCATION);
      if(location != null)
      {
        Bundle bundle = Platform.getBundle(element.getDeclaringExtension().getNamespace());
        location = getAbsoluteLocation(location, bundle);
        XMLCatalog.addSchemaDir(location);
      }
    }
  }

  private String getAbsoluteLocation(String location, Bundle bundle)
  {
    URL url = null;
    if(bundle != null)
    {
      url = bundle.getEntry(location);
    }
    
    if(url != null)
    {
      try
      {
        url = Platform.resolve(url);
        return url.toExternalForm();
      }
      catch(IOException e)
      {
        //Unable to register the schema.
      }
    }
    return location;
  }
}




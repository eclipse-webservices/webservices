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
package org.eclipse.wst.wsdl.ui.internal;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ContentGeneratorExtension;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ContentGeneratorExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ContentGeneratorProviderExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.ExtensibilityItemTreeProviderRegistry;
import org.eclipse.wst.wsdl.ui.internal.extension.NSKeyedExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;


public class WSDLEditorPlugin extends AbstractUIPlugin //, IPluginHelper
{
  public static final String DEFAULT_PAGE = "org.eclipse.wst.wsdl.ui.internal.defaultpage";
  public static final String GRAPH_PAGE = "org.eclipse.wst.wsdl.ui.internal.graphpage";
  public static final String SOURCE_PAGE = "org.eclipse.wst.wsdl.ui.internal.sourcepage";

  public final static String PLUGIN_ID = "org.eclipse.wst.wsdl.ui";
  public final static String XSD_EDITOR_ID = "org.eclipse.wst.xsd.ui.XSDEditor"; 
  
  public final static String DEFAULT_TARGET_NAMESPACE = "http://tempuri.org";
  
  public static int DEPENDECIES_CHANGED_POLICY_PROMPT = 0;
  public static int DEPENDECIES_CHANGED_POLICY_IGNORE = 1;
  public static int DEPENDECIES_CHANGED_POLICY_RELOAD = 2;

  protected static WSDLEditorPlugin instance;
  //  protected XMLSchemaPackage xmlschemaPackage;
//  private static MsgLogger myMsgLogger;

  private WSDLEditorExtensionRegistry wsdlEditorExtensionRegistry;
  private NSKeyedExtensionRegistry extensiblityElementFilterRegistry;
  private ExtensibilityItemTreeProviderRegistry treeProviderRegistry;
  private NSKeyedExtensionRegistry propertyDescriptorProviderRegistry;
  private NSKeyedExtensionRegistry propertySourceProviderRegistry;
  private NSKeyedExtensionRegistry propertySectionDescriptorProviderRegistry;
  private ContentGeneratorExtensionRegistry contentGeneratorExtensionRegistry;
  private NSKeyedExtensionRegistry detailsViewerProviderRegistry;  
  //private static Hashtable grayedImageMap = new Hashtable();
  private int dependenciesChangedPolicy = DEPENDECIES_CHANGED_POLICY_RELOAD;

  
  public static void logMessage(String message)
  {
  }

  /**
   * Resources helper.
   */

  public WSDLEditorPlugin(IPluginDescriptor descriptor)
  {
    super(descriptor);
    instance = this;

    // TODO... remove this code when we add 'dependenciesChangedPolicy'
    // to an editor preferences page
    try
    {
      String string = getWSDLString("_DEBUG_UPDATE_POLICY");
      int policy = Integer.parseInt(string);
      if (policy >= 0 && policy <= DEPENDECIES_CHANGED_POLICY_RELOAD)
      {
        dependenciesChangedPolicy = policy;
      }
    }
    catch (Exception e)
    {
    }
  }

  public WSDLEditorExtensionRegistry getWSDLEditorExtensionRegistry()
  {
    if (wsdlEditorExtensionRegistry == null)
    {
      wsdlEditorExtensionRegistry = new WSDLEditorExtensionRegistry();
      new InternalEditorExtensionRegistryReader(wsdlEditorExtensionRegistry).readRegistry();
    }
    return wsdlEditorExtensionRegistry;
  }

  public NSKeyedExtensionRegistry getDetailsViewerProviderRegistry()
  {
    if (detailsViewerProviderRegistry == null)
    {
	  detailsViewerProviderRegistry = new NSKeyedExtensionRegistry();
      new DetailsViewerProviderRegistryReader(detailsViewerProviderRegistry).readRegistry();
    }
    return detailsViewerProviderRegistry;
  }

  public NSKeyedExtensionRegistry getExtensiblityElementFilterRegistry()
  {
    if (extensiblityElementFilterRegistry == null)
    {
      extensiblityElementFilterRegistry = new NSKeyedExtensionRegistry();
      new ElementContentFilterExtensionRegistryReader(extensiblityElementFilterRegistry).readRegistry();
    }
    return extensiblityElementFilterRegistry;
  }

  public ExtensibilityItemTreeProviderRegistry getExtensibilityItemTreeProviderRegistry()
  {
    if (treeProviderRegistry == null)
    {
      treeProviderRegistry = new ExtensibilityItemTreeProviderRegistry();
      new ExtensibilityItemTreeProviderRegistryReader(treeProviderRegistry).readRegistry();
    }
    return treeProviderRegistry;
  }

  public NSKeyedExtensionRegistry getPropertyDescriptorProviderRegistry()
  {
    if (propertyDescriptorProviderRegistry == null)
    {
      propertyDescriptorProviderRegistry = new NSKeyedExtensionRegistry();
      new PropertyDescriptorProviderRegistryReader(propertyDescriptorProviderRegistry).readRegistry();
    }
    return propertyDescriptorProviderRegistry;
  }

  public NSKeyedExtensionRegistry getPropertySourceProviderRegistry()
  {
    if (propertySourceProviderRegistry == null)
    {
      propertySourceProviderRegistry = new NSKeyedExtensionRegistry();
      new PropertySourceProviderRegistryReader(propertySourceProviderRegistry).readRegistry();
    }
    return propertySourceProviderRegistry;
  }

  public NSKeyedExtensionRegistry getPropertySectionDescriptorProviderRegistry()
  {
    if (propertySectionDescriptorProviderRegistry == null)
    {
      propertySectionDescriptorProviderRegistry = new NSKeyedExtensionRegistry();
      new PropertySectionDescriptorProviderRegistry(propertySectionDescriptorProviderRegistry).readRegistry();
    }
    return propertySectionDescriptorProviderRegistry;
  }

  public ContentGeneratorExtensionRegistry getContentGeneratorExtensionRegistry()
  {
    if (contentGeneratorExtensionRegistry == null)
    {
      contentGeneratorExtensionRegistry = new ContentGeneratorExtensionRegistry();
      new ContentGeneratorExtensionRegistryReader(contentGeneratorExtensionRegistry).readRegistry();
    }
    return contentGeneratorExtensionRegistry;
  }

  /**
   * Get the Install URL
   */
  public static URL getInstallURL()
  {
    return getInstance().getDescriptor().getInstallURL();
  }

  /**
   * Get resource string
   */
  public static String getWSDLString(String key)
  {
    return getInstance().getDescriptor().getResourceBundle().getString(key);
  }

  /**
   * Get resource string
   */
  public static String getWSDLString(String key, String arg0)
  {
    return MessageFormat.format(getWSDLString(key), new Object [] { arg0 });
  }
  
  /**
   * Get resource string
   */
  public static String getWSDLString(String key, String arg0, String arg1)
  {
    return MessageFormat.format(getWSDLString(key), new Object [] { arg0, arg1 });
  }

  /**
   * Return the plugin physical directory location
   */
  public static IPath getPluginLocation()
  {
    try
    {
      IPath installPath = new Path(getInstallURL().toExternalForm()).removeTrailingSeparator();
      String installStr = Platform.asLocalURL(new URL(installPath.toString())).getFile();
      return new Path(installStr);
    }
    catch (IOException e)
    {
      //System.out.println("WSDLEditorPlugin.getPluginLocation() exception.." + e);
    }
    return null;
  }

  protected Hashtable imageDescriptorCache = new Hashtable();

  protected ImageDescriptor internalGetImageDescriptor(String key)
  {
    ImageDescriptor imageDescriptor = (ImageDescriptor) imageDescriptorCache.get(key);
    if (imageDescriptor == null)
    {
      imageDescriptor = ImageDescriptor.createFromFile(WSDLEditorPlugin.class, key);
      if (imageDescriptor != null)
      {
        imageDescriptorCache.put(key, imageDescriptor);
      }
    }
    return imageDescriptor;
  }

  public static ImageDescriptor getImageDescriptor(String key)
  {
    return getInstance().internalGetImageDescriptor(key);
  }

  public Image getImage(String iconName)
  {
    ImageRegistry imageRegistry = getImageRegistry();
    
    if (imageRegistry.get(iconName) != null)
    {
      return imageRegistry.get(iconName);
    }
    else
    {
      imageRegistry.put(iconName, ImageDescriptor.createFromFile(getClass(), iconName));
      return imageRegistry.get(iconName);
    }
  }
  
  /*
    public Image getImage(String key, boolean isGrayed)
    { 
      Image image = null;
      if (!isGrayed)
      {
        image = super.getImage(key);
      }                             
      else
      {
        image = (Image)grayedImageMap.get(key);
        if (image == null)
        {
          Image colorImage = super.getImage(key);
          if (colorImage != null)
          {
            image = new Image(Display.getCurrent(), colorImage, SWT.IMAGE_GRAY);//SWT.IMAGE_DISABLE);
            grayedImageMap.put(key, image);
          }
        }
      }
      return image;
    }
  */

  /**
   * Get the metadata directory for this plugin
   */
  public static String getMetaDataDirectory()
  {
    return getInstance().getStateLocation().toOSString();
  }

  /**
   * Get the singleton instance.
   */
  public static WSDLEditorPlugin getInstance()
  {
    return instance;
  }

  public IWorkspace getWorkspace()
  {
    return ResourcesPlugin.getWorkspace();
  }

  public static Shell getShell()
  {
    return getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
   */
  protected void initializeDefaultPreferences(IPreferenceStore store)
  {
    super.initializeDefaultPreferences(store);
    store.setDefault(DEFAULT_PAGE, GRAPH_PAGE);
    
    // WSDLPreferencePage prefs
    store.setDefault(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"), DEFAULT_TARGET_NAMESPACE);
    // Do we need this preference below?  Look at WSDLPreferencePage.java
//    store.setDefault("Defualt Location:", "http://www.example.com");
  }
  
  /**
   * setDefaultPage
   * Set the default page to open when the editor starts. Maintains the state
   * when WSAD is shutdown and restarted.
   * @param page
   */
  public void setDefaultPage(String page)
  {
    getPreferenceStore().setValue(DEFAULT_PAGE, page);
  }

  public String getDefaultPage()
  {
    return getPreferenceStore().getString(DEFAULT_PAGE);
  }

  public int getDependenciesChangedPolicy()
  {
    //return getPreferenceStore().getInt(DEPENDECIES_CHANGED_POLICY);
    return dependenciesChangedPolicy;
  }
}

class BaseRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.ui";

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry(String extensionPointId)
  {
  	boolean  boo = true;
    IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
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

  protected void readElement(IConfigurationElement element)
  {
  }
}

/**
 * This class reads the plugin manifests and registers each internal editor extension
 */
class InternalEditorExtensionRegistryReader extends BaseRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "internalEditorExtensions";
  protected static final String TAG_NAME = "internalEditorExtension";
  protected static final String ATT_CLASS = "class";
  protected WSDLEditorExtensionRegistry registry;

  public InternalEditorExtensionRegistryReader(WSDLEditorExtensionRegistry registry)
  {
    this.registry = registry;
  }

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    super.readRegistry(EXTENSION_POINT_ID);
  }

  /**
   * readElement()
   */
  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(TAG_NAME))
    {
      String className = element.getAttribute(ATT_CLASS);
      if (className != null)
      {
        try
        {
          ClassLoader pluginClsLoader = element.getDeclaringExtension().getDeclaringPluginDescriptor().getPlugin().getClass().getClassLoader();
          registry.add(pluginClsLoader, className);
        }
        catch (Exception e)
        {
        }
      }
    }
  }
}

/**
 * This class reads the plugin manifests and registers each extensibility item tree provider
 */
class ContentGeneratorExtensionRegistryReader extends BaseRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.ui";
  protected static final String EXTENSION_POINT_ID = "contentGenerators";
  protected static final String ELEMENT_CONTENT_GENERATOR = "contentGenerator";
  protected static final String ELEMENT_CONTENT_GENERATOR_PROVIDER = "contentGeneratorProvider";
  protected static final String ATT_CLASS = "class";
  protected static final String ATT_NAME = "name";
  protected static final String ATT_NAMESPACE = "namespace";
  protected static final String ATT_CONTENT_GENERATOR_CLASS = "contentGeneratorClass";
  protected static final String ATT_PORT_OPTIONS_PAGE_CLASS = "portOptionsPageClass";
  protected static final String ATT_BINDING_OPTIONS_PAGE_CLASS = "bindingOptionsPageClass";

  protected ContentGeneratorExtensionRegistry registry;

  public ContentGeneratorExtensionRegistryReader(ContentGeneratorExtensionRegistry registry)
  {
    this.registry = registry;
  }

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    super.readRegistry(EXTENSION_POINT_ID);
  }

  /**
   * readElement()
   */
  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(ELEMENT_CONTENT_GENERATOR))
    {
      String name = element.getAttribute(ATT_NAME);
      String namespace = element.getAttribute(ATT_NAMESPACE);
      String generatorClass = element.getAttribute(ATT_CONTENT_GENERATOR_CLASS);
      if (name != null && generatorClass != null)
      {
        ContentGeneratorExtension bindingGeneratorExtension = new ContentGeneratorExtension(name, generatorClass);
        bindingGeneratorExtension.setNamespace(namespace);
        bindingGeneratorExtension.setPortOptionsPageClassName(element.getAttribute(ATT_PORT_OPTIONS_PAGE_CLASS));
        bindingGeneratorExtension.setBindingOptionsPageClassName(element.getAttribute(ATT_BINDING_OPTIONS_PAGE_CLASS));
        try
        {
          ClassLoader pluginClasssLoader = element.getDeclaringExtension().getDeclaringPluginDescriptor().getPlugin().getClass().getClassLoader();
          bindingGeneratorExtension.setClassLoader(pluginClasssLoader);
          registry.add(bindingGeneratorExtension);
        }
        catch (Exception e)
        {
        }
      }
    }
    else if (element.getName().equals(ELEMENT_CONTENT_GENERATOR_PROVIDER))
    {
      String className = element.getAttribute(ATT_CLASS);
      if (className != null)
      {
		ContentGeneratorProviderExtension contentGeneratorExtension = new ContentGeneratorProviderExtension(className);
        registry.add(contentGeneratorExtension);
      }
    }
  }
}

/**
 * 
 */
abstract class NSKeyedExtensionRegistryReader extends BaseRegistryReader
{
  protected static final String ATT_NAME_SPACE = "namespace";
  protected String extensionPointId;
  protected String tagName;
  protected String[] attributeNames;
  protected NSKeyedExtensionRegistry nsKeyedExtensionRegistry;

  private NSKeyedExtensionRegistryReader(String extensionPointId, String tagName, NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    this.extensionPointId = extensionPointId;
    this.tagName = tagName;
    this.nsKeyedExtensionRegistry = nsKeyedExtensionRegistry;
  }

  public NSKeyedExtensionRegistryReader(String extensionPointId, String tagName, String attributeName, NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    this(extensionPointId, tagName, nsKeyedExtensionRegistry);
    attributeNames = new String[1];
    attributeNames[0] = attributeName;
  }

  public NSKeyedExtensionRegistryReader(String extensionPointId, String tagName, String[] attributeNames, NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    this(extensionPointId, tagName, nsKeyedExtensionRegistry);
    this.attributeNames = attributeNames;
  }
  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    super.readRegistry(extensionPointId);
  }

  /**
   * readElement()
   */
  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(tagName))
    {
      String namespace = element.getAttribute(ATT_NAME_SPACE);
      if (namespace != null)
      {
        ClassLoader pluginClasssLoader = element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
        if (attributeNames.length == 1)
        {
          String className = element.getAttribute(attributeNames[0]);
          if (className != null)
          {
            nsKeyedExtensionRegistry.put(namespace, className, pluginClasssLoader);
          }
        }
        else
        {
          HashMap map = new HashMap();
          for (int i = 0; i < attributeNames.length; i++)
          {
            String attributeName = attributeNames[i];
            String className = element.getAttribute(attributeName);
            if (className != null && className.length() > 0)
            {
              map.put(attributeName, className);
            }
          }
          nsKeyedExtensionRegistry.put(namespace, map, pluginClasssLoader);
        }
      }
    }
  }
}

/**
 * This class reads the plugin manifests and registers each extensibility item tree provider
 */
class ExtensibilityItemTreeProviderRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "extensibilityItemTreeProviders";
  protected static final String TAG_NAME = "extensibilityItemTreeProvider";
  protected static final String[] ATT_NAMES = { "labelProviderClass", "contentProviderClass" };

  public ExtensibilityItemTreeProviderRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, ATT_NAMES, nsKeyedExtensionRegistry);
  }
}

/**
 * 
 */
class ElementContentFilterExtensionRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "extensibilityElementFilter";
  protected static final String TAG_NAME = "extensibilityElementFilter";

  public ElementContentFilterExtensionRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry);
  }
}

/**
 * 
 */
class PropertyDescriptorProviderRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "propertyDescriptorProvider";
  protected static final String TAG_NAME = "propertyDescriptorProvider";

  public PropertyDescriptorProviderRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry);
  }

  protected void readElement(IConfigurationElement element)
  {
    super.readElement(element);
  }
}

class PropertySourceProviderRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "propertySourceProvider";
  protected static final String TAG_NAME = "propertySourceProvider";

  public PropertySourceProviderRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry);
  }

  protected void readElement(IConfigurationElement element)
  {
    super.readElement(element);
  }
}

class PropertySectionDescriptorProviderRegistry extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "propertySectionDescriptorProvider";
  protected static final String TAG_NAME = "propertySectionDescriptorProvider";

  public PropertySectionDescriptorProviderRegistry(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry);
  }

  protected void readElement(IConfigurationElement element)
  {
    super.readElement(element);
  }
}

/**
 * 
 */
class DetailsViewerProviderRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "detailsViewerProviders";
  protected static final String TAG_NAME = "detailsViewerProvider";

  public DetailsViewerProviderRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry);
  }

  protected void readElement(IConfigurationElement element)
  {
    super.readElement(element);
  }
}
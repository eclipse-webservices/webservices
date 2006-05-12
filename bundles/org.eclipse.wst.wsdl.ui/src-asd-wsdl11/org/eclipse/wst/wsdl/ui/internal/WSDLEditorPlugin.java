/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.wsdl.ui.internal.extensions.NSKeyedExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.extensions.WSDLEditorConfiguration;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.ExtensionsSchemasRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class WSDLEditorPlugin extends AbstractUIPlugin //, IPluginHelper
{
  public static final String DEFAULT_PAGE = "org.eclipse.wst.wsdl.ui.internal.defaultpage"; //$NON-NLS-1$
  public static final String DESIGN_PAGE = "org.eclipse.wst.wsdl.ui.internal.designpage"; //$NON-NLS-1$
  public static final String SOURCE_PAGE = "org.eclipse.wst.wsdl.ui.internal.sourcepage"; //$NON-NLS-1$

  public final static String PLUGIN_ID = "org.eclipse.wst.wsdl.ui"; //$NON-NLS-1$
  public final static String XSD_EDITOR_ID = "org.eclipse.wst.xsd.ui.internal.editor.InternalXSDMultiPageEditor";  //$NON-NLS-1$
  
  public final static String DEFAULT_TARGET_NAMESPACE = "http://www.example.org"; //$NON-NLS-1$
  
  public static int DEPENDECIES_CHANGED_POLICY_PROMPT = 0;
  public static int DEPENDECIES_CHANGED_POLICY_IGNORE = 1;
  public static int DEPENDECIES_CHANGED_POLICY_RELOAD = 2;

  protected static WSDLEditorPlugin instance;
  private ExtensionsSchemasRegistry registry;
  private WSDLEditorConfiguration wsdlEditorConfiguration = null;

  private NSKeyedExtensionRegistry extensiblityElementFilterRegistry;
  private int dependenciesChangedPolicy = DEPENDECIES_CHANGED_POLICY_RELOAD;

  public WSDLEditorConfiguration getWSDLEditorConfiguration()
  {
    if (wsdlEditorConfiguration == null)
    {
      wsdlEditorConfiguration = new WSDLEditorConfiguration();
    }
    return wsdlEditorConfiguration;
  }
  
	public ExtensionsSchemasRegistry getExtensionsSchemasRegistry() {
		if (registry == null) {
			registry = new ExtensionsSchemasRegistry("org.eclipse.wst.wsdl.ui.ExtensionsSchemasDescription"); //$NON-NLS-1$
		}
		
		return registry;
	}
	
  public static void logMessage(String message)
  {
  }

  /**
   * Resources helper.
   */

  public WSDLEditorPlugin()
  {
    super();
    instance = this;

    // TODO... remove this code when we add 'dependenciesChangedPolicy'
    // to an editor preferences page
    try
    {
    	// _DEBUG_UPDATE_POLICY = 0Dummy.label=
      String string = "0Dummy.label"; //$NON-NLS-1$
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

  /* (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception 
  {
	super.start(context);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception 
  {
	super.stop(context);
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

  /**
   * Get the Install URL
   */
  public static URL getInstallURL()
  {
	try
	{
	  return FileLocator.resolve(instance.getBundle().getEntry("/")); //$NON-NLS-1$
	}
	catch (IOException e)
	{
	  return null;
	}
  }

  /**
   * Return the plugin physical directory location
   */
  public static IPath getPluginLocation()
  {
    try
    {
      IPath installPath = new Path(getInstallURL().toExternalForm()).removeTrailingSeparator();
      String installStr = FileLocator.toFileURL(new URL(installPath.toString())).getFile();
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
    if (instance == null)
    {
	  new WSDLEditorPlugin();
	}
    
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
    store.setDefault(DEFAULT_PAGE, DESIGN_PAGE);
    
    // WSDLPreferencePage prefs
    store.setDefault(Messages.getString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"), DEFAULT_TARGET_NAMESPACE); //$NON-NLS-1$
    store.setDefault(Messages.getString("_UI_PREF_PAGE_AUTO_REGENERATE_BINDING"), false); //$NON-NLS-1$
    store.setDefault(Messages.getString("_UI_PREF_PAGE_PROMPT_REGEN_BINDING_ON_SAVE"), false);	// TODO: Externalize //$NON-NLS-1$
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
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.ui"; //$NON-NLS-1$

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry(String extensionPointId)
  {
	IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
	IExtensionPoint point = extensionRegistry.getExtensionPoint(PLUGIN_ID, extensionPointId);

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
 * 
 */
abstract class NSKeyedExtensionRegistryReader extends BaseRegistryReader
{
  protected static final String ATT_NAME_SPACE = "namespace"; //$NON-NLS-1$
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
    	Bundle bundle = Platform.getBundle(element.getDeclaringExtension().getContributor().getName());
        if (attributeNames.length == 1)
        {
          String className = element.getAttribute(attributeNames[0]);
          if (className != null)
          {
            nsKeyedExtensionRegistry.put(namespace, className, bundle);
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
          nsKeyedExtensionRegistry.put(namespace, map, bundle);
        }
      }
    }
  }
}

/**
 * 
 */
class ElementContentFilterExtensionRegistryReader extends NSKeyedExtensionRegistryReader
{
  protected static final String EXTENSION_POINT_ID = "extensibilityElementFilter"; //$NON-NLS-1$
  protected static final String TAG_NAME = "extensibilityElementFilter"; //$NON-NLS-1$

  public ElementContentFilterExtensionRegistryReader(NSKeyedExtensionRegistry nsKeyedExtensionRegistry)
  {
    super(EXTENSION_POINT_ID, TAG_NAME, "class", nsKeyedExtensionRegistry); //$NON-NLS-1$
  }
}
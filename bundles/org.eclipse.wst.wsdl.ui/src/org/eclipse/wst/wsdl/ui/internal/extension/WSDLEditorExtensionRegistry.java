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
package org.eclipse.wst.wsdl.ui.internal.extension;
        
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;

public class WSDLEditorExtensionRegistry
{                   
  //protected List classNameClassLoaderPairList = new ArrayList();
  //protected HashMap treeProviderMap = new HashMap(); 
  protected List registeredExtensionList = new ArrayList();

  public WSDLEditorExtensionRegistry()
  {
    add(new WSDLEditor.BuiltInWSDLEditorExtension());
  }            

  //public ExtensibilityItemTreeProvider getExtensibilityItemTreeProvider(String namespaceURI)
  //{
  //  return (ExtensibilityItemTreeProvider)treeProviderMap.get(namespaceURI);
  //}

  //public void putExtensibilityItemTreeProvider(String namespaceURI, ExtensibilityItemTreeProvider treeProvider)
  //{
  //  treeProviderMap.put(namespaceURI, treeProvider);
  //}
  
  public List getRegisteredExtensions()
  {                            
    return registeredExtensionList;                                
  }                       

  public WSDLEditorExtension getApplicableExtension(int type, Object object)
  { 
    WSDLEditorExtension result = null;                                    
    for (Iterator i = getRegisteredExtensions().iterator(); i.hasNext(); )
    {
      WSDLEditorExtension extension = (WSDLEditorExtension)i.next();
      if (extension.isExtensionTypeSupported(type) && extension.isApplicable(object))
      { 
        result = extension;
        break;
      }
    }         
    return result;                                                           
  }

  public WSDLEditorExtension[] getRegisteredExtensions(int type)
  { 
    List list = new ArrayList();                                    
    for (Iterator i = getRegisteredExtensions().iterator(); i.hasNext(); )
    {
      WSDLEditorExtension extension = (WSDLEditorExtension)i.next();
      if (extension.isExtensionTypeSupported(type))
      { 
        list.add(extension);
      }
    } 

    WSDLEditorExtension[] result = new WSDLEditorExtension[list.size()];
    int count = 0;
    for (Iterator i = list.iterator(); i.hasNext(); )
    { 
      WSDLEditorExtension extension = (WSDLEditorExtension)i.next();
      result[count] = extension;
      count++;
    }
    return result;                          
  }
             
  public void addAsFirst(WSDLEditorExtension extension)
  {               
    getRegisteredExtensions().add(0, extension);
  }    

  public void add(WSDLEditorExtension extension)
  {               
    getRegisteredExtensions().add(extension);
  }          

  public void add(ClassLoader classLoader, String className)
  {                      
    // TODO... consider defered instantiation of WSDLEditorExtensions         
    try
    {       
      Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
      WSDLEditorExtension extension = (WSDLEditorExtension)theClass.newInstance();
      registeredExtensionList.add(extension);
    }
    catch (Exception e)
    {
    	e.printStackTrace();
    }
  }
}
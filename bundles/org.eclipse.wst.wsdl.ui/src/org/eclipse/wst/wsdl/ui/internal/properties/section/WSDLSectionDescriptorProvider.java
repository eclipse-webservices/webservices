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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.properties.ISectionDescriptor;
import org.eclipse.wst.common.ui.properties.ISectionDescriptorProvider;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;

public class WSDLSectionDescriptorProvider implements ISectionDescriptorProvider
{
  protected WSDLEditorExtension[] propertySectionDescriptorProviderExtensions;
  protected ISectionDescriptorProvider[] propertySectionDescriptorProviders;

  protected WSDLEditorExtension[] labelProviderExtensions;

  protected final static Object[] EMPTY_ARRAY = {};
  
  IEditorPart editorPart;

  /**
   * 
   */
  public WSDLSectionDescriptorProvider()
  {
    super();
    // TODO Check this
    this.editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptorProvider#getSectionDescriptors()
   */
  public ISectionDescriptor[] getSectionDescriptors()
  {
    List list = new ArrayList();
    
    
    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 

    propertySectionDescriptorProviderExtensions = registry.getRegisteredExtensions(WSDLEditorExtension.PROPERTY_SECTION_DESCRIPTOR_PROVIDER); 
    propertySectionDescriptorProviders = new ISectionDescriptorProvider[propertySectionDescriptorProviderExtensions.length]; 
    for (int i = 0; i < propertySectionDescriptorProviderExtensions.length; i++)
    {
      propertySectionDescriptorProviders[i] = (ISectionDescriptorProvider)propertySectionDescriptorProviderExtensions[i].createExtensionObject(WSDLEditorExtension.PROPERTY_SECTION_DESCRIPTOR_PROVIDER, (WSDLEditor)editorPart);
      
      ISectionDescriptor [] extensionSectionDescriptors = propertySectionDescriptorProviders[i].getSectionDescriptors();
      for (int j = 0; j < extensionSectionDescriptors.length; j++)
      {
        list.add(extensionSectionDescriptors[j]);
      }
    }

    
    list.add(new NameSectionDescriptor());
    list.add(new PartSectionDescriptor());
    list.add(new DocumentationSectionDescriptor());
    list.add(new ReferenceSectionDescriptor());
    list.add(new NamespaceSectionDescriptor());
    list.add(new ExtensibilityElementSectionDescriptor());
    list.add(new ImportSectionDescriptor());
    
		ISectionDescriptor[] descriptors = new ISectionDescriptor[list.size()];
	  list.toArray(descriptors);

    return descriptors;
  }
}

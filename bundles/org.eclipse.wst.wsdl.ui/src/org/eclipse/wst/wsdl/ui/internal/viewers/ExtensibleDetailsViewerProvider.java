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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.IDetailsViewerProvider;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;

public class ExtensibleDetailsViewerProvider implements IDetailsViewerProvider
{
  protected WSDLEditorExtension[] extensions;
  protected IDetailsViewerProvider[] detailsViewerProviders;

  protected final static Object[] EMPTY_ARRAY = {};

  public ExtensibleDetailsViewerProvider(WSDLEditor wsdlEditor)
  {  	
    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 

    extensions = registry.getRegisteredExtensions(WSDLEditorExtension.DETAILS_VIEWER_PROVIDER); 
    detailsViewerProviders = new IDetailsViewerProvider[extensions.length]; 
    for (int i = 0; i < extensions.length; i++)
    {
      detailsViewerProviders[i] = (IDetailsViewerProvider)extensions[i].createExtensionObject(WSDLEditorExtension.DETAILS_VIEWER_PROVIDER, wsdlEditor);
    }
  }          
      

  protected IDetailsViewerProvider getApplicableDetailsProvider(Object object)
  {                             
    IDetailsViewerProvider provider = null;
    for (int i = 0; i < extensions.length; i++)
    {
      if (extensions[i].isApplicable(object))
      {
        provider = detailsViewerProviders[i];
        if (provider != null)
        {
          break;
        }
      }
    }
    return provider;
  }
   
  
  public Object getViewerKey(Object object)
  {
    IDetailsViewerProvider provider = getApplicableDetailsProvider(object);
    return provider != null ? provider.getViewerKey(object) : null;
  }
  

  public Viewer createViewer(Object object, Composite parent, IEditorPart editorPart)
  {
    IDetailsViewerProvider provider = getApplicableDetailsProvider(object);
    return provider != null ? provider.createViewer(object, parent, editorPart) : null;
  }
}
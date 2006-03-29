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
package org.eclipse.wst.wsdl.ui.internal.extensions;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * This class reads the plugin manifests and registers each extensibility item tree provider
 */
public class ExtensibilityItemTreeProviderRegistry extends NSKeyedExtensionRegistry
{
  protected static final String LABEL_PROVIDER_PROPERTY = "labelProviderClass";
  protected static final String CONTENT_PROVIDER_PROPERTY = "contentProviderClass";
  protected static final String[] ATT_NAMES = { "labelProviderClass", "contentProviderClass" };

  public ILabelProvider getLabelProvider(String namespace)
  {
    return (ILabelProvider) getProperty(namespace, LABEL_PROVIDER_PROPERTY);
  }

  public ITreeChildProvider getContentProvider(String namespace)
  {
    return (ITreeChildProvider) getProperty(namespace, CONTENT_PROVIDER_PROPERTY);
  }
}

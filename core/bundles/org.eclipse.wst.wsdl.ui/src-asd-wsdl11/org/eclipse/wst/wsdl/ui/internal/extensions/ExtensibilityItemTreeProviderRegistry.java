/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
  protected static final String LABEL_PROVIDER_PROPERTY = "labelProviderClass"; //$NON-NLS-1$
  protected static final String CONTENT_PROVIDER_PROPERTY = "contentProviderClass"; //$NON-NLS-1$
  protected static final String[] ATT_NAMES = { "labelProviderClass", "contentProviderClass" }; //$NON-NLS-1$ //$NON-NLS-2$

  public ILabelProvider getLabelProvider(String namespace)
  {
    return (ILabelProvider) getProperty(namespace, LABEL_PROVIDER_PROPERTY);
  }

  public ITreeChildProvider getContentProvider(String namespace)
  {
    return (ITreeChildProvider) getProperty(namespace, CONTENT_PROVIDER_PROPERTY);
  }
}

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
package org.eclipse.wst.wsdl.ui.internal.xsd;

import org.eclipse.wst.common.ui.properties.ISectionDescriptor;
import org.eclipse.wst.common.ui.properties.ISectionDescriptorProvider;

public class XSDSectionDescriptorProvider implements ISectionDescriptorProvider
{
  org.eclipse.wst.xsd.ui.internal.properties.section.XSDSectionDescriptorProvider descriptor;
  /**
   * 
   */
  public XSDSectionDescriptorProvider()
  {
    super();
    descriptor = new org.eclipse.wst.xsd.ui.internal.properties.section.XSDSectionDescriptorProvider();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptorProvider#getSectionDescriptors()
   */
  public ISectionDescriptor[] getSectionDescriptors()
  {
    return descriptor.getSectionDescriptors();
  }
}

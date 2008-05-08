/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080501   229728 makandre@ca.ibm.com - Andrew Mak, uppercase .WSDL cannot be found by the Web Service Client wizard
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.common;

import org.eclipse.core.resources.IFile;

/**
* This is the interface for a class of objects that filter
* other objects for inclusion or exclusion by some consumer.
*/
public class FileExtensionFilter implements IFilter
{
  private String[] extensions_;

  public FileExtensionFilter(String[] extensions)
  {
    extensions_ = (extensions != null) ? extensions : new String[0];
  }

  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName()
  {
    return "org.eclipse.jst.ws.atk.ui.editor.common.FileExtensionFilter";
  }

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription()
  {
    return "org.eclipse.jst.ws.atk.ui.editor.common.FileExtensionFilter";
  }

  /**
  * Returns true if and only if this <code>Filter</code>
  * accepts the given <code>object</code>. This method
  * must return true if and only if {@link #statusOf}
  * returns an <code>IStatus</code> with a severity of
  * less than <code>IStatus.ERROR</code>.
  * @param object The object to filter.
  * @return True if and only if this <code>Filter</code>
  * accepts the given <code>object</code>.
  */
  public boolean accepts(Object object)
  {
    if (object instanceof IFile)
    {
      IFile file = (IFile)object;
      for (int i = 0; i < extensions_.length; i++)
      {
        if (file.getFileExtension() != null && file.getFileExtension().equalsIgnoreCase(extensions_[i]))
          return true;
      }
    }
    return false;
  }
}

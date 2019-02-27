/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.report.impl;

import org.eclipse.wst.wsi.internal.core.report.EntryContainer;

/**
 * This class represents a container for one or more entries.  
 * An example would be a WSDL document.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class EntryContainerImpl
  extends EntryResultImpl
  implements EntryContainer
{
  /**
   * Container ID.
   */
  protected String id;

  /* (non-Javadoc)
   * @see org.wsi.test.report.EntryContainer#getId()
   */
  public String getId()
  {
    return this.id;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.EntryContainer#setId(String)
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Return string representation of this object.
   */
  public String toString()
  {
    return "[" + this.id + "] " + this.assertionResultList;
  }

}

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
/**
 */
package org.eclipse.wst.ws.internal.data;

public class LabelsAndIds
{
  private String[] labels_;
  private String[] ids_;
  
  /**
   * @return Returns the ids_.
   */
  public String[] getIds_()
  {
    return ids_;
  }
  /**
   * @param ids_ The ids_ to set.
   */
  public void setIds_(String[] ids_)
  {
    this.ids_ = ids_;
  }
  /**
   * @return Returns the labels_.
   */
  public String[] getLabels_()
  {
    return labels_;
  }
  /**
   * @param labels_ The labels_ to set.
   */
  public void setLabels_(String[] labels_)
  {
    this.labels_ = labels_;
  }
}

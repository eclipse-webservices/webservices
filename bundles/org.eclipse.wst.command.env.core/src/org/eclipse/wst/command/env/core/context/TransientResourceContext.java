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
package org.eclipse.wst.command.env.core.context;

/**
 * This class implements a ResourceContext interface where the state
 * of the context data is transient.
 *
 */
public class TransientResourceContext implements ResourceContext 
{
  private boolean overWriteFiles;
  private boolean createFolders;
  private boolean checkOutFiles;

  public TransientResourceContext() {
    setOverwriteFilesEnabled(ResourceDefaults.getOverwriteFilesDefault());
    setCreateFoldersEnabled(ResourceDefaults.getCreateFoldersDefault());
    setCheckoutFilesEnabled(ResourceDefaults.getCheckoutFilesDefault());
  }

  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#setOverwriteFilesEnabled(boolean)
   */
  public void setOverwriteFilesEnabled(boolean enable) {
    overWriteFiles = enable;
  }
  
  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#isOverwriteFilesEnabled()
   */
  public boolean isOverwriteFilesEnabled() {
    return overWriteFiles;
  }

  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#setCreateFoldersEnabled(boolean)
   */
  public void setCreateFoldersEnabled(boolean enable) {
    createFolders = enable;
  }
  
  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#isCreateFoldersEnabled()
   */
  public boolean isCreateFoldersEnabled() {
    return createFolders;
  }

  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#setCheckoutFilesEnabled(boolean)
   */
  public void setCheckoutFilesEnabled(boolean enable) {
    checkOutFiles = enable;
  }
  
  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#isCheckoutFilesEnabled()
   */
  public boolean isCheckoutFilesEnabled() {
    return checkOutFiles;
  }
  
  /**  
   * @see org.eclipse.wst.command.env.core.context.ResourceContext#copy()
   */
  public ResourceContext copy() {
    ResourceContext cc = new TransientResourceContext();
    cc.setOverwriteFilesEnabled(isOverwriteFilesEnabled());
    cc.setCreateFoldersEnabled(isCreateFoldersEnabled());
    cc.setCheckoutFilesEnabled(isCheckoutFilesEnabled());
    return cc;
  }
}

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
package org.eclipse.wst.command.internal.env.core.context;

public interface ResourceContext 
{
 /**
   * This constant string is used to lookup the overwrite files general preference from
   * the plugins local preferences store.
 **/
 public static final String PREFERENCE_OVERWRITE = "filesOverwrite";

 /**
   * This constant string is used to lookup the create folders general preference from
   * the plugins local preferences store.
 **/
 public static final String PREFERENCE_CREATE_FOLDERS = "createFolders";
 
 /**
   * This constant string is used to lookup the checkout files general preference from
   * the plugins local preferences store.
 **/
 public static final String PREFERENCE_CHECKOUT = "filesCheckout";

 /**
  * 
  * @param enable set whether overwriting of files is enabled.
  */
 public void setOverwriteFilesEnabled ( boolean enable);
 
 /**
  * 
  * @return returns whether overwriting of files is enabled.
  */
 public boolean isOverwriteFilesEnabled();
 
 /**
  * 
  * @param enable set whether creation of folders is enabled.
  */
 public void setCreateFoldersEnabled( boolean enable);
 
 /**
  * 
  * @return returns whether creation of folders is enabled.
  */
 public boolean isCreateFoldersEnabled(); 
 
 /**
  * 
  * @param enable sets whether automatic checkout of files is enabled.
  */
 public void setCheckoutFilesEnabled( boolean enable);
 
 /**
  * 
  * @return returns whether automatic checkout of files is enabled.
  */
 public boolean isCheckoutFilesEnabled();
 
 /**
  * 
  * @return returns a copy of this ResourceContext.
  */
 public ResourceContext copy();
}

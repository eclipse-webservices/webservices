/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060331   128827 kathy@ca.ibm.com - Kathy Chan
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
  * This constant string is used to lookup the enable skeleton merge general preference from
  * the plugins local preferences store.
**/
public static final String PREFERENCE_SKELETON_MERGE = "skeletonMerge";

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
  * @param enable sets whether automatic checkout of files is enabled.
  */
 
 public void setSkeletonMergeEnabled( boolean enable);
 
 /**
  * 
  * @return returns whether skeleton merge is enabled.
  */
 public boolean isSkeletonMergeEnabled();
 
 /**
  * 
  * @return returns a copy of this ResourceContext.
  */
 public ResourceContext copy();
}

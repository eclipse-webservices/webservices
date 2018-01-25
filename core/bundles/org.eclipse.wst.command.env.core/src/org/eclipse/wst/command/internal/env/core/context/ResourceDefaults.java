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
package org.eclipse.wst.command.internal.env.core.context;

public class ResourceDefaults
{
 private static final boolean PREFERENCE_OVERWRITE_DEFAULT = false;
 private static final boolean PREFERENCE_CREATE_FOLDERS_DEFAULT = true;
 private static final boolean PREFERENCE_CHECKOUT_DEFAULT = false;

 /**
  * 
  * @return returns the default setting for overwriting files.
  */
 public static boolean getOverwriteFilesDefault ()
 {
 	return PREFERENCE_OVERWRITE_DEFAULT;
 }
 
 /**
  * 
  * @return returns the default setting ofr creating folders.
  */
 public static boolean getCreateFoldersDefault ()
 { 
	return PREFERENCE_CREATE_FOLDERS_DEFAULT;
 }

 /**
  * 
  * @return returns the default setting for checking out files.
  */
 public static boolean getCheckoutFilesDefault()
 {
	return PREFERENCE_CHECKOUT_DEFAULT;
 }
}

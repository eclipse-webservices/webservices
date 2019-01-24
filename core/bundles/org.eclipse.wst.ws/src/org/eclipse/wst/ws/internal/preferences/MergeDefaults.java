/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060403   128827 kathy@ca.ibm.com - Kathy Chan
 * 20071109   209075 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

public class MergeDefaults
{
 private static final boolean PREFERENCE_SKELETON_MERGE_DEFAULT = true;
 
 /**
  * 
  * @return returns the default setting for skeleton merge.
  */
 public static boolean getSkeletonMergeDefault()
 {
	return PREFERENCE_SKELETON_MERGE_DEFAULT;
 }
}

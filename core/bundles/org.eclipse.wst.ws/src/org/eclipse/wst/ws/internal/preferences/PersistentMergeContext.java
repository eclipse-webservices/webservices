/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060403   128827 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.wst.command.internal.env.context.PersistentContext;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;


public class PersistentMergeContext extends PersistentContext
{
	 /**
	  * This constant string is used to lookup the enable skeleton merge general preference from
	  * the plugins local preferences store.
	**/
	public static final String PREFERENCE_SKELETON_MERGE = "skeletonMerge";
	
  private static PersistentMergeContext context_ = null;
  
  public static PersistentMergeContext getInstance()
  {
    if( context_ == null )
    {
      context_ = new PersistentMergeContext();
      context_.load();
    }
    
    return context_;
  }
  
  public PersistentMergeContext()
  {
	  super(WSPlugin.getInstance());
  }

  public void load()
  {
    setDefault(PREFERENCE_SKELETON_MERGE, MergeDefaults.getSkeletonMergeDefault());
  }

  public void setSkeletonMergeEnabled(boolean enable)
  {
    setValue(PREFERENCE_SKELETON_MERGE, enable);
  }

  public boolean isSkeletonMergeEnabled()
  {
    return getValueAsBoolean(PREFERENCE_SKELETON_MERGE);
  }

}

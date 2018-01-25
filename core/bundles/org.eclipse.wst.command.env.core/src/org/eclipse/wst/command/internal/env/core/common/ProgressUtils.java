/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.command.internal.env.core.common;

import org.eclipse.core.runtime.IProgressMonitor;

public class ProgressUtils
{
  static public void report( IProgressMonitor monitor, String message )
  {
    if( monitor != null ) 
    {
       monitor.beginTask( message, IProgressMonitor.UNKNOWN );
    }
  }
}

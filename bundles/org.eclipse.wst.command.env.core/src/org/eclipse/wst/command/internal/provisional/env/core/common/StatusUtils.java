/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.command.internal.provisional.env.core.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

public class StatusUtils
{
  public static IStatus errorStatus( String errorMessage )
  {
    return new Status( IStatus.ERROR, "id", 0, errorMessage, null );
  }
  
  public static IStatus errorStatus( Throwable exc )
  {
    return new Status( IStatus.ERROR, "id", 0, exc.getMessage(), exc );
  }
  
  public static IStatus errorStatus( String message, Throwable exc )
  {
    return new Status( IStatus.ERROR, "id", 0, message, exc );
  }
  
  public static MultiStatus multiStatus( String message, IStatus[] children, Throwable exc )
  {
    return new MultiStatus( "id", 0, children, message, exc );  
  }
  
  public static MultiStatus multiStatus( String message, IStatus[] children )
  {
    return new MultiStatus( "id", 0, children, message, null );  
  }
  
  public static IStatus warningStatus( String warningMessage )
  {
    return new Status( IStatus.WARNING, "id", 0, warningMessage, null );
  }
  
  public static IStatus warningStatus( String warningMessage, Throwable exc )
  {
    return new Status( IStatus.WARNING, "id", 0, warningMessage, exc );
  }
  
  public static IStatus infoStatus( String infoMessage )
  {
    return new Status( IStatus.INFO, "id", 0, infoMessage, null );
  }
}

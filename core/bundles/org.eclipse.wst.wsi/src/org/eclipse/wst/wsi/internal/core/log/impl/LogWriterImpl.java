/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.log.impl;

import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.impl.DocumentWriterImpl;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogWriter;

/**
 * Defines the implementation used to write the Log file.
 * 
 * @version 1.0.1
 * @author Neil Delima (nddelima@ca.ibm.com)
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class LogWriterImpl extends DocumentWriterImpl implements LogWriter
{
  /**
   * @see org.eclipse.wst.wsi.internal.core.log.LogWriter#write(Log)
   */
  public synchronized void write(Log log)
    throws IllegalStateException, WSIException
  {
    // If the writer was not specified, then throw an exception
    if (writer == null)
      throw new IllegalStateException("Could not write log file since writer was not set.");

    // Write log file
    write(log, writer);
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.LogWriter#write(Log, Writer)
   */
  public synchronized void write(Log log, Writer writer) throws WSIException
  {
    // ADD:
    throw new WSIException("Write entire log file function not supported yet.");
  }
}

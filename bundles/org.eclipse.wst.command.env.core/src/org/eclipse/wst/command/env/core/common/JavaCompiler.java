/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.env.core.common;

import org.eclipse.wst.command.env.core.uri.URI;

/**
 * This is the interface for an object that does Java compilation
 * of a resource.
 */
public interface JavaCompiler
{
  /**
   * Compiles the given Java file to the given class file,
   * using context information on classpaths, options, etc.
   */
  public void compile ( URI javaUri, URI classUri, JavaCompilerContext context );
}

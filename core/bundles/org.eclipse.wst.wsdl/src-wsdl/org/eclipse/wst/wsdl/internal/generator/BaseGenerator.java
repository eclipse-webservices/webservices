/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.generator;


import org.eclipse.wst.wsdl.Definition;


public abstract class BaseGenerator
{
  private String name;

  private String refName;

  private boolean overwrite;

  protected Definition definition;

  protected ContentGenerator contentGenerator;

  public void setContentGenerator(ContentGenerator generator)
  {
    contentGenerator = generator;
  }

  public ContentGenerator getContentGenerator()
  {
    return contentGenerator;
  }

  public void setOverwrite(boolean overwrite)
  {
    this.overwrite = overwrite;
  }

  public boolean getOverwrite()
  {
    return overwrite;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setRefName(String refName)
  {
    this.refName = refName;
  }

  public String getName()
  {
    return name;
  }

  public String getRefName()
  {
    return refName;
  }

  public Definition getDefinition()
  {
    return definition;
  }

  public String getProtocol()
  {
    if (contentGenerator != null)
    {
      return contentGenerator.getProtocol();
    }

    return null;
  }
}

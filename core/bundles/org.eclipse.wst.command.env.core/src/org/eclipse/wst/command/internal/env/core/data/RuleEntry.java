/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.core.data;


public class RuleEntry 
{
  public String      sourceType_;
  public String      sourceProperty_;
  public String      targetProperty_;
  public Transformer transformer_;
  
 
  public RuleEntry( String sourceType,
                    String sourceProperty,
                    String targetProperty, 
                    Transformer transformer )
  {
    sourceType_     = sourceType;
    sourceProperty_ = sourceProperty;
    targetProperty_ = targetProperty;    
    transformer_    = transformer;
  }
}

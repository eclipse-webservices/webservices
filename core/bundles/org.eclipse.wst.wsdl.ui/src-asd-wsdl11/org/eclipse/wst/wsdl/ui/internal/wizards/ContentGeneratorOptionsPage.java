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
package org.eclipse.wst.wsdl.ui.internal.wizards;  
        
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;

public interface ContentGeneratorOptionsPage
{                                          
  public void init(BaseGenerator baseGenerator);             
  public Composite createControl(Composite parent);
  public Composite getControl();
  
  // TODO can this go into init?
  //
  public void setOptionsOnGenerator();
  public boolean isOverwriteApplicable();
}
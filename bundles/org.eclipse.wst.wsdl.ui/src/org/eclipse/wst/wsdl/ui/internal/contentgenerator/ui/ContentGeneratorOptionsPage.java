/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui;  
        
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.AbstractGenerator;


public interface ContentGeneratorOptionsPage
{                                          
  public void init(AbstractGenerator abstractGenerator);             
  public Composite createControl(Composite parent);
  public boolean isOverwriteApplicable();
}
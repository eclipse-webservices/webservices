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
package org.eclipse.wst.wsdl.ui.internal.extension;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
                   
// Note this class will likely be removed in the future when the ModelQuery's extension mechanism is updated
//
public interface IModelQueryContributor
{                                      
  public void setModel(IDOMModel xmlModel);
}
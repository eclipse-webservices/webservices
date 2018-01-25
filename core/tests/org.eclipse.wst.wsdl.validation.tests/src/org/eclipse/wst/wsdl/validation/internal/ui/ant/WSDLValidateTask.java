/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import java.util.List;

/**
 * This class is used to expose functionality from the WSDLValidate class for testing.
 */
public class WSDLValidateTask extends WSDLValidate
{

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.ui.ant.WSDLValidate#getFileList()
   */
  public List getFileList()
  {
    return super.getFileList();
  }

}

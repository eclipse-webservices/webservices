/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.wsi.ui.internal.actions.WSIValidateAction;
import org.eclipse.validate.IReporter;
import org.eclipse.validate.IValidator;
import org.eclipse.validate.DefaultReporter;

/**
 * A Validator that performs validation on a WS-I Message Log file. 
 * 
 * @author David Lauzon, IBM
 */
public class WSIMessageValidator implements IValidator
{
  protected IReporter reporter;
  public static final String WSI_MESSAGE_VALIDATOR_ID = "wsimessagevalidator".intern();

  /* (non-Javadoc)
   * @see org.eclipse.validate.IValidator#validate(org.eclipse.core.resources.IFile)
   */
  public void validate(IFile file)
  {  
    WSIValidateAction validateAction = new WSIValidateAction(file, false);
    validateAction.setValidator(this);
    validateAction.setReporter(getReporter());
    validateAction.run();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.validate.IValidator#getReporter()
   */
  public IReporter getReporter()
  {
    if (reporter == null)
    {
      reporter = new DefaultReporter(WSI_MESSAGE_VALIDATOR_ID);	
    }	
    return reporter;
  }

  /* (non-Javadoc)
   * @see org.eclipse.validate.IValidator#setReporter(org.eclipse.validate.IReporter)
   */
  public void setReporter(IReporter reporter)
  {
  	this.reporter = reporter;
  }
}

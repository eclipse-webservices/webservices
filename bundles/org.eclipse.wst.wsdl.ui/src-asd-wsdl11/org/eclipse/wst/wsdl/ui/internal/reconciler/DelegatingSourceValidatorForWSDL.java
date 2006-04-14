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
package org.eclipse.wst.wsdl.ui.internal.reconciler;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.validation.internal.ConfigurationManager;
import org.eclipse.wst.validation.internal.ProjectConfiguration;
import org.eclipse.wst.validation.internal.ValidationRegistryReader;
import org.eclipse.wst.validation.internal.ValidatorMetaData;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.validation.DelegatingSourceValidator;

/**
 * This performs the as-you-type validation
 * @author Mark Hutchinson
 *
 */
public class DelegatingSourceValidatorForWSDL extends DelegatingSourceValidator
{

  final private static String VALIDATOR_CLASS = "org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLDelegatingValidator"; //$NON-NLS-1$ 

  public DelegatingSourceValidatorForWSDL()
  { 
    super();
  }
  
  protected IValidator getDelegateValidator()
  {
    try
    {
      ValidationRegistryReader registry = ValidationRegistryReader.getReader();
      return registry.getValidator(VALIDATOR_CLASS);
    }
    catch (Exception e)
    { //
    } 
    return null;
  }
  
	protected boolean isDelegateValidatorEnabled(IFile file) {
		boolean enabled = true;
		try {
			ProjectConfiguration configuration = ConfigurationManager.getManager().getProjectConfiguration(file.getProject());
			ValidatorMetaData vmd = ValidationRegistryReader.getReader().getValidatorMetaData(VALIDATOR_CLASS);
			if (configuration.isBuildEnabled(vmd) || configuration.isManualEnabled(vmd))
				enabled = true;
			else
				enabled = false;
		}
		catch (InvocationTargetException e) {
			Logger.log(Logger.WARNING_DEBUG, e.getMessage(), e);
		}
		return enabled;
	}
}

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

package org.eclipse.wst.wsdl.validation.internal.ui.eclipse;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.validation.core.IFileDelta;
import org.eclipse.wst.validation.core.IHelper;
import org.eclipse.wst.validation.core.IReporter;
import org.eclipse.wst.validation.core.IValidator;
import org.eclipse.wst.validation.core.MessageLimitException;
import org.eclipse.wst.validation.core.ValidationException;
import org.eclipse.wst.validation.internal.operations.IRuleGroup;
import org.eclipse.wst.validation.internal.operations.ValidatorManager;
import org.eclipse.wst.xml.validation.internal.core.Helper;


/**
 * A validator to plug the WSDL validator into the validation framework.
 */
public class Validator implements IValidator
{

  /**
   * Validate the given IFile.
   * 
   * @param file
   *          The file to validate.
   */
  public void validate(IFile file)
  {
    ValidateWSDLAction validateAction = new ValidateWSDLAction(file, false);
    validateAction.setValidator(this);
    validateAction.run();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.validation.core.IValidator#validate(org.eclipse.wst.validation.core.IHelper, org.eclipse.wst.validation.core.IReporter, org.eclipse.wst.validation.core.IFileDelta[])
   */
  public void validate(IHelper helper, IReporter reporter, IFileDelta[] changedFiles) throws ValidationException
  {
    if (changedFiles != null && changedFiles.length > 0)
    {
      for (int i = 0; i < changedFiles.length; i++)
      {
        IFileDelta changedFile = changedFiles[i];

        String fileName = changedFile.getFileName();
        if (fileName != null)
        {
          Object[] parms = {fileName};

          IFile file = (IFile) helper.loadModel(Helper.GET_FILE, parms);
          if (file != null)
          {
            if (((Helper) helper).isInJavaBuildPath(file) && !((Helper)helper).isInJavaSourcePath(file))
            {
              continue;
            }
            validateIfNeeded(file, helper, reporter);
          }
        }
      }
    } else
    {
      Object[] parms = {this.getClass().getName()};
      Collection files = (Collection) helper.loadModel(Helper.GET_PROJECT_FILES, parms);
      Iterator iter = files.iterator();
      while (iter.hasNext())
      {
        IFile file = (IFile) iter.next();
        validateIfNeeded(file, helper, reporter);
      }
    }
  }

  /**
   * @param file
   * @param reporter
   * @param ruleGroup
   */
  public void validate(IFile file, IReporter reporter, int ruleGroup)
  {
    ValidateWSDLAction validateAction = new ValidateWSDLAction(file, false);
    validateAction.setValidator(this);
    validateAction.run();
  }

  /**
   * Validate the given file if validation is required.
   * 
   * @param file
   * @param model
   * @param helper
   * @param reporter
   */
  protected void validateIfNeeded(IFile file, Object model, IHelper helper, IReporter reporter)
  {
    if (model == null)
    {
      validateIfNeeded(file, helper, reporter);
    }
  }

  /**
   * Unpacks the fileModelPair and returns an IFile object.
   */
  protected IFile getFile(Object object)
  {
    IFile result = null;
    if (object instanceof List)
    {
      List fileModelPair = (List) object;
      if (fileModelPair.size() > 0)
      {
        Object file = fileModelPair.get(0);
        if (file instanceof IFile)
        {
          result = (IFile) file;
        }
      }
    }
    return result;
  }

  /**
   * Validate the given file if validation is required.
   * 
   * @param file
   *          The file to validate.
   * @param helper
   *          An aid for the validation.
   * @param reporter
   *          The reporter to report the validation messages.
   */
  protected void validateIfNeeded(IFile file, IHelper helper, IReporter reporter)
  {
    ValidatorManager mgr = ValidatorManager.getManager();
    if (mgr.isMessageLimitExceeded(file))
    {
      throw new MessageLimitException();
    }
    // Pass in a "null" so that loadModel doesn't attempt to cast the result into a RefObject.
    Integer ruleGroupInt = (Integer) helper.loadModel(IRuleGroup.PASS_LEVEL, null); 
    int ruleGroup = (ruleGroupInt == null) ? IRuleGroup.PASS_FULL : ruleGroupInt.intValue();

    validate(file, reporter, ruleGroup);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.etools.validation.IValidator#cleanup(com.ibm.etools.validation.IReporter)
   */
  public void cleanup(IReporter reporter)
  {
  }
}
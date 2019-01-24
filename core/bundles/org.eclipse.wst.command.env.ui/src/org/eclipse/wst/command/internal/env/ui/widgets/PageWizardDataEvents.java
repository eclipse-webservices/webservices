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
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * Dynamic wizard implementations that want to create different
 * kinds of wizard pages need to implement this interface.
 * The WizardPageFactory returns a class of this type.
 *
 */
public interface PageWizardDataEvents extends IWizardPage
{
  /**
   * 
   * @return returns the WidgetDataEvents object created by the addControls
   * method of a WidgetContributor.
   */
  public WidgetDataEvents getDataEvents();
  
  /**
   * Validates that this wizard page is complete. 
   *
   */
  public void validatePageToStatus();
}

/*******************************************************************************
 * Copyright (c) 2002-2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;

/**
 * Interface definition for validation test procedures common to envelopes and
 * messages.
 */
public interface LogValidator extends BaseValidator {
    /**
     * Validate the envelope located by the log entry. 
     * @param entryContext a log entry locating an envelope.
     * @throws WSIException if an unexpected error occurred while
     *         processing the log entry.
     */
    public void validate(EntryContext entryContext) throws WSIException;
}

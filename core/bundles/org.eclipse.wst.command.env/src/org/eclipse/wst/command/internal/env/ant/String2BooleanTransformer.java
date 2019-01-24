/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.ant;

import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class String2BooleanTransformer implements Transformer {

	public Object transform(Object value) {
			String str = (String)value;
			str = str.toLowerCase();
			if (str.equals("true"))
			{
				return new Boolean(true);
			}
			else return new Boolean(false);		

	}

}

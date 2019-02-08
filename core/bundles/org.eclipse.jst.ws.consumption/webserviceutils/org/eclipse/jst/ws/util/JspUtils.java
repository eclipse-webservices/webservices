/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

package org.eclipse.jst.ws.util;

/**
 * @author gilberta
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JspUtils {
	public static String markup(String text) {
			if (text == null) {
				return null;
			}

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				switch (c) {
					case '<':
						buffer.append("&lt;");
						break;
					case '&':
						buffer.append("&amp;");
						break;
					case '>':
						buffer.append("&gt;");
						break;
					case '"':
						buffer.append("&quot;");
						break;
					default:
						buffer.append(c);
						break;
				}
			}
			return buffer.toString();
		}

}

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package webserviceutils.org.eclipse.jst.ws.util;

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

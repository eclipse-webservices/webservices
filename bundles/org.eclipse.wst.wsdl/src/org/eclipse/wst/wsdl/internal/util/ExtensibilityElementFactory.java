/*
 * Created on Apr 23, 2004
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.wst.wsdl.internal.util;

import org.eclipse.wst.wsdl.ExtensibilityElement;


public interface ExtensibilityElementFactory
{
  ExtensibilityElement createExtensibilityElement(String namespace, String localName);
}

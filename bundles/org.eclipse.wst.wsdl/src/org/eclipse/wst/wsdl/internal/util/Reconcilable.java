package org.eclipse.wst.wsdl.internal.util;

import org.w3c.dom.Element;

public interface Reconcilable 
{
  public void setElement(Element element);
  public Element getElement();
  public void reconcileAttributes(Element changedElement);
  public void reconcileReferences(boolean deep);
}

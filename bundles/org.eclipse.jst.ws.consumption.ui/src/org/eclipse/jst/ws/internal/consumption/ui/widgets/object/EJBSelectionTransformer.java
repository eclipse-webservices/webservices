package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class EJBSelectionTransformer implements Transformer
{

  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
      Object sel = ((IStructuredSelection)value).getFirstElement();
      if (sel instanceof EnterpriseBean)
      {
        return new StructuredSelection(((EnterpriseBean)sel).getName());
      }
    }
    return value;
  }
  

}

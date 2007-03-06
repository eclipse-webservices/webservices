/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.xsd.ui.internal.adapters.XSDAdapterFactory;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.RootContentEditPart;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.RootEditPart;
import org.eclipse.wst.xsd.ui.internal.adt.typeviz.design.figures.TypeVizFigureFactory;
import org.eclipse.wst.xsd.ui.internal.design.editparts.XSDEditPartFactory;
import org.eclipse.xsd.XSDConcreteComponent;

public class XSDGraphViewerDialog extends PopupDialog
{
  protected Object model;
  protected ScrollingGraphicalViewer viewer;
  protected IOpenExternalEditorHelper openExternalEditorHelper;
  private CloseDialogListener closeDialogListener;

  public XSDGraphViewerDialog(Shell parentShell, String titleText, String infoText, Object model)
  {
    super(parentShell, SWT.RESIZE, false, false, true, false, titleText, infoText);
    setModel(model);
    closeDialogListener = new CloseDialogListener();
  }

  public void setOpenExternalEditor(IOpenExternalEditorHelper helper)
  {
    this.openExternalEditorHelper = helper;
  }
  
  protected void fillDialogMenu(IMenuManager dialogMenu)
  {
    super.fillDialogMenu(dialogMenu);
    dialogMenu.add(new Separator());
    dialogMenu.add(new SetOpenInEditor());
  }

  protected Control createDialogArea(Composite parent)
  {
    viewer = new ScrollingGraphicalViewer();
    Composite c = new Composite(parent, SWT.NONE);
    c.setBackground(ColorConstants.white);
    c.setLayout(new FillLayout());

    RootEditPart root = new RootEditPart();
    viewer.setRootEditPart(root);

    viewer.createControl(c);
    viewer.getControl().setBackground(ColorConstants.white);
    EditPartFactory editPartFactory = new XSDEditPartFactory(new TypeVizFigureFactory());
    viewer.setEditPartFactory(editPartFactory);

    RootContentEditPart rootContentEditPart = new RootContentEditPart();
    rootContentEditPart.setModel(model);
    viewer.setContents(rootContentEditPart);

    return c;
  }

  public int open()
  {
    int value = super.open();
    closeDialogListener.addListeners();
    return value;
  }
  
  private void setModel(Object model)
  {
    Assert.isTrue(model instanceof XSDConcreteComponent);
    this.model = XSDAdapterFactory.getInstance().adapt((XSDConcreteComponent) model);
  }
    
  protected class SetOpenInEditor extends Action
  {
    public SetOpenInEditor()
    {
      super(Messages._UI_ACTION_OPEN_IN_NEW_EDITOR);
    }
    
    public void run()
    {
      if (openExternalEditorHelper != null)
      {
        try
        {
          openExternalEditorHelper.openExternalEditor();
        }
        catch (Exception e)
        {
          
        }
      }
      close();
    }
  }
  
  public boolean close()
  {
    closeDialogListener.removeListeners();
    return super.close();
  }
  
  private final class CloseDialogListener implements Listener {

    public void handleEvent(Event e) {
     
      if (e.type == SWT.Close || e.type == SWT.Deactivate) {
        close();
        return;
      }
    }

    void addListeners() {
      Shell shell = getShell();
      shell.addListener(SWT.Deactivate, this);
      shell.addListener(SWT.Close, this);
    }

    void removeListeners() {
      Shell shell = getShell();
      shell.removeListener(SWT.Deactivate, this);
      shell.removeListener(SWT.Close, this);
    }
  }
}

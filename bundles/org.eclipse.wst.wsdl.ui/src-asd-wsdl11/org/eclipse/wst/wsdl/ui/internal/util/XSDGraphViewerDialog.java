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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
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
  private OpenEditorLinkListener linkListener;
  private Label nsInfoLabel;
  private Hyperlink link;
  private String infoText;
  private Font infoFont;

  public XSDGraphViewerDialog(Shell parentShell, String titleText, String infoText, Object model)
  {
    super(parentShell, SWT.RESIZE, false, false, true, false, titleText, infoText);
    setModel(model);
    closeDialogListener = new CloseDialogListener();
    linkListener = new OpenEditorLinkListener();
    this.infoText = infoText;
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
  
  protected Control createInfoTextArea(Composite parent) {
    Composite infoComposite = new Composite(parent, SWT.NONE);
    GridLayout gridLayout = new GridLayout(2, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    infoComposite.setLayout(gridLayout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    infoComposite.setLayoutData(gd);
    
    nsInfoLabel = new Label(infoComposite, SWT.LEFT);
    nsInfoLabel.setText(infoText);
    
    Font font = nsInfoLabel.getFont();
    FontData[] fontDatas = font.getFontData();
    for (int i = 0; i < fontDatas.length; i++) {
      fontDatas[i].setHeight(fontDatas[i].getHeight() * 9 / 10);
    }
    infoFont = new Font(nsInfoLabel.getDisplay(), fontDatas);
    nsInfoLabel.setFont(infoFont);
    gd = new GridData(GridData.FILL_HORIZONTAL
        | GridData.HORIZONTAL_ALIGN_BEGINNING
        | GridData.VERTICAL_ALIGN_BEGINNING);
    nsInfoLabel.setLayoutData(gd);
    nsInfoLabel.setForeground(parent.getDisplay().getSystemColor(
        SWT.COLOR_WIDGET_DARK_SHADOW));
    
    link = new Hyperlink(infoComposite, SWT.RIGHT);
    link.setText(Messages._UI_ACTION_OPEN_IN_NEW_EDITOR);
    link.setFont(infoFont);
    link.addHyperlinkListener(linkListener);
    return infoComposite;
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
    }
  }
  
  public boolean close()
  {
    closeDialogListener.removeListeners();
    link.removeHyperlinkListener(linkListener);
    infoFont.dispose();
    infoFont = null;
    return super.close();
  }
  
  private final class OpenEditorLinkListener implements IHyperlinkListener
  {

    public void linkActivated(HyperlinkEvent e)
    {
      new SetOpenInEditor().run();
      
    }

    public void linkEntered(HyperlinkEvent e)
    {
      link.setForeground(ColorConstants.lightBlue);
    }

    public void linkExited(HyperlinkEvent e)
    {
      link.setForeground(link.getParent().getForeground());
    }
    
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

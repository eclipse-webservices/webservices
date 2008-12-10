/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.ISelectionMapper;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;
import org.eclipse.wst.wsdl.ui.internal.refactor.actions.RenameComponentAction;

public class NameSection extends ASDAbstractSection implements IHyperlinkListener {
	protected static final String NEW_STRING = Messages._UI_BUTTON_NEW; //$NON-NLS-1$
	protected static final String BROWSE_STRING = Messages._UI_BUTTON_BROWSE; //$NON-NLS-1$
	protected boolean isTraversing = false;
	CLabel nameLabel;
	protected Text nameText;
  /**
   * Clicking on it invokes the refactor->rename action.
   */
  protected ImageHyperlink renameHyperlink;
	
	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);

		composite =	getWidgetFactory().createFlatFormComposite(parent);
		
		FormData data;		
    
		nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace);
		data.top = new FormAttachment(0, 0);
		nameText.setLayoutData(data);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(nameText, ASDEditorCSHelpIds.PROPERTIES_NAME_TEXT);

		nameLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_NAME + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText, 0, SWT.CENTER);
		nameLabel.setLayoutData(data);
		applyTextListeners(nameText);
	}
	
  private void showRefactorButton() {
    if (isReadOnly) {
      return;
    }
      
    if (renameHyperlink == null) {
      renameHyperlink = getWidgetFactory().createImageHyperlink(composite, SWT.NONE);

      renameHyperlink.setImage(WSDLEditorPlugin.getInstance().getImage("icons/quickassist.gif")); //$NON-NLS-1$
      renameHyperlink.setToolTipText(Messages._UI_TOOLTIP_RENAME_REFACTOR);
      renameHyperlink.addHyperlinkListener(this);
    }

    FormData data = new FormData();
    data.right = new FormAttachment(100, -rightMarginSpace);
    data.top = new FormAttachment(0, 0);
    renameHyperlink.setLayoutData(data);
    
    data = (FormData) nameText.getLayoutData(); 
    FormAttachment right = data.right;
    right.control = renameHyperlink;
    right.offset = -ITabbedPropertyConstants.HSPACE;
    right.alignment = SWT.LEFT;
    data.top = new FormAttachment(renameHyperlink, 0, SWT.CENTER);
    renameHyperlink.setVisible(true);
  }
  
  private void hideRefactorButton() {
    if (isReadOnly) {
      return;
    }

    if (renameHyperlink == null) {
      return;
    }
    FormData data = (FormData) nameText.getLayoutData();
    FormAttachment right = data.right;
    right.control = null;
    right.numerator = 100;
    right.offset = -rightMarginSpace; 
    data.top = new FormAttachment(0, 0);
    renameHyperlink.setVisible(false);
  }
  
  
	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (nameText.isDisposed() || nameText.isFocusControl()) {
			return;
		}
		
		setListenerEnabled(false);
		String name = null;
		if (getModel() instanceof INamedObject) {
			name = ((INamedObject) getModel()).getName();
		}
		else if (getModel() instanceof EditPart) {
			Object model = ((EditPart) getModel()).getModel(); 
			name = ((INamedObject) model).getName();
		}
		
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}
		
		nameText.setText(name);
		setControlForegroundColor(nameText);
    
    if (canRefactor()) {
      showRefactorButton();
    }
    else {
      hideRefactorButton();
    }
    setListenerEnabled(true);
	}
	
	public boolean shouldUseExtraSpace()
	{
		return false;
	}
	
	public void doHandleEvent(Event event)
	{
		if (event.widget == nameText && !nameText.isDisposed()) {
			String newValue = nameText.getText();
			Object model = getModel();
			INamedObject namedObject = null;
			
			if (model instanceof INamedObject) {
				namedObject = (INamedObject) model;
			}
			
			if (namedObject != null) {
				if ( !newValue.equals( namedObject.getName() ) ){
				  Command command = namedObject.getSetNameCommand(newValue);
				  executeCommand(command);
				}
			}
		}
	}
  
  private void invokeRenameRefactoring() {
    IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    Definition definition = (Definition)editor.getAdapter(Definition.class);
    ISelection selection = editor.getSite().getSelectionProvider().getSelection();
    ISelectionMapper mapper = (ISelectionMapper) editor.getAdapter(ISelectionMapper.class);
    selection = mapper != null ? mapper.mapSelection(selection) : selection;
    RenameComponentAction action = new RenameComponentAction(selection, definition);
    action.update(selection);
    action.run();
  }

  private Object getRealModel() {
    Object realModel = getModel();
    
    if (realModel instanceof EditPart) {
      realModel = ((EditPart) getModel()).getModel();
    }
    
    return realModel;
  }

  /**
   * Determines if the model object's name can be refactored.
   * @return true if the model object can be refactored, false otherwise.
   */
  private boolean canRefactor() {
    Object model = getRealModel();

    boolean canRefactor = model instanceof IMessage || 
                          model instanceof IBinding || 
                          model instanceof IInterface;
    return canRefactor;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
   */
  public void linkActivated(HyperlinkEvent e)
  {
    invokeRenameRefactoring();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent)
   */
  public void linkEntered(HyperlinkEvent e)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org.eclipse.ui.forms.events.HyperlinkEvent)
   */
  public void linkExited(HyperlinkEvent e)
  {
  }
  
  protected boolean shouldPerformComboSelection(Event event, Object selectedItem)
  {
    // if traversing through combobox, don't automatically pop up
    // the browse and new dialog boxes
    boolean wasTraversing = isTraversing;
    if (isTraversing)
      isTraversing = false;
      
    // we only care about default selecting (hitting enter in combobox)
    // for browse.. and new..
    if (event.type == SWT.DefaultSelection)
    {
      if (!(selectedItem instanceof String))
        return false;
      if (!(BROWSE_STRING.equals(selectedItem) || NEW_STRING.equals(selectedItem)))
        return false;
    }
    
    if (wasTraversing && selectedItem instanceof String)
    {
      if (BROWSE_STRING.equals(selectedItem) || NEW_STRING.equals(selectedItem))
        return false;
    }
    return true;
  }
}

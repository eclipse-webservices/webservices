/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.graph;

import java.util.List;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.actions.ExtensibleMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.gef.util.figures.CenterLayout;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.ComponentViewerRootEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.figures.MyConnectionRenderingHelper;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.w3c.dom.Node;

import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ConnectionRenderingFigure;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.IConnectionRenderingViewer;

public class WSDLComponentViewer extends ScrollingGraphicalViewer implements IConnectionRenderingViewer
{
  protected EditDomain editDomain;
  protected WSDLEditor editor;
  protected ISelectionProvider menuSelectionProvider;
  protected ComponentViewerRootEditPart componentViewerRootEditPart;
  protected ConnectionRenderingFigure connectionRenderingFigure;
  protected boolean isPreserveExpansionEnabled;

  public WSDLComponentViewer(WSDLEditor editor, ISelectionProvider menuSelectionProvider)
  {
    super();
    this.editor = editor;
    this.menuSelectionProvider = menuSelectionProvider;
  }

  public void setPreserveExpansionEnabled(boolean isPreserveExpansionEnabled)
  {
    this.isPreserveExpansionEnabled = isPreserveExpansionEnabled;
  }

  public boolean isPreserveExpansionEnabled()
  {
    return isPreserveExpansionEnabled;
  }

  public ConnectionRenderingFigure getConnectionRenderingFigure()
  {
    return connectionRenderingFigure;
  }

  public Object getInput()
  {
    return componentViewerRootEditPart.getModel();
  }

  public void setInput(Object object)
  {
    componentViewerRootEditPart.setInput(object);
    
    if (object instanceof Definition)
    {
      editor.getGraphViewer().setBackButtonEnabled(false);
    }
    else
    {
      editor.getGraphViewer().setBackButtonEnabled(true);
    }
    
    // todo.. revisit this to understand why we don't get a set input
    // when drilling down into a schema element or type
    //
    int layoutAlignment = (object instanceof Definition) ? 
                          CenterLayout.ALIGNMENT_TOP :
                          CenterLayout.ALIGNMENT_CENTER;
    
    ScalableRootEditPart graphicalRootEditPart = (ScalableRootEditPart)getRootEditPart();
    CenterLayout centerLayout = (CenterLayout)graphicalRootEditPart.getLayer(LayerConstants.PRIMARY_LAYER).getLayoutManager();
    centerLayout.setVerticalAlignment(layoutAlignment);
  }

  //public void setSelection(Object object)
  //{                                      
  //}

  protected void hookControl()
  {
    super.hookControl();

    getControl().setBackground(ColorConstants.white);

    editDomain = new DefaultEditDomain(null);
    ((DefaultEditDomain)editDomain).setDefaultTool(new SelectionTool());
    editDomain.loadDefaultTool();
    editDomain.addViewer(this);

    componentViewerRootEditPart = new ComponentViewerRootEditPart();

    KeyAdapter keyListener = new KeyAdapter()
    {
      public void keyReleased(KeyEvent e)
      {   	
        if (e.keyCode == SWT.F3)
        {
          ISelection selection = editor.getSelectionManager().getSelection();
          if (selection instanceof IStructuredSelection)
          {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            if (object instanceof EObject)
            {
			  OpenOnSelectionHelper helper = new OpenOnSelectionHelper(editor.getDefinition());
              helper.openEditor((EObject)object);
            }
          }

        }
      }
    };
    getControl().addKeyListener(keyListener);

    setContents(componentViewerRootEditPart);
    //getFigureCanvas().addKeyListener(new FigureCanvasKeyboardHandler(editor));
    getRootEditPart().activate();

    componentViewerRootEditPart.setInput(editor.getDefinition());

    ScalableRootEditPart graphicalRootEditPart = (ScalableRootEditPart)getRootEditPart();

    // set the layout for the primary layer so that the children are always centered
    //
    CenterLayout centerLayout = new CenterLayout();
    centerLayout.setVerticalAlignment(CenterLayout.ALIGNMENT_TOP);
    graphicalRootEditPart.getLayer(LayerConstants.PRIMARY_LAYER).setLayoutManager(centerLayout); //new ContainerLayout());//

    setContextMenu(new InternalContextMenuProvider(this, editor));

    // add the ConnectionFigure which is responsible for drawing all of the lines in the view
    //                       
    IFigure figure = graphicalRootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
    figure.setLayoutManager(new StackLayout());

    final MyConnectionRenderingHelper connectionRenderingHelper = new MyConnectionRenderingHelper(this);

    connectionRenderingFigure = new ConnectionRenderingFigure(graphicalRootEditPart.getLayer(LayerConstants.PRIMARY_LAYER))
    {
      protected void fillShape(Graphics graphics)
      {
        super.fillShape(graphics);
        connectionRenderingHelper.fillShapeHelper(graphics);
      }
    };
    figure.add(connectionRenderingFigure);
    figure.validate();
  }
  
  public WSDLEditor getWSDLEditor() {
  	return editor;
  }

  public class InternalContextMenuProvider extends ContextMenuProvider
  {
    protected EditPartViewer viewer;
    protected ExtensibleMenuActionContributor extensibleMenuActionContributor;

    public InternalContextMenuProvider(EditPartViewer viewer, WSDLEditor editor)
    {
      super(viewer);
      this.viewer = viewer;
      extensibleMenuActionContributor = new ExtensibleMenuActionContributor(editor);
    }

    public void buildContextMenu(IMenuManager menu)
    {
      List list = getSelectedEditParts();
      if (list.size() > 0)
      {
        EditPart editPart = (EditPart)list.get(0);
        Object object = editPart.getModel();

        //if (editPart instanceof PropertyEditPart)
        //{
        //  object = ((PropertyEditPart)editPart).getOwnerModel();
        //}               

        if (object != null)
        {
          Node node = null;
          if (object instanceof Node)
          {
            node = (Node)object;
          }
          else if (object instanceof WSDLElement)
          {
            node = ((WSDLElement)object).getElement();
          }
          else if (object instanceof WSDLGroupObject)
          {
            node = ((WSDLGroupObject)object).getDefinition().getElement();
          }
          extensibleMenuActionContributor.contributeMenuActions(menu, node, object);
        }
      }
    }
  }
}

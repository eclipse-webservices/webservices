package org.eclipse.wst.wsdl.ui.internal.asd.util;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicalViewer;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.ASDEditPartFactory;

/**
 * @depracated
 */
public class ASDEditPartFactoryHelper {
	private static ASDEditPartFactoryHelper instance;
	
	private EditPartFactory editPartFactory;
	
	public static ASDEditPartFactoryHelper getInstance() {
		if (instance == null) {
			instance = new ASDEditPartFactoryHelper();
		}
		
		return instance;
	}
	
	public EditPartFactory getEditPartFactory() {
		if (editPartFactory == null) {
			editPartFactory = new ASDEditPartFactory();
		}
		return editPartFactory;
	}
	
	public void setEditPartFactory(EditPartFactory factory) {
		editPartFactory = factory;
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Object adapter = editor.getAdapter(GraphicalViewer.class);
		if (adapter instanceof DesignViewGraphicalViewer) {
			((DesignViewGraphicalViewer) adapter).setEditPartFactory(factory);
		}
	}
}

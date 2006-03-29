package org.eclipse.wst.wsdl.asd.editor.util;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.wst.wsdl.asd.design.editparts.ASDEditPartFactory;

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
	}
}

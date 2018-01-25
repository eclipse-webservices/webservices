package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;

@SuppressWarnings("restriction")
public class Java2WSTypeConfigWidget extends SimpleWidgetDataContributor {

    protected boolean isMethodImplemented(IType type, IMethod seiMethod) throws JavaModelException {
    	if (type.findMethods(seiMethod) != null) {
    		return true;
    	} else {
            ITypeHierarchy typeHierarchy = type.newTypeHierarchy(new NullProgressMonitor());
            for (IType t : typeHierarchy.getAllSuperclasses(type)) {
            	if (t.findMethods(seiMethod) != null) {
            		return true;
            	}
            }
    	}
    	return false;
    }
	
}

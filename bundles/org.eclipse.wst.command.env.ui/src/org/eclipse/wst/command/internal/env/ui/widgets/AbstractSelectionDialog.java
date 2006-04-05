package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractSelectionDialog extends SimpleDialog {
	
	public AbstractSelectionDialog( Shell shell, PageInfo pageInfo)
	{
		super(shell, pageInfo);
	}

	protected void callSetters() {
		// extenders should override this method

	}
	
	public String getDisplayableSelectionString()
	{
		return "";
	}
	
	public abstract IStructuredSelection getObjectSelection();

}

package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.AbstractSelectionDialog;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;

public class ServiceImplSelectionDialog extends AbstractSelectionDialog {

	TypeRuntimeServer trs_;
	String titleText_;
	IStructuredSelection selection_;
	
	public ServiceImplSelectionDialog(Shell parent, PageInfo pageInfo) {
		super(parent, pageInfo);
		titleText_ = pageInfo.getPageName();
	}
	
	protected void callSetters()
	{		
		((ObjectSelectionWidget)getWidget()).setTypeRuntimeServer(trs_);
		((ObjectSelectionWidget)getWidget()).setInitialSelection(selection_);
	}
	
	public void setTypeRuntimeServer(TypeRuntimeServer trs)
	{
         trs_ = trs;      	   
	}
	 
	  protected void setShellStyle(int newShellStyle)
	  {
	    super.setShellStyle( newShellStyle | SWT.RESIZE );  
	  }

	  protected void configureShell(Shell newShell)
	  {
	    newShell.setText(titleText_);   
	    super.configureShell(newShell);
	  }

	  public String getDisplayableSelectionString() {
		  
		 return ((ObjectSelectionWidget)getWidget()).getObjectSelectionDisplayableString();
	}
	  
	  public IStructuredSelection getObjectSelection() {
		  return ((ObjectSelectionWidget)getWidget()).getObjectSelection();
	}
	  
	  public IProject getProject(){
		  return ((ObjectSelectionWidget)getWidget()).getProject();
	  }
	  
	  public String getComponentName(){
		  return ((ObjectSelectionWidget)getWidget()).getComponentName();  
	  }
	  
	  public void setInitialSelection(IStructuredSelection selection)
	  {
		  selection_ = selection; 
	  }
	  
	  protected Point getInitialSize()
	  {
	    return new Point( 550, 500 );  
	  }
}

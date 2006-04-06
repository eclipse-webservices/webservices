package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleDialog;

public class ProjectSelectionDialog extends SimpleDialog {

	String titleText_="";
	String projectName_="";
	String earProjectName_="";
	String componentType_="";
	TypeRuntimeServer trs_;
	boolean needEAR_;
	
	public ProjectSelectionDialog(Shell shell, PageInfo pageInfo)
	{
		super(shell, pageInfo);
		titleText_ = pageInfo.getPageName();
	}	
	
	protected void callSetters() {
		// TODO Auto-generated method stub
		ProjectSelectionWidget projWidget = (ProjectSelectionWidget)getWidget();		
		projWidget.setEarProjectName(earProjectName_);
		projWidget.setProjectName(projectName_);
		projWidget.setComponentType(componentType_);
		projWidget.setNeedEAR(needEAR_);
		projWidget.setTypeRuntimeServer(trs_);
		
		projWidget.refreshProjectItems();
	}
	
	  protected Point getInitialSize()
	  {
	    return new Point( 250, 300 );  
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
		
		public void setProjectName(String name)
		  {
				projectName_ = name;
		  }
		
		public String getProjectName()
		  {
				return projectName_;
		  }
		
		 public void setEarProjectName(String name)
		  {			
				earProjectName_ = name;
		  } 
		 
		 public String getEarProjectName()
		 {
			 return earProjectName_;
		 }
		 
		 public void setProjectComponentType( String type )
		  {
			    componentType_ = type;
		  }

		 public String getProjectComponentType()
		  {
			 return componentType_;
		  }
		 
		 public void setNeedEAR(boolean b)
		  {
			    needEAR_ = b;			    
		  }
		 
		 public boolean getNeedEAR()
		  {
			    return needEAR_;				
		  }
		 
		 public void setTypeRuntimeServer(TypeRuntimeServer trs)
		 {
			    trs_ = trs; 
			    ((ProjectSelectionWidget)getWidget()).setTypeRuntimeServer(trs);
		 }
		 
		 public boolean close() {

			projectName_ = ((ProjectSelectionWidget)getWidget()).getProjectName();
			earProjectName_ = ((ProjectSelectionWidget)getWidget()).getEarProjectName(); 
			needEAR_ = ((ProjectSelectionWidget)getWidget()).getNeedEAR();
			componentType_ = ((ProjectSelectionWidget)getWidget()).getComponentType();
			return super.close();
		}
}

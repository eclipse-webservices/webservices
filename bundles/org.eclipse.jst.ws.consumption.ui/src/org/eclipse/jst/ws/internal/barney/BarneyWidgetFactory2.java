package org.eclipse.jst.ws.internal.barney;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.provisional.ui.widgets.SimpleWidgetContributor;

public class BarneyWidgetFactory2 implements INamedWidgetContributorFactory {

    private Button checkBox_;
	private SimpleWidgetContributor widget1_;
	private SimpleWidgetContributor widget2_;
	
	public BarneyWidgetFactory2()
	{
		widget1_ = new SimpleWidgetContributor();		
		widget1_.setDescription( "Barney2 Extentions Second round desc 1" );
		widget1_.setTitle( "Barney2 Extention Second round title 1" );
		widget1_.setName( "Barney2 Extension name 1" );
		widget1_.setFactory( getContributor1() );
		
	    widget2_ = new SimpleWidgetContributor();
		widget2_.setDescription( "Barney2 Extentions Second round desc 2" );
		widget2_.setTitle( "Barney2 Extention Second round  title 2" );
		widget2_.setName( "Barney2 Extension name 2" );
		widget2_.setFactory( getContributor2() );
	}
	
	public INamedWidgetContributor getFirstNamedWidget() 
	{
	  return widget1_;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
	{
	  return widgetContributor == widget1_ && checkBox_.getSelection() ? widget2_ : null;
	}
	
	private WidgetContributorFactory getContributor1()
	{
	  return new WidgetContributorFactory()
	  {

		public WidgetContributor create()
		{
		  return new SimpleWidgetDataContributor()
		  {
		    public WidgetDataEvents addControls(Composite parent, final Listener statusListener) 
		    {
			  checkBox_ = new Button( parent, SWT.CHECK );
			  checkBox_.setText( "Barney2 Display next page again?" );
			  checkBox_.addSelectionListener( new SelectionAdapter()
					                          {
												public void widgetSelected(SelectionEvent e) 
												{
												  statusListener.handleEvent( null );
												}
					                          });
			  
			  return this;
		    }

		    public Status getStatus() 
		    {
			  return new SimpleStatus("");
		    }
		  };
		}  
	  };
	}
	
	private WidgetContributorFactory getContributor2()
	{
	  return new WidgetContributorFactory()
	  {

		public WidgetContributor create()
		{
		  return new SimpleWidgetDataContributor()
		  {
		    public WidgetDataEvents addControls(Composite parent, final Listener statusListener) 
		    {
              Label label = new Label( parent, SWT.NONE );
			  label.setText( "Barney2 Optional second round 2 page" );
			  
			  return this;
		    }

		    public Status getStatus() 
		    {
			  return new SimpleStatus("");
		    }
		  };
		}  
	  };
	}

}

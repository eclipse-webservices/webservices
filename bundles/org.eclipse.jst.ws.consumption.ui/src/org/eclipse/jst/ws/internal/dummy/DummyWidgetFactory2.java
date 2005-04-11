package org.eclipse.jst.ws.internal.dummy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.provisional.ui.widgets.SimpleWidgetContributor;

public class DummyWidgetFactory2 implements INamedWidgetContributorFactory {

    private Button checkBox_;
	private SimpleWidgetContributor widget1_;
	private SimpleWidgetContributor widget2_;
	
	public DummyWidgetFactory2()
	{
		widget1_ = new SimpleWidgetContributor();		
		widget1_.setDescription( "Extentions Second round desc 1" );
		widget1_.setTitle( "Extention Second round title 1" );
		widget1_.setName( "Extension name 1" );
		widget1_.setFactory( getContributor1() );
		
	    widget2_ = new SimpleWidgetContributor();
		widget2_.setDescription( "Extentions Second round desc 2" );
		widget2_.setTitle( "Extention Second round  title 2" );
		widget2_.setName( "Extension name 2" );
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
			  checkBox_.setText( "Display next page again?" );
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
			  label.setText( "Optional second round 2 page" );
			  
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

	public void registerDataMappings(DataMappingRegistry dataRegistry) {
		// TODO Auto-generated method stub
		
	}

}

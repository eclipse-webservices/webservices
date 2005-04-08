/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.util.Hashtable;
import java.util.Stack;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.env.ui.widgets.PageWizardDataEvents;
import org.eclipse.wst.command.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.ui.registry.WidgetRegistry;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.ui.widgets.INamedWidgetContributorFactory;



public class WizardPageManager extends SimpleCommandEngineManager
{
  private   WizardPageFactory      pageFactory_;
  private   Hashtable              pageTable_;
  private   IWizard                wizard_;
  protected SimpleWidgetRegistry   registry_;
  
  // These variables are used and set by our methods which is
  // why they are not initialized in the constructor.
  private PageWizardDataEvents currentPage_;
  private PageWizardDataEvents nextPage_;
  private PageWizardDataEvents firstPage_;
  private CommandFragment      lastUndoFragment_;
  private boolean              firstFragment_;
  
  private INamedWidgetContributorFactory widgetFactory_;
  private Stack                          widgetStack_;
  private Stack                          widgetStackStack_;
  private Hashtable                      widgetTable_;
  private WidgetRegistry                 widgetRegistry_;
    
  public WizardPageManager( SimpleWidgetRegistry  registry,
                            WizardPageFactory     pageFactory,
                            IWizard               wizard,
                            DataFlowManager       dataManager,
                            EclipseEnvironment    environment )
  { 
    super( environment, dataManager );  
    
  	pageFactory_    = pageFactory;
  	pageTable_      = new Hashtable();  
  	registry_       = registry;
  	wizard_         = wizard;
	widgetTable_    = new Hashtable();
	widgetRegistry_ = WidgetRegistry.instance();
	
	widgetStackStack_ = new Stack();
  }
  
  public Status runForwardToNextStop()
  {    
  	firstFragment_  = true;
  	nextPage_       = null;
 
  	IWizardContainer container = wizard_.getContainer();
  	
  	if( container == null || container.getCurrentPage() == null )
  	{
  	  container = null;
  	}
  	
  	return super.runForwardToNextStop( container );
  }
    
  public IWizardPage getCurrentPage()
  {
    return currentPage_;
  }
    
  public boolean hasNextPage()
  {	
  	//nextPage_ = getNextPageInGroup( widgetFactory_, false );
		
	//if( nextPage_ == null )
	//{
	  // If a page is found nextPage_ will be set to its value in the method below.
  	//  engine_.peekForwardToNextStop();
	//}
  	
    //return nextPage_ != null;
	return true;
  }
  
  public IWizardPage getNextPage()
  {  	
	System.out.println( "Start get next page" );
	
  	// Need to externalize the data for the current page so that
  	// when we move forward below the data is available.
  	if( currentPage_ != null ) currentPage_.getDataEvents().externalize();
  	
	nextPage_ = getNextPageInGroup( widgetFactory_, true );
	
	if( nextPage_ == null )
	{
	  widgetFactory_ = null;
	  widgetStack_   = null;
		
  	  Status status = runForwardToNextStop();
  	
  	  if( status.getSeverity() == Status.ERROR )
  	  {
  	    // An error has occured so we need undo to the previous stop point.
  	    undoToLastPage();
  	  }
	}
	
  	if( nextPage_ != null ) nextPage_.setWizard( wizard_ );
  	  	
  	return nextPage_;
  }
    
  public void handlePageVisible( PageWizardDataEvents page, boolean isPageVisible )
  {
    if( !isPageVisible && page == firstPage_ && page == currentPage_ )
    {
      // We are moving back one page from the first page.  This can't happen with popup wizards
      // since the previous button would be greyed out.  But when the new wizard launches this wizard
      // the previous button is not greyed out and brings the user back to the list wizards to select.
      // Therefore, we must unwind the command engine and get ready for the user to invoke this wizard 
      // again.
  	  undoToLastPage();	  
      currentPage_ = null;
      firstPage_   = null;
    }
  	if( currentPage_ != null && page == currentPage_.getPreviousPage() && isPageVisible )
  	{
  	  // We are moving back one page.
  	  // Note: we don't internalize the previous page and we do not externalize the current page.
  	  //       we are basically just leaving the current page without retrieving its state and
  	  //       moving to the previous page which already has its state set.
	
	  if( widgetFactory_ != null )
	  {
		if( widgetStack_.size() > 0 )
		{
		  widgetStack_.pop();
		}
		
		if( widgetStack_.size() == 0 )
		{
		  widgetFactory_ = null;
		  widgetStack_ = null;
		  widgetStackStack_.pop();
		}
	  }
	  
	  if( widgetFactory_ == null )
	  {
  	    undoToLastPage();
	  }
	  
  	  currentPage_ = page;
  	}	
  	else if( isPageVisible )
  	{
  	  // We are moving forward one page.
  	  WidgetDataEvents dataEvents = page.getDataEvents();
  	  
  	  dataManager_.process( dataEvents );  // Push model data into the new page.
  	  dataEvents.internalize();
  	  
  	  page.validatePageToStatus();
  	  
  	  if( currentPage_ != null ) page.setPreviousPage( currentPage_ );
  	  
  	  if( firstPage_ == null ) firstPage_ = page;
  	  
  	  currentPage_ = page;
  	}
  }
  
  public boolean performFinish()
  { 	
  	// We need to simulate the next button being pressed until there are no more pages.
  	// If an error occurs we will return false and reset the command stack back our original state.
  	boolean    doneOk    = true;
  	PageWizardDataEvents startPage = currentPage_;
  	
  	// Externalize the current page.
  	currentPage_.getDataEvents().externalize();
  	
  	// Loop through subsequent pages.
  	do
  	{  	  
  	  Status status = runForwardToNextStop();	  
  	  
  	  if( status.getSeverity() == Status.ERROR )
  	  {
  	  	// An error occured in one of the commands.
  	  	doneOk = false;
  	  }
  	  else
  	  {
  	  	currentPage_ = nextPage_;
  	  }
  	}
  	while( nextPage_ != null && doneOk);
  	
  	if( !doneOk )
  	{
  	  // An error occured, so we need to return the command stack to it's
  	  // orginal state.
  	  PageWizardDataEvents page = null;
  	  boolean              done = false;
  	  
  	  do
  	  {
  	    done = engine_.undoToLastStop();
  	    page = getPage( lastUndoFragment_ );
  	  }
  	  while( page != startPage && !done ); 	  
  	  
  	  currentPage_ = page;
  	}
  	
  	return doneOk;
  }
  
  public boolean performCancel()
  {
  	while( !engine_.undoToLastStop() ){}
  	
  	return true;
  }
    
  protected boolean peekFragment( CommandFragment fragment )
  {     	
    // Check to see if this fragment is in our page table.
    nextPage_ = getPage( fragment );
          	
	if( nextPage_ == null )
	{
	  INamedWidgetContributorFactory factory = widgetRegistry_.getFactory( fragment.getId() );
	  
	  if( factory != null )
	  {
		nextPage_ = getNextPageInGroup( factory, false );  
	  }
	}
	
    return nextPage_ == null;
  }
  
  protected boolean nextFragment( CommandFragment fragment )
  {
	//System.out.println( "Next frag=" + fragment.getClass().getName() + " stack size=" + widgetStackStack_.size() );
	
    // If the super class nextFragment want us to stop then we will stop right away.
    if( !super.nextFragment( fragment ) ) return false;
    
  	// The first fragment is either at the start of the wizard
  	// or it is on a wizard page.  In either case we do not want 
  	// to stop if a page is found.
  	if( firstFragment_ )
  	{
  	  firstFragment_ = false;
  	}
  	else
  	{
	  widgetFactory_ = widgetRegistry_.getFactory( fragment.getId() );
	  
	  if( widgetFactory_ != null )
	  {
		widgetStack_ = new Stack();
		
		StackEntry entry = new StackEntry();
		
		entry.factory_ = widgetFactory_;
		entry.stack_   = widgetStack_;
		widgetStackStack_.push( entry );
		
		nextPage_ = getNextPageInGroup( widgetFactory_, true );
	  }
	  else
	  {
  	    nextPage_ = getPage( fragment );
	  }
  	}  	  	
  	
    return nextPage_ == null;
  }
  
  protected boolean undoFragment( CommandFragment fragment )
  {
	//System.out.println( "Undo frag=" + fragment.getClass().getName() + " stack size=" + widgetStackStack_.size() );
  	lastUndoFragment_ = fragment;
  	
  	return true;
  }
  
  
  private PageWizardDataEvents getNextPageInGroup( INamedWidgetContributorFactory factory, boolean pushNewWidget )
  {
	PageWizardDataEvents page = null;
	
	if( factory != null )
	{	  
	  INamedWidgetContributor newWidget = null;
		  
	  if( widgetStack_.size() == 0 )
	  {
		newWidget = factory.getFirstNamedWidget();   
	  }
	  else
	  {
		INamedWidgetContributor currentWidget = (INamedWidgetContributor)widgetStack_.peek();
		newWidget = factory.getNextNamedWidget( currentWidget ); 
	  }
		  
	  if( newWidget != null )
	  {
		page = getPage( newWidget );
		
		if( pushNewWidget )
		{
		  widgetStack_.push( newWidget );
		}
	  }
	}
		
	return page;  
  }
  
  private void undoToLastPage()
  {
	System.out.println( "Undo to last page" );
	
    boolean stackEmpty  = false;
	boolean doneUndoing = false;
    
	while( !doneUndoing )
	{
	  stackEmpty = engine_.undoToLastStop();
	  
	  IWizardPage page = getPage( lastUndoFragment_ );
		
	  if( page == null && lastUndoFragment_ != null )
	  {
		// Check to see if a group page fragment was found.
		INamedWidgetContributorFactory factory = widgetRegistry_.getFactory( lastUndoFragment_.getId() );
		  
		if( factory != null )
		{
		  StackEntry entry = (StackEntry)widgetStackStack_.peek();
			
		  widgetFactory_ = entry.factory_;
		  widgetStack_   = entry.stack_;	
			
		  INamedWidgetContributor currentWidget = (INamedWidgetContributor)widgetStack_.peek();
			
		  page = getPage( currentWidget );
		}
	  }
	  
	  doneUndoing = page != null && !stackEmpty;
	}	
  }
  
  private PageWizardDataEvents getPage( CommandFragment fragment )
  {
    // Check to see if this fragment is in our page table.
  	PageWizardDataEvents page = (PageWizardDataEvents)pageTable_.get( fragment ); 
  	String      id   = fragment.getId();      	
          	
    if( page == null && id != null )
    {
      // Check to see if this fragment id is in the registry.
      PageInfo pageInfo = registry_.getPageInfo( id );          	  
              
      if( pageInfo != null )
      {
        page = pageFactory_.getPage( pageInfo, this );
        pageTable_.put( fragment, page );            	
      }
    }

    return page;
  }    
  
  private PageWizardDataEvents getPage( INamedWidgetContributor widget )
  {
	PageWizardDataEvents page = (PageWizardDataEvents)widgetTable_.get( widget );

	if( page == null )
	{
	  PageInfo pageInfo = new PageInfo(widget.getTitle(), widget.getDescription(), widget.getWidgetContributorFactory() );
	  
	  page = pageFactory_.getPage( pageInfo, this );
	  widgetTable_.put( widget, page );
	  widget.registerDataMappings( dataManager_.getMappingRegistry() );
	}
	
	return page;
  }
  
  private class StackEntry
  {
    INamedWidgetContributorFactory factory_;
	Stack                          stack_;
  }
}
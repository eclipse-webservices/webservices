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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.ui.widgets.PageWizardDataEvents;
import org.eclipse.wst.command.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;



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
    
  public WizardPageManager( SimpleWidgetRegistry  registry,
                            WizardPageFactory     pageFactory,
                            IWizard               wizard,
                            DataFlowManager       dataManager,
                            Environment           environment )
  { 
    super( environment, dataManager );  
    
  	pageFactory_ = pageFactory;
  	pageTable_   = new Hashtable();  
  	registry_    = registry;
  	wizard_      = wizard;
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
  	nextPage_ = null;
  	engine_.peekForwardToNextStop();
  	
    return nextPage_ != null;
  }
  
  public IWizardPage getNextPage()
  {  	  	
  	// Need to externalize the data for the current page so that
  	// when we move forward below the data is available.
  	if( currentPage_ != null ) currentPage_.getDataEvents().externalize();
  	
  	Status status = runForwardToNextStop();
  	
  	if( status.getSeverity() == Status.ERROR )
  	{
  	  // An error has occured so we need undo to the previous stop point.
  	  undoToLastPage();
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
  	  undoToLastPage();	  
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
          	
    return nextPage_ == null;
  }
  
  protected boolean nextFragment( CommandFragment fragment )
  {
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
  	  nextPage_ = getPage( fragment );
  	}  	  	
  	
    return nextPage_ == null;
  }
  
  protected boolean undoFragment( CommandFragment fragment )
  {
  	lastUndoFragment_ = fragment;
  	
  	return true;
  }
  
  private void undoToLastPage()
  {
    boolean stackEmpty = false;
    
    do
    {
      stackEmpty = engine_.undoToLastStop();
    }while( getPage( lastUndoFragment_ ) == null && !stackEmpty );
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
}
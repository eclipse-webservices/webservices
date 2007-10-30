package org.eclipse.wst.ws.tests.utils;

import java.awt.Button;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveBrackets extends Frame 
{
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3617292320299824176L;
  private TextArea inputArea_;
  private TextArea outputArea_;
  private Button   button_;
  
  public RemoveBrackets()
  {
  	super( "Convert HTML brackets" );
  }
  
  public static void main(String[] args) 
  {
  	RemoveBrackets convert = new RemoveBrackets();
  	
  	convert.buildUI();
  }
  
  private void buildUI()
  {
    GridLayout layout = new GridLayout(3,1);
    //layout.setColumns(1);
    setLayout( layout );
    
    inputArea_  = new TextArea( 10, 50 );
    outputArea_ = new TextArea( 10, 50 );
    button_     = new Button( "Press here to convert to HTML" );
    
    inputArea_.setEditable( true );
    outputArea_.setEditable( true );
    button_.addActionListener( new ActionListener()
    		                   {
    	                         public void actionPerformed( ActionEvent evt )
    	                         {
    	                           convertText();
    	                         }
    		                   });
    
    add( inputArea_ );
    add( button_ );
    add( outputArea_ );
    //setSize( 100, 100 );
    pack();
    show();
  }
  
  public boolean handleEvent( Event evt )
  {
    if( evt.id == Event.WINDOW_DESTROY )
    {
      System.exit(0);
    }
    
    return true;
  }
  
  private void convertText()
  {
  	String inputString = inputArea_.getText();
  	String outputString = inputString.replaceAll( "<", "&lt;");
  	
  	outputString = outputString.replaceAll( ">", "&gt;" );
  	
  	outputArea_.setText( outputString );
  }
}

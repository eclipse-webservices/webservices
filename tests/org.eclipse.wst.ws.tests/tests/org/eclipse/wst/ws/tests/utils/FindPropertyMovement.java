package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.Vector;

public class FindPropertyMovement 
{
  public static void main( String[] args )
  {
    File oldPath    = new File( args[0] );
    File newPath    = new File( args[1] );
    File outputFile = new File( args[2] );
   
    Vector    oldPropertyFiles = new Vector();
    Vector    newPropertyFiles = new Vector();
    
    Hashtable oldKeyMap        = new Hashtable();
    Hashtable newKeyMap        = new Hashtable();
    
    Utils.getFiles( oldPath, oldPropertyFiles, "properties", new String[0] );
    Utils.getFiles( newPath, newPropertyFiles, "properties", new String[0] );

    buildMap( oldPropertyFiles, oldKeyMap );
    buildMap( newPropertyFiles, newKeyMap );
    
    Set         newKeySet = newKeyMap.entrySet();
    Iterator    iter      = newKeySet.iterator();
    PrintWriter writer    = null;
    
    try
    {
      writer = new PrintWriter( new FileWriter( outputFile ));
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
    
    while( iter.hasNext() && writer != null )
    {
      Map.Entry entry       = (Map.Entry)iter.next();
      String    property    = (String)entry.getKey();
      String    newFileName = (String)entry.getValue();
      String    oldFileName = (String)oldKeyMap.get( property );
      
      if( oldFileName == null || newFileName.equals( oldFileName ) )
      {
        // Do nothing.  Either this key didn't exist in the old property
        // files or this key is still in the same property file.
      }
      else
      {
        writer.println( property + " " + oldFileName + " " + newFileName ); 
      }
    }
    
    try
    {
      if( writer != null ) writer.close();
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
  }
  
  private static void buildMap( Vector inputFiles, Hashtable keyMap )
  {
    for( int index = 0; index < inputFiles.size(); index++ )
    {
      File        propFile    = (File)inputFiles.elementAt(index);
      String      propName    = propFile.getAbsolutePath();
      int         pluginIndex = propName.indexOf( "plugins" );
      String      propString  = propName.substring( pluginIndex, propName.length() );      
      InputStream stream      = null;
      
      try
      {
        stream = new FileInputStream( propFile );
        PropertyResourceBundle bundle = new PropertyResourceBundle( stream );
        
        Enumeration keys = bundle.getKeys();
        
        while( keys.hasMoreElements() )
        {
          keyMap.put( keys.nextElement(), propString );
        }
      }
      catch( Exception exc )
      {
        exc.printStackTrace();
      }
      finally
      {
        try
        {
          if( stream != null ) stream.close();
        }
        catch( Exception exc )
        {
          exc.printStackTrace();
        }
      }
    }    
  }
}

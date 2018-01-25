package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class FindPercentString 
{
  public static void main( String[] args )
  {
    File        startPath     = new File( args[0] );
    String[]    pluginFiles   = Utils.readLines( args[1] );
    PrintWriter writer        = null;
    Vector      srcFiles      = new Vector();
    
    try
    {
      writer = new PrintWriter( new FileWriter( args[2] ));
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
    
    for( int index = 0; index < pluginFiles.length; index++ )
    {
      File   pluginDir     = Utils.getFile( startPath, pluginFiles[index] );
      
      if( pluginDir != null )
      {
        Utils.getFiles( pluginDir, srcFiles, "java", new String[0] );
      }
    }
          
    // Find all the java strings.
    for( int index = 0; index < srcFiles.size(); index++ )
    {
      File     srcFile     = (File)srcFiles.elementAt(index);
      Vector   fileSrcKeys = new Vector();
      String[] lines       = Utils.readLines(srcFile );
      
      for( int srcIndex = 0; srcIndex < lines.length; srcIndex++ )
      {
        Utils.findJavaStrings( lines[srcIndex], fileSrcKeys );
      }
      
      for( int keysIndex = 0; keysIndex < fileSrcKeys.size(); keysIndex++ )
      {
        String  key = (String)fileSrcKeys.elementAt( keysIndex );
        
        if( key.startsWith("%" ) )
        {
          writer.println( key + " >> " + srcFile.getName() );
        }
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
}

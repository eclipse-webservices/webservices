package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class CopyPropertyFiles 
{
  public static void main( String[] args )
  {
    File        startPath     = new File( args[0] );
    String[]    pluginFiles   = Utils.readLines( args[1] );
    String      newPath       = args[2];
    String      pathRelativeTo= args[3];
    Vector      propertyFiles = new Vector();
    
    for( int index = 0; index < pluginFiles.length; index++ )
    {
      File   pluginDir     = Utils.getFile( startPath, pluginFiles[index] );
      
      if( pluginDir != null )
      {
        Utils.getFiles( pluginDir, propertyFiles, "properties", new String[]{ "bin", "build.properties", 
                                                                              } );
      }
    }
    
    // Loop through each property file
    for( int index = 0; index < propertyFiles.size(); index++ )
    {
      File   propFile    = (File)propertyFiles.elementAt(index);
      String propName    = propFile.getAbsolutePath();
      int    pluginIndex = propName.indexOf( pathRelativeTo );
      int    firstSlash  = propName.indexOf( "\\", pluginIndex );
      int    nextSlash   = propName.indexOf( "\\", firstSlash+1 );
      String pluginName  = propName.substring( firstSlash + 1, nextSlash );
      int    underIndex  = pluginName.indexOf( '_' );
      
      if( underIndex != -1 )
      {
        pluginName = pluginName.substring( 0, underIndex );
      }
      
      String newName = newPath + File.separator + "plugins" + 
                                 File.separator + pluginName +
                                 File.separator + propName.substring( nextSlash+1, propName.length() );
      
      File        newFile = new File( newName );
      File        parent  = new File( newFile.getParent() );
      PrintWriter writer  = null;
      
      try
      {
        parent.mkdirs();
        
        writer = new PrintWriter( new FileWriter( newFile ));
        String[] lines = Utils.readLines( propFile );
        
        for( int lineIndex = 0; lineIndex < lines.length; lineIndex++ )
        {
          writer.println( lines[lineIndex] );
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
          if( writer != null ) writer.close(); 
        }
        catch( Exception exc )
        {
        }
      }
    }    
  }
}

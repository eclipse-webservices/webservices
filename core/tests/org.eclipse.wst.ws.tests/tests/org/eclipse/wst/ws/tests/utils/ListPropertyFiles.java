package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.util.Vector;

public class ListPropertyFiles 
{
  public static void main( String[] args )
  {
    File        startPath     = new File( args[0] );
    Vector      propertyFiles = new Vector();
    
    Utils.getFiles( startPath, propertyFiles, "backup", new String[]{ "bin", "build.properties", 
                                                                              } );
    
    // Loop through each property file
    for( int index = 0; index < propertyFiles.size(); index++ )
    {
      File   propFile    = (File)propertyFiles.elementAt(index);
      String propName    = propFile.getAbsolutePath();
      int    pluginIndex = propName.indexOf( "plugins" );
      int    firstSlash  = propName.indexOf( "\\", pluginIndex );
      int    nextSlash   = propName.indexOf( "\\", firstSlash+1 );
      String pluginName  = propName.substring( firstSlash + 1, nextSlash );
      int    underIndex  = pluginName.indexOf( '_' );
      
      if( underIndex != -1 )
      {
        pluginName = pluginName.substring( 0, underIndex );
      }
      
      String newName = "del  plugins" + 
                       File.separator + pluginName +
                       File.separator + propName.substring( nextSlash+1, propName.length() );

      System.out.println( newName );
    }
  }
}

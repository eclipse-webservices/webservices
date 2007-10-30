package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class UpdateFeatureXMLFiles 
{
  public static void main( String[] args )
  {
  	String rootPath     = args[0];
  	File   rootFile     = new File( rootPath );
  	Vector featureFiles = new Vector();
  	
  	getFiles( featureFiles, rootFile );
  	
  	int size = featureFiles.size();
  	
  	for( int index = 0; index < size; index++ )
  	{
  	  File     file      = (File)featureFiles.elementAt( index );
  	  String   fileName  = file.getName();
  	  int      langIndex = fileName.indexOf( "feature_" );
  	  int      dotIndex  = fileName.indexOf( '.', langIndex );
  	  String   lang      = fileName.substring( langIndex + 8, dotIndex );
  	  String[] lines     = Utils.readLines( file );
  	  
	  PrintWriter writer     = null;
  	  FileWriter  fileWriter = null;
  	  
  	  try
	  {
	    fileWriter = new FileWriter( file );
  	    writer = new PrintWriter( fileWriter );
  	    
  	    for( int lineIndex = 0; lineIndex < lines.length; lineIndex++ )
  	    {
  	  	  String line = lines[lineIndex];
  	  	
  	  	  if( line.indexOf( "licenseURL" ) != -1 )
  	  	  {
  	  		writer.println( "licenseURL=license_" + lang + ".html" );
  	  	  }
  	  	  else
  	  	  {
  	  		writer.println( line );
  	  	  }
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
  	  	  writer.close();
  	  	  fileWriter.close();
		}
  	  	catch( Exception exc )
		{
  	  	  exc.printStackTrace();
		}
	  }
  	}
  }
  
  static private void getFiles( Vector files, File path )
  {
    if( path.isDirectory() )
    {
      File[] children = path.listFiles();
      
      for( int index = 0; index < children.length; index++ )
      {
      	getFiles( files, children[index] );
      }
    }
    else
    {
      String pathName = path.getName();
            
      int    index    = pathName.indexOf( "feature_" );
      
      if( index != -1 )
      {
      	files.add( path );
      }
    }  
  }
}

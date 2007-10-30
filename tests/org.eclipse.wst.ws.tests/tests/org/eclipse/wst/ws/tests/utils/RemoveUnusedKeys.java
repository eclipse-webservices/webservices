package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

public class RemoveUnusedKeys 
{
  public static void main( String[] args )
  {
    File        startPath     = new File( args[0] );
    String[]    pluginFiles   = Utils.readLines( args[1] );
    Vector      propertyFiles = new Vector();
    Hashtable   keys          = new Hashtable();
    Hashtable   srcKeys       = new Hashtable();
    Vector      srcFiles      = new Vector();
    Vector      propNames     = new Vector();
    Vector      pluginNames   = new Vector();
    String[]    pluginArray   = null;
    Hashtable   srcPluginMap  = new Hashtable();
    
    
    // Find all the property files.
    for( int index = 0; index < pluginFiles.length; index++ )
    {
      File   pluginDir     = Utils.getFile( startPath, pluginFiles[index] );
      
      if( pluginDir == null )
      {
        System.out.println( "Plugin: " + pluginFiles[index] + " not found!" );
        continue;
      }
      
      Utils.getFiles( pluginDir, propertyFiles, "properties", new String[]{ "bin", "build.properties", 
                                                                            "wsexplorer.properties",
                                                                            "uddi.properties",
                                                                            "wsdl.properties",
                                                                            "wsil.properties",
                                                                            "favorites.properties",
                                                                            "newprops.properties",
                                                                            "delprops.properties"
                                                                            } );
      Utils.getFiles( pluginDir, srcFiles, "java", new String[0] );
    }
    
    // Loop through each property file
    for( int index = 0; index < propertyFiles.size(); index++ )
    {
      File   propFile = (File)propertyFiles.elementAt(index);
      File   parent   = new File( propFile.getParent() );
      
      // Skip properties files that are in the plugin directory.
      if( Utils.stringInArray( parent.getName(), pluginFiles ) )
      {
        continue;
      }
      
      String propName = propFile.getAbsolutePath();
      int    srcStart = propName.indexOf( "src" );
      
      if( srcStart != -1 )
      {
        int lastSlash = propName.lastIndexOf( '\\' );
        int lastDot   = propName.lastIndexOf( '.' );
        String name   = propName.substring( srcStart + 4, lastSlash );
        String name2  = propName.substring( srcStart + 4, lastDot );
        name  = name.replace( '\\', '.' ); 
        name2 = name2.replace( '\\', '.' );
        
        pluginNames.add( name );
        pluginNames.add( name2 );
        //System.out.println( propName + ">>" + name + ">>" + name2 );
      }
    }
    
    pluginArray = (String[])pluginNames.toArray( new String[0] );
    
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
        String  key     = (String)fileSrcKeys.elementAt( keysIndex );
        HashSet fileSet = (HashSet)srcKeys.get( key );
        
        if( fileSet == null )
        {
          fileSet = new HashSet();
          srcKeys.put( key, fileSet );
        }
        
        if( Utils.stringInArray( key, pluginArray ) )
        {
          HashSet names = (HashSet)srcPluginMap.get( srcFile );
          
          if( names == null )
          {
            names = new HashSet();
            srcPluginMap.put( srcFile, names );
          }
          
          names.add( key );
        }
        
        fileSet.add( srcFile );
      }
    }
                        
    int count = 1;
    
    // Loop through each property file
    for( int index = 0; index < propertyFiles.size(); index++ )
    {
      File   propFile = (File)propertyFiles.elementAt(index);
      Vector propKeys = Utils.getPropertyKeys( propFile );  
      File   parent   = new File( propFile.getParent() );
      
      // Skip properties files that are in the plugin directory.
      if( Utils.stringInArray( parent.getName(), pluginFiles ) )
      {
        continue;
      }
            
      String propName   = propFile.getAbsolutePath();
      int    srcStart   = propName.indexOf( "src" );
      String folderName = "";
      String pkgName  = "";
      
      if( srcStart != -1 )
      {
        int lastSlash = propName.lastIndexOf( '\\' );
        String name   = propName.substring( srcStart + 4, lastSlash );
        name  = name.replace( '\\', '.' ); 
        
        pkgName    = name;
        folderName = propName.substring( 0, lastSlash + 1 ); 
      }
      else
      {
        continue;
      }
      
      System.out.println( propFile.getAbsolutePath() );
      
      String newPropName = folderName + "newprops.properties";
      String delPropName = folderName + "delprops.properties";
      
      PrintWriter newPropertyFile = null;
      PrintWriter delPropertyFile = null;
        
      try
      {
        newPropertyFile = new PrintWriter( new FileWriter( newPropName ));
        delPropertyFile = new PrintWriter( new FileWriter( delPropName ));
        
        Utils.createOutputFile( srcKeys.keySet(), propFile, newPropertyFile, delPropertyFile );
        
      }
      catch( Exception exc )
      {
       exc.printStackTrace(); 
      }
      finally
      {
        if( newPropertyFile != null ) newPropertyFile.close();
        if( delPropertyFile != null ) delPropertyFile.close();
      }     
    }
  }
}

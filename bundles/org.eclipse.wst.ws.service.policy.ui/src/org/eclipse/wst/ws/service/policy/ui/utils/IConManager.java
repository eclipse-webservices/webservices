/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071025   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.osgi.framework.Bundle;

public class IConManager
{
  private Map<String, Image> iconTable;
  
  public final String favorite = "fav"; //$NON-NLS-1$
  public final String invalid  = "inv"; //$NON-NLS-1$
  public final String warning  = "war"; //$NON-NLS-1$
  public final String lock     = "loc"; //$NON-NLS-1$
  
  private String folderBaseURL; 
  private String leafBaseURL;
  private Image  favoriteImage;
  private Image  invalidImage;
  private Image  warningImage;
  private Image  lockImage;
  
  private DecoratorDescriptor iconCreator;
  
  public IConManager()
  {
    iconTable = new HashMap<String, Image>();
    addOverlayIcons();  
    iconCreator = new DecoratorDescriptor();
  }
  
  public String getFolderBaseUrl()
  {
    return folderBaseURL;  
  }
  
  public String getLeafBaseUrl()
  {
    return leafBaseURL;  
  }
  
  private void addOverlayIcons()
  {
    try
    {
      Bundle bundle      = ServicePolicyActivatorUI.getDefault().getBundle();
      URL    folderUrl   = FileLocator.find( bundle, new Path( "icons/full/obj16/fldr_obj.gif" ), null ); //$NON-NLS-1$
      URL    leafUrl     = FileLocator.find( bundle, new Path( "icons/full/obj16/file_obj.gif" ), null ); //$NON-NLS-1$
      URL    favoriteUrl = FileLocator.find( bundle, new Path( "icons/full/ovr16/favorite_ovr.gif" ), null ); //$NON-NLS-1$
      URL    invalidUrl  = FileLocator.find( bundle, new Path( "icons/full/ovr16/invalidtype_ovr.gif" ), null ); //$NON-NLS-1$
      URL    warningUrl  = FileLocator.find( bundle, new Path( "icons/full/ovr16/warning_ovr.gif" ), null ); //$NON-NLS-1$
      URL    lockUrl     = FileLocator.find( bundle, new Path( "icons/full/ovr16/unmodifiable_ovr.gif" ), null ); //$NON-NLS-1$
      
      getIconImage( folderUrl );
      getIconImage( leafUrl );
      favoriteImage   = getIconImage( favoriteUrl );
      invalidImage    = getIconImage( invalidUrl );
      warningImage    = getIconImage( warningUrl );
      lockImage       = getIconImage( lockUrl );
      
      folderBaseURL = folderUrl.toString();
      leafBaseURL   = leafUrl.toString();
    }
    catch( Throwable exc )
    {
      ServicePolicyActivatorUI.logError( "Error reading icon overlays", exc ); //$NON-NLS-1$
      exc.printStackTrace();
    }
  }
  
  public boolean hasChanged( String[] string1, String[] string2 )
  {
    if( string1 == null || string2 == null || string1.length != string2.length ) return true;
    
    for( int index = 0; index < string1.length; index++ )
    {
      String value1 = string1[index];
      String value2 = string2[index];
      
      // We can do pointer comparison since we are using constant Strings.
      if( value1 != value2 )
      {
        return true;
      }
    }
    
    return false;
  }
  
  public Image getIconOverlay( String baseUrl, String[] overlays )
  {
    String imageUrl  = getImageURL( baseUrl, overlays ); 
    Image  result    = (Image)iconTable.get( imageUrl );
    
    if( result == null )
    {
      Image baseImage = (Image)iconTable.get( baseUrl );
      
      if( baseImage == null )
      {
        // We need to load this baseImage
        try
        {
          baseImage = getIconImage( new URL( baseUrl ) );
        }
        catch( Exception exc )
        {
          ServicePolicyActivatorUI.logError( "Error loading image from:" + baseUrl, exc); //$NON-NLS-1$
        }
      }
      
      iconCreator.setBaseImage( baseImage, overlays );
      
      result = iconCreator.createImage();
      iconTable.put( imageUrl, result );
    }
    
    return result;
  }
  
  private boolean hasOverlay( String[] overlays, String value )
  {
    boolean result = false;
       
    for( String overlay : overlays )
    {
      // Note: We are using string pointer comparison here since, the strings
      // should only be the constants defined above.
      if( overlay == value )
      {
        result = true;
        break;
      }     
    }
    
    return result;
  }
  
  private String getImageURL( String baseUrl, String[] overlays )
  {
    String url = baseUrl;
    
    for( String value : overlays )
    {
      if( value != null )
      {
        url = url + ":" + value; //$NON-NLS-1$
      }
    }
    
    return url;
  }
  
  public Image getIconImage( URL url )
  {
    String urlString = url.toString();
    Image  image     = iconTable.get( urlString );
    
    if( image == null )
    {
      ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
      
      image = imageDesc.createImage();
      iconTable.put( urlString, image );
    }
    
    return image;
  }
  
  public void dispose()
  {
    for( Image image : iconTable.values() )
    {
      image.dispose();
    }
  }
  
  private class DecoratorDescriptor extends CompositeImageDescriptor
  {
    private Image    baseImage;
    private Point    size;
    private String[] overlays;
    
    protected void drawCompositeImage(int width, int height)
    {
      drawImage( baseImage.getImageData(), 0, 0);
      
      for( int index = 0; index < overlays.length; index++ )
      {
        if( hasOverlay(overlays, favorite) )
        {
          drawImage( favoriteImage.getImageData(), size.x - favoriteImage.getBounds().width, 0 );
        }
        
        if( hasOverlay( overlays, warning ) )
        {
          drawImage( warningImage.getImageData(), 0, size.y - warningImage.getBounds().height );       
        }
        
        if( hasOverlay( overlays, invalid ) )
        {
          drawImage( invalidImage.getImageData(), 0, size.y - invalidImage.getBounds().height );       
        }
        
        if( hasOverlay( overlays, lock ) )
        {
          drawImage( lockImage.getImageData(), 
                     size.x - lockImage.getBounds().width, 
                     size.y - lockImage.getBounds().height );       
        }
      }
    }

    public void setBaseImage( Image image, String[] overlays )
    { 
      Rectangle bounds = image.getBounds();
      
      this.baseImage = image;
      this.size      = new Point( bounds.width, bounds.height );
      this.overlays  = overlays;
    }
    
    protected Point getSize()
    {
      return size;
    }
  }
}

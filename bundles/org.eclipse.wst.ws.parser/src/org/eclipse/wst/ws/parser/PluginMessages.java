package org.eclipse.wst.ws.parser;

import org.eclipse.osgi.util.NLS;

public class PluginMessages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.wst.ws.parser.plugin"; //$NON-NLS-1$
  
  static
  {
    NLS.initializeMessages(BUNDLE_NAME, PluginMessages.class);
  }

  public static String PUBLICUDDIREGISTRYTYPE_NAME_SAP;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_SAP_TEST;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_XMETHODS;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_NTTCOMM;
}
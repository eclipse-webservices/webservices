package org.eclipse.wst.command.internal.provisional.ui.widgets;

public interface INamedWidgetContributorFactory 
{
  INamedWidgetContributor getFirstNamedWidget();
  
  INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor );
}

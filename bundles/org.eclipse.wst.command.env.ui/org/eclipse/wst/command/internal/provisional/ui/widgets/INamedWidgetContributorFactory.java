package org.eclipse.wst.command.internal.provisional.env.ui.widgets;

public interface INamedWidgetContributorFactory 
{
  INamedWidgetContributor getFirstNamedWidget();
  
  INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor );
}

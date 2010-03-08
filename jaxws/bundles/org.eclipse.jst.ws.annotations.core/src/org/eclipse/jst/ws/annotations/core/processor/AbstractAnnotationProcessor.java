/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.annotations.core.processor;

import org.eclipse.jdt.apt.core.util.EclipseMessager;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.util.SourcePosition;

/**
 * Abstract base class for processors contributed to the
 * <code>org.eclipse.jst.ws.annotations.core.annotationProcessor</code> extension point.
 *
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 *
 */
public abstract class AbstractAnnotationProcessor implements AnnotationProcessor {
    protected AnnotationProcessorEnvironment environment;

    /**
     * Set the {@link AnnotationProcessorEnvironment} environment to use during processing.
     * @param environment the annotation processor environment.
     */
    public void setAnnotationProcessorEnvironment(AnnotationProcessorEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Process all program elements supported by this annotation processor.
     * 
     * @see com.sun.mirror.apt.AnnotationProcessor
     */
    public abstract void process();

    /**
     * Prints an error message.
     * @param position the position where the error occured, or null if it is unknown or not applicable
     * @param message the message, or an empty string if none
     */
    protected void printError(SourcePosition position, String message) {
        environment.getMessager().printError(position, message);
    }

    /**
     * Prints an error message.
     * @param message the message, or an empty string if none
     */
    protected void printError(String message) {
        environment.getMessager().printError(message);
    }

    /**
     * Prints a warning message.
     * @param position the position where the warning occured, or null if it is unknown or not applicable
     * @param message the message, or an empty string if none
     */
    protected void printWarning(SourcePosition position, String message) {
        environment.getMessager().printWarning(position, message);
    }

    /**
     * Prints a warning message.
     * @param message the message, or an empty string if none
     */
    protected void printWarning(String message) {
        environment.getMessager().printWarning(message);
    }

    /**
     * Prints a notice.
     * @param position the position where the notice occured, or null if it is unknown or not applicable
     * @param message the message, or an empty string if none
     */
    protected void printNotice(SourcePosition position, String message) {
        environment.getMessager().printNotice(position, message);
    }

    /**
     * Prints a notice.
     * @param message the message, or an empty string if none
     */
    protected void printNotice(String message) {
        environment.getMessager().printNotice(message);
    }

    /**
     * Print an error including the given arguments for use in quick-fixes.
     * @param position position of the error
     * @param message message to display to the user
     */
    protected void printFixableError(SourcePosition position, String message) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableError(position, message, "", message);
        } else {
            printError(position, message);
        }
    }

    /**
     * Print an error including the given arguments for use in quick-fixes.
     * @param position position of the error
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableError(SourcePosition position, String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableError(position, message, pluginId, errorId);
        } else {
            printError(position, message);
        }
    }

    /**
     * Print an error including the given arguments for use in quick-fixes.
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableError(String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableError(message, pluginId, errorId);
        } else {
            printError(message);
        }
    }

    /**
     * Print a warning including the given arguments for use in quick-fixes.
     * @param position position of the warning
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableWarning(SourcePosition position, String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableWarning(position, message, pluginId, errorId);
        } else {
            printWarning(position, message);
        }
    }

    /**
     * Print a warning including the given arguments for use in quick-fixes.
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableWarning(String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableWarning(message, pluginId, errorId);
        } else {
            printWarning(message);
        }
    }

    /**
     * Print a notice including the given arguments for use in quick-fixes.
     * @param position position of the notice
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableNotice(SourcePosition position, String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableNotice(position, message, pluginId, errorId);
        } else {
            printNotice(position, message);
        }
    }

    /**
     * Print a notice including the given arguments for use in quick-fixes.
     * @param message message to display to the user
     * @param pluginId plug-in which will provide a java quick fix processor for this error. Cannot be null.
     * @param errorId a plug-in provided error code which will be meaningful to the java quick fix processor. Cannot be null.
     */
    protected void printFixableNotice(String message, String pluginId, String errorId) {
        if (environment.getMessager() instanceof EclipseMessager) {
            EclipseMessager messager = (EclipseMessager) environment.getMessager();
            messager.printFixableNotice(message, pluginId, errorId);
        } else {
            printNotice(message);
        }
    }

}

/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget and others
 *******************************************************************************/
package org.dadacoalition.yedit;

import java.util.HashMap;
import java.util.logging.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * Utility class for logging exceptions and for tracing plugin
 * execution.
 * 
 * @author oysteto
 *
 */
public class YEditLog {
	
	public static Logger logger;
	
	//the trace level that is used if tracing/debugging is enabled and trace level not specified
	public static String DEFAULT_TRACE_LEVEL = "INFO";
	
	public static String TRACE_LEVEL_OPTION = Activator.PLUGIN_ID + "/debug/trace_level";
	
	public static HashMap<String,Level> VALID_LEVELS = new HashMap<String,Level>();
	
	static {
		VALID_LEVELS.put("FINEST", Level.FINEST );
		VALID_LEVELS.put("FINER", Level.FINER );
		VALID_LEVELS.put("FINE", Level.FINE );
		VALID_LEVELS.put("CONFIG", Level.CONFIG );
		VALID_LEVELS.put("INFO", Level.INFO );
		VALID_LEVELS.put("WARNING", Level.WARNING );
		VALID_LEVELS.put("SEVERE", Level.SEVERE );
	}
		
	/**
	 * Initialize the trace logger.
	 * 
	 * The trace logger can be configured by changing the tracing options
	 * when running Eclipse.
	 */
	public static void initializeTraceLogger() {

		logger = Logger.getLogger(Activator.PLUGIN_ID);

		//when not running in debug mode do not perform any tracing. Errors will still
		//be logged with the standard Eclipse error logging mechanism.
		if( !Activator.getDefault().isDebugging() ) {
			logger.setLevel(Level.OFF);
			return;
		}
			
		String traceLevelString = Platform.getDebugOption(TRACE_LEVEL_OPTION);
		if( null == traceLevelString ){
			traceLevelString = DEFAULT_TRACE_LEVEL;
		}
		traceLevelString = traceLevelString.toUpperCase();
		
		Level traceLevel;
		if( !VALID_LEVELS.containsKey(traceLevelString) ){
			logError( "The traceLevel '" + traceLevelString + "' is not valid. Trace level set to SEVERE" );
			traceLevel = Level.SEVERE;
		} else {
			traceLevel = VALID_LEVELS.get( traceLevelString );
            logger.info("Setting trace level to " + traceLevel);			
		}	
		
		logger.setLevel(traceLevel);
	}
	
	public static void logException( Throwable exception ){
		logException( exception, "Unexpected exception: " );
	}
	
	public static void logException( Throwable exception, String message ){
		
		IStatus status = new Status( IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, message, exception );		
		Activator.getDefault().getLog().log(status);
		
	}
	
	public static void logError( String message ){
		
		IStatus status = new Status( IStatus.ERROR, Activator.PLUGIN_ID, message );		
		Activator.getDefault().getLog().log(status);
				
	}
	

}

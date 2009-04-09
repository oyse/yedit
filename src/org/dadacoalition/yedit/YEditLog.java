package org.dadacoalition.yedit;

import java.util.logging.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Utility class for logging exceptions and for tracing application
 * execution.
 * 
 * @author oysteto
 *
 */
public class YEditLog {
	
	public static Logger logger;
	
	public static void initializeTraceLogger() {
		logger = Logger.getLogger(Activator.PLUGIN_ID);
		logger.setLevel(Level.INFO);
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

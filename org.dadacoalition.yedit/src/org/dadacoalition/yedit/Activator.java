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

import java.io.IOException;

import org.dadacoalition.yedit.template.YAMLContentType;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.dadacoalition.yedit";
	
	public static final String TEMPLATE_STORE_ID = PLUGIN_ID + ".template";

	// The shared instance
	private static Activator plugin;
	
	private TemplateStore templateStore;
	
	private ContributionContextTypeRegistry contextTypeRegistry;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		YEditLog.initializeTraceLogger();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public TemplateStore getTemplateStore() {
        
	    if (templateStore == null) {
            templateStore = new ContributionTemplateStore(getContextTypeRegistry(), getDefault().getPreferenceStore(), TEMPLATE_STORE_ID);
            try {
                templateStore.load();
            } catch (IOException e) {
                YEditLog.logException(e);
            }
        }
        return templateStore;	    
	}
	
    public ContextTypeRegistry getContextTypeRegistry() {
        if (contextTypeRegistry == null) {
            contextTypeRegistry = new ContributionContextTypeRegistry();
            contextTypeRegistry.addContextType(YAMLContentType.YAML_CONTENT_TYPE);
        }
        return contextTypeRegistry;
    }	

}

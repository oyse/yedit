/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget
 *******************************************************************************/
package org.dadacoalition.yedit.template;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;

/**
 * Completion processor used for YEdit templates. 
 */
public class YEditCompletionProcessor extends TemplateCompletionProcessor {

    protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
        YEditLog.logger.info( "called getContextType" );
        return Activator.getDefault().getContextTypeRegistry().getContextType(YAMLContentType.YAML_CONTENT_TYPE);
    }

    protected Image getImage(Template template) {
        return null;
    }

    /**
     * @return All the templates for the specified context type id. All the template objects
     * are actually YEditTemplate objects and not Template objects. By returning YEditTemplate
     * objects we can override the default match() method in template and get more sensible
     * template matching.
     */
    protected Template[] getTemplates(String contextTypeId) {
        YEditLog.logger.info( "called getTemplates" );
        Template[] templates = Activator.getDefault().getTemplateStore().getTemplates();
        YEditTemplate[] yeditTemplates = new YEditTemplate[templates.length]; 
        
        for( int i = 0; i < templates.length; i++ ){
            yeditTemplates[i] = new YEditTemplate( templates[i] ); 
        }
        return yeditTemplates;
    }       

}

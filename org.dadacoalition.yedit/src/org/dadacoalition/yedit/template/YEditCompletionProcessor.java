package org.dadacoalition.yedit.template;

import org.dadacoalition.yedit.Activator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;

public class YEditCompletionProcessor extends TemplateCompletionProcessor {

    protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
        return Activator.getDefault().getContextTypeRegistry().getContextType(YAMLContentType.YAML_CONTENT_TYPE);
    }

    @Override
    protected Image getImage(Template template) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return All the templates for the specified context type id. All the template objects
     * are actually YEditTemplate objects and not Template objects.
     */
    protected Template[] getTemplates(String contextTypeId) {
        Template[] templates = Activator.getDefault().getTemplateStore().getTemplates();
        YEditTemplate[] yeditTemplates = new YEditTemplate[templates.length]; 
        
        for( int i = 0; i < templates.length; i++ ){
            yeditTemplates[i] = new YEditTemplate( templates[i] ); 
        }
        return yeditTemplates;
    }       

}

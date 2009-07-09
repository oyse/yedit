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

    protected Template[] getTemplates(String contextTypeId) {
        return Activator.getDefault().getTemplateStore().getTemplates();
    }

}

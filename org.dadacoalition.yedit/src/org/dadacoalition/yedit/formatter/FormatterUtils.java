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
package org.dadacoalition.yedit.formatter;

import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

public class FormatterUtils {
    
    public static YamlFormatter preferencesToFormatter(IPreferenceStore prefs){
        
        YamlFormatter.Builder builder = new YamlFormatter.Builder();
        
        builder.indent(prefs.getInt(PreferenceConstants.SPACES_PER_TAB));
        builder.lineLength(prefs.getInt(PreferenceConstants.FORMATTER_LINE_WIDTH));
        
        String flowStyle = prefs.getString(PreferenceConstants.FORMATTER_FLOW_STYLE);
        builder.flowStyle(FlowStyle.valueOf(flowStyle));
        builder.prettyFlow(prefs.getBoolean(PreferenceConstants.FORMATTER_PRETTY_FLOW));
        
        String scalarStyle = prefs.getString(PreferenceConstants.FORMATTER_SCALAR_STYLE);
        builder.scalarStyle(ScalarStyle.valueOf(scalarStyle));
        
        builder.explicitStart(prefs.getBoolean(PreferenceConstants.FORMATTER_EXPLICIT_START));
        builder.explicitEnd(prefs.getBoolean(PreferenceConstants.FORMATTER_EXPLICIT_END));
        
        return builder.build();
    }
    

}

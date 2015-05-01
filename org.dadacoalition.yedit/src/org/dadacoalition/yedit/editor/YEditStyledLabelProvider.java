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
package org.dadacoalition.yedit.editor;

import java.net.URL;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.osgi.framework.Bundle;

/**
 * Label provider that is capable of using more than one style for a single label.
 * @author oysteto
 *
 */
public class YEditStyledLabelProvider extends StyledCellLabelProvider {
    
    private final Styler TAG_STYLER;
    private final Styler TEXT_STYLER;
    
    private static final IPath ICONS_PATH = new Path("/icons");
    private static ImageRegistry imageRegistry;

    public static final String IMG_SEQUENCE = "outline_sequence.png";
    public static final String IMG_MAPPING = "outline_mapping.gif";
    public static final String IMG_DOCUMENT = "outline_document.gif";
    public static final String IMG_SCALAR = "outline_scalar.gif";
    public static final String IMG_MAPPINGSCALAR = "outline_mappingscalar.gif"; 
    
    private static final String[] images = { IMG_SEQUENCE, IMG_MAPPING, IMG_DOCUMENT, IMG_SCALAR, IMG_MAPPINGSCALAR };
    
    
    public YEditStyledLabelProvider( ColorManager colorManager ) {
        final Color c = colorManager.getColor( new RGB( 149, 125, 71) );
        
        TAG_STYLER = new Styler () {
            public void applyStyles( TextStyle textStyle ){
                textStyle.foreground = c;
            }
        };
        
        TEXT_STYLER = new Styler() {
            public void applyStyles( TextStyle textStyle ){
                //do not apply any extra styles
            }
        };
    }

    
    public void update(ViewerCell cell) {
        
        Object e = cell.getElement();
        if( e instanceof YAMLOutlineElement ){
            YAMLOutlineElement element = (YAMLOutlineElement) e;
            StyledString styledString = new StyledString( elementLabel(element), TEXT_STYLER );
            
            boolean showTags = Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.OUTLINE_SHOW_TAGS );
            if( showTags ){
                styledString.append( " " + element.node.getTag(), TAG_STYLER );
            }
            
            cell.setText(styledString.toString());
            cell.setStyleRanges(styledString.getStyleRanges());            
            cell.setImage(getImage( element ) );
        }              


        super.update(cell);
    }
    
    /** 
     * @return The label string for the element. For scalar elements the label string could
     * be shortened depending on the value of OULTINE_SCALAR_MAX_LENGTH
     */
    private String elementLabel( YAMLOutlineElement element ){
        
        if( 0 == element.children.size() ){
            
            int maxLength = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.OUTLINE_SCALAR_MAX_LENGTH );
            
            //max length of 0 means that is could be of any length.
            String elemString = element.toString();
            if( maxLength != 0 && elemString.length() > maxLength ){
                elemString = elemString.substring(0, maxLength );
                elemString += " ...";
            }
            return elemString;
            
        } else {
            return element.toString();
        }
        
    }

        

    public Image getImage(Object element) {

        if( element instanceof YAMLOutlineElement ){
            
            YAMLOutlineElement segment = ( YAMLOutlineElement ) element;
            if( segment.type == YAMLOutlineElement.DOCUMENT ){
                return get( IMG_DOCUMENT );
            } else if( segment.type == YAMLOutlineElement.MAPPINGITEM ){
                if( 0 == segment.children.size() ){
                    return get( IMG_MAPPINGSCALAR );
                } else {
                    return get( IMG_MAPPING );
                }
            } else if( segment.type == YAMLOutlineElement.SEQUENCEITEM ){
                if( 0 == segment.children.size() ){
                    return get( IMG_SCALAR );
                } else {
                    return get( IMG_SEQUENCE );
                }
            }           
        } 
        
        return null;
        
    }
    
    private static Image get( String imageName ){
        return getImageRegistry().get( imageName );
    }
    
    private static ImageRegistry getImageRegistry(){
        
        if( imageRegistry == null ){
            imageRegistry = new ImageRegistry();
            for( String s : images ){
                imageRegistry.put( s, create(s) );
            }
        }
        
        return imageRegistry;
        
    }

    private static ImageDescriptor create(String name) {
        IPath path = ICONS_PATH.append(name);
        return createImageDescriptor(Activator.getDefault().getBundle(), path);
    }


    private static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path) {
        URL url = FileLocator.find(bundle, path, null );
        
        YEditLog.logger.fine( "Attempting to load image " + url );
        if (url != null){
            return ImageDescriptor.createFromURL(url);
        } else {
            YEditLog.logger.warning( "Could not load image " + path );
            return null;
        }
    }    
    
}

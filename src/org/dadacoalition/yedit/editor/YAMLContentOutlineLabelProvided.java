package org.dadacoalition.yedit.editor;

import java.net.URL;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.editor.YAMLContentOutlinePage.YAMLSegment;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;


public class YAMLContentOutlineLabelProvided extends LabelProvider {

	private static final IPath ICONS_PATH = new Path("/icons");
	private static ImageRegistry imageRegistry;

	public static final String IMG_SEQUENCE = "outline_sequence.png";
	public static final String IMG_MAPPING = "outline_mapping.gif";
	public static final String IMG_DOCUMENT = "outline_document.gif";
	public static final String IMG_SCALAR = "outline_scalar.gif";
	public static final String IMG_MAPPINGSCALAR = "outline_mappingscalar.gif"; 
	
	private static final String[] images = { IMG_SEQUENCE, IMG_MAPPING, IMG_DOCUMENT, IMG_SCALAR, IMG_MAPPINGSCALAR };
		

	public Image getImage(Object element) {

		if( element instanceof YAMLSegment ){
			
			YAMLSegment segment = ( YAMLSegment ) element;
			if( segment.type == YAMLSegment.DOCUMENT ){
				return get( IMG_DOCUMENT );
			} else if( segment.type == YAMLSegment.MAPPINGITEM ){
				return get( IMG_MAPPING );
			} else if( segment.type == YAMLSegment.SEQUENCEITEM ){
				return get( IMG_SEQUENCE );
			} else if( segment.type == YAMLSegment.SCALAR ){
				return get( IMG_SCALAR );
			} else if( segment.type == YAMLSegment.MAPPINGSCALAR ){
				return get( IMG_MAPPINGSCALAR );
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
		System.out.println( url );
		if (url != null)
			return ImageDescriptor.createFromURL(url);
		else
			return null;
	}

}

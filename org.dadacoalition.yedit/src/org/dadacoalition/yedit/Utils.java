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
package org.dadacoalition.yedit;

import java.util.Collection;

/**
 * Various utility functions.
 */
public class Utils {

    private Utils(){}
    
    /**
     * Join each element in a collection using a string separator
     * @param collection The collection to join
     * @param separator The separator to use
     * @return A string where the toString method on each object in the collection has been called and each element is
     * separated with the separator string.
     */
    public static String joinAsString(Collection<? extends Object> collection, String separator){               
        
        if(collection.isEmpty()){
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for( Object o : collection ){

            if(first){
                first = false;
            } else {
                result.append(separator);
            }
            result.append(o.toString());
            
        }
        return result.toString();
    }
    
}

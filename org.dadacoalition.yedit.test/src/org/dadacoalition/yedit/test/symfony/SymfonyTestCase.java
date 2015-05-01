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
package org.dadacoalition.yedit.test.symfony;

import org.dadacoalition.yedit.test.YEditTestCase;

public class SymfonyTestCase extends YEditTestCase {

    private String content;
    private String expectedContent;
        
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getExpectedContent() {
        return expectedContent;
    }
    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }
    
}

/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canoo.groovy.ulc.factory

import com.ulcjava.base.application.ULCMenu
import com.ulcjava.base.application.ULCPopupMenu
import com.ulcjava.base.application.ULCToolBar
import com.ulcjava.base.application.ULCSeparator

public class SeparatorFactory extends AbstractFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        Object parent = builder.getCurrent();
        if (parent instanceof ULCMenu) {
            return new ULCPopupMenu.ULCSeparator();
        } else if (parent instanceof ULCToolBar) {
            return new ULCToolBar.ULCSeparator();
        } else {
            return new ULCSeparator();
        }
    }
}

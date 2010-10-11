/*
 * Copyright 2007 the original author or authors.
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

import com.ulcjava.base.application.border.ULCAbstractBorder
import com.ulcjava.base.application.border.ULCCompoundBorder

class CompoundBorderFactory extends ULCBorderFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context.applyBorderToParent = attributes.remove('parent')

        ULCAbstractBorder border  = null
        if (value instanceof List) {
            switch (value.size()) {
                case 0:
                    throw new RuntimeException("$name does not accept an empty array as an value argument")
                case 1:
                    border = value[0]
                    break
                case 2:
                    border = new ULCCompoundBorder(value[0], value[1])
                    break
                case 3:
                default:
                    border = value[1..-1].inject(value[0]) {compound, single -> new ULCCompoundBorder(compound, single) }
                    break;
            }
        }

        if (!border && attributes) {
            if (value) {
                throw new RuntimeException("$name only accepts an array of borders as a value argument")
            }
            def inner = attributes.remove("inner")
            def outer = attributes.remove("outer")
            if (inner instanceof ULCAbstractBorder && outer instanceof ULCAbstractBorder) {
                border = new ULCCompoundBorder((ULCAbstractBorder)outer, (ULCAbstractBorder)inner)
            }
        }

        if (!border) {
            throw new RuntimeException("$name only accepts an array of com.ulcjava.base.application.border.ULCAbstractBorder or an inner: and outer: attribute")
        }

        return border
    }
}
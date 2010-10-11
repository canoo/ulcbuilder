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

import com.ulcjava.base.application.util.Color
import com.ulcjava.base.application.BorderFactory


/**
 * accepts no value
 * accepts attributes:<br />
 * none <br />
 * highlight: com.ulcjava.application.util.Color, shadow: com.ulcjava.application.util.Color<br />
 * highlightOuter: com.ulcjava.application.util.Color, highlightInner: com.ulcjava.application.util.Color, shadowOuter: com.ulcjava.application.util.Color, shadowInner: com.ulcjava.application.util.Color<br />
 *
 */
class BevelBorderFactory extends ULCBorderFactory {

    final int type;

    public BevelBorderFactory(int newType) {
        type = newType;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context.applyBorderToParent = attributes.remove('parent')

        // no if-else-if chain so that we can have one attribute failure block
        if (attributes.containsKey("highlight")) {
            Color highlight = (Color)attributes.remove("highlight")
            Color shadow = (Color)attributes.remove("shadow")
            if (highlight && shadow && !attributes) {
                return BorderFactory.createBevelBorder(type, highlight, shadow);
            }
        }
        if (attributes.containsKey("highlightOuter")) {
            Color highlightOuter = (Color)attributes.remove("highlightOuter")
            Color highlightInner = (Color)attributes.remove("highlightInner")
            Color shadowOuter = (Color)attributes.remove("shadowOuter")
            Color shadowInner = (Color)attributes.remove("shadowInner")
            if (highlightOuter && highlightInner && shadowOuter && shadowInner && !attributes) {
                return BorderFactory.createBevelBorder(type, highlightOuter, highlightInner, shadowOuter, shadowInner);
            }
        }
        if (attributes) {
            throw new RuntimeException("$name only accepts no attributes, or highlight: and shadow: attributes, or highlightOuter: and highlightInner: and shadowOuter: and shadowInner: attributes")
        }
        return BorderFactory.createBevelBorder(type);
    }
}
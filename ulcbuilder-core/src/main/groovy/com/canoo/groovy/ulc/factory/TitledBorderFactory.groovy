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
import com.ulcjava.base.application.border.ULCTitledBorder
import com.ulcjava.base.application.border.ULCAbstractBorder
import com.ulcjava.base.application.util.Font;

/**
 * The node must be called with either a value arugment or a title: attribute. <br />
 * The following attributes are optional. <br />
 *    position: one of "default", "aboveTop", "top", "belowTop", "aboveBottom", "bottom", "belowBottom", or a constant from com.ulcjava.base.application.border.TitledBorder
 *    justification: one of "default", "left", "center", "right", "leading", "trailing", or a constant from com.ulcjava.base.application.border.TitledBorder
 *    border: com.ulcjava.base.application.border.ULCAbstractBorder, some other border, if unset the look and feel default will be used (re
 *    color: com.ulcjava.base.application.util.Color the color of the text for the title
 *    font: com.ulcjava.base.application.util.Font the font of the text for the title
 */
class TitledBorderFactory extends ULCBorderFactory {
    static final Map positions = [
        'default':    ULCTitledBorder.DEFAULT_POSITION,
        aboveTop:    ULCTitledBorder.ABOVE_TOP,
        top:          ULCTitledBorder.TOP,
        belowTop:    ULCTitledBorder.BELOW_TOP,
        aboveBottom: ULCTitledBorder.ABOVE_BOTTOM,
        bottom:       ULCTitledBorder.BOTTOM,
        belowBottom: ULCTitledBorder.BELOW_BOTTOM,
    ]

    static final Map justifications = [
        'default': ULCTitledBorder.DEFAULT_JUSTIFICATION,
        left:      ULCTitledBorder.LEFT,
        center:    ULCTitledBorder.CENTER,
        right:     ULCTitledBorder.RIGHT,
        leading:   ULCTitledBorder.LEADING,
        trailing:  ULCTitledBorder.TRAILING,
    ]


    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context.applyBorderToParent = attributes.remove('parent')
        
        String title
        if (value) {
            title = value as String
            if (attributes.containsKey("title")) {
                throw new RuntimeException("$name cannot have both a value attribute and an attribute title:")
            }
        } else {
            title = attributes.remove("title") as String
        }
        ULCTitledBorder border = new ULCTitledBorder(title)

        def position = attributes.remove("position")
        position = positions[position] ?: position
        if (position instanceof Integer) { border.setTitlePosition((Integer)position) }

        def justification = attributes.remove("justification")
        justification = positions[justification] ?: justification
        if (justification instanceof Integer) { border.setTitleJustification((Integer)justification) }

        ULCAbstractBorder otherBorder = (ULCAbstractBorder)attributes.remove("border")
        if (otherBorder != null) { border.setBorder(otherBorder) }

        Color color = (Color)attributes.remove("color")
        if (color) { border.setTitleColor(color) }

        Font font = (Font)attributes.remove("font")
        if (font) { border.setTitleFont(font) }

        return border
    }

}

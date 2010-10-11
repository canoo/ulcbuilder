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

import com.ulcjava.base.application.ULCFiller
import com.ulcjava.base.application.util.Dimension


public class FillerFactory extends ComponentFactory {

    public FillerFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (FactoryBuilderSupport.checkValueIsType(value, name, ULCFiller.class)) {
            return value;
        }
        if (attributes.containsKey("dimension")) {
            Dimension dimension = (Dimension) attributes.remove("dimension");
            return new ULCFiller(dimension);
        }
        if (attributes.containsKey("minimumSize") || attributes.containsKey("preferredSize") || attributes.containsKey("maximumSize")) {
            Dimension minimumSize = (Dimension) attributes.remove("minimumSize");
            Dimension preferredSize = (Dimension) attributes.remove("preferredSize");
            Dimension maximumSize = (Dimension) attributes.remove("maximumSize");
            return new ULCFiller(minimumSize, preferredSize, maximumSize);
        }
        if (attributes.containsKey("width") || attributes.containsKey("height")) {
            int width = (int) attributes.remove("width");
            int height = (int) attributes.remove("height");
            return new ULCFiller(width, height);
        }
        return new ULCFiller();
    }
}

public class GlueFactory extends ComponentFactory {

    public GlueFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        return ULCFiller.createGlue();
    }
}

public class HorizontalGlueFactory extends ComponentFactory {

    public HorizontalGlueFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        return ULCFiller.createHorizontalGlue();
    }
}

public class HorizontalStrutFactory extends ComponentFactory {

    public HorizontalStrutFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsType(value, name, Integer.class);
        Integer width;
        if (value != null) {
            width = (Integer)value;
        } else {
            width = (Integer)attributes.remove("width");
        }
        return ULCFiller.createHorizontalStrut(width);
    }
}

public class RigidAreaFactory extends ComponentFactory {

    public RigidAreaFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsType(value, name, Dimension.class);
        Dimension dimension;
        if (value != null) {
            dimension = (Dimension)value;
        } else {
            dimension = (Dimension)attributes.remove("dimension");
        }
         return ULCFiller.createRigidArea(dimension);
    }
}

public class VerticalGlueFactory extends ComponentFactory {

    public VerticalGlueFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        return ULCFiller.createVerticalGlue();
    }
}

public class VerticalStrutFactory extends ComponentFactory {

    public VerticalStrutFactory() {
        super(null);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsType(value, name, Integer.class);
        Integer height;
        if (value != null) {
            height = (Integer)value;
        } else {
            height = (Integer)attributes.remove("height");
        }
        return ULCFiller.createVerticalStrut(height);
    }
}

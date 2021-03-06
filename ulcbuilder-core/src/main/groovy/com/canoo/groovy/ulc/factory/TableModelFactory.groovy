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

import com.canoo.groovy.ulc.binding.DefaultTableModel
import com.ulcjava.base.application.ULCTable
import com.ulcjava.base.application.table.ITableModel
import groovy.model.ValueHolder
import groovy.model.ValueModel

public class TableModelFactory extends AbstractFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (FactoryBuilderSupport.checkValueIsType(value, name, ITableModel.class)) {
            return value;
        } else if (attributes.get(name) instanceof ITableModel) {
            return attributes.remove(name);
        } else {
            ValueModel model = (ValueModel) attributes.remove("model");
            if (model == null) {
                Object list = attributes.remove("list");
                if (list == null) {
                    list = new ArrayList();
                }
                model = new ValueHolder(list);
            }
            return new DefaultTableModel(model);
        }
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if ((node.columnCount > 0) && (parent instanceof ULCTable)) {
            parent.model = node
        }
    }
}

public class PropertyColumnFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        Object current = builder.getCurrent();
        if (current instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) current;
            String property = (String) attributes.remove("propertyName");
            if (property == null) {
                throw new IllegalArgumentException("Must specify a property for a propertyColumn");
            }
            Object header = attributes.remove("header");
            if (header == null) {
                header = "";
            }
            Class type = (Class) attributes.remove("type");
            if (type == null) {
                type = Object.class;
            }
            Boolean editable = (Boolean) attributes.remove("editable");
            if (editable == null) {
                editable = Boolean.TRUE;
            }
            return model.addPropertyColumn(header, property, type, editable.booleanValue());
        } else {
            throw new RuntimeException("propertyColumn must be a child of a tableModel");
        }
    }
}

public class ClosureColumnFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        Object current = builder.getCurrent();
        if (current instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) current;
            Object header = attributes.remove("header");
            if (header == null) {
                header = "";
            }
            Closure readClosure = (Closure) attributes.remove("read");
            if (readClosure == null) {
                throw new IllegalArgumentException("Must specify 'read' Closure property for a closureColumn");
            }
            Closure writeClosure = (Closure) attributes.remove("write");
            Class type = (Class) attributes.remove("type");
            if (type == null) {
                type = Object.class;
            }
            return model.addClosureColumn(header, readClosure, writeClosure, type);
        } else {
            throw new RuntimeException("closureColumn must be a child of a tableModel");
        }
    }
}

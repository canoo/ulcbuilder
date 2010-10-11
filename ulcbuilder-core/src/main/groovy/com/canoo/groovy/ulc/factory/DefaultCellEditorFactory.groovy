package com.canoo.groovy.ulc.factory

import com.ulcjava.base.application.table.ULCTableColumn
import com.ulcjava.base.application.IEditorComponent
import com.ulcjava.base.application.DefaultCellEditor

public class DefaultCellEditorFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsType(value, name, IEditorComponent);
        if (value == null) {
            throw new IllegalArgumentException("value must not be null for DefaultCellEditorFactory")
        }
        return new DefaultCellEditor(value as IEditorComponent)
    }
}

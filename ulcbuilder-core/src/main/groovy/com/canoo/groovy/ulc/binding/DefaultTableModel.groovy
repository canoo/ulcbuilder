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
package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.table.AbstractTableModel
import groovy.model.ClosureModel
import groovy.model.PropertyModel
import groovy.model.ValueModel
import groovy.model.ValueHolder;

/**
 * A default table model made up of PropertyModels on a Value model.
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision: 8044 $
 */
public class DefaultTableModel extends AbstractTableModel {
    private ValueModel data
    private ValueModel rowModel
    private List modelColumns

    public DefaultTableModel(ValueModel data) {
        this.data = data
        this.rowModel = new ValueHolder()
        this.modelColumns = []
    }

    public ModelColumn addPropertyColumn(Object headerValue, String property, Class type) {
        return addColumn(headerValue, new PropertyModel(rowModel, property, type))
    }

    public ModelColumn addPropertyColumn(Object headerValue, String property, Class type, boolean editable) {
        return addColumn(headerValue, new PropertyModel(rowModel, property, type, editable))
    }

    public ModelColumn addClosureColumn(Object headerValue, Closure readClosure, Closure writeClosure, Class type) {
        return addColumn(headerValue, new ClosureModel(rowModel, readClosure, writeClosure, type))
    }

    public ModelColumn addColumn(Object headerValue, ValueModel columnValueModel) {
        ModelColumn answer = new ModelColumn(headerValue, columnValueModel)
        modelColumns.add(answer)

       fireTableStructureChanged()
       return answer
    }

    void removeColumn(ModelColumn column) {
        modelColumns.remove(column)
        fireTableStructureChanged()
    }

    int getRowCount() {
        return data.getValue().size()
    }

    List getRows() {
        return (List)data.getValue()
    }

    Object getRow(int rowIndex) {
        return getRows()[rowIndex]
    }

    void addRow(Object row) {
        getRows().add row
        fireTableRowsInserted(getRows().size() - 1, getRows().size() - 1)
    }

    void updateRow(Object row) {
        def index = getRows().indexOf(row)
        fireTableRowsUpdated(index, index)
    }

    void removeRow(int rowIndex) {
        getRows().remove rowIndex
        fireTableRowsDeleted(rowIndex, rowIndex)
    }

    int getColumnCount() {
        return modelColumns.size()
    }

    ModelColumn getColumn(int columnIndex) {
        return modelColumns[columnIndex]
    }

    String getColumnName(int columnIndex) {
        return String.valueOf(getColumn(columnIndex).header)
    }

    Class getColumnClass(int columnIndex) {
        return getColumn(columnIndex).valueModel.getType()
    }

    boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumn(columnIndex).valueModel.isEditable()
    }

    Object getValueAt(int rowIndex, int columnIndex) {
        rowModel.setValue(data.getValue()[rowIndex])
        ModelColumn modelColumn = getColumn(columnIndex)
        return modelColumn.getValueModel().getValue()
    }

    void setValueAt(Object value, int rowIndex, int columnIndex) {
        rowModel.setValue(data.getValue()[rowIndex])
        ModelColumn modelColumn = getColumn(columnIndex)
        modelColumn.getValueModel().setValue(value)
        fireTableCellUpdated(rowIndex, columnIndex)
    }
}

class ModelColumn {
    def Object header
    def ValueModel valueModel

    ModelColumn(Object header, ValueModel valueModel) {
        this.header = header
        this.valueModel = valueModel
    }
}
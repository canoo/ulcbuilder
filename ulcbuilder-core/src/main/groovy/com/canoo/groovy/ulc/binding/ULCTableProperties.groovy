package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCTable
import com.ulcjava.base.application.event.IListSelectionListener
import com.ulcjava.base.application.event.ListSelectionEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCTableProperties {
    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();

        def bindableProperties = ["selectedRow", "selectedColumn"]

        bindableProperties.each {String bindableProperty ->
            result.put(ULCTable.class.name + "#$bindableProperty",
                    [createBinding: {SourceBinding source, TargetBinding target ->
                        return new ULCTableSelectedElementBinding((PropertyBinding) source, target, bindableProperty)
                    }] as TriggerBinding)
        }


        return result
    }
}

class ULCTableSelectedElementBinding extends AbstractSyntheticBinding implements IListSelectionListener {
    ULCTable boundTable;

    protected ULCTableSelectedElementBinding(PropertyBinding source, TargetBinding target, String propertyName) {
        super(source, target, ULCTable.class, propertyName)
    }

    public synchronized void syntheticBind() {
        boundTable = (ULCTable) ((PropertyBinding) sourceBinding).getBean()
        boundTable.getSelectionModel().addListSelectionListener(this);
    }

    public synchronized void syntheticUnbind() {
        boundTable.getSelectionModel().removeListSelectionListener(this);
        boundTable = null;
    }

    public void valueChanged(ListSelectionEvent e) {
        update();
    }
}

package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCList
import com.ulcjava.base.application.event.IListDataListener
import com.ulcjava.base.application.event.IListSelectionListener
import com.ulcjava.base.application.event.ListDataEvent
import com.ulcjava.base.application.event.ListSelectionEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCListProperties {


    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();

        def bindableProperties = ["selectedIndex", "selectedIndices", "selectedValue", "selectedValues"]

        bindableProperties.each {String bindableProperty ->
            result.put(ULCList.class.name + "#${bindableProperty}",
                    [createBinding: {SourceBinding source, TargetBinding target ->
                        return new ULCListSelectedElementBinding((PropertyBinding) source, target, bindableProperty)
                    }] as TriggerBinding)
        }

        return result;
    }

}

class ULCListSelectedElementBinding extends AbstractSyntheticBinding implements IListDataListener, IListSelectionListener {
    ULCList boundList;
    def model

    public ULCListSelectedElementBinding(PropertyBinding source, TargetBinding target, String propertyName) {
        super(source, target, ULCList.class, propertyName);
    }

    public synchronized void syntheticBind() {
        boundList = sourceBinding.getBean();
        model = boundList.model
        boundList.addListSelectionListener this
        model.addListDataListener(this)
    }

    public synchronized void syntheticUnbind() {
        boundList.removeListSelectionListener this
        model.removeListDataListener this
        boundList = null
        model = null
    }

    public void setTargetBinding(TargetBinding target) {
        super.setTargetBinding(target);
    }

    public void valueChanged(ListSelectionEvent event) {
        update()
    }


    public void intervalAdded(ListDataEvent e) {

    }

    public void intervalRemoved(ListDataEvent e) {

    }

    public void contentsChanged(ListDataEvent e) {
        syntheticUnbind()
        syntheticBind()
    }


}
package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCComboBox
import com.ulcjava.base.application.event.ActionEvent
import com.ulcjava.base.application.event.IActionListener
import com.ulcjava.base.application.event.IListDataListener
import com.ulcjava.base.application.event.ListDataEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCComboBoxProperties {


    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();
        result.put(ULCComboBox.class.name + "#selectedIndex",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCComboBoxSelectedElementBinding((PropertyBinding) source, target, "selectedIndex")
                }] as TriggerBinding)
        result.put(ULCComboBox.class.name + "#selectedItem",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCComboBoxSelectedElementBinding((PropertyBinding) source, target, "selectedItem")
                }] as TriggerBinding)
        return result;
    }

}

class ULCComboBoxSelectedElementBinding extends AbstractSyntheticBinding implements IListDataListener, IActionListener {
    ULCComboBox boundComboBox;
    def model

    public ULCComboBoxSelectedElementBinding(PropertyBinding source, TargetBinding target, String propertyName) {
        super(source, target, ULCComboBox.class, propertyName);
    }

    public synchronized void syntheticBind() {
        boundComboBox = sourceBinding.getBean();
        model = boundComboBox.model
        boundComboBox.addActionListener this
        model.addListDataListener(this)
    }

    public synchronized void syntheticUnbind() {
        boundComboBox.removeActionListener this
        model.removeListDataListener this
        boundComboBox = null
        model = null
    }

    public void setTargetBinding(TargetBinding target) {
        super.setTargetBinding(target);
    }

    public void actionPerformed(ActionEvent event) {
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
package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCSpinner
import com.ulcjava.base.application.event.IValueChangedListener
import com.ulcjava.base.application.event.ValueChangedEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCSpinnerProperties {
    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>()
        result.put(ULCSpinner.class.getName() + "#value",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCSpinnerValueBinding((PropertyBinding) source, target)
                }] as TriggerBinding)
        return result
    }


}

class ULCSpinnerValueBinding extends AbstractSyntheticBinding implements IValueChangedListener {
    ULCSpinner boundSpinner

    public ULCSpinnerValueBinding(PropertyBinding source, TargetBinding target) {
        super(source, target, ULCSpinner.class, "value")
    }


    public synchronized void syntheticBind() {
        boundSpinner = sourceBinding.getBean()
        boundSpinner.addValueChangedListener this
    }

    public synchronized void syntheticUnbind() {
        boundSpinner.removeValueChangedListener this
        boundSpinner = null
    }

    public void valueChanged(ValueChangedEvent event) {
        update()
    }


}

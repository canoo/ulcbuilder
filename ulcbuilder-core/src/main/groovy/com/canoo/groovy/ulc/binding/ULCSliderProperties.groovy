package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCSlider
import com.ulcjava.base.application.event.IValueChangedListener
import com.ulcjava.base.application.event.ValueChangedEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCSliderProperties {

    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();
        result.put(ULCSlider.class.getName() + "#value",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCSliderValueBinding((PropertyBinding) source, target)
                }] as TriggerBinding);
        return result;
    }


}

class ULCSliderValueBinding extends AbstractSyntheticBinding implements IValueChangedListener {
    ULCSlider boundSlider;

    public ULCSliderValueBinding(PropertyBinding source, TargetBinding target) {
        super(source, target, ULCSlider.class, "value");
    }


    public synchronized void syntheticBind() {
        boundSlider = sourceBinding.getBean();
        boundSlider.addValueChangedListener(this);
    }

    public synchronized void syntheticUnbind() {
        boundSlider.removeValueChangedListener(this);
        boundSlider = null;
    }

    public void valueChanged(ValueChangedEvent event) {
        update()
    }


}

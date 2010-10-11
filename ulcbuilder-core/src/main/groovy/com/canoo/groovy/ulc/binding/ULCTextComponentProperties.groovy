package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCTextComponent
import com.ulcjava.base.application.event.IValueChangedListener
import com.ulcjava.base.application.event.ValueChangedEvent
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCTextComponentProperties {

    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();
        result.put(ULCTextComponent.class.getName() + "#text",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCTextComponentTextBinding((PropertyBinding) source, target)
                }] as TriggerBinding);
        return result;
    }


}

class ULCTextComponentTextBinding extends AbstractSyntheticBinding implements IValueChangedListener {
    ULCTextComponent boundTextComponent;

    public ULCTextComponentTextBinding(PropertyBinding source, TargetBinding target) {
        super(source, target, ULCTextComponent.class, "text");
    }


    public synchronized void syntheticBind() {
        boundTextComponent = sourceBinding.getBean();
        boundTextComponent.addValueChangedListener(this);
    }

    public synchronized void syntheticUnbind() {
        boundTextComponent.removeValueChangedListener(this);
        boundTextComponent = null;
    }

    public void valueChanged(ValueChangedEvent event) {
        update()
    }


}

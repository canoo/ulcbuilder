package com.canoo.groovy.ulc.binding

import com.ulcjava.base.application.ULCScrollBar
import com.ulcjava.base.application.event.AdjustmentEvent
import com.ulcjava.base.application.event.IAdjustmentListener
import groovy.swing.binding.AbstractSyntheticBinding
import org.codehaus.groovy.binding.PropertyBinding
import org.codehaus.groovy.binding.SourceBinding
import org.codehaus.groovy.binding.TargetBinding
import org.codehaus.groovy.binding.TriggerBinding

public class ULCScrollBarProperties {
    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();
        result.put(ULCScrollBarProperties.class.getName() + "#position",
                [createBinding: {SourceBinding source, TargetBinding target ->
                    return new ULCScrollBarPositionBinding((PropertyBinding) source, target)
                }] as TriggerBinding);
        return result;
    }
}

class ULCScrollBarPositionBinding extends AbstractSyntheticBinding implements IAdjustmentListener {

    ULCScrollBar boundScrollBar;

    public ULCScrollBarPositionBinding(PropertyBinding source, TargetBinding target) {
        super(source, target, ULCScrollBar.class, "position");
    }


    public synchronized void syntheticBind() {
        boundScrollBar = sourceBinding.getBean();
        boundScrollBar.addAdjustmentListener(this);
    }

    public synchronized void syntheticUnbind() {
        boundScrollBar.removeAdjustmentListener(this);
        boundScrollBar = null;
    }

    public void adjustmentValueChanged(AdjustmentEvent event) {
        upadte()
    }

}
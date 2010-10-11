/*
 * Copyright 2009-2010 the original author or authors.
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
package com.canoo.groovy.ulc.binding;

import com.ulcjava.base.application.ULCAbstractButton;
import com.ulcjava.base.application.event.IValueChangedListener;
import com.ulcjava.base.application.event.ValueChangedEvent;
import groovy.swing.binding.AbstractSyntheticBinding;
import org.codehaus.groovy.binding.*;
import java.util.Map;
import java.util.HashMap;

/**
 * @author ulcteam
 */
public class ULCAbstractButtonProperties {
    public static Map<String, TriggerBinding> getSyntheticProperties() {
        Map<String, TriggerBinding> result = new HashMap<String, TriggerBinding>();
        result.put(ULCAbstractButton.class.getName() + "#selected",
            new TriggerBinding() {
                public FullBinding createBinding(SourceBinding source, TargetBinding target) {
                    return new ULCAbstractButtonSelectedBinding((PropertyBinding) source, target);
                }
            });
        return result;
    }
}

/**
 * @author ulcteam
 */
class ULCAbstractButtonSelectedBinding extends AbstractSyntheticBinding implements IValueChangedListener {
    ULCAbstractButton boundButton;

    public ULCAbstractButtonSelectedBinding(PropertyBinding source, TargetBinding target) {
        super(source, target, ULCAbstractButton.class, "selected");
    }


    public synchronized void syntheticBind() {
        boundButton = (ULCAbstractButton) ((PropertyBinding) sourceBinding).getBean();
        boundButton.addValueChangedListener(this);
    }

    public synchronized void syntheticUnbind() {
        boundButton.removeValueChangedListener(this);
        boundButton = null;
    }

    public void valueChanged(ValueChangedEvent event) {
        update();
    }
}

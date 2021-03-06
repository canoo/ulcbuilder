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

package com.canoo.groovy.ulc.factory

import com.canoo.groovy.ulc.impl.DefaultAction
import org.codehaus.groovy.runtime.InvokerHelper
import com.ulcjava.base.application.IAction
import com.ulcjava.base.application.util.KeyStroke
import com.ulcjava.base.application.ULCComponent

/**
 *
 * @author Danno Ferrin
 */
public class ActionFactory extends AbstractFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        IAction action = null;
        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, IAction.class)) {
            action = (IAction) value;
        } else if (attributes.get(name) instanceof IAction) {
            action = (IAction) attributes.remove(name);
        } else {
            action = new DefaultAction();
        }
        return action;
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object action,
            Map attributes)
    {
        if ((attributes.get("closure") instanceof Closure) && (action instanceof DefaultAction)){
            Closure closure = (Closure) attributes.remove("closure");
            ((DefaultAction)action).setClosure(closure);
        }

        Object accel = attributes.remove("accelerator");
        if (accel != null) {
            KeyStroke stroke = null;
            if (accel instanceof KeyStroke) {
                stroke = (KeyStroke) accel;
            } else {
                stroke = KeyStroke.getKeyStroke(accel.toString());
            }
            action.putValue(IAction.ACCELERATOR_KEY, stroke);
        }

        Object mnemonic = attributes.remove("mnemonic");
        if (mnemonic != null) {
            if (!(mnemonic instanceof Number)) {
                mnemonic = mnemonic.toString().charAt(0);
            }
            action.putValue(IAction.MNEMONIC_KEY, mnemonic as Integer);
        }

        for (entry in attributes.entrySet()) {
            String propertyName = (String) entry.getKey();
            // first attempt to set as a straight proeprty
            try {
                InvokerHelper.setProperty(action, propertyName, entry.getValue());
            } catch (MissingPropertyException mpe) {
                // failing that store them in the action values list
                // typically standard Action names start with upper case, so lets upper case it
                propertyName = capitalize(propertyName);
                action.putValue(propertyName, entry.getValue());
            }

        }

        return false
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object action) {
        try {
            InvokerHelper.setProperty(parent, "action", action);
        } catch (RuntimeException re) {
            // must not have an action property...
            // so we ignore it and go on
        }
        Object keyStroke = action.getValue("KeyStroke");
        if (parent instanceof ULCComponent) {
            ULCComponent component = (ULCComponent) parent;
            KeyStroke stroke = null;
            if (keyStroke instanceof String) {
                stroke = KeyStroke.getKeyStroke((String) keyStroke);
            } else if (keyStroke instanceof KeyStroke) {
                stroke = (KeyStroke) keyStroke;
            }
            if (stroke != null) {
                String key = action.toString();
                component.getInputMap().put(stroke, key);
                component.getActionMap().put(key, action);
            }
        }
    }


    String capitalize(String text) {
        char ch = text.charAt(0);
        if (Character.isUpperCase(ch)) {
            return text;
        }
        StringBuffer buffer = new StringBuffer(text.length());
        buffer.append(Character.toUpperCase(ch));
        buffer.append(text.substring(1));
        return buffer.toString();
    }

}

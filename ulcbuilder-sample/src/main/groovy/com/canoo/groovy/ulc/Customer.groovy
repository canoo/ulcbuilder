package com.canoo.groovy.ulc;

import java.beans.PropertyChangeSupport
import java.beans.PropertyChangeListener

class Customer {
    def private PropertyChangeSupport propertyChangeSupport

    def String firstName
    def String lastName

    Customer() {
        propertyChangeSupport = new PropertyChangeSupport(this)

        // TODO, JetGroovy problem prevents usage of "this()"
        // this(null, null)
    }

    Customer(String firstName, String lastName) {
        this.firstName = firstName
        this.lastName = lastName

        propertyChangeSupport = new PropertyChangeSupport(this)
    }

    void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener listener
    }

    void setProperty(String name, Object value) {
        def oldValue = metaClass.getProperty(this, name)
        metaClass.setProperty(this, name, value)
        propertyChangeSupport.firePropertyChange(name, oldValue, value);
    }
}
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

package com.canoo.groovy.ulc

import com.ulcjava.base.application.border.ULCBevelBorder
import com.ulcjava.base.application.border.ULCEtchedBorder
import com.ulcjava.base.application.table.DefaultTableCellRenderer
import com.ulcjava.base.application.tabletree.DefaultTableTreeCellRenderer
import com.ulcjava.base.application.tree.DefaultTreeCellRenderer
import java.util.logging.Logger
import com.canoo.groovy.ulc.factory.*
import com.ulcjava.base.application.*
import java.lang.reflect.*

/**
 * @author ulcteam
 */
class ULCBuilder extends FactoryBuilderSupport {
    static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";
    // Properties

    // local fields
    private static final Logger LOG = Logger.getLogger(ULCBuilder.name)

    ULCBuilder(boolean init = true) {
        super(false)

        // tracks all containing windows, for auto-owned dialogs
        containingWindows = new LinkedList()
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID

        if(init) {
           ulcAutoRegister()
        }
    }

    public ulcAutoRegister() {
        // if java did atomic blocks, this would be one
        synchronized (this) {
            if (autoRegistrationRunning || autoRegistrationComplete) {
                // registration already done or in process, abort
                return
            }
        }
        autoRegistrationRunning = true
        try {
            ulcCallAutoRegisterMethods(getClass())
        } finally {
            autoRegistrationComplete = true
            autoRegistrationRunning = false
        }
    }

    private void ulcCallAutoRegisterMethods(Class declaredClass) {
        if (declaredClass == null) {
            return
        }
        ulcCallAutoRegisterMethods(declaredClass.getSuperclass())
        callRegisterMethodsOn(this)

        ClassLoader cl = Thread.currentThread().contextClassLoader
        Enumeration urls = cl.getResources('META-INF/ulcbuilder/plugin.properties')
        urls.each { url ->
            url.eachLine { text ->
                if(text.startsWith('#')) return
                String pluginClassName = text.trim()

                try {
                    Class pluginClass = cl.loadClass(pluginClassName)
                    callRegisterMethodsOn(pluginClass.newInstance(), this)
                } catch(NoClassDefFoundError ncdfe) {
                    // skip
                } catch(ClassNotFoundException cnfe) {
                    // skip
                }
            }
        }
    }

    private void callRegisterMethodsOn(instance) {
        Class declaredClass = instance.getClass()
        for (Method method in declaredClass.getDeclaredMethods()) {
            if (method.name.startsWith("register") && method.getParameterTypes().length == 0) {
                registringGroupName = method.getName().substring("register".length())
                registrationGroup.put(registringGroupName, new TreeSet<String>())
                try {
                    if (Modifier.isPublic(method.getModifiers())) {
                        method.invoke(instance)
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not init ${instance.class.name} because of an access error in ${declaredClass.name}.${method.name}", e)
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Could not init ${instance.class.name} because of an exception in ${declaredClass.name}.${method.name}", e)
                } finally {
                    registringGroupName = ""
                }
            }
        }
    }

    private void callRegisterMethodsOn(source, target) {
        Class declaredClass = source.getClass()
        for (Method method in declaredClass.getDeclaredMethods()) {
            if (method.name.startsWith("register") && method.parameterTypes == [ULCBuilder]) {
                registringGroupName = method.getName().substring("register".length())
                registrationGroup.put(registringGroupName, new TreeSet<String>())
                try {
                    if (Modifier.isPublic(method.getModifiers())) {
                        method.invoke(source, [target] as Object[])
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not init ${target.class.name} because of an access error in ${declaredClass.name}.${method.name}", e)
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Could not init ${target.class.name} because of an exception in ${declaredClass.name}.${method.name}", e)
                } finally {
                    registringGroupName = ""
                }
            }
        }
    }

    def registerSupportNodes() {
        registerFactory("action", new ActionFactory())
        registerFactory("actions", new CollectionFactory())
        registerFactory("map", new MapFactory())
        registerFactory("icon", new IconFactory())
        registerBeanFactory("buttonGroup", ULCButtonGroup)
        addAttributeDelegate(ULCBuilder.&buttonGroupAttributeDelegate)

        //object id delegage, for propertyNotFound
        addAttributeDelegate(ULCBuilder.&objectIDAttributeDelegate)
    }

    def registerBinding() {
//        binding related classes
        BindingFactory bindFactory = new BindingFactory()
        registerFactory("bind", bindFactory)
        addAttributeDelegate(bindFactory.&bindingAttributeDelegate)
//        registerFactory("model", new ModelFactory())
    }

    def registerPassThruNodes() {
        registerFactory("widget", new WidgetFactory(ULCComponent, true))
        registerFactory("container", new WidgetFactory(ULCComponent, false))
        registerFactory("bean", new WidgetFactory(Object, true))
    }

    def registerWindows() {
        registerFactory("dialog", new DialogFactory())
        registerFactory("frame", new FrameFactory())
        registerFactory("window", new WindowFactory())
    }

    def registerActionButtonWidgets() {
        registerFactory("button", new RichActionWidgetFactory(ULCButton))
        registerFactory("checkBox", new RichActionWidgetFactory(ULCCheckBox))
        registerFactory("checkBoxMenuItem", new RichActionWidgetFactory(ULCCheckBoxMenuItem))
        registerFactory("menuItem", new RichActionWidgetFactory(ULCMenuItem))
        registerFactory("radioButton", new RichActionWidgetFactory(ULCRadioButton))
        registerFactory("radioButtonMenuItem", new RichActionWidgetFactory(ULCRadioButtonMenuItem))
        registerFactory("toggleButton", new RichActionWidgetFactory(ULCToggleButton))
    }

    def registerTextWidgets() {
        registerFactory("label", new TextArgWidgetFactory(ULCLabel))
        registerFactory("passwordField", new TextArgWidgetFactory(ULCPasswordField))
        registerFactory("textArea", new TextArgWidgetFactory(ULCTextArea))
        registerFactory("textField", new TextArgWidgetFactory(ULCTextField))
        // TODO, add ULCHTMLPane
    }

    def registerMDIWidgets() {
        registerBeanFactory("desktopPane", ULCDesktopPane)
        registerFactory("internalFrame", new InternalFrameFactory())
    }

    def registerBasicWidgets() {
        registerFactory("comboBox", new ComboBoxFactory())
        registerBeanFactory("list", ULCList)
        registerBeanFactory("progressBar", ULCProgressBar)
        registerFactory("separator", new SeparatorFactory())
        registerBeanFactory("scrollBar", ULCScrollBar)
        registerBeanFactory("slider", ULCSlider)
        registerBeanFactory("spinner", ULCSpinner)
        registerBeanFactory("tree", ULCTree)
    }

    def registerMenuWidgets() {
        registerBeanFactory("menu", ULCMenu)
        registerBeanFactory("menuBar", ULCMenuBar)
        registerBeanFactory("popupMenu", ULCPopupMenu)
    }

    def registerContainers() {
        registerBeanFactory("boxPane", ULCBoxPane)
        registerFactory("scrollPane", new ScrollPaneFactory())
        registerFactory("splitPane", new SplitPaneFactory())
        registerBeanFactory("cardPane", ULCCardPane) // ??
        registerFactory("tabbedPane", new TabbedPaneFactory(ULCTabbedPane))
        registerBeanFactory("toolBar", ULCToolBar)
        registerBeanFactory("layeredPane", ULCLayeredPane)
    }

    def registerDataModels() {
//        registerBeanFactory("boundedRangeModel", DefaultBoundedRangeModel)
        registerBeanFactory("spinnerDateModel", ULCSpinnerDateModel)
        registerBeanFactory("spinnerListModel", ULCSpinnerListModel)
        registerBeanFactory("spinnerNumberModel", ULCSpinnerNumberModel)
    }

    def registerTableComponents() {
        registerFactory("table", new TableFactory())
        registerFactory("tableColumn", new TableColumnFactory())
        registerFactory("tableModel", new TableModelFactory())
        registerFactory("propertyColumn", new PropertyColumnFactory())
        registerFactory("closureColumn", new ClosureColumnFactory())

        registerBeanFactory("tableTree", ULCTableTree)
    }

    def registerBasicLayouts() {
        registerBeanFactory("borderLayoutPane", ULCBorderLayoutPane)
        registerBeanFactory("flowLayoutPane", ULCFlowLayoutPane)
        registerBeanFactory("gridLayoutPane", ULCGridLayoutPane)
        registerBeanFactory("gridBagLayoutPane", ULCGridBagLayoutPane)
    }

    def registerBoxLayout() {
        registerBeanFactory("boxLayoutPane", ULCBoxLayoutPane)
        registerFactory("filler", new FillerFactory());
        registerFactory("hglue", new HorizontalGlueFactory());
        registerFactory("hstrut", new HorizontalStrutFactory());
        registerFactory("rigidarea", new RigidAreaFactory());
        registerFactory("vglue", new VerticalGlueFactory());
        registerFactory("vstrut", new VerticalStrutFactory())
        // constraints delegate
        addAttributeDelegate(ULCBuilder.&constraintsAttributeDelegate)

    }

    def registerBorders() {
        registerFactory("lineBorder", new LineBorderFactory())
        registerFactory("loweredBevelBorder", new BevelBorderFactory(ULCBevelBorder.LOWERED))
        registerFactory("raisedBevelBorder", new BevelBorderFactory(ULCBevelBorder.RAISED))
        registerFactory("etchedBorder", new EtchedBorderFactory(ULCEtchedBorder.LOWERED))
        registerFactory("loweredEtchedBorder", new EtchedBorderFactory(ULCEtchedBorder.LOWERED))
        registerFactory("raisedEtchedBorder", new EtchedBorderFactory(ULCEtchedBorder.RAISED))
        registerFactory("titledBorder", new TitledBorderFactory())
        registerFactory("emptyBorder", new EmptyBorderFactory())
        registerFactory("compoundBorder", new CompoundBorderFactory())
        registerFactory("matteBorder", new MatteBorderFactory())
    }

    def registerRenderersAndEditors() {
        registerBeanFactory("tableCellRenderer", DefaultTableCellRenderer)
        registerBeanFactory("treeCellRenderer", DefaultTreeCellRenderer)
        registerBeanFactory("tableTreeCellRenderer", DefaultTableTreeCellRenderer)
        registerFactory("cellEditor", new DefaultCellEditorFactory())
    }

    /**
     * Do some overrides for standard component handlers, else use super
     */
    void registerBeanFactory(String nodeName, Class klass) {
        if (ULCComponent.isAssignableFrom(klass)) {
            registerFactory(nodeName, new ComponentFactory(klass))
        } else {
            super.registerBeanFactory(nodeName, klass)
        }
    }

    static ULCBuilder build(Closure c) {
        ULCBuilder builder = new ULCBuilder()
        c.setDelegate(builder)
        c.call(builder)
        return builder
    }

//    KeyStroke shortcut(key, modifier = 0) {
//        return KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | modifier)
//    }
//
//    KeyStroke shortcut(String key, modifier = 0) {
//        KeyStroke ks = KeyStroke.getKeyStroke(key)
//        if (ks == null) {
//            return null
//        } else {
//            return KeyStroke.getKeyStroke(ks.getKeyCode(), ks.getModifiers() | modifier | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())        }
//    }

    static buttonGroupAttributeDelegate(def builder, def node, def attributes) {
        if (attributes.containsKey("group")) {
            def o = attributes.get("group")
            if ((o instanceof ULCButtonGroup) && (node instanceof ULCToggleButton)) {
                node.group = o
                attributes.remove("group")
            }
        }
    }

    static objectIDAttributeDelegate(def builder, def node, def attributes) {
        def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
        def theID = attributes.remove(idAttr)
        if (theID) {
            builder.setVariable(theID, node)
        }
    }

    static constraintsAttributeDelegate(def builder, def node, def attributes) {
        if (attributes.containsKey('constraints')) {
            builder.context.constraints = attributes.remove('constraints')
        }
    }
}

package com.canoo.groovy.ulc

import com.canoo.groovy.ulc.Customer
import com.canoo.groovy.ulc.FileTableTreeModel
import com.canoo.groovy.ulc.ULCBuilder
import com.canoo.groovy.ulc.tree.TreeNodeBuilder
import com.ulcjava.base.application.tree.DefaultTreeModel
import com.ulcjava.base.application.util.Color
import com.ulcjava.base.development.DevelopmentRunner
import com.ulcjava.base.application.*
import com.ulcjava.base.application.tabletree.DefaultMutableTableTreeNode
import com.ulcjava.base.application.tabletree.DefaultTableTreeModel

public class DemoApplication extends AbstractApplication {
    def builder = new ULCBuilder()

    def dialog

    def showDialog = builder.action(name: "Show Dialog", closure: {dialog.setVisible(true)})
    def sayHello = builder.action(name: "Say Hello", closure: {println "Hello"})
    def quit = builder.action(name: "Quit", closure: {ApplicationContext.terminate()})

    def data = [
            new Customer("Andreas", "Henle"),
            new Customer("Rolf", "Pfenninger"),
            new Customer("Dierk", "König"),
            new Customer("Patrick", "Lisser"),
    ]

    public void start() {

        ULCFrame frame = builder.frame(title: "ULC Builder Demo", defaultCloseOperation: ULCFrame.TERMINATE_ON_CLOSE, show: true, pack: true) {
            menuBar {
                menu("File") {
                    menuItem(action: sayHello)
                    menuItem(action: quit)
                }
            }
        }
        def content = builder.boxLayoutPane() {
            boxPane {
                tabbedPane(constraints: ULCBoxPane.BOX_EXPAND_EXPAND) {
                    labelsTab()
                    textComponentsTab()
                    buttonsTab()
                    toggleButtonsTab()
                    listTab()
                    tableTab()
                    treeTab()
                    tableTreeTab()
                    bindingTab()
                }
            }
        }

        dialog = builder.dialog(title: "Dies ist ein Dialog") {
            label "Dialog Inhalt"
        }

        frame.getContentPane().add(content)

//        frame.pack()
        frame.visible = true
    }


    def labelsTab() {
        builder.boxPane(columns: 1, title: "Labels") {
            label(icon: icon(resource: 'flag_switzerland.png'))
            label 'Hello World from Switzerland'
        }
    }

    def textComponentsTab() {
        builder.boxPane(columns: 1, title: "Text Components") {
            label(text: 'Password Field')
            passwordField(columns: 20)
            label(text: 'Text Field')
            textField(columns: 20)
            label(text: 'Text Area')
        }
    }

    def buttonsTab() {
        builder.boxPane(columns: 1, title: "Buttons") {
            button(action: showDialog)
            button(text: 'Hide Dialog', actionPerformed: {dialog.setVisible(false)})
        }
    }

    def toggleButtonsTab() {
        builder.boxPane(columns: 1, title: "Toggle Buttons") {
            sampleButtonGroup = buttonGroup()
            radioButton(text: "First", group: sampleButtonGroup)
            radioButton(text: "Second", group: sampleButtonGroup)
            radioButton(text: "Third", group: sampleButtonGroup)
        }
    }

    def tableTab() {
        builder.scrollPane(title: "Table") {
            table {
                tableModel(list: data) {
                    propertyColumn(header: 'First Name', propertyName: 'firstName')
                    propertyColumn(header: 'Last Name', propertyName: 'lastName')
                }
            }
        }
    }

    def listTab() {
        builder.scrollPane(title: "List") {
            list(listData: data.collect {String.valueOf("$it.firstName $it.lastName")})
        }
    }

    def treeTab() {
        TreeNodeBuilder treeNodeBuilder = new TreeNodeBuilder()
        def root = treeNodeBuilder.node("Root") {
            node("Child 1") {
                node("Child 11")
                node("Child 12")
                node("Child 13")
            }
            node("Child 2") {
                node("Child 21", leaf: true)
                node("Child 22", leaf: true)
                node("Child 23", leaf: true)
            }
        }

        def treeModel = new DefaultTreeModel(root)
        builder.scrollPane(title: "Tree") {
            tree(model: treeModel, editable: true, cellRenderer: treeCellRenderer(foreground: Color.blue), cellEditor: cellEditor(textField(background: Color.yellow.brighter())))
        }
    }

    def tableTreeTab() {

        def nodeBuilder = new TreeNodeBuilder(DefaultMutableTableTreeNode, "data")
        def root = nodeBuilder.node(["","", ""]) {
            data.each {
                final def customer = it
                node(["${it.lastName} ${customer.firstName}".toString(), customer.firstName, customer.lastName]) {
                    node(["lastname", "", customer.lastName], leaf:true)
                    node(["firstname", customer.firstName, ""], leaf:true)
                }
            }
        }
        def model = new DefaultTableTreeModel(root, ["Customer", "Firstname", "Lastname"] as String[])
        builder.scrollPane(title: "TableTree") {
            tableTree(model: model)
        }
    }

    def bindingTab() {
        builder.scrollPane(title: "Bindings") {
            boxPane(columns: 2, horizontalGap: 5, verticalGap: 10) {

                label("Binding to a label")
                filler()
                def textFieldForLabel = textField(constraints: ULCBoxPane.BOX_LEFT_TOP, columns: 15)
                label(text: bind {textFieldForLabel.text})

                label("Binding text components")
                filler() // TODO (Jun 17, 2009, msh): how to define a span here ?
                def textField1 = textField(constraints: ULCBoxPane.BOX_LEFT_TOP, columns: 15)
                def textField2 = textField(constraints: ULCBoxPane.BOX_LEFT_TOP, columns: 15, text: bind { textField1.text})

                label("Binding buttons")
                filler()
                def button1 = radioButton(text: "Radiobutton 1", constraints: ULCBoxPane.BOX_LEFT_TOP, selected: false)
                def button2 = checkBox(text: "Radiobutton selected", constraints: ULCBoxPane.BOX_LEFT_TOP, selected: bind { button1.selected})

                label("Binding a slider value")
                filler()
                def sliderPosition = textField(columns: 3, text: "2")
                def slider = slider(orientation: ULCSlider.HORIZONTAL, minimum: 0, maximum: 100, value: bind {sliderPosition.text as int})
                bind(source: slider, sourceProperty: 'value', target: sliderPosition, targetProperty: 'text')

                label("Binding a comboBox")
                filler()
                def combo = comboBox(items: ["Foo", "Bar", "Item1", "Item2"])
                textField(columns: 10, text: bind {combo.selectedItem})

                label("Binding a list")
                filler()
                def list = list(listData: data.collect {String.valueOf("$it.firstName $it.lastName")})
                textField(columns: 15, text: bind {list.selectedValue})

                label("Binding a spinner")
                filler()
                def spinner = spinner(model: new ULCSpinnerDateModel())
                textField(columns: 15, text: bind {spinner.value})

                label("Binding a table")
                filler()
                def table = table {
                    tableModel(list: data) {
                        propertyColumn(header: 'First Name', propertyName: 'firstName')
                        propertyColumn(header: 'Last Name', propertyName: 'lastName')
                    }
                }
                table.setRowSelectionInterval(0, 0)
                textField(columns: 15, text: bind {table.model.getValueAt(table.selectedRow, 1)})

            }
        }
    }

    public static void run(Closure view) {
        DevelopmentRunner.setApplicationClass(DemoApplication.class);
        DevelopmentRunner.run();
    }

    public static void main(String[] args) {
        DemoApplication.run {}
    }

}

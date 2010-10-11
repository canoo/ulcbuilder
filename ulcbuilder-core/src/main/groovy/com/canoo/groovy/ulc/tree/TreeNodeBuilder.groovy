package com.canoo.groovy.ulc.tree

import com.ulcjava.base.application.tree.DefaultMutableTreeNode
import com.ulcjava.base.application.tree.IMutableTreeNode
import com.ulcjava.base.application.tree.ITreeNode
import java.lang.reflect.Method
import java.lang.reflect.Constructor

/**
 * Use this builder to create the node hierachy for a ITreeModel.
 * Example:
 * <code>
 * def builder =  new TreeNodeBuilder()
 * def root = builder.node("Root") {
 *      node("Child1") {
 *          node("Child2")
 *      }
 *    }
 * }
 * def treeModel = new DefaultTreeModel(root)
 * </code>
 *
 * Note:
 * Per default DefaultMutableTreeNodes are generated using the node() method. The given parameter is used as userObject.
 * <br>
 * You can specify  a nodeClass and a userObjectParamName to configure which type of nodes should be created.
 * The userObjectParamName represents the propertyName for the given value.
 *
 * E.g. : new TreeNodeBuilder(MyNode.class, "nodeValue")
 *
 * Also a single node can be created from a different type:
 *
 * <code>
 * def builder =  new TreeNodeBuilder()
 * def root = builder.node("Root") {
 *       node("Child1") {
 *           node(value:"Child2", type:MyNode.class, userObjectName:"nodeValue")
 * }
 * </code>
 *
 * Bot parameter "type" and "userObjectName" have to be specified in this case
 */

public class TreeNodeBuilder extends FactoryBuilderSupport {
    def nodeClass
    def userObjectParamName

    public TreeNodeBuilder(nodeClass = DefaultMutableTreeNode, userObjectParamName = "userObject") {
        super(false);
        this.nodeClass = nodeClass
        this.userObjectParamName = userObjectParamName
        autoRegisterNodes()
    }

    def registerFactories() {
        registerFactory("node", new DefaultMutableTreeNodeFactory(nodeClass, userObjectParamName))
    }
}

class DefaultMutableTreeNodeFactory extends AbstractFactory {

    Class nodeClass
    String userObjectParamName



    protected DefaultMutableTreeNodeFactory(nodeClass, userObjectParamName) {
        this.nodeClass = nodeClass
        this.userObjectParamName = userObjectParamName
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {

        if(value == null) {
            value = attributes.remove("value")
        }
        Class nodeClassToBuilt = attributes.remove('type')
        def userObjectName

        if(nodeClassToBuilt) {
            userObjectName = attributes.remove('userObjectName')
            if (!userObjectName) {
                throw new IllegalArgumentException("If you specify a type for the node, you also have to specify the propertyName for the userObjectName")
            }
        } else {
            nodeClassToBuilt = nodeClass
            userObjectName = userObjectParamName
        }
        def node = getNodeForValue(value, nodeClassToBuilt)


        if (!node) {
            node = nodeClassToBuilt.newInstance()
            node."$userObjectName" = value
        }



        if (attributes.containsKey("leaf")) {
            node.leaf = attributes.leaf
        }
        return node
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        def parentNode = parent
        def childNode = child

        try {
            parentNode.insert(childNode, parentNode.childCount)
        } catch (RuntimeException e) {
            throw new RuntimeException("Error adding child", e)
        }
    }


    private def getNodeForValue(value, Class clazz) {
        def node
        try {
            clazz.getConstructor(value.class)
            node = clazz.newInstance([value] as Object[])
        } catch (NoSuchMethodException e1) {
            if(value instanceof Collection) {
                try {
                    clazz.getConstructor(Object[])
                    node = clazz.newInstance([value.toArray()] as Object[])
                } catch (NoSuchMethodException e2) {
                }
            }
        }
        return node
    }

}



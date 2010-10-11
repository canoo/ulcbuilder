package com.canoo.groovy.ulc.tree

import com.canoo.groovy.ulc.tree.TreeNodeBuilder
import com.ulcjava.base.application.tree.DefaultMutableTreeNode
import com.ulcjava.base.application.tree.ITreeNode
import com.ulcjava.base.application.tabletree.DefaultMutableTableTreeNode

class TreeNodeBuilderTest extends GroovyTestCase {

    void testBuildNodes() {
        def child1, child2, child21

        TreeNodeBuilder builder = new TreeNodeBuilder()

        DefaultMutableTreeNode root = builder.node("root") {
            child1 = node("child1")
            child2 = node("child2") {
                child21 = node("child21")
            }
        }

        assertNotNull "no root created", root
        assertNull "root root has parent", root.parent
        assertEquals "childcount", 2, root.childCount
        assertSame "wrong parent (root)", root, child1.parent
        assertSame "wrong parent (child2)", child2, child21.parent
    }

    void testLeafNodes() {
        TreeNodeBuilder builder = new TreeNodeBuilder()

        def root = builder.node("root") {
            node("child1", leaf: true)
            node("child2") {
                node("child21")
            }
        }
        assertTrue "non leaf node created", root.getChildAt(0).leaf

        builder = new TreeNodeBuilder()

        shouldFail(RuntimeException) {
            builder.node("root") {
                node("child1")
                node("child2", leaf: true) {
                    node("child21")
                }
            }
        }

    }


    void testUserObject() {
        def userObject1 = new Date()
        def userObject2 = new Integer(2)

        TreeNodeBuilder builder = new TreeNodeBuilder()

        def root = builder.node("root") {
            node(userObject1)
            node(userObject2)
        }

        assertSame userObject1, root.getChildAt(0).userObject
        assertSame userObject2, root.getChildAt(1).userObject
    }

    void testCustomNodes() {
        def userObject1 = new Date()
        def userObject2 = new Integer(2)

        TreeNodeBuilder builder = new TreeNodeBuilder()

        def root = builder.node("root") {
            node(userObject1)
            node(value:userObject2, type:TestTreeNode, userObjectName:"value")
        }
        
        assertTrue root.getChildAt(0) instanceof DefaultMutableTreeNode
        assertTrue root.getChildAt(1) instanceof TestTreeNode

        assertSame userObject1, root.getChildAt(0).userObject
        assertSame userObject2, root.getChildAt(1).value
        assertSame root.getChildAt(1).value, root.getChildAt(1).userObject

        shouldFail(RuntimeException) {
            builder.node(value:"Foo", type:TestTreeNode) // missing userObjectName
        }

    }

    void testTableTreeNodes() {
        def valueCol1 = "col1"
        def valueCol2 = "col2"
        def builder = new TreeNodeBuilder(DefaultMutableTableTreeNode, "data")

        def root = builder.node(["Root", "root"]) {
            node([valueCol1, valueCol2])
        }

        assertTrue root instanceof DefaultMutableTableTreeNode
        assertEquals "Root", root.getValueAt(0)
        assertEquals "root", root.getValueAt(1)
        assertEquals valueCol1, root.getChildAt(0).getValueAt(0)
        assertEquals valueCol2, root.getChildAt(0).getValueAt(1) 
    }
}

class TestTreeNode extends DefaultMutableTreeNode {

    void setValue(value) {
        userObject = value
    }

    def getValue() {
        userObject
    }

}
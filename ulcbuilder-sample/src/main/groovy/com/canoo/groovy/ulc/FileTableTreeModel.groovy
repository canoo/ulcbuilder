package com.canoo.groovy.ulc

import com.ulcjava.base.application.tabletree.AbstractTableTreeModel

class FileTableTreeModel extends AbstractTableTreeModel {
    int getColumnCount() {
        return 2
    }

    Object getValueAt(Object file, int i) {
        switch (i) {
            case 0: return file.getName()
            case 1: return (file.size() / 1024) as int
            default: throw new IllegalArgumentException("unknown column")
        }
    }

    Object getRoot() {
        return new File(System.getProperty("user.home"))
    }

    Object getChild(Object file, int i) {
        return file.listFiles()[i]
    }

    int getChildCount(Object file) {
        def files = file.listFiles()
        if (files == null) {
            return 0;
        }
        return files.size()
    }

    boolean isLeaf(Object file) {
        return getChildCount(file) == 0
    }

    int getIndexOfChild(Object directory, Object child) {
        return (directory.listFiles() as List).indexOf(child)
    }
}
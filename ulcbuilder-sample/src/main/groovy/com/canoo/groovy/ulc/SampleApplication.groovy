package com.canoo.groovy.ulc

import com.ulcjava.applicationframework.application.SingleFrameApplication
import com.ulcjava.base.application.ULCComponent
import com.ulcjava.base.application.ULCFrame
import com.ulcjava.base.application.util.Color
import com.ulcjava.base.server.GradientPaint
import com.ulcjava.base.server.GradientPaint.Orientation

class SampleApplication /*extends SingleFrameApplication*/ {
    private final ULCBuilder builder = new ULCBuilder()

    @Override
    protected ULCComponent createStartupMainContent() {
        def paints = new GradientPaint(Color.darkGray  , Color.lightGray, Orientation.HORIZONTAL)
        builder.gridLayoutPane(columns: 2) {
            migLayoutPane(layoutConstraints: '', paints: paints) {
                label 'Label1'
                textField constraints: 'span 2, growx, wrap'
                label 'Label2'
                textField constraints: 'wrap' 
                label 'Label3'
                textField()
                button 'Click!', constraints: 'right' 
            }
            formLayoutPane(columns: 'right:pref, 6dlu, 50dlu, 4dlu, default',
                           rows: 'pref, 3dlu, pref, 3dlu, pref') {
                label 'Label1', constraints: '1, 1' 
                textField constraints: '3, 1, 3, 1' 
                label 'Label2', constraints: '1, 3' 
                textField constraints: '3, 3' 
                label 'Label3', constraints: '1, 5' 
                textField constraints: '3, 5' 
                button 'Click!', constraints: '5, 5' 
            }
        }
    }

    @Override
    protected void initFrame(ULCFrame frame) {
        super.initFrame(frame)
        frame.setLocationRelativeTo(null)
    }
}

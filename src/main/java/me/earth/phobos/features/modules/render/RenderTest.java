package me.earth.phobos.features.modules.render;

import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.util.RenderUtil;

import java.awt.*;

public
class RenderTest extends Module {
    public
    RenderTest ( ) {
        super ( "RenderTest" , "RenderTest" , Category.RENDER , true , false , false );
    }

    @Override
    public
    void onRender3D ( final Render3DEvent event ) {
        RenderUtil.drawBetterGradientBox ( RenderTest.mc.player.getPosition ( ) , new Color ( 255 , 0 , 0 , 255 ) , new Color ( 0 , 255 , 0 , 255 ) , new Color ( 0 , 0 , 255 , 255 ) );
    }
}
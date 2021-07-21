package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;

public
class AirJump
        extends Module {
    public
    AirJump ( ) {
        super ( "AirJump" , "Jump but in air swag." , Module.Category.MOVEMENT , false , false , false );
    }

    @Override
    public
    void onUpdate ( ) {
        mc.player.onGround = true;
    }
}
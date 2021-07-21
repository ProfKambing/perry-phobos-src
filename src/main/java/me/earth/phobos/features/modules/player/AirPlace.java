package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.Util;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public
class AirPlace
        extends Module {
    private final Setting < Mode > mode = this.register ( new Setting <> ( "Mode" , Mode.UP ) );

    public
    AirPlace ( ) {
        super ( "AirPlace" , "Place blocks in the air for gay 1.13+" , Category.PLAYER , false , false , false );
    }

    public
    void onEnable ( ) {
        switch (this.mode.getValue ( )) {
            case UP:
                BlockPos pos = ( Util.mc.player.getPosition ( ).add ( 0 , 1 , 0 ) );
                BlockUtil.placeBlock ( pos , EnumFacing.UP , false );
                disable ( );
            case DOWN:
                pos = ( Util.mc.player.getPosition ( ).add ( 0 , 0 , 0 ) );
                BlockUtil.placeBlock ( pos , EnumFacing.DOWN , false );
                disable ( );
        }
    }

    public
    enum Mode {
        UP,
        DOWN

    }
}
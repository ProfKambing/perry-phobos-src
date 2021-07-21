package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.Util;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Objects;

public
class Rubberband extends Module {
    private final Setting < RubberMode > mode;
    private final Setting < Integer > Ym;

    public
    Rubberband ( ) {
        super ( "Rubberband" , "Teleports u to the latest ground pos." , Category.PLAYER , true , false , false );
        this.mode = (Setting < RubberMode >) this.register ( new Setting <> ( "Mode" , RubberMode.Motion ) );
        this.Ym = (Setting < Integer >) this.register ( new Setting ( "Motion" , 1 , - 15 , 15 , v -> this.mode.getValue ( ) == RubberMode.Motion ) );
    }

    @Override
    public
    void onEnable ( ) {
    }

    @Override
    public
    void onUpdate ( ) {
        switch (this.mode.getValue ( )) {
            case Motion: {
                Util.mc.player.motionY = this.Ym.getValue ( );
                break;
            }
            case Packet: {
                Objects.requireNonNull ( Rubberband.mc.getConnection ( ) ).sendPacket ( new CPacketPlayer.Position ( Rubberband.mc.player.posX , Rubberband.mc.player.posY + this.Ym.getValue ( ) , Rubberband.mc.player.posZ , true ) );
                break;
            }
            case Teleport: {
                Rubberband.mc.player.setPositionAndUpdate ( Rubberband.mc.player.posX , Rubberband.mc.player.posY + this.Ym.getValue ( ) , Rubberband.mc.player.posZ );
                break;
            }
        }
        this.toggle ( );
    }

    public
    enum RubberMode {
        Motion,
        Teleport,
        Packet
    }
}
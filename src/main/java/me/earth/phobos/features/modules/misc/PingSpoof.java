package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public
class PingSpoof
        extends Module {
    private final Setting < Boolean > seconds = this.register ( new Setting <> ( "Seconds" , false ) );
    private final Setting < Integer > delay = this.register ( new Setting < Object > ( "DelayMS" , 20 , 0 , 1000 , v -> ! this.seconds.getValue ( ) ) );
    private final Setting < Integer > secondDelay = this.register ( new Setting < Object > ( "DelayS" , 5 , 0 , 30 , v -> this.seconds.getValue ( ) ) );
    private final Setting < Boolean > offOnLogout = this.register ( new Setting <> ( "Logout" , false ) );
    private final Queue < Packet < ? > > packets = new ConcurrentLinkedQueue ( );
    private final Timer timer = new Timer ( );
    private boolean receive = true;

    public
    PingSpoof ( ) {
        super ( "PingSpoof" , "Spoofs your ping!" , Module.Category.MISC , true , false , false );
    }

    @Override
    public
    void onLoad ( ) {
        if ( this.offOnLogout.getValue ( ) ) {
            this.disable ( );
        }
    }

    @Override
    public
    void onLogout ( ) {
        if ( this.offOnLogout.getValue ( ) ) {
            this.disable ( );
        }
    }

    @Override
    public
    void onUpdate ( ) {
        this.clearQueue ( );
    }

    @Override
    public
    void onDisable ( ) {
        this.clearQueue ( );
    }

    @SubscribeEvent
    public
    void onPacketSend ( PacketEvent.Send event ) {
        if ( this.receive && PingSpoof.mc.player != null && ! mc.isSingleplayer ( ) && PingSpoof.mc.player.isEntityAlive ( ) && event.getStage ( ) == 0 && event.getPacket ( ) instanceof CPacketKeepAlive ) {
            this.packets.add ( event.getPacket ( ) );
            event.setCanceled ( true );
        }
    }

    public
    void clearQueue ( ) {
        if ( PingSpoof.mc.player != null && ! mc.isSingleplayer ( ) && PingSpoof.mc.player.isEntityAlive ( ) && ( ! this.seconds.getValue ( ) && this.timer.passedMs ( this.delay.getValue ( ) ) || this.seconds.getValue ( ) && this.timer.passedS ( this.secondDelay.getValue ( ) ) ) ) {
            double limit = MathUtil.getIncremental ( Math.random ( ) * 10.0 , 1.0 );
            this.receive = false;
            int i = 0;
            while ( (double) i < limit ) {
                Packet < ? > packet = this.packets.poll ( );
                if ( packet != null ) {
                    PingSpoof.mc.player.connection.sendPacket ( packet );
                }
                ++ i;
            }
            this.timer.reset ( );
            this.receive = true;
        }
    }
}


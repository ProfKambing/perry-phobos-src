package me.earth.phobos.features.modules.combat;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public
class AnvilAura
        extends Module {
    public Setting < Float > range = this.register ( new Setting <> ( "Range" , 6.0f , 0.0f , 10.0f ) );
    public Setting < Boolean > rotate = this.register ( new Setting <> ( "Rotate" , true ) );
    public Setting < Boolean > packet = this.register ( new Setting <> ( "Packet" , true ) );
    public Setting < Boolean > switcher = this.register ( new Setting <> ( "Switch" , true ) );
    public Setting < Integer > rotations = this.register ( new Setting <> ( "Spoofs" , 1 , 1 , 20 ) );
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private BlockPos placeTarget;

    public
    AnvilAura ( ) {
        super ( "AnvilAura" , "Useless" , Module.Category.COMBAT , true , false , false );
    }

    @Override
    public
    void onTick ( ) {
        this.doAnvilAura ( );
    }

    @SubscribeEvent
    public
    void onPacketSend ( PacketEvent.Send event ) {
        if ( event.getStage ( ) == 0 && this.rotate.getValue ( ) && this.rotating ) {
            if ( event.getPacket ( ) instanceof CPacketPlayer ) {
                CPacketPlayer packet = event.getPacket ( );
                packet.yaw = this.yaw;
                packet.pitch = this.pitch;
            }
            ++ this.rotationPacketsSpoofed;
            if ( this.rotationPacketsSpoofed >= this.rotations.getValue ( ) ) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }

    public
    void doAnvilAura ( ) {
        EntityPlayer finalTarget = this.getTarget ( );
        if ( finalTarget != null ) {
            this.placeTarget = this.getTargetPos ( finalTarget );
        }
        if ( this.placeTarget != null && finalTarget != null ) {
            this.placeAnvil ( this.placeTarget );
        }
    }

    public
    void placeAnvil ( BlockPos pos ) {
        if ( this.rotate.getValue ( ) ) {
            this.rotateToPos ( pos );
        }
        if ( this.switcher.getValue ( ) && ! this.isHoldingAnvil ( ) ) {
            this.doSwitch ( );
        }
        BlockUtil.placeBlock ( pos , EnumHand.MAIN_HAND , false , this.packet.getValue ( ) , AnvilAura.mc.player.isSneaking ( ) );
    }

    public
    boolean isHoldingAnvil ( ) {
        InventoryUtil.findHotbarBlock ( BlockObsidian.class );
        return AnvilAura.mc.player.getHeldItemMainhand ( ).getItem ( ) instanceof ItemBlock && ( (ItemBlock) AnvilAura.mc.player.getHeldItemMainhand ( ).getItem ( ) ).getBlock ( ) instanceof BlockAnvil || AnvilAura.mc.player.getHeldItemOffhand ( ).getItem ( ) instanceof ItemBlock && ( (ItemBlock) AnvilAura.mc.player.getHeldItemOffhand ( ).getItem ( ) ).getBlock ( ) instanceof BlockAnvil;
    }

    public
    void doSwitch ( ) {
        int obbySlot = InventoryUtil.findHotbarBlock ( BlockObsidian.class );
        if ( obbySlot == - 1 ) {
            for (int l = 0; l < 9; ++ l) {
                ItemStack stack = AnvilAura.mc.player.inventory.getStackInSlot ( l );
                Block block = ( (ItemBlock) stack.getItem ( ) ).getBlock ( );
                if ( ! ( block instanceof BlockObsidian ) ) continue;
                obbySlot = l;
            }
        }
        if ( obbySlot != - 1 ) {
            AnvilAura.mc.player.inventory.currentItem = obbySlot;
        }
    }

    public
    EntityPlayer getTarget ( ) {
        double shortestDistance = - 1.0;
        EntityPlayer target = null;
        for (EntityPlayer player : AnvilAura.mc.world.playerEntities) {
            if ( this.getPlaceableBlocksAboveEntity ( player ).isEmpty ( ) || shortestDistance != - 1.0 && ! ( AnvilAura.mc.player.getDistanceSq ( player ) < MathUtil.square ( shortestDistance ) ) )
                continue;
            shortestDistance = AnvilAura.mc.player.getDistance ( player );
            target = player;
        }
        return target;
    }

    public
    BlockPos getTargetPos ( Entity target ) {
        double distance = - 1.0;
        BlockPos finalPos = null;
        for (BlockPos pos : this.getPlaceableBlocksAboveEntity ( target )) {
            if ( distance != - 1.0 && ! ( AnvilAura.mc.player.getDistanceSq ( pos ) < MathUtil.square ( distance ) ) )
                continue;
            finalPos = pos;
            distance = AnvilAura.mc.player.getDistance ( pos.getX ( ) , pos.getY ( ) , pos.getZ ( ) );
        }
        return finalPos;
    }

    public
    List < BlockPos > getPlaceableBlocksAboveEntity ( Entity target ) {
        BlockPos pos;
        new BlockPos ( Math.floor ( AnvilAura.mc.player.posX ) , Math.floor ( AnvilAura.mc.player.posY ) , Math.floor ( AnvilAura.mc.player.posZ ) );
        ArrayList < BlockPos > positions = new ArrayList <> ( );
        for (int i = (int) Math.floor ( AnvilAura.mc.player.posY + 2.0 ); i <= 256 && BlockUtil.isPositionPlaceable ( pos = new BlockPos ( Math.floor ( AnvilAura.mc.player.posX ) , i , Math.floor ( AnvilAura.mc.player.posZ ) ) , false ) != 0 && BlockUtil.isPositionPlaceable ( pos , false ) != - 1 && BlockUtil.isPositionPlaceable ( pos , false ) != 2; ++ i) {
            positions.add ( pos );
        }
        return positions;
    }

    private
    void rotateToPos ( BlockPos pos ) {
        if ( this.rotate.getValue ( ) ) {
            float[] angle = MathUtil.calcAngle ( AnvilAura.mc.player.getPositionEyes ( mc.getRenderPartialTicks ( ) ) , new Vec3d ( (float) pos.getX ( ) + 0.5f , (float) pos.getY ( ) - 0.5f , (float) pos.getZ ( ) + 0.5f ) );
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
}


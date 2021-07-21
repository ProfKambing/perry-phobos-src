package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.InventoryUtil;
import net.minecraft.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public
class SelfAnvil
        extends Module {
    private final Setting < Boolean > rotate = this.register ( new Setting <> ( "Rotate" , true ) );
    private final Setting < Boolean > onlyHole = this.register ( new Setting <> ( "HoleOnly" , false ) );
    private final Setting < Boolean > helpingBlocks = this.register ( new Setting <> ( "HelpingBlocks" , true ) );
    private final Setting < Boolean > chat = this.register ( new Setting <> ( "Chat Msgs" , true ) );
    private final Setting < Boolean > packet = this.register ( new Setting <> ( "Packet" , false ) );
    private final Setting < Integer > blocksPerTick = this.register ( new Setting <> ( "Blocks/Tick" , 2 , 1 , 8 ) );
    private BlockPos placePos;
    private BlockPos playerPos;
    private int blockSlot;
    private int obbySlot;
    private int lastBlock;
    private int blocksThisTick;

    public
    SelfAnvil ( ) {
        super ( "SelfAnvil" , "funne falling block" , Module.Category.COMBAT , true , false , false );
    }

    @Override
    public
    void
    onEnable ( ) {
        this.playerPos = new BlockPos ( SelfAnvil.mc.player.posX , SelfAnvil.mc.player.posY , SelfAnvil.mc.player.posZ );
        this.placePos = playerPos.offset ( EnumFacing.UP , 2 );
        this.blockSlot = this.findBlockSlot ( );
        this.obbySlot = InventoryUtil.findHotbarBlock ( BlockObsidian.class );
        this.lastBlock = SelfAnvil.mc.player.inventory.currentItem;
        if ( ! doFirstChecks ( ) )
            this.disable ( );
    }

    @Override
    public
    void
    onTick ( ) {
        this.blocksThisTick = 0;
        this.doSelfAnvil ( );
    }

    private
    void
    doSelfAnvil ( ) {
        if ( this.helpingBlocks.getValue ( ) && BlockUtil.isPositionPlaceable ( this.placePos , false , true ) == 2 ) {
            InventoryUtil.switchToHotbarSlot ( this.obbySlot , false );
            doHelpBlocks ( );
        }
        if ( this.blocksThisTick < this.blocksPerTick.getValue ( ) && BlockUtil.isPositionPlaceable ( this.placePos , false , true ) == 3 ) {
            InventoryUtil.switchToHotbarSlot ( this.blockSlot , false );
            BlockUtil.placeBlock ( this.placePos , EnumHand.MAIN_HAND , this.rotate.getValue ( ) , this.packet.getValue ( ) , false );
            InventoryUtil.switchToHotbarSlot ( this.lastBlock , false );
            SelfAnvil.mc.player.connection.sendPacket ( new CPacketEntityAction ( SelfAnvil.mc.player , CPacketEntityAction.Action.STOP_SNEAKING ) );
            this.disable ( );
        }
    }

    private
    void
    doHelpBlocks ( ) { // TODO: 6/7/2021 make it prioritize placing behind the player if possible
        if ( this.blocksThisTick >= this.blocksPerTick.getValue ( ) ) return;
        for ( EnumFacing side1 : EnumFacing.values ( ) ) {
            if ( side1 == EnumFacing.DOWN ) continue;
            if ( BlockUtil.isPositionPlaceable ( this.placePos.offset ( side1 ) , false , true ) == 3 ) {
                BlockUtil.placeBlock ( this.placePos.offset ( side1 ) , EnumHand.MAIN_HAND , this.rotate.getValue ( ) , this.packet.getValue ( ) , false );
                this.blocksThisTick++;
                return;
            }
        }
        for ( EnumFacing side1 : EnumFacing.values ( ) ) {
            if ( side1 == EnumFacing.DOWN ) continue;
            for ( EnumFacing side2 : EnumFacing.values ( ) ) {
                if ( BlockUtil.isPositionPlaceable ( this.placePos.offset ( side1 ).offset ( side2 ) , false , true ) == 3 ) {
                    BlockUtil.placeBlock ( this.placePos.offset ( side1 ).offset ( side2 ) , EnumHand.MAIN_HAND , this.rotate.getValue ( ) , this.packet.getValue ( ) , false );
                    this.blocksThisTick++;
                    return;
                }
            }
        }
        for ( EnumFacing side1 : EnumFacing.values ( ) ) {
            for ( EnumFacing side2 : EnumFacing.values ( ) ) {
                for ( EnumFacing side3 : EnumFacing.values ( ) ) { // fuck this is retarded but it works
                    if ( BlockUtil.isPositionPlaceable ( this.placePos.offset ( side1 ).offset ( side2 ).offset ( side3 ) , false , true ) == 3 ) {
                        BlockUtil.placeBlock ( this.placePos.offset ( side1 ).offset ( side2 ).offset ( side3 ) , EnumHand.MAIN_HAND , this.rotate.getValue ( ) , this.packet.getValue ( ) , false );
                        this.blocksThisTick++;
                        return;
                    }
                }
            }
        }
    }

    private
    int findBlockSlot ( ) {
        for ( int i = 0; i < 9; i++ ) {
            ItemStack item = SelfAnvil.mc.player.inventory.getStackInSlot ( i );
            if ( ! ( item.getItem ( ) instanceof ItemBlock ) )
                continue;
            Block block = Block.getBlockFromItem ( SelfAnvil.mc.player.inventory.getStackInSlot ( i ).getItem ( ) );
            if ( block instanceof BlockFalling )
                return i;
        }
        return - 1;
    }

    private
    boolean
    doFirstChecks ( ) {
        int canPlace = BlockUtil.isPositionPlaceable ( this.placePos , false , true );
        if ( SelfAnvil.fullNullCheck ( ) || ! SelfAnvil.mc.world.isAirBlock ( this.playerPos ) ) return false;
        if ( ! BlockUtil.isBothHole ( this.playerPos ) && this.onlyHole.getValue ( ) ) return false;
        if ( this.blockSlot == - 1 ) {
            if ( this.chat.getValue ( ) )
                Command.sendMessage ( "<" + this.getDisplayName ( ) + "> " + "\u00a7c" + "No Anvils in hotbar." );
            return false;
        }
        if ( canPlace == 2 ) {
            if ( ! this.helpingBlocks.getValue ( ) ) {
                if ( this.chat.getValue ( ) )
                    Command.sendMessage ( "<" + this.getDisplayName ( ) + "> " + "\u00a7c" + "Nowhere to place." );
                return false;
            }
            if ( this.obbySlot == - 1 ) {
                if ( this.chat.getValue ( ) )
                    Command.sendMessage ( "<" + this.getDisplayName ( ) + "> " + "\u00a7c" + "No Obsidian in hotbar." );
                return false;
            }
        } else if ( canPlace != 3 ) {
            if ( this.chat.getValue ( ) )
                Command.sendMessage ( "<" + this.getDisplayName ( ) + "> " + "\u00a7c" + "Not enough room." );
            return false;
        }
        return true;
    }
}

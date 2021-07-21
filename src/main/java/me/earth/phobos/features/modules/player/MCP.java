package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public
class MCP
        extends Module {
    private final Setting < Mode > mode = this.register ( new Setting <> ( "Mode" , Mode.MIDDLECLICK ) );
    private final Setting < Boolean > antiFriend = this.register ( new Setting <> ( "AntiFriend" , true ) );
    private final Setting < Boolean > skyonly = this.register ( new Setting <> ( "SkyOnly" , false ) );
    private boolean clicked;

    public
    MCP ( ) {
        super ( "MCP" , "Throws a pearl" , Module.Category.PLAYER , false , false , false );
    }

    @Override
    public
    void onEnable ( ) {
        if ( ! MCP.fullNullCheck ( ) && this.mode.getValue ( ) == Mode.TOGGLE ) {
            this.throwPearl ( );
            this.disable ( );
        }
    }

    @Override
    public
    void onTick ( ) {
        if ( this.mode.getValue ( ) == Mode.MIDDLECLICK ) {
            if ( Mouse.isButtonDown ( 2 ) ) {
                if ( ! this.clicked ) {
                    this.throwPearl ( );
                }
                this.clicked = true;
            } else {
                this.clicked = false;
            }
        }
    }

    private
    void throwPearl ( ) {
        boolean offhand;
        RayTraceResult result;
        if ( this.antiFriend.getValue ( ) && ( result = MCP.mc.objectMouseOver ) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer )
            return;
        if ( this.skyonly.getValue ( ) && ( result = MCP.mc.objectMouseOver ) != null && result.typeOfHit == RayTraceResult.Type.BLOCK )
            return;
        int pearlSlot = InventoryUtil.findHotbarBlock ( ItemEnderPearl.class );
        offhand = MCP.mc.player.getHeldItemOffhand ( ).getItem ( ) == Items.ENDER_PEARL;
        if ( pearlSlot != - 1 || offhand ) {
            int oldslot = MCP.mc.player.inventory.currentItem;
            if ( ! offhand ) {
                InventoryUtil.switchToHotbarSlot ( pearlSlot , false );
            }
            MCP.mc.playerController.processRightClick ( MCP.mc.player , MCP.mc.world , offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND );
            if ( ! offhand ) {
                InventoryUtil.switchToHotbarSlot ( oldslot , false );
            }
        }
    }

    public
    enum Mode {
        TOGGLE,
        MIDDLECLICK

    }
}

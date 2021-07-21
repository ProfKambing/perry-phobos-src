package me.earth.phobos.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.manager.FileManager;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

public
class Notifications
        extends Module {
    private static final String fileName = "phobos/util/ModuleMessage_List.txt";
    private static final List < String > modules = new ArrayList <> ( );
    private static Notifications INSTANCE = new Notifications ( );
    private final Timer timer = new Timer ( );
    private final List < EntityPlayer > burrowedPlayers = new ArrayList <> ( );
    private final Set < EntityPlayer > sword = Collections.newSetFromMap ( new WeakHashMap <> ( ) );
    public Setting < Boolean > totemPops = this.register ( new Setting <> ( "TotemPops" , true ) );
    public Setting < Boolean > totemNoti = this.register ( new Setting < Object > ( "TotemNoti" , Boolean.FALSE , v -> this.totemPops.getValue ( ) ) );
    public Setting < Integer > delay = this.register ( new Setting < Object > ( "Delay" , 0 , 0 , 5000 , v -> this.totemPops.getValue ( ) , "Delays messages." ) );
    public Setting < Boolean > clearOnLogout = this.register ( new Setting <> ( "LogoutClear" , false ) );
    public Setting < Boolean > moduleMessage = this.register ( new Setting <> ( "ModuleMessage" , true ) );
    private final Setting < Boolean > readfile = this.register ( new Setting < Object > ( "LoadFile" , Boolean.FALSE , v -> this.moduleMessage.getValue ( ) ) );
    public Setting < Boolean > list = this.register ( new Setting < Object > ( "List" , Boolean.FALSE , v -> this.moduleMessage.getValue ( ) ) );
    public Setting < Boolean > watermark = this.register ( new Setting < Object > ( "Watermark" , Boolean.TRUE , v -> this.moduleMessage.getValue ( ) ) );
    public Setting < Boolean > visualRange = this.register ( new Setting <> ( "VisualRange" , false ) );
    public Setting < Boolean > VisualRangeSound = this.register ( new Setting <> ( "VisualRangeSound" , false ) );
    public Setting < Boolean > coords = this.register ( new Setting < Object > ( "Coords" , Boolean.TRUE , v -> this.visualRange.getValue ( ) ) );
    public Setting < Boolean > leaving = this.register ( new Setting < Object > ( "Leaving" , Boolean.TRUE , v -> this.visualRange.getValue ( ) ) );
    public Setting < Boolean > pearls = this.register ( new Setting <> ( "PearlNotifs" , true ) );
    public Setting < Boolean > crash = this.register ( new Setting <> ( "Crash" , true ) );
    public Setting < Boolean > popUp = this.register ( new Setting <> ( "PopUpVisualRange" , false ) );
    public Setting < Boolean > burrow = this.register ( new Setting <> ( "Burrow" , false ) );
    public Setting < Boolean > thirtytwokay = this.register ( new Setting <> ( "32k" , false ) );
    public Timer totemAnnounce = new Timer ( );
    private boolean flag;
    private List < EntityPlayer > knownPlayers = new ArrayList <> ( );
    private boolean check;

    public
    Notifications ( ) {
        super ( "Notifications" , "Sends Messages." , Module.Category.CLIENT , true , false , false );
        this.setInstance ( );
    }

    public static
    Notifications getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new Notifications ( );
        }
        return INSTANCE;
    }

    public static
    void displayCrash ( Exception e ) {
        Command.sendMessage ( "\u00a7cException caught: " + e.getMessage ( ) );
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }

    private
    boolean is32k ( ItemStack stack ) {
        if ( stack.getItem ( ) instanceof net.minecraft.item.ItemSword ) {
            NBTTagList enchants = stack.getEnchantmentTagList ( );
            for (int i = 0; i < enchants.tagCount ( ); i++) {
                if ( enchants.getCompoundTagAt ( i ).getShort ( "lvl" ) >= Short.MAX_VALUE )
                    return true;
            }
        }
        return false;
    }


    @Override
    public
    void onLoad ( ) {
        this.check = true;
        this.loadFile ( );
        this.check = false;
    }

    @Override
    public
    void onEnable ( ) {
        this.knownPlayers = new ArrayList <> ( );
        if ( ! this.check ) {
            this.loadFile ( );
            flag = true;
        }
    }

    @Override
    public
    void onUpdate ( ) {
        if ( this.readfile.getValue ( ) ) {
            if ( ! this.check ) {
                Command.sendMessage ( "Loading File..." );
                this.timer.reset ( );
                this.loadFile ( );
            }
            this.check = true;
        }
        if ( this.check && this.timer.passedMs ( 750L ) ) {
            this.readfile.setValue ( false );
            this.check = false;
        }
        if ( this.visualRange.getValue ( ) ) {
            ArrayList < EntityPlayer > tickPlayerList = new ArrayList <> ( Notifications.mc.world.playerEntities );
            if ( tickPlayerList.size ( ) > 0 ) {
                for (EntityPlayer player : tickPlayerList) {
                    if ( player.getName ( ).equals ( Notifications.mc.player.getName ( ) ) || this.knownPlayers.contains ( player ) )
                        continue;
                    this.knownPlayers.add ( player );
                    if ( Phobos.friendManager.isFriend ( player ) ) {
                        Command.sendMessage ( "Player \u00a7a" + player.getName ( ) + "\u00a7r" + " entered your visual range" + ( this.coords.getValue ( ) ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!" ) , this.popUp.getValue ( ) );
                    } else {
                        Command.sendMessage ( "Player \u00a7c" + player.getName ( ) + "\u00a7r" + " entered your visual range" + ( this.coords.getValue ( ) ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!" ) , this.popUp.getValue ( ) );
                    }
                    if ( this.VisualRangeSound.getValue ( ) ) {
                        me.earth.phobos.features.modules.client.Notifications.mc.player.playSound ( SoundEvents.BLOCK_ANVIL_LAND , 1.0f , 1.0f );
                    }
                    return;
                }
            }
            if ( this.knownPlayers.size ( ) > 0 ) {
                for (EntityPlayer player : this.knownPlayers) {
                    if ( tickPlayerList.contains ( player ) ) continue;
                    this.knownPlayers.remove ( player );
                    if ( this.leaving.getValue ( ) ) {
                        if ( Phobos.friendManager.isFriend ( player ) ) {
                            Command.sendMessage ( "Player \u00a7a" + player.getName ( ) + "\u00a7r" + " left your visual range" + ( this.coords.getValue ( ) ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!" ) , this.popUp.getValue ( ) );
                        } else {
                            Command.sendMessage ( "Player \u00a7c" + player.getName ( ) + "\u00a7r" + " left your visual range" + ( this.coords.getValue ( ) ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!" ) , this.popUp.getValue ( ) );
                        }
                    }
                    return;
                }
            }
        }
        if ( this.pearls.getValue ( ) ) {
            if ( mc.world == null || mc.player == null ) {
                return;
            }
            Entity enderPearl = null;
            for (final Entity e : mc.world.loadedEntityList) {
                if ( e instanceof EntityEnderPearl ) {
                    enderPearl = e;
                    break;
                }
            }
            if ( enderPearl == null ) {
                flag = true;
                return;
            }
            EntityPlayer closestPlayer = null;
            for (final EntityPlayer entity : mc.world.playerEntities) {
                if ( closestPlayer == null ) {
                    closestPlayer = entity;
                } else {
                    if ( closestPlayer.getDistance ( enderPearl ) <= entity.getDistance ( enderPearl ) ) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
            if ( closestPlayer == mc.player ) {
                flag = false;
            }
            if ( closestPlayer != null && flag ) {
                String facing = enderPearl.getHorizontalFacing ( ).toString ( );
                if ( facing.equals ( "west" ) ) {
                    facing = "east";
                } else if ( facing.equals ( "east" ) ) {
                    facing = "west";
                }
                Command.sendSilentMessage ( Phobos.friendManager.isFriend ( closestPlayer.getName ( ) ) ? ( ChatFormatting.AQUA + closestPlayer.getName ( ) + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + facing + "!" ) : ( ChatFormatting.RED + closestPlayer.getName ( ) + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + facing + "!" ) );
                flag = false;
            }
        }
    }

    @Override
    public
    void onTick ( ) {
        if ( ! this.burrow.getValue ( ) || Notifications.fullNullCheck ( ) )
            return;
        for (EntityPlayer entityPlayer : mc.world.playerEntities.stream ( ).filter ( entityPlayer -> entityPlayer != mc.player ).collect ( Collectors.toList ( ) )) {
            if ( ! burrowedPlayers.contains ( entityPlayer ) && isInBurrow ( entityPlayer ) ) {
                Command.sendMessage ( ChatFormatting.RED + entityPlayer.getDisplayNameString ( ) + ChatFormatting.GREEN + " has burrowed." );
                burrowedPlayers.add ( entityPlayer );
            }
        }
        if ( ! this.thirtytwokay.getValue ( ) ) {
            int once = 0;
            for (EntityPlayer player : mc.world.playerEntities) {
                if ( player.equals ( mc.player ) )
                    continue;
                if ( is32k ( player.getHeldItem ( EnumHand.MAIN_HAND ) ) && ! this.sword.contains ( player ) ) {
                    Command.sendMessage ( ChatFormatting.RED + player.getDisplayNameString ( ) + " is holding a 32k" );
                    this.sword.add ( player );
                }
                if ( is32k ( player.getHeldItem ( EnumHand.MAIN_HAND ) ) ) {
                    if ( once > 0 ) {
                        return;
                    }
                    once++;
                }
                if ( ! this.sword.contains ( player ) )
                    continue;
                if ( is32k ( player.getHeldItem ( EnumHand.MAIN_HAND ) ) )
                    continue;
                Command.sendMessage ( ChatFormatting.GREEN + player.getDisplayNameString ( ) + " is no longer holding a 32k" );
                this.sword.remove ( player );
            }
        }
    }

    private
    boolean isInBurrow ( EntityPlayer entityPlayer ) {
        BlockPos playerPos = new BlockPos ( getMiddlePosition ( entityPlayer.posX ) , entityPlayer.posY , getMiddlePosition ( entityPlayer.posZ ) );

        return mc.world.getBlockState ( playerPos ).getBlock ( ) == Blocks.OBSIDIAN
                || mc.world.getBlockState ( playerPos ).getBlock ( ) == Blocks.ENDER_CHEST
                || mc.world.getBlockState ( playerPos ).getBlock ( ) == Blocks.ANVIL;
    }

    private
    double getMiddlePosition ( double positionIn ) {
        double positionFinal = Math.round ( positionIn );

        if ( Math.round ( positionIn ) > positionIn ) {
            positionFinal -= 0.5;
        } else if ( Math.round ( positionIn ) <= positionIn ) {
            positionFinal += 0.5;
        }

        return positionFinal;
    }

    public
    void loadFile ( ) {
        List < String > fileInput = FileManager.readTextFileAllLines ( fileName );
        Iterator < String > i = fileInput.iterator ( );
        modules.clear ( );
        while ( i.hasNext ( ) ) {
            String s = i.next ( );
            if ( s.replaceAll ( "\\s" , "" ).isEmpty ( ) ) continue;
            modules.add ( s );
        }
    }

    @SubscribeEvent
    public
    void onToggleModule ( ClientEvent event ) {
        int moduleNumber;
        Module module;
        if ( ! this.moduleMessage.getValue ( ) ) {
            return;
        }
        if ( ! ( event.getStage ( ) != 0 || ( module = (Module) event.getFeature ( ) ).equals ( this ) || ! modules.contains ( module.getDisplayName ( ) ) && this.list.getValue ( ) ) ) {
            moduleNumber = 0;
            for (char character : module.getDisplayName ( ).toCharArray ( )) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            if ( this.watermark.getValue ( ) ) {
                TextComponentString textComponentString = new TextComponentString ( Phobos.commandManager.getClientMessage ( ) + " " + "\u00a7r" + "\u00a7c" + module.getDisplayName ( ) + " disabled." );
                Notifications.mc.ingameGUI.getChatGUI ( ).printChatMessageWithOptionalDeletion ( textComponentString , moduleNumber );
            } else {
                TextComponentString textComponentString = new TextComponentString ( "\u00a7c" + module.getDisplayName ( ) + " disabled." );
                Notifications.mc.ingameGUI.getChatGUI ( ).printChatMessageWithOptionalDeletion ( textComponentString , moduleNumber );
            }
        }
        if ( event.getStage ( ) == 1 && ( modules.contains ( ( module = (Module) event.getFeature ( ) ).getDisplayName ( ) ) || ! this.list.getValue ( ) ) ) {
            moduleNumber = 0;
            for (char character : module.getDisplayName ( ).toCharArray ( )) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            if ( this.watermark.getValue ( ) ) {
                TextComponentString textComponentString = new TextComponentString ( Phobos.commandManager.getClientMessage ( ) + " " + "\u00a7r" + "\u00a7a" + module.getDisplayName ( ) + " enabled." );
                Notifications.mc.ingameGUI.getChatGUI ( ).printChatMessageWithOptionalDeletion ( textComponentString , moduleNumber );
            } else {
                TextComponentString textComponentString = new TextComponentString ( "\u00a7a" + module.getDisplayName ( ) + " enabled." );
                Notifications.mc.ingameGUI.getChatGUI ( ).printChatMessageWithOptionalDeletion ( textComponentString , moduleNumber );
            }
        }
    }
}
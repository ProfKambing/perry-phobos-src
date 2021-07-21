package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public
class AntiPackets
        extends Module {
    private final Setting < Mode > mode = this.register ( new Setting <> ( "Packets" , Mode.CLIENT ) );
    private final Setting < Integer > page = this.register ( new Setting < Object > ( "SPackets" , 1 , 1 , 10 , v -> this.mode.getValue ( ) == Mode.SERVER ) );
    private final Setting < Integer > pages = this.register ( new Setting < Object > ( "CPackets" , 1 , 1 , 4 , v -> this.mode.getValue ( ) == Mode.CLIENT ) );
    private final Setting < Boolean > AdvancementInfo = this.register ( new Setting < Object > ( "AdvancementInfo" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > Animation = this.register ( new Setting < Object > ( "Animation" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > BlockAction = this.register ( new Setting < Object > ( "BlockAction" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > BlockBreakAnim = this.register ( new Setting < Object > ( "BlockBreakAnim" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > BlockChange = this.register ( new Setting < Object > ( "BlockChange" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > Camera = this.register ( new Setting < Object > ( "Camera" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > ChangeGameState = this.register ( new Setting < Object > ( "ChangeGameState" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > Chat = this.register ( new Setting < Object > ( "Chat" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 1 ) );
    private final Setting < Boolean > ChunkData = this.register ( new Setting < Object > ( "ChunkData" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > CloseWindow = this.register ( new Setting < Object > ( "CloseWindow" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > CollectItem = this.register ( new Setting < Object > ( "CollectItem" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > CombatEvent = this.register ( new Setting < Object > ( "Combatevent" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > ConfirmTransaction = this.register ( new Setting < Object > ( "ConfirmTransaction" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > Cooldown = this.register ( new Setting < Object > ( "Cooldown" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > CustomPayload = this.register ( new Setting < Object > ( "CustomPayload" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > CustomSound = this.register ( new Setting < Object > ( "CustomSound" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 2 ) );
    private final Setting < Boolean > DestroyEntities = this.register ( new Setting < Object > ( "DestroyEntities" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > Disconnect = this.register ( new Setting < Object > ( "Disconnect" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > DisplayObjective = this.register ( new Setting < Object > ( "DisplayObjective" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > Effect = this.register ( new Setting < Object > ( "Effect" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > Entity = this.register ( new Setting < Object > ( "Entity" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > EntityAttach = this.register ( new Setting < Object > ( "EntityAttach" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > EntityEffect = this.register ( new Setting < Object > ( "EntityEffect" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > EntityEquipment = this.register ( new Setting < Object > ( "EntityEquipment" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > EntityHeadLook = this.register ( new Setting < Object > ( "EntityHeadLook" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > EntityMetadata = this.register ( new Setting < Object > ( "EntityMetadata" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > EntityProperties = this.register ( new Setting < Object > ( "EntityProperties" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > EntityStatus = this.register ( new Setting < Object > ( "EntityStatus" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > EntityTeleport = this.register ( new Setting < Object > ( "EntityTeleport" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > EntityVelocity = this.register ( new Setting < Object > ( "EntityVelocity" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > Explosion = this.register ( new Setting < Object > ( "Explosion" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > HeldItemChange = this.register ( new Setting < Object > ( "HeldItemChange" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 4 ) );
    private final Setting < Boolean > JoinGame = this.register ( new Setting < Object > ( "JoinGame" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > KeepAlive = this.register ( new Setting < Object > ( "KeepAlive" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > Maps = this.register ( new Setting < Object > ( "Maps" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > MoveVehicle = this.register ( new Setting < Object > ( "MoveVehicle" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > MultiBlockChange = this.register ( new Setting < Object > ( "MultiBlockChange" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > OpenWindow = this.register ( new Setting < Object > ( "OpenWindow" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > Particles = this.register ( new Setting < Object > ( "Particles" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > PlaceGhostRecipe = this.register ( new Setting < Object > ( "PlaceGhostRecipe" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 5 ) );
    private final Setting < Boolean > PlayerAbilities = this.register ( new Setting < Object > ( "PlayerAbilities" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > PlayerListHeaderFooter = this.register ( new Setting < Object > ( "PlayerListHeaderFooter" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > PlayerListItem = this.register ( new Setting < Object > ( "PlayerListItem" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > PlayerPosLook = this.register ( new Setting < Object > ( "PlayerPosLook" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > RecipeBook = this.register ( new Setting < Object > ( "RecipeBook" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > RemoveEntityEffect = this.register ( new Setting < Object > ( "RemoveEntityEffect" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > ResourcePackSend = this.register ( new Setting < Object > ( "ResourcePackSend" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > Respawn = this.register ( new Setting < Object > ( "Respawn" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 6 ) );
    private final Setting < Boolean > ScoreboardObjective = this.register ( new Setting < Object > ( "ScoreboardObjective" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SelectAdvancementsTab = this.register ( new Setting < Object > ( "SelectAdvancementsTab" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > ServerDifficulty = this.register ( new Setting < Object > ( "ServerDifficulty" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SetExperience = this.register ( new Setting < Object > ( "SetExperience" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SetPassengers = this.register ( new Setting < Object > ( "SetPassengers" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SetSlot = this.register ( new Setting < Object > ( "SetSlot" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SignEditorOpen = this.register ( new Setting < Object > ( "SignEditorOpen" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SoundEffect = this.register ( new Setting < Object > ( "SoundEffect" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 7 ) );
    private final Setting < Boolean > SpawnExperienceOrb = this.register ( new Setting < Object > ( "SpawnExperienceOrb" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnGlobalEntity = this.register ( new Setting < Object > ( "SpawnGlobalEntity" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnMob = this.register ( new Setting < Object > ( "SpawnMob" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnObject = this.register ( new Setting < Object > ( "SpawnObject" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnPainting = this.register ( new Setting < Object > ( "SpawnPainting" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnPlayer = this.register ( new Setting < Object > ( "SpawnPlayer" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > SpawnPosition = this.register ( new Setting < Object > ( "SpawnPosition" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > Statistics = this.register ( new Setting < Object > ( "Statistics" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 8 ) );
    private final Setting < Boolean > TabComplete = this.register ( new Setting < Object > ( "TabComplete" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > Teams = this.register ( new Setting < Object > ( "Teams" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > TimeUpdate = this.register ( new Setting < Object > ( "TimeUpdate" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > Title = this.register ( new Setting < Object > ( "Title" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > UnloadChunk = this.register ( new Setting < Object > ( "UnloadChunk" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > UpdateBossInfo = this.register ( new Setting < Object > ( "UpdateBossInfo" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > UpdateHealth = this.register ( new Setting < Object > ( "UpdateHealth" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > UpdateScore = this.register ( new Setting < Object > ( "UpdateScore" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 9 ) );
    private final Setting < Boolean > UpdateTileEntity = this.register ( new Setting < Object > ( "UpdateTileEntity" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 10 ) );
    private final Setting < Boolean > UseBed = this.register ( new Setting < Object > ( "UseBed" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 10 ) );
    private final Setting < Boolean > WindowItems = this.register ( new Setting < Object > ( "WindowItems" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 10 ) );
    private final Setting < Boolean > WindowProperty = this.register ( new Setting < Object > ( "WindowProperty" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 10 ) );
    private final Setting < Boolean > WorldBorder = this.register ( new Setting < Object > ( "WorldBorder" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.SERVER && this.page.getValue ( ) == 10 ) );
    private final Setting < Boolean > Animations = this.register ( new Setting < Object > ( "Animations" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ChatMessage = this.register ( new Setting < Object > ( "ChatMessage" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ClickWindow = this.register ( new Setting < Object > ( "ClickWindow" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ClientSettings = this.register ( new Setting < Object > ( "ClientSettings" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ClientStatus = this.register ( new Setting < Object > ( "ClientStatus" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > CloseWindows = this.register ( new Setting < Object > ( "CloseWindows" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ConfirmTeleport = this.register ( new Setting < Object > ( "ConfirmTeleport" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > ConfirmTransactions = this.register ( new Setting < Object > ( "ConfirmTransactions" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 1 ) );
    private final Setting < Boolean > CreativeInventoryAction = this.register ( new Setting < Object > ( "CreativeInventoryAction" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > CustomPayloads = this.register ( new Setting < Object > ( "CustomPayloads" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > EnchantItem = this.register ( new Setting < Object > ( "EnchantItem" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > EntityAction = this.register ( new Setting < Object > ( "EntityAction" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > HeldItemChanges = this.register ( new Setting < Object > ( "HeldItemChanges" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > Input = this.register ( new Setting < Object > ( "Input" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > KeepAlives = this.register ( new Setting < Object > ( "KeepAlives" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > PlaceRecipe = this.register ( new Setting < Object > ( "PlaceRecipe" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 2 ) );
    private final Setting < Boolean > Player = this.register ( new Setting < Object > ( "Player" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > PlayerAbility = this.register ( new Setting < Object > ( "PlayerAbility" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > PlayerDigging = this.register ( new Setting < Object > ( "PlayerDigging" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.page.getValue ( ) == 3 ) );
    private final Setting < Boolean > PlayerTryUseItem = this.register ( new Setting < Object > ( "PlayerTryUseItem" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > PlayerTryUseItemOnBlock = this.register ( new Setting < Object > ( "TryUseItemOnBlock" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > RecipeInfo = this.register ( new Setting < Object > ( "RecipeInfo" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > ResourcePackStatus = this.register ( new Setting < Object > ( "ResourcePackStatus" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > SeenAdvancements = this.register ( new Setting < Object > ( "SeenAdvancements" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 3 ) );
    private final Setting < Boolean > PlayerPackets = this.register ( new Setting < Object > ( "PlayerPackets" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > Spectate = this.register ( new Setting < Object > ( "Spectate" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > SteerBoat = this.register ( new Setting < Object > ( "SteerBoat" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > TabCompletion = this.register ( new Setting < Object > ( "TabCompletion" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > UpdateSign = this.register ( new Setting < Object > ( "UpdateSign" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > UseEntity = this.register ( new Setting < Object > ( "UseEntity" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private final Setting < Boolean > VehicleMove = this.register ( new Setting < Object > ( "VehicleMove" , Boolean.FALSE , v -> this.mode.getValue ( ) == Mode.CLIENT && this.pages.getValue ( ) == 4 ) );
    private int hudAmount;

    public
    AntiPackets ( ) {
        super ( "AntiPackets" , "Blocks Packets" , Module.Category.MISC , true , false , false );
    }

    @SubscribeEvent
    public
    void onPacketSend ( PacketEvent.Send event ) {
        if ( ! this.isEnabled ( ) ) {
            return;
        }
        if ( event.getPacket ( ) instanceof CPacketAnimation && this.Animations.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketChatMessage && this.ChatMessage.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketClickWindow && this.ClickWindow.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketClientSettings && this.ClientSettings.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketClientStatus && this.ClientStatus.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketCloseWindow && this.CloseWindows.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketConfirmTeleport && this.ConfirmTeleport.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketConfirmTransaction && this.ConfirmTransactions.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketCreativeInventoryAction && this.CreativeInventoryAction.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketCustomPayload && this.CustomPayloads.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketEnchantItem && this.EnchantItem.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketEntityAction && this.EntityAction.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketHeldItemChange && this.HeldItemChanges.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketInput && this.Input.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketKeepAlive && this.KeepAlives.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlaceRecipe && this.PlaceRecipe.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlayer && this.Player.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlayerAbilities && this.PlayerAbility.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlayerDigging && this.PlayerDigging.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlayerTryUseItem && this.PlayerTryUseItem.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketPlayerTryUseItemOnBlock && this.PlayerTryUseItemOnBlock.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketRecipeInfo && this.RecipeInfo.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketResourcePackStatus && this.ResourcePackStatus.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketSeenAdvancements && this.SeenAdvancements.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketSpectate && this.Spectate.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketSteerBoat && this.SteerBoat.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketTabComplete && this.TabCompletion.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketUpdateSign && this.UpdateSign.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketUseEntity && this.UseEntity.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof CPacketVehicleMove && this.VehicleMove.getValue ( ) ) {
            event.setCanceled ( true );
        }
    }

    @SubscribeEvent
    public
    void onPacketReceive ( PacketEvent.Receive event ) {
        if ( ! this.isEnabled ( ) ) {
            return;
        }
        if ( event.getPacket ( ) instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketAnimation && this.Animation.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketBlockAction && this.BlockAction.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketBlockBreakAnim && this.BlockBreakAnim.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketBlockChange && this.BlockChange.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCamera && this.Camera.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketChangeGameState && this.ChangeGameState.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketChat && this.Chat.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketChunkData && this.ChunkData.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCloseWindow && this.CloseWindow.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCollectItem && this.CollectItem.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCombatEvent && this.CombatEvent.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketConfirmTransaction && this.ConfirmTransaction.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCooldown && this.Cooldown.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCustomPayload && this.CustomPayload.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCustomSound && this.CustomSound.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketDestroyEntities && this.DestroyEntities.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketDisconnect && this.Disconnect.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketChunkData && this.ChunkData.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCloseWindow && this.CloseWindow.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketCollectItem && this.CollectItem.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketDisplayObjective && this.DisplayObjective.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEffect && this.Effect.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntity && this.Entity.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityAttach && this.EntityAttach.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityEffect && this.EntityEffect.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityEquipment && this.EntityEquipment.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityHeadLook && this.EntityHeadLook.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityMetadata && this.EntityMetadata.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityProperties && this.EntityProperties.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityStatus && this.EntityStatus.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityTeleport && this.EntityTeleport.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketEntityVelocity && this.EntityVelocity.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketExplosion && this.Explosion.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketHeldItemChange && this.HeldItemChange.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketJoinGame && this.JoinGame.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketKeepAlive && this.KeepAlive.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketMaps && this.Maps.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketMoveVehicle && this.MoveVehicle.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketMultiBlockChange && this.MultiBlockChange.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketOpenWindow && this.OpenWindow.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketParticles && this.Particles.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketPlaceGhostRecipe && this.PlaceGhostRecipe.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketPlayerAbilities && this.PlayerAbilities.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketPlayerListHeaderFooter && this.PlayerListHeaderFooter.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketPlayerListItem && this.PlayerListItem.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketPlayerPosLook && this.PlayerPosLook.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketRecipeBook && this.RecipeBook.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketRemoveEntityEffect && this.RemoveEntityEffect.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketResourcePackSend && this.ResourcePackSend.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketRespawn && this.Respawn.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketScoreboardObjective && this.ScoreboardObjective.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSelectAdvancementsTab && this.SelectAdvancementsTab.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketServerDifficulty && this.ServerDifficulty.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSetExperience && this.SetExperience.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSetPassengers && this.SetPassengers.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSetSlot && this.SetSlot.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSignEditorOpen && this.SignEditorOpen.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSoundEffect && this.SoundEffect.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnExperienceOrb && this.SpawnExperienceOrb.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnGlobalEntity && this.SpawnGlobalEntity.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnMob && this.SpawnMob.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnObject && this.SpawnObject.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnPainting && this.SpawnPainting.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnPlayer && this.SpawnPlayer.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketSpawnPosition && this.SpawnPosition.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketStatistics && this.Statistics.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketTabComplete && this.TabComplete.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketTeams && this.Teams.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketTimeUpdate && this.TimeUpdate.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketTitle && this.Title.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUnloadChunk && this.UnloadChunk.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUpdateBossInfo && this.UpdateBossInfo.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUpdateHealth && this.UpdateHealth.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUpdateScore && this.UpdateScore.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUpdateTileEntity && this.UpdateTileEntity.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketUseBed && this.UseBed.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketWindowItems && this.WindowItems.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketWindowProperty && this.WindowProperty.getValue ( ) ) {
            event.setCanceled ( true );
        }
        if ( event.getPacket ( ) instanceof SPacketWorldBorder && this.WorldBorder.getValue ( ) ) {
            event.setCanceled ( true );
        }
    }

    @Override
    public
    void onEnable ( ) {
        String standart = "\u00a7aAntiPackets On!\u00a7f Cancelled Packets: ";
        StringBuilder text = new StringBuilder ( standart );
        if ( ! this.settings.isEmpty ( ) ) {
            for (Setting setting : this.settings) {
                if ( ! ( setting.getValue ( ) instanceof Boolean ) || ! (Boolean) setting.getValue ( ) || setting.getName ( ).equalsIgnoreCase ( "Enabled" ) || setting.getName ( ).equalsIgnoreCase ( "drawn" ) )
                    continue;
                String name = setting.getName ( );
                text.append ( name ).append ( ", " );
            }
        }
        if ( text.toString ( ).equals ( standart ) ) {
            Command.sendMessage ( "\u00a7aAntiPackets On!\u00a7f Currently not cancelling any Packets." );
        } else {
            String output = this.removeLastChar ( this.removeLastChar ( text.toString ( ) ) );
            Command.sendMessage ( output );
        }
    }

    @Override
    public
    void onUpdate ( ) {
        int amount = 0;
        if ( ! this.settings.isEmpty ( ) ) {
            for (Setting setting : this.settings) {
                if ( ! ( setting.getValue ( ) instanceof Boolean ) || ! (Boolean) setting.getValue ( ) || setting.getName ( ).equalsIgnoreCase ( "Enabled" ) || setting.getName ( ).equalsIgnoreCase ( "drawn" ) )
                    continue;
                ++ amount;
            }
        }
        this.hudAmount = amount;
    }

    @Override
    public
    String getDisplayInfo ( ) {
        if ( this.hudAmount == 0 ) {
            return "";
        }
        return this.hudAmount + "";
    }

    public
    String removeLastChar ( String str ) {
        if ( str != null && str.length ( ) > 0 ) {
            str = str.substring ( 0 , str.length ( ) - 1 );
        }
        return str;
    }

    public
    enum Mode {
        CLIENT,
        SERVER

    }
}


package com.protectItemNotify.ProtectItemNotify;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "Protect Item Notify"
)
public class ProtectItemNotifyPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ProtectItemNotifyOverlay protectItemNotifyOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
    private boolean protectItemOn = true;

	@Getter
	private boolean isInWilderness = false;

	@Override
	protected void startUp() {
		overlayManager.add(protectItemNotifyOverlay);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(protectItemNotifyOverlay);
	}

	@SuppressWarnings("unused")
    @Subscribe
	public void onGameTick(GameTick event) {
		this.protectItemOn = client.getVarbitValue(VarbitID.PRAYER_PROTECTITEM) == 1
				|| client.getVarbitValue(VarbitID.PRAYER_PROTECT_ITEM_R) == 1;
		this.isInWilderness = isInPVP();
	}

	@Provides
	ProtectItemNotifyConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ProtectItemNotifyConfig.class);
	}

	private boolean isInPVP() {
		EnumSet<WorldType> worldType = client.getWorldType();
		if (worldType.contains(WorldType.DEADMAN)
				|| worldType.contains(WorldType.SEASONAL)
				|| worldType.contains(WorldType.TOURNAMENT_WORLD)
				|| worldType.contains(WorldType.LAST_MAN_STANDING)
				|| worldType.contains(WorldType.PVP_ARENA)) {
			// dmm = can't protect
			// leagues = safe pvp
			// lms, pvp arena = safe
			return false;
		}
		if (worldType.contains(WorldType.PVP) || worldType.contains(WorldType.HIGH_RISK)) {
			// pvp worlds = only outside safe area
			return client.getVarbitValue(VarbitID.PVP_AREA_CLIENT) == 1;
		}
		return client.getVarbitValue(VarbitID.INSIDE_WILDERNESS) == 1;
	}
}

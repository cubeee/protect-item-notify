package com.protectItemNotify.ProtectItemNotify;

import com.google.inject.Provides;
import com.protectItemNotify.ProtectItemNotify.ProtectItemNotifyOverlay;
import java.util.Optional;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Prayer;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Protect Item Notify"
)
public class ProtectItemNotifyPlugin extends Plugin
{
	@Inject private Client client;

	@Inject private ProtectItemNotifyConfig config;

	@Inject private ProtectItemNotifyOverlay protectItemNotifyOverlay;

	@Inject private OverlayManager overlayManager;

	private boolean protectItemOn = true;

	@Override
	protected void startUp() {
		overlayManager.add(protectItemNotifyOverlay);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(protectItemNotifyOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		this.protectItemOn = !client.isPrayerActive(Prayer.PROTECT_ITEM);
	}

	@Provides
	ProtectItemNotifyConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ProtectItemNotifyConfig.class);
	}

	public boolean isInPVP() {
		return client.getVarbitValue(VarbitID.PVP_AREA_CLIENT) == 1;
	}

	public boolean isProtectItemOn() {
		return protectItemOn;
	}
}

package com.protectItemNotify.ProtectItemNotify;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("protectitemnotify")
public interface ProtectItemNotifyConfig extends Config
{
	@ConfigItem(
			keyName = "scale",
			name = "Scale",
			description = "The scale of the ring of protect item image.",
			position = 0
	)
	default double scale() {
		return 1.0;
	}

	@ConfigItem(
			keyName = "pvponly",
			name = "PVP Only",
			description = "Should the icon only be displayed when in a PvP area.",
			position = 1
	)
	default boolean showPvpOnly() {
		return false;
	}

	@ConfigItem(
			keyName = "hideOnHighRisk",
			name = "Hide on High Risk worlds",
			description = "Choose if you don't want to be warned on High Risk worlds.",
			position = 2
	)
	default boolean hideOnHighRisk() {
		return true;
	}
}

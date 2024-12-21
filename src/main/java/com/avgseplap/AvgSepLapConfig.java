package com.avgseplap;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("avgseplapconfig")
public interface AvgSepLapConfig extends Config
{
	@ConfigItem(
			keyName = "greenTime",
			name = "Green Time Threshold",
			description = "The time (in seconds) for green color (default: 365 seconds = 6:05)"
	)
	default int greenTime()
	{
		return 365;  // Default to 6:05 (365 seconds)
	}

	@ConfigItem(
			keyName = "yellowTime",
			name = "Yellow Time Threshold",
			description = "The time (in seconds) for yellow color (default: 420 seconds = 7:00)"
	)
	default int yellowTime()
	{
		return 420;  // Default to 7:00 (420 seconds)
	}

	@ConfigItem(
			keyName = "redTime",
			name = "Red Time Threshold",
			description = "The time (in seconds) for red color (default: 480 seconds = 8:00)"
	)
	default int redTime()
	{
		return 480;  // Default to 8:00 (480 seconds)
	}
}

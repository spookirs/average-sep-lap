package com.avgseplap;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AvgSepLapPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AvgSepLapPlugin.class);
		RuneLite.main(args);
	}
}
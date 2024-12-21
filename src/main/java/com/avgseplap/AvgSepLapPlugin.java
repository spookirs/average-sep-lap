package com.avgseplap;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Sepulchre Lap Tracker"
)
public class AvgSepLapPlugin extends Plugin
{
	private static final Pattern LAP_TIME_PATTERN = Pattern.compile("Overall time: <col=ff0000>(\\d{1}):(\\d{2})");
	private final ArrayList<Long> lapTimes = new ArrayList<>();
	private long totalLapTime = 0;

	@Inject
	private Client client;

	@Inject
	private AvgSepLapConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AvgSepLapOverlay avgSepLapOverlay;

	@Inject
	private EventBus eventBus;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Sepulchre Lap Tracker started!");
		overlayManager.add(avgSepLapOverlay);
		avgSepLapOverlay.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Lap Timer"));
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Sepulchre Lap Tracker stopped!");
		overlayManager.remove(avgSepLapOverlay);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() == ChatMessageType.GAMEMESSAGE)
		{
			System.out.println("Type: " + event.getType() + " | Message: " + event.getMessage());
			Matcher matcher = LAP_TIME_PATTERN.matcher(event.getMessage());
			if (matcher.find())
			{
				int minutes = Integer.parseInt(matcher.group(1));
				int seconds = Integer.parseInt(matcher.group(2));
				//int mseconds = Integer.parseInt(matcher.group(3));

				//long lapTime = hours * 3600 + minutes * 60 + seconds; // Convert to seconds
				long lapTime = minutes * 60 + seconds;
				lapTimes.add(lapTime);
				totalLapTime += lapTime;

				// Optional: Log or debug output
				System.out.println("Lap time added: " + lapTime + " seconds.");
			}
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		// Check if the menu entry is for the Mysterious Stranger
		if (event.getTarget().contains("Mysterious Stranger") && event.getOption().equals("Talk-to"))
		{
			// Add a custom "Reset" menu option
			client.createMenuEntry(-1)
					.setOption("Reset")
					.setTarget(event.getTarget())
					.setType(MenuAction.RUNELITE) // This ensures it’s a custom option
					.onClick(e -> handleResetOption());
		}
	}

	private void handleResetOption()
	{
		// Your logic to reset the lap data
		lapTimes.clear();
		totalLapTime = 0;

		// Notify the user
		System.out.println("Lap data reset via Mysterious Stranger!");
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Lap data has been reset!", null);
	}

	public double getAverageLapTime()
	{
		return lapTimes.isEmpty() ? 0 : (double) totalLapTime / lapTimes.size();
	}

	@Provides
	AvgSepLapConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AvgSepLapConfig.class);
	}
}

package com.avgseplap;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.FontManager;

import javax.inject.Inject;
import java.awt.*;

public class AvgSepLapOverlay extends Overlay
{
    @Inject
    private AvgSepLapConfig config;

    private final AvgSepLapPlugin plugin;

    @Inject
    public AvgSepLapOverlay(AvgSepLapPlugin client)
    {
        this.plugin = client;
        setPosition(OverlayPosition.DYNAMIC); // Position the overlay
        setLayer(OverlayLayer.ABOVE_WIDGETS); // Choose where to render
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Inventory"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {

        double avgTimeSeconds = plugin.getAverageLapTime();
        long avgMinutes = (long) avgTimeSeconds / 60;
        long avgSeconds = (long) avgTimeSeconds % 60;

        String labelText = "Avg. Lap: ";
        String timeText = (avgMinutes + ":" + String.format("%02d", avgSeconds));

        Color timeColor = determineColor(avgTimeSeconds);

        graphics.setFont(FontManager.getRunescapeBoldFont());

        graphics.setColor(Color.WHITE);
        graphics.drawString(labelText, 23, 65);

        graphics.setColor(timeColor);
        graphics.drawString(timeText, 22 + graphics.getFontMetrics().stringWidth(labelText), 65);  // Adjust position after static text

        return null;
    }

    private Color determineColor(double timeInSeconds)
    {
        if (timeInSeconds == 0)
        {
            return Color.WHITE;
        }
        if (timeInSeconds <= config.greenTime())
        {
            return Color.GREEN;
        }
        else if (timeInSeconds <= config.yellowTime())
        {
            return Color.YELLOW;
        }
        else if (timeInSeconds <= config.redTime())
        {
            return Color.RED;
        }
        else
        {
            return Color.RED;
        }
    }
}

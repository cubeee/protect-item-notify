package com.protectItemNotify.ProtectItemNotify;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.util.ImageUtil;

public class ProtectItemNotifyOverlay extends Overlay {
    private static final ScaledImage previouslyScaledImage = new ScaledImage();
    private static BufferedImage protectItemImage;
    private final ProtectItemNotifyPlugin plugin;
    private final ProtectItemNotifyConfig protectItemConfig;

    @Inject
    ProtectItemNotifyOverlay(ProtectItemNotifyPlugin plugin, ProtectItemNotifyConfig config) {
        super(plugin);
        setPriority(OverlayPriority.MED);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        this.plugin = plugin;
        this.protectItemConfig = config;
        loadProtectItemImage();
        previouslyScaledImage.scale = 1;
        previouslyScaledImage.scaledBufferedImage = protectItemImage;
    }

    private static void loadProtectItemImage() {
        protectItemImage = ImageUtil.getResourceStreamFromClass(ProtectItemNotifyPlugin.class, "/protect-item.png");
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isInPVP() && protectItemConfig.pvponly() == true) {
            return null;
        }

        if (!plugin.isProtectItemOn()) {
            return null;
        }

        BufferedImage scaledProtectItemImage = scaleImage(protectItemImage);
        ImageComponent imagePanelComponent = new ImageComponent(scaledProtectItemImage);
        return imagePanelComponent.render(graphics);
    }

    private BufferedImage scaleImage(BufferedImage protectItemImage) {
        if (previouslyScaledImage.scale == protectItemConfig.scale() || protectItemConfig.scale() <= 0) {
            return previouslyScaledImage.scaledBufferedImage;
        }
        int w = protectItemImage.getWidth();
        int h = protectItemImage.getHeight();
        BufferedImage scaledProtectItemImage =
                new BufferedImage(
                        protectItemConfig.scale() * w, protectItemConfig.scale() * h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(protectItemConfig.scale(), protectItemConfig.scale());
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledProtectItemImage = scaleOp.filter(protectItemImage, scaledProtectItemImage);
        previouslyScaledImage.scaledBufferedImage = scaledProtectItemImage;
        previouslyScaledImage.scale = protectItemConfig.scale();
        return scaledProtectItemImage;
    }

    private static class ScaledImage {
        private int scale;
        private BufferedImage scaledBufferedImage;
    }

}

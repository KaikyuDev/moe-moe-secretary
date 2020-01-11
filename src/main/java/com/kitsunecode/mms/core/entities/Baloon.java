package com.kitsunecode.mms.core.entities;

import com.kitsunecode.mms.core.utils.Util;
import com.kitsunecode.mms.core.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Baloon extends JLabel {

    private static final String HIGH_QUALITY      = "baloon.highQuality";
    private static final String HIGH_QUALITY_TEXT = "baloon.highQualityText";
    private static final String BALOON_Y_OFFSET   = "baloon.yOffset";
    private static final String BALOON_X_OFFSET   = "baloon.xOffset";
    private static final String BALOON_WIDTH      = "baloon.width";
    private static final String BALOON_HEIGHT     = "baloon.height";
    private static final String BALOON_FONT_SIZE  = "baloon.fontSize";
    private static final String BALOON_FONT       = "baloon.font";
    private static final String BALOON_BACKGROUND = "baloon.background";
    private static final String BALOON_FOREGROUND = "baloon.foreground";

    private boolean isVisible = false;

    // TODO fix this mess
    public Baloon(int windowWidth, int windowHeight) {

        setSize(Settings.get(BALOON_WIDTH, 400), Settings.get(BALOON_HEIGHT, 100));
        setMinimumSize(new Dimension(Settings.get(BALOON_WIDTH, 400), Settings.get(BALOON_HEIGHT, 100)));
        setMaximumSize(new Dimension(Settings.get(BALOON_WIDTH, 400), Settings.get(BALOON_HEIGHT, 100) * 3));

        setLocation((windowWidth / 2 - Settings.get(BALOON_WIDTH, 400) / 2) + Settings.get(BALOON_X_OFFSET, 0),
                windowHeight - Settings.get(BALOON_Y_OFFSET, 300));
        setForeground(Settings.get(BALOON_FOREGROUND, Color.WHITE));
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);

        setFont(new Font(Settings.get(BALOON_FONT, "Arial"), Font.PLAIN, Settings.get(BALOON_FONT_SIZE, 15)));
        setVerticalAlignment(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Rectangle getDesiredSize(int parentWidth, int parentHeight) {
        return new Rectangle(
                (parentWidth / 2 - Settings.get(BALOON_WIDTH, 400) / 2) + Settings.get(BALOON_X_OFFSET, 0),
                parentHeight - Settings.get(BALOON_Y_OFFSET, 300),
                Settings.get(BALOON_WIDTH, 400),
                Settings.get(BALOON_HEIGHT, 100) * 3
        );
    }

    public void toggle(boolean visible) {
        isVisible = visible;
        setVisible(visible);
    }

    @Override
    public void paint(Graphics g) {
        if (isVisible) {

            setSize(getWidth(), getPreferredSize().height);

            Graphics2D g2d = (Graphics2D) g;

            boolean qualitySet = Settings.get(HIGH_QUALITY, true);

            if (qualitySet) {
                Util.setHighQuality(g2d);
            }

            g2d.setPaint(Settings.get(BALOON_BACKGROUND, Color.BLACK));
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10.0, 10.0));

            if (Settings.get(HIGH_QUALITY_TEXT, true) && !qualitySet) {
                Util.setHighQuality(g2d);
            }

            super.paint(g2d);
        }
    }
}
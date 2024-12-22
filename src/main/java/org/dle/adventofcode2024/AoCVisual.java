package org.dle.adventofcode2024;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.TimeUnit;

public class AoCVisual<D> {

    private final VisualisationPanel<D> panel;

    AoCVisual(String title, Drawable<D> drawable) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int maxSize = Math.min(dim.height, dim.width);
        frame.setSize(maxSize - (maxSize / 10), maxSize - (maxSize / 10));
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.black);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new VisualisationPanel<>(drawable);
        frame.add(panel);
        frame.setVisible(true);
    }

    void drawAndWait(D data, String text, long ms) {
        long start = System.currentTimeMillis();
        panel.drawable.setData(data);
        panel.drawable.setText(text);
        panel.repaint();
        long diff = System.currentTimeMillis() - start;
        if (ms > diff) {
            try {
                TimeUnit.MILLISECONDS.sleep(ms - diff);
            } catch (InterruptedException ignored) {}
        }
    }

    public abstract static class Drawable<D> {
        protected String text;
        protected D data;

        abstract void draw(Graphics g, int width, int height);

        void setData(D data) {
            this.data = data;
        }
        void setText(String text) {
            this.text = text;
        }
        String getText() {
            return text;
        }
    }

    public static class VisualisationPanel<D> extends JPanel {
        Drawable<D> drawable;

        public VisualisationPanel(Drawable<D> drawable) {
            this.drawable = drawable;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //draw main data
            if (drawable.data != null) {
                drawable.draw(g, getWidth(), getHeight());
            }

            // Draw text indicator
            if (drawable.text != null) {
                g.setFont(new Font("Arial", Font.BOLD, getWidth() / 50));
                FontMetrics fm = g.getFontMetrics();
                Rectangle2D rect = fm.getStringBounds(drawable.getText(), g);
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRect(getWidth() / 50, getHeight() / 25 - fm.getAscent(), (int) rect.getWidth(), (int) rect.getHeight());
                g.setColor(Color.RED);
                g.drawString(drawable.getText(), getWidth() / 50, getHeight() / 25);
            }
        }
    }
}

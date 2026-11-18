package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Timer;
import lombok.Getter;
import model.entity.Satellite;
import model.entity.Tracker;
import nicellipse.component.NiSpace;

@Getter
public class GlobalView extends NiSpace {

    private static final double SKY_RATIO = 0.55;

    private final SkyView skyView;
    private final OceanView oceanView;
    private final Timer timer;

    public GlobalView(int width, int height) {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);

        this.skyView = new SkyView();
        this.oceanView = new OceanView();

        add(skyView);
        add(oceanView);
        doLayout();

        this.timer = new Timer(20, e -> {
            skyView.update();
            oceanView.update();
            repaint();
        });
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight();
        int skyHeight = (int) (h * SKY_RATIO);
        skyView.setBounds(0, 0, w, skyHeight);
        oceanView.setBounds(0, skyHeight, w, h - skyHeight);
    }

    public void addSatellite(Satellite satellite) {
        skyView.addSatellite(satellite);
    }

    public void addTracker(Tracker tracker) {
        oceanView.addTracker(tracker);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        // Dessine les faisceaux de transmission par-dessus les deux sous-vues
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int oceanOffsetY = oceanView.getY();
        for (TrackerView tv : oceanView.getTrackerViews()) {
            if (tv.getTracker().getY() <= 14) {
                int tx = tv.getX() + tv.getWidth() / 2;
                int ty = oceanOffsetY + tv.getY() + 6;
                for (SatelliteView sv : skyView.getSatelliteViews()) {
                    int sx = sv.getX() + sv.getWidth() / 2;
                    int sy = sv.getY() + sv.getHeight() / 2;
                    if (Math.abs(tx - sx) < 130) {
                        g2.setColor(new Color(110, 245, 255, 150));
                        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{5f, 5f}, 0f));
                        g2.drawLine(tx, ty, sx, sy);
                    }
                }
            }
        }
        g2.dispose();
    }
}
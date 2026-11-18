package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.entity.Satellite;
import nicellipse.component.NiSpace;

public class SkyView extends NiSpace {

    private final List<SatelliteView> satelliteViews = new ArrayList<>();
    private final List<int[]> stars = new ArrayList<>();

    public SkyView() {
        super();
        setLayout(null);
        Random rand = new Random(42);
        for (int i = 0; i < 35; i++) {
            stars.add(new int[]{rand.nextInt(1200), rand.nextInt(260), rand.nextInt(2) + 1});
        }
    }

    public void addSatellite(Satellite satellite) {
        SatelliteView satView = new SatelliteView(satellite);
        satelliteViews.add(satView);
        add(satView);
    }

    public List<SatelliteView> getSatelliteViews() {
        return satelliteViews;
    }

    public void update() {
        for (SatelliteView satView : satelliteViews) {
            satView.update(getWidth(), getHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Dégradé espace profond -> bleu horizon
        g2.setPaint(new GradientPaint(0, 0, new Color(13, 27, 48), 0, h, new Color(135, 185, 224)));
        g2.fillRect(0, 0, w, h);

        // Étoiles discrètes en haute altitude
        g2.setColor(new Color(255, 255, 255, 130));
        for (int[] s : stars) {
            if (s[1] < h - 25) {
                g2.fillOval(s[0] % Math.max(1, w), s[1], s[2], s[2]);
            }
        }

        // Lignes d'orbite en pointillés
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 6f}, 0f));
        g2.drawLine(0, (int) (h * 0.25), w, (int) (h * 0.25));
        g2.drawLine(0, (int) (h * 0.55), w, (int) (h * 0.55));

        g2.dispose();
    }
}
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.SatelliteView;
import view.SimulationView;
import view.TrackerView;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Satellite & Balises");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SimulationView view = new SimulationView(1000, 680);

            // Satellites dans la partie supérieure (mouvement gauche -> droite uniquement)
            view.addSatellite(new SatelliteView(280, 45, 2));
            view.addSatellite(new SatelliteView(430, 95, 3));
            view.addSatellite(new SatelliteView(550, 145, 2));
            view.addSatellite(new SatelliteView(390, 205, 1));
            view.addSatellite(new SatelliteView(760, 50, 2));

            // Trackers dans la partie bleue (mouvement libre toutes directions, confiné dans l'océan)
            view.addTracker(new TrackerView(270, 380, 2, -1));
            view.addTracker(new TrackerView(100, 480, -2, 1));
            view.addTracker(new TrackerView(195, 535, 1, -2));
            view.addTracker(new TrackerView(465, 490, -1, -2));
            view.addTracker(new TrackerView(125, 600, 2, -1));

            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            view.start();
        });
    }
}
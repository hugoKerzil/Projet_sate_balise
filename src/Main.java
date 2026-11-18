import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.entity.Satellite;
import model.entity.Tracker;
import view.GlobalView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Satellite & Balises");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GlobalView globalView = new GlobalView(1000, 680);

            // Satellites dans la SkyView
            globalView.addSatellite(new Satellite("SAT-1", 280, 45, 2));
            globalView.addSatellite(new Satellite("SAT-2", 430, 95, 3));
            globalView.addSatellite(new Satellite("SAT-3", 550, 145, 2));
            globalView.addSatellite(new Satellite("SAT-4", 390, 205, 1));
            globalView.addSatellite(new Satellite("SAT-5", 760, 50, 2));

            // Trackers dans l'OceanView (y = 0 correspond à la surface de l'eau)
            globalView.addTracker(new Tracker("TRK-1", 270, 15));
            globalView.addTracker(new Tracker("TRK-2", 100, 100));
            globalView.addTracker(new Tracker("TRK-3", 195, 160));
            globalView.addTracker(new Tracker("TRK-4", 465, 110));
            globalView.addTracker(new Tracker("TRK-5", 125, 210));

            frame.setContentPane(globalView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            globalView.start();
        });
    }
}
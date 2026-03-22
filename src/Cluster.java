import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private double[] centroid;
    private List<Iris> points = new ArrayList<>();

    Cluster(double[] centroid) {
        this.centroid = centroid;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public List<Iris> getPoints() {
        return points;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }
}

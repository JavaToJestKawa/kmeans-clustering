public class Iris {
    private double[] features;
    private String label;

    public Iris(double[] features, String label) {
        this.features = features;
        this.label = label;
    }

    public double[] getFeatures() {
        return features;
    }

    public String getLabel() {
        return label;
    }
}

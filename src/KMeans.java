import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KMeans {
    public KMeans(String trainingFile, int k) throws IOException {
        List<Iris> dataset = loadData("iris_training.txt");
        List<Cluster> clusters = initializeCentroids(dataset, k);

        double prevError = Double.MAX_VALUE;

        while (true) {
            for (Cluster cluster : clusters) {
                cluster.getPoints().clear();
            }

//            Przydzielenie do najblizszego centroidu
            for (Iris iris : dataset) {
                Cluster bestCluster = null;
                double minDist = Double.MAX_VALUE;

                for (Cluster cluster : clusters) {
                    double dist = euclideanDistance(iris.getFeatures(), cluster.getCentroid());

                    if (dist < minDist) {
                        minDist = dist;
                        bestCluster = cluster;
                    }
                }

                if (bestCluster != null) {
                    bestCluster.getPoints().add(iris);
                }
            }

//            Obliczenie nowego centroidu i bledu
            double totalError = 0;
            for (Cluster cluster : clusters) {
                cluster.setCentroid(calculateCentroid(cluster.getPoints()));

                for (Iris iris : cluster.getPoints()) {
                    totalError += Math.pow(euclideanDistance(iris.getFeatures(), cluster.getCentroid()), 2);
                }
            }

            System.out.println("Suma kwadratów odległości: " + totalError);

//
            if (Math.abs(prevError - totalError) < 1e-6) break;
            prevError = totalError;
        }

//        Wyswietlanie składow i entropii
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            System.out.println("\nKlaster " + (i + 1) + ": " + cluster.getPoints().size() + " elementów");

            Map<String, Integer> labelCounts = new HashMap<>();
            for (Iris iris : cluster.getPoints()) {
                labelCounts.put(iris.getLabel(), labelCounts.getOrDefault(iris.getLabel(), 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            double entropy = 0.0;
            int total = cluster.getPoints().size();

            for (int count : labelCounts.values()) {
                double p = (double) count / total;
                entropy -= p * Math.log(p) / Math.log(2);
            }

            System.out.printf("Entropia: %.4f\n", entropy);
        }

//        Klasyfikacja nowego obiektu
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nCzy chcesz sklasyfikować nowy obiekt? (tak/nie): ");
        String decyzja = scanner.next();

        while (decyzja.equalsIgnoreCase("tak")) {
            double[] noweDane = new double[4];
            System.out.println("Podaj 4 cechy irysa (oddzielone Enterem):");
            for (int i = 0; i < 4; i++) {
                System.out.print("Cecha " + (i + 1) + ": ");
                noweDane[i] = scanner.nextDouble();
            }

            int najblizszyKlaster = -1;
            double minDist = Double.MAX_VALUE;

            for (int i = 0; i < clusters.size(); i++) {
                double dist = euclideanDistance(noweDane, clusters.get(i).getCentroid());
                if (dist < minDist) {
                    minDist = dist;
                    najblizszyKlaster = i;
                }
            }

            System.out.println("Nowy obiekt należy do klastra " + (najblizszyKlaster + 1));
            System.out.println("\nCzy chcesz sklasyfikować nowy obiekt? (tak/nie): ");
            decyzja = scanner.next();
        }

    }

    private double[] calculateCentroid(List<Iris> points) {
        int n = points.get(0).getFeatures().length;
        double[] centroid = new double[n];

        for (Iris iris : points) {
            for (int i = 0; i < n; i++) {
                centroid[i] += iris.getFeatures()[i];
            }
        }

        for (int i = 0; i < n; i++) {
            centroid[i] /= points.size();
        }

        return centroid;
    }

    private List<Cluster> initializeCentroids(List<Iris> data, int k) {
        List<Cluster> clusters = new ArrayList<>();
        Collections.shuffle(data, new Random());

        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster(data.get(i).getFeatures()));
        }

        return clusters;
    }

    private List<Iris> loadData(String filename) throws IOException {
        List<Iris> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            line = line.replace(",", ".").replaceAll("\\s+", " ");
            String[] parts = line.trim().split(" ");

            double[] features = new double[parts.length - 1];

            for (int i = 0; i < features.length; i++) {
                features[i] = Double.parseDouble(parts[i]);
            }

            String label = parts[parts.length - 1];
            data.add(new Iris(features, label));
        }
        return data;
    }

    static double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;

        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
    }

}

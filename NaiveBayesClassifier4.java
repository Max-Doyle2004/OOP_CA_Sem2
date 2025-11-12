import java.io.*;
import java.util.*;

public class NaiveBayesClassifier4 implements Serializable {

    // Maps to store feature counts for 'yes' and 'no' classes
    private Map<String, Integer> featureCountsYes = new HashMap<>();
    private Map<String, Integer> featureCountsNo = new HashMap<>();
    
    // Counters for number of 'yes' and 'no' instances
    private int totalYes = 0;
    private int totalNo = 0;

    // Train the classifier on in-memory data (list of String arrays)
    public void train(List<String[]> data) {
        // Reset training state
        featureCountsYes.clear();
        featureCountsNo.clear();
        totalYes = 0;
        totalNo = 0;

        // Iterate through each data row
        for (String[] row : data) {
            // Expecting format: [carAge, mileage, owners, service, label]
            if (row.length < 5) continue; // Skip invalid rows

            String label = row[4];
            if (label.equalsIgnoreCase("yes")) {
                totalYes++;
                updateFeatureCount(featureCountsYes, row);
            } else if (label.equalsIgnoreCase("no")) {
                totalNo++;
                updateFeatureCount(featureCountsNo, row);
            }
        }
    }

    // Helper method to count features for each label
    private void updateFeatureCount(Map<String, Integer> map, String[] row) {
        map.put("carAge_" + row[0], map.getOrDefault("carAge_" + row[0], 0) + 1);
        map.put("mileage_" + row[1], map.getOrDefault("mileage_" + row[1], 0) + 1);
        map.put("owners_" + row[2], map.getOrDefault("owners_" + row[2], 0) + 1);
        map.put("service_" + row[3], map.getOrDefault("service_" + row[3], 0) + 1);
    }

    // Reads CSV data from a file and trains the classifier
    public void trainFromCSV(String filename) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                data.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        train(data);
    }

    // Predicts the label (yes/no) based on input features
    public String predict(String carAge, String mileage, String owners, String service) {
        // Check if the classifier has been trained
        if ((totalYes + totalNo) == 0) {
            return "Model not trained yet!";
        }

        // Calculate prior probabilities
        double probYes = (double) totalYes / (totalYes + totalNo);
        double probNo = (double) totalNo / (totalYes + totalNo);

        // Multiply by conditional probabilities for 'yes'
        probYes *= (double) featureCountsYes.getOrDefault("carAge_" + carAge, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("mileage_" + mileage, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("owners_" + owners, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("service_" + service, 1) / totalYes;

        // Multiply by conditional probabilities for 'no'
        probNo *= (double) featureCountsNo.getOrDefault("carAge_" + carAge, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("mileage_" + mileage, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("owners_" + owners, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("service_" + service, 1) / totalNo;

        // Return prediction with higher probability
        return probYes > probNo ? "yes" : "no";
    }

    // Calculates accuracy by training on 75% and testing on 25% of the data
    public double calculateAccuracy(String filename) {
        List<String[]> yesList = new ArrayList<>();
        List<String[]> noList = new ArrayList<>();

        // Read and split data into 'yes' and 'no' lists
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                if (parts[4].equalsIgnoreCase("yes")) {
                    yesList.add(parts);
                } else {
                    noList.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }

        // Determine sizes for testing set
        int testSizeYes = (int) (0.25 * yesList.size());
        int testSizeNo = (int) (0.25 * noList.size());

        // Prepare training and testing datasets
        List<String[]> testData = new ArrayList<>();
        List<String[]> trainData = new ArrayList<>();

        // Shuffle to ensure random selection
        Collections.shuffle(yesList);
        Collections.shuffle(noList);

        // Add to test and train lists
        testData.addAll(yesList.subList(0, testSizeYes));
        testData.addAll(noList.subList(0, testSizeNo));
        trainData.addAll(yesList.subList(testSizeYes, yesList.size()));
        trainData.addAll(noList.subList(testSizeNo, noList.size()));

        // Train the model on the training dataset
        train(trainData);

        // Evaluate on the testing dataset
        int correct = 0;
        for (String[] row : testData) {
            String prediction = predict(row[0], row[1], row[2], row[3]);
            if (prediction.equalsIgnoreCase(row[4])) {
                correct++;
            }
        }

        // Return accuracy percentage
        return 100.0 * correct / testData.size();
    }
}

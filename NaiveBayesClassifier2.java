import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayesClassifier2 {
    
    // Maps for counting features conditional on "yes" or "no"
    private Map<String, Integer> featureCountsYes = new HashMap<>();
    private Map<String, Integer> featureCountsNo = new HashMap<>();

    // Total counts of "yes" and "no" labels
    private int totalYes = 0;
    private int totalNo = 0;

    // Trains the classifier by reading labeled data from a CSV file
    public void train(String filename) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) 
            {
                String[] parts = line.split(",");
                String carAge = parts[0];
                String mileage = parts[1];
                String owners = parts[2];
                String service = parts[3];
                String label = parts[4];

                // Update counts for "yes" label
                if (label.equalsIgnoreCase("yes")) 
                {
                    totalYes++;
                    featureCountsYes.put("carAge_" + carAge, featureCountsYes.getOrDefault("carAge_" + carAge, 0) + 1);
                    featureCountsYes.put("mileage_" + mileage, featureCountsYes.getOrDefault("mileage_" + mileage, 0) + 1);
                    featureCountsYes.put("owners_" + owners, featureCountsYes.getOrDefault("owners_" + owners, 0) + 1);
                    featureCountsYes.put("service_" + service, featureCountsYes.getOrDefault("service_" + service, 0) + 1);
                } 
                // Update counts for "no" label
                else if (label.equalsIgnoreCase("no")) 
                {
                    totalNo++;
                    featureCountsNo.put("carAge_" + carAge, featureCountsNo.getOrDefault("carAge_" + carAge, 0) + 1);
                    featureCountsNo.put("mileage_" + mileage, featureCountsNo.getOrDefault("mileage_" + mileage, 0) + 1);
                    featureCountsNo.put("owners_" + owners, featureCountsNo.getOrDefault("owners_" + owners, 0) + 1);
                    featureCountsNo.put("service_" + service, featureCountsNo.getOrDefault("service_" + service, 0) + 1);
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); // Print error if file reading fails
        }
    }

    // Predicts "yes" or "no" for given features using Naive Bayes
    public String predict(String carAge, String mileage, String owners, String service) 
    {
        // Calculate prior probabilities
        double probYes = (double) totalYes / (totalYes + totalNo);
        double probNo = (double) totalNo / (totalYes + totalNo);

        // Multiply with likelihoods (Laplace smoothing using default 1)
        probYes *= (double) featureCountsYes.getOrDefault("carAge_" + carAge, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("mileage_" + mileage, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("owners_" + owners, 1) / totalYes;
        probYes *= (double) featureCountsYes.getOrDefault("service_" + service, 1) / totalYes;

        probNo *= (double) featureCountsNo.getOrDefault("carAge_" + carAge, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("mileage_" + mileage, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("owners_" + owners, 1) / totalNo;
        probNo *= (double) featureCountsNo.getOrDefault("service_" + service, 1) / totalNo;

        // Return the class with the higher probability
        return probYes > probNo ? "yes" : "no"; 
    }
}

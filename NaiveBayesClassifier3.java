import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayesClassifier3 implements Serializable 
{
    // Feature count maps for "yes" and "no" classifications
    private Map<String, Integer> featureCountsYes = new HashMap<>();
    private Map<String, Integer> featureCountsNo = new HashMap<>();

    // Total number of "yes" and "no" examples
    private int totalYes = 0;
    private int totalNo = 0;

    // Train the classifier from a CSV file
    public void train(String filename) 
    {
        // Clear any existing data
        featureCountsYes.clear();
        featureCountsNo.clear();
        totalYes = 0;
        totalNo = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) 
            {
                String[] parts = line.split(",");
                if (parts.length != 5) continue; // Skip malformed lines

                String carAge = parts[0];
                String mileage = parts[1];
                String owners = parts[2];
                String service = parts[3];
                String label = parts[4];

                // Update feature counts based on label
                if (label.equalsIgnoreCase("yes")) 
                {
                    totalYes++;
                    incrementCount(featureCountsYes, "carAge_" + carAge);
                    incrementCount(featureCountsYes, "mileage_" + mileage);
                    incrementCount(featureCountsYes, "owners_" + owners);
                    incrementCount(featureCountsYes, "service_" + service);
                } 
                else if (label.equalsIgnoreCase("no")) 
                {
                    totalNo++;
                    incrementCount(featureCountsNo, "carAge_" + carAge);
                    incrementCount(featureCountsNo, "mileage_" + mileage);
                    incrementCount(featureCountsNo, "owners_" + owners);
                    incrementCount(featureCountsNo, "service_" + service);
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); // Handle file read errors
        }
    }

    // Increment count for a given feature in the map
    private void incrementCount(Map<String, Integer> map, String key) 
    {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    // Predict the label ("yes" or "no") based on input features
    public String predict(String carAge, String mileage, String owners, String service) 
    {
        double probYes = (double) totalYes / (totalYes + totalNo);
        double probNo = (double) totalNo / (totalYes + totalNo);

        // Multiply conditional probabilities
        probYes *= ((double) featureCountsYes.getOrDefault("carAge_" + carAge, 1)) / totalYes;
        probYes *= ((double) featureCountsYes.getOrDefault("mileage_" + mileage, 1)) / totalYes;
        probYes *= ((double) featureCountsYes.getOrDefault("owners_" + owners, 1)) / totalYes;
        probYes *= ((double) featureCountsYes.getOrDefault("service_" + service, 1)) / totalYes;

        probNo *= ((double) featureCountsNo.getOrDefault("carAge_" + carAge, 1)) / totalNo;
        probNo *= ((double) featureCountsNo.getOrDefault("mileage_" + mileage, 1)) / totalNo;
        probNo *= ((double) featureCountsNo.getOrDefault("owners_" + owners, 1)) / totalNo;
        probNo *= ((double) featureCountsNo.getOrDefault("service_" + service, 1)) / totalNo;

        return probYes > probNo ? "yes" : "no";
    }

    // Save the trained model to a file
    public void saveModel(String filename) 
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) 
        {
            oos.writeObject(this);
            System.out.println("Model saved to " + filename);
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); // Handle file write errors
        }
    }

    // Load a model from file; return null if load fails
    public static NaiveBayesClassifier3 loadModel(String filename) 
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) 
        {
            return (NaiveBayesClassifier3) ois.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println("Could not load model from " + filename + ". Starting fresh.");
            return null;
        }
    }
}

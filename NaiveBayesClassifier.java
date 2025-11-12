import java.util.HashMap;
import java.util.Map;

// A basic Naive Bayes classifier for predicting accident history based on car attributes
public class NaiveBayesClassifier {

    // Stores the prior probabilities for each class label (yes/no)
    private Map<String, Double> priorProbabilities;

    // Stores conditional probabilities for each feature given a class label
    private Map<String, Map<String, Double>> conditionalProbabilities;

    // Constructor initializes prior and conditional probabilities
    public NaiveBayesClassifier() 
    {
        priorProbabilities = new HashMap<>();
        conditionalProbabilities = new HashMap<>();

        // Hardcoded Prior Probabilities
        priorProbabilities.put("yes", 0.4); // 40% chance of "yes"
        priorProbabilities.put("no", 0.6);  // 60% chance of "no"

        // Hardcoded Conditional Probabilities for Car Age
        Map<String, Double> carAge = new HashMap<>();
        carAge.put("new|yes", 0.3);
        carAge.put("old|yes", 0.7); 
        carAge.put("new|no", 0.6); 
        carAge.put("old|no", 0.4); 

        // Hardcoded Conditional Probabilities for Mileage
        Map<String, Double> mileage = new HashMap<>();
        mileage.put("low|yes", 0.4); 
        mileage.put("high|yes", 0.6); 
        mileage.put("low|no", 0.7); 
        mileage.put("high|no", 0.3); 

        // Hardcoded Conditional Probabilities for Previous Owners
        Map<String, Double> owners = new HashMap<>();
        owners.put("one|yes", 0.5); 
        owners.put("multiple|yes", 0.5); 
        owners.put("one|no", 0.7); 
        owners.put("multiple|no", 0.3); 

        // Hardcoded Conditional Probabilities for Service History
        Map<String, Double> service = new HashMap<>();
        service.put("full|yes", 0.4); 
        service.put("partial|yes", 0.6); 
        service.put("full|no", 0.7); 
        service.put("partial|no", 0.3); 

        // Add feature probability maps to the main conditional probability map
        conditionalProbabilities.put("CarAge", carAge);
        conditionalProbabilities.put("Mileage", mileage);
        conditionalProbabilities.put("PreviousOwners", owners);
        conditionalProbabilities.put("ServiceHistory", service);
    }

    // Predicts whether the car has a history of accidents using Naive Bayes
    public String predict(String carAge, String mileage, String owners, String serviceHistory) {
        // Calculate P(yes | features)
        double probYes = priorProbabilities.get("yes")
            * conditionalProbabilities.get("CarAge").get(carAge + "|yes")
            * conditionalProbabilities.get("Mileage").get(mileage + "|yes")
            * conditionalProbabilities.get("PreviousOwners").get(owners + "|yes")
            * conditionalProbabilities.get("ServiceHistory").get(serviceHistory + "|yes");

        // Calculate
        double probNo = priorProbabilities.get("no")
            * conditionalProbabilities.get("CarAge").get(carAge + "|no")
            * conditionalProbabilities.get("Mileage").get(mileage + "|no")
            * conditionalProbabilities.get("PreviousOwners").get(owners + "|no")
            * conditionalProbabilities.get("ServiceHistory").get(serviceHistory + "|no");

        // Return the class label with higher probability
        return probYes > probNo ? "yes" : "no";
    }
}

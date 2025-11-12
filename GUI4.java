import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUI4 {

    // Declaring GUI components
    private JFrame frame;
    private JComboBox<String> carAgeBox;
    private JComboBox<String> mileageBox;
    private JComboBox<String> ownersBox;
    private JComboBox<String> serviceBox;
    private JButton trainButton;
    private JButton predictButton;
    private JButton accuracyButton;
    private JLabel resultLabel;

    // Instance of the Naive Bayes classifier
    private NaiveBayesClassifier4 classifier;

    // Constructor to build the GUI and define actions
    public GUI4() {
        // Initialize classifier
        classifier = new NaiveBayesClassifier4();

        // Create main frame
        frame = new JFrame("Car Accident History Predictor - Level 4");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);  // Use absolute positioning

        // Dropdowns for input features
        carAgeBox = new JComboBox<>(new String[]{"new", "old"});
        mileageBox = new JComboBox<>(new String[]{"low", "high"});
        ownersBox = new JComboBox<>(new String[]{"one", "multiple"});
        serviceBox = new JComboBox<>(new String[]{"full", "partial"});

        // Buttons for actions
        trainButton = new JButton("Train Classifier");
        predictButton = new JButton("Predict");
        accuracyButton = new JButton("Test Accuracy");

        // Label to display prediction result
        resultLabel = new JLabel("Result: ");

        // Positioning components on the frame
        carAgeBox.setBounds(50, 30, 120, 25);
        mileageBox.setBounds(200, 30, 120, 25);
        ownersBox.setBounds(50, 70, 120, 25);
        serviceBox.setBounds(200, 70, 120, 25);

        trainButton.setBounds(50, 120, 140, 30);
        predictButton.setBounds(200, 120, 140, 30);
        accuracyButton.setBounds(125, 170, 200, 30);
        resultLabel.setBounds(50, 220, 400, 30);

        // Add components to the frame
        frame.add(carAgeBox);
        frame.add(mileageBox);
        frame.add(ownersBox);
        frame.add(serviceBox);
        frame.add(trainButton);
        frame.add(predictButton);
        frame.add(accuracyButton);
        frame.add(resultLabel);

        // Action for Train button: Trains model using CSV file
        trainButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                classifier.trainFromCSV("car_accident_history_dataset.csv");
                JOptionPane.showMessageDialog(frame, "Training completed.");
            }
        });

        // Action for Predict button: Predicts outcome based on selected features
        predictButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String carAge = (String) carAgeBox.getSelectedItem();
                String mileage = (String) mileageBox.getSelectedItem();
                String owners = (String) ownersBox.getSelectedItem();
                String service = (String) serviceBox.getSelectedItem();
                String prediction = classifier.predict(carAge, mileage, owners, service);
                resultLabel.setText("Result: " + prediction.toUpperCase());
            }
        });

        // Action for Accuracy button: Calculates and shows accuracy on the dataset
        accuracyButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                double accuracy = classifier.calculateAccuracy("car_accident_history_dataset.csv");
                JOptionPane.showMessageDialog(frame, "Accuracy: " + String.format("%.2f", accuracy) + "%");
            }
        });

        // Show the GUI
        frame.setVisible(true);
    }
}


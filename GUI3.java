import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI3 {

    // GUI components
    private JFrame frame;
    private JComboBox<String> carAgeBox;
    private JComboBox<String> mileageBox;
    private JComboBox<String> ownersBox;
    private JComboBox<String> serviceBox;
    private JButton trainButton;
    private JButton predictButton;
    private JButton saveButton;
    private JButton loadButton;
    private JLabel resultLabel;

    // Naive Bayes Classifier
    private NaiveBayesClassifier3 classifier;

    public GUI3() {
        classifier = new NaiveBayesClassifier3(); // Create a new classifier instance

        // Initialize frame
        frame = new JFrame("Car Accident History Predictor");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Drop-down boxes for input features
        carAgeBox = new JComboBox<>(new String[]{"new", "old"});
        mileageBox = new JComboBox<>(new String[]{"low", "high"});
        ownersBox = new JComboBox<>(new String[]{"one", "multiple"});
        serviceBox = new JComboBox<>(new String[]{"full", "partial"});

        // Buttons for actions
        trainButton = new JButton("Train");
        predictButton = new JButton("Predict");
        saveButton = new JButton("Save Model");
        loadButton = new JButton("Load Model");

        // Label to display prediction result
        resultLabel = new JLabel("Result: ");

        // Set component positions
        carAgeBox.setBounds(50, 30, 120, 25);
        mileageBox.setBounds(200, 30, 120, 25);
        ownersBox.setBounds(50, 70, 120, 25);
        serviceBox.setBounds(200, 70, 120, 25);

        trainButton.setBounds(50, 120, 120, 30);
        predictButton.setBounds(200, 120, 120, 30);
        saveButton.setBounds(50, 170, 120, 30);
        loadButton.setBounds(200, 170, 120, 30);
        resultLabel.setBounds(50, 230, 300, 30);

        // Add components to frame
        frame.add(carAgeBox);
        frame.add(mileageBox);
        frame.add(ownersBox);
        frame.add(serviceBox);
        frame.add(trainButton);
        frame.add(predictButton);
        frame.add(saveButton);
        frame.add(loadButton);
        frame.add(resultLabel);

        // Train model using CSV file
        trainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                classifier.train("car_accident_history_dataset.csv"); // CSV should exist
                JOptionPane.showMessageDialog(frame, "Training completed!");
            }
        });

        // Predict accident history based on input
        predictButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carAge = (String) carAgeBox.getSelectedItem();
                String mileage = (String) mileageBox.getSelectedItem();
                String owners = (String) ownersBox.getSelectedItem();
                String service = (String) serviceBox.getSelectedItem();

                String prediction = classifier.predict(carAge, mileage, owners, service);
                resultLabel.setText("Result: " + prediction.toUpperCase()); // Display result
            }
        });

        // Save the trained model to a file
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                classifier.saveModel("naive_model.ser"); // Save to default file
                JOptionPane.showMessageDialog(frame, "Model saved!");
            }
        });

        // Load a model from file
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NaiveBayesClassifier3 loaded = NaiveBayesClassifier3.loadModel("naive_model.ser");
                if (loaded != null) {
                    classifier = loaded; // Replace current instance with loaded one
                    JOptionPane.showMessageDialog(frame, "Model loaded!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to load model.");
                }
            }
        });

        frame.setVisible(true);
    }
}


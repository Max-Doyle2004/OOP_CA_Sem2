import javax.swing.*;  // For GUI components
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GUI class for interacting with the NaiveBayesClassifier
public class GUI 
{
    // GUI components
    private JFrame frame;
    private JComboBox<String> carAgeBox;
    private JComboBox<String> mileageBox;
    private JComboBox<String> ownersBox;
    private JComboBox<String> serviceBox;
    private JButton predictButton;
    private JLabel resultLabel;

    // Naive Bayes classifier instance
    private NaiveBayesClassifier classifier;

    // Constructor initializes GUI components and logic
    public GUI() {
        classifier = new NaiveBayesClassifier(); // Initializes classifier with hardcoded probabilities

        frame = new JFrame("Car Accident History Predictor");
        frame.setSize(400, 300);  // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on window close
        frame.setLayout(null); // Use absolute positioning

        // Dropdowns for user input
        carAgeBox = new JComboBox<>(new String[]{"new", "old"});
        mileageBox = new JComboBox<>(new String[]{"low", "high"});
        ownersBox = new JComboBox<>(new String[]{"one", "multiple"});
        serviceBox = new JComboBox<>(new String[]{"full", "partial"});

        // Button and label
        predictButton = new JButton("Predict");
        resultLabel = new JLabel("Result: ");

        // Set bounds (x, y, width, height) for all components
        carAgeBox.setBounds(50, 30, 120, 25);
        mileageBox.setBounds(200, 30, 120, 25);
        ownersBox.setBounds(50, 70, 120, 25);
        serviceBox.setBounds(200, 70, 120, 25);
        predictButton.setBounds(125, 120, 120, 30);
        resultLabel.setBounds(50, 170, 300, 30);

        // Add components to the frame
        frame.add(carAgeBox);
        frame.add(mileageBox);
        frame.add(ownersBox);
        frame.add(serviceBox);
        frame.add(predictButton);
        frame.add(resultLabel);

        // Set up event listener for prediction button
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String carAge = (String) carAgeBox.getSelectedItem();
                String mileage = (String) mileageBox.getSelectedItem();
                String owners = (String) ownersBox.getSelectedItem();
                String service = (String) serviceBox.getSelectedItem();

                // Use classifier to make prediction
                String prediction = classifier.predict(carAge, mileage, owners, service);
                resultLabel.setText("Result: " + prediction.toUpperCase());
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}

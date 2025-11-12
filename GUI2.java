import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUI2 {

    // GUI components
    private JFrame frame;
    private JComboBox<String> carAgeBox;
    private JComboBox<String> mileageBox;
    private JComboBox<String> ownersBox;
    private JComboBox<String> serviceBox;
    private JButton trainButton;
    private JButton predictButton;
    private JLabel resultLabel;

    // Classifier instance
    private NaiveBayesClassifier2 classifier;

    public GUI2() 
    {
        // Initialize classifier
        classifier = new NaiveBayesClassifier2();

        // Create the main application window
        frame = new JFrame("Car Accident History Predictor");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);  // Absolute positioning

        // Dropdowns for feature inputs
        carAgeBox = new JComboBox<>(new String[]{"new", "old"});
        mileageBox = new JComboBox<>(new String[]{"low", "high"});
        ownersBox = new JComboBox<>(new String[]{"one", "multiple"});
        serviceBox = new JComboBox<>(new String[]{"full", "partial"});

        // Buttons and result display
        trainButton = new JButton("Train Classifier");
        predictButton = new JButton("Predict");
        resultLabel = new JLabel("Result: ");

        // Position GUI components
        carAgeBox.setBounds(50, 30, 120, 25);
        mileageBox.setBounds(200, 30, 120, 25);
        ownersBox.setBounds(50, 70, 120, 25);
        serviceBox.setBounds(200, 70, 120, 25);
        trainButton.setBounds(50, 120, 140, 30);
        predictButton.setBounds(200, 120, 140, 30);
        resultLabel.setBounds(50, 180, 300, 30);

        // Add components to the frame
        frame.add(carAgeBox);
        frame.add(mileageBox);
        frame.add(ownersBox);
        frame.add(serviceBox);
        frame.add(trainButton);
        frame.add(predictButton);
        frame.add(resultLabel);

        // Train button behavior
        trainButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Train classifier using the dataset
                classifier.train("car_accident_history_dataset.csv");
                JOptionPane.showMessageDialog(frame, "Training Completed!");
            }
        });

        // Predict button behavior
        predictButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Get selected feature values
                String carAge = (String) carAgeBox.getSelectedItem();
                String mileage = (String) mileageBox.getSelectedItem();
                String owners = (String) ownersBox.getSelectedItem();
                String service = (String) serviceBox.getSelectedItem();

                // Run prediction and show result
                String prediction = classifier.predict(carAge, mileage, owners, service);
                resultLabel.setText("Result: " + prediction.toUpperCase());
            }
        });

        // Make the window visible
        frame.setVisible(true);
    }
}

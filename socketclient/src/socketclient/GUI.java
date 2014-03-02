package socketclient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.*;

/**
 * FormattedTextFieldDemo.java requires no other files.
 *
 * It implements a mortgage calculator that uses four
 * JFormattedTextFields.
 */
public class GUI extends JPanel implements ActionListener {


	//Labels to identify the fields
	private JLabel inputLabel;
	private JLabel outputLabel;

	//Strings for the labels
	protected static final String inputString = "Type your commands";
	private static String outputString= "Output from server";

	//Fields for data entry
	private JTextField inputField;
	private JTextField outputField;

	public GUI() {
		super(new BorderLayout());
		//        double payment = computePayment(amount,
		//                                        rate,
		//                                        numPeriods);

		//Create the labels.
		inputLabel = new JLabel(inputString);
		outputLabel = new JLabel(outputString);

		//Create the text fields and set them up.
		inputField = new JTextField();
		inputField.setText("your input");
		inputField.setColumns(50);
		inputField.setActionCommand(inputString);
		inputField.addActionListener(this);

		outputField = new JTextField();
		outputField.setText("response");
		outputField.setColumns(50);
		outputField.setEditable(false);
		outputField.setForeground(Color.red);

		//Tell accessibility tools about label/textfield pairs.
		inputLabel.setLabelFor(inputField);
		outputLabel.setLabelFor(outputField);

		//Lay out the labels in a panel.
		JPanel labelPane = new JPanel(new GridLayout(0,1));
		labelPane.add(inputLabel);
		labelPane.add(outputLabel);

		//Layout the text fields in a panel.
		JPanel fieldPane = new JPanel(new GridLayout(0,1));
		fieldPane.add(inputField);
		fieldPane.add(outputField);

		//Put the panels in this panel, labels on left,
		//text fields on right.
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		add(labelPane, BorderLayout.CENTER);
		add(fieldPane, BorderLayout.LINE_END);
	}


	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("FormattedTextFieldDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add contents to the window.
		frame.add(new GUI());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (inputString.equals(e.getActionCommand())) {
			JTextField source = (JTextField)e.getSource();
			outputField.setText(source.getText() + "\"");

		}}}
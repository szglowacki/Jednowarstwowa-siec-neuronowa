package MP3_s20872;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static MP3_s20872.MP3_s20872.*;


public class Gui extends JFrame {
    public Gui(List<Perceptron> perceptronList)
    {
        JLabel infoLabel = new JLabel("Write something");
        JButton button = new JButton("Confirm");
        JTextField textField = new JTextField("");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        add(infoLabel);
        add(textField);
        add(button);
        setSize(400,200);
       // pack();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double[] proportionVector = MP3_s20872.getProportionVectorFromText(textField.getText());
                double[] outputTable = new double[perceptronList.size()];
                int counter = 0;
                normaliseVector(proportionVector);
                for(Perceptron p : perceptronList)
                {
                    outputTable[counter] = countSigmoidFunction(proportionVector,p);
                    counter++;
                }
                int index = maximumSelector(outputTable);
                infoLabel.setText("Language: "+perceptronList.get(index).getLanguage() );
                infoLabel.updateUI();


            }
        });

    }
}

package pl.krzysztof.drzazga.view;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import pl.krzysztof.drzazga.model.Leaf;
import pl.krzysztof.drzazga.service.ManageDataService;
import pl.krzysztof.drzazga.model.Vertex;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Frame extends JFrame {
    private JTextArea textArea;
    private JTextField entropyField;
    private JTextField averageField;
    private JTextArea binary;
    private JPanel codesPanel;
    private JPanel graphPanel;
    private ManageDataService service;

    public Frame() {
        BoxLayout frameLayout = new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS);
        this.setLayout(frameLayout);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.service = new ManageDataService();
        this.prepareLeftPanel(dimension);
        this.prepareGraphPanel(dimension);
        this.addText();
        this.add(graphPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void prepareLeftPanel(Dimension dimension){
        JPanel leftPanel = new JPanel();
        BoxLayout leftLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(leftLayout);

        leftPanel.add(this.prepareTextArea());
        Button button = new Button("Wyczyść");
        button.addActionListener(new OnClickHandler());
        leftPanel.add(createStatisticsArea());
        leftPanel.add(button);
        leftPanel.setSize((int)dimension.getWidth()/2, (int)dimension.getHeight() );
        this.add(leftPanel);
    }

    private void prepareGraphPanel(Dimension dimension){
        graphPanel = new JPanel();
        graphPanel.setSize((int)dimension.getWidth()*2/3, (int)dimension.getHeight() );
        BoxLayout layout = new BoxLayout(this.graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(layout);
    }

    private JPanel prepareTextArea() {
        JPanel areasPanel = new JPanel();
        BoxLayout layout = new BoxLayout(areasPanel, BoxLayout.Y_AXIS);
        areasPanel.setLayout(layout);
        this.textArea = new JTextArea(15, 50);
        textArea.addKeyListener(new KeyListener());
        prepareTextComponent(textArea, "Podaj wyrażenie do zakodowania", areasPanel, true);
        this.binary = new JTextArea(15, 50);
        prepareTextComponent(binary, "Zakodowane wyrażenie", areasPanel, false);
        return areasPanel;
    }

    private void addText(){
        String text = "She sells seashells by the seashore," +
                "The shells she sells are seashells, I'm sure" +
                "So if she sells seashells on the seashore," +
                "Then I'm sure she sells seashore shells.";
        this.textArea.setText(text);
        for(int i = 0; i<text.length();i++){
            this.service.addLetter(text.charAt(i));
        }
        displayCodes(this.service.getRoot(), "");
        entropyField.setText(Double.toString(this.service.getEntropy()));
        averageField.setText(Double.toString(this.service.getAverage()));
        pack();
    }

    public class OnClickHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            service.prepareLeafsList();
            textArea.setText("");
            codesPanel.removeAll();
            service.setEntropy(0.0);
            service.setAverage(0.0);
            entropyField.setText("");
            averageField.setText("");
            graphPanel.removeAll();
            service.prepareLeafsList();
        }
    }

    private JPanel createStatisticsArea() {
        JPanel statisticsArea = new JPanel();
        BoxLayout layout = new BoxLayout(statisticsArea, BoxLayout.X_AXIS);
        statisticsArea.setLayout(layout);
        JPanel generalInfoPanel = prepareGeneralInfoPanel();
        codesPanel = new JPanel();
        BoxLayout codesLayout = new BoxLayout(codesPanel, BoxLayout.Y_AXIS);
        codesPanel.setLayout(codesLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        statisticsArea.add(generalInfoPanel, constraints);
        statisticsArea.add(codesPanel, constraints);
        return statisticsArea;
    }

    private JPanel prepareGeneralInfoPanel() {
        JPanel generalInfoPanel = new JPanel();
        BoxLayout layout = new BoxLayout(generalInfoPanel, BoxLayout.Y_AXIS);
        generalInfoPanel.setLayout(layout);
        entropyField = new JTextField(20);
        prepareTextComponent(entropyField, "Entropia", generalInfoPanel, false);
        averageField = new JTextField(20);
        prepareTextComponent(averageField, "Średnia długość słowa kodowego", generalInfoPanel, false);
        return generalInfoPanel;
    }

    private void prepareTextComponent(JTextComponent field, String label, JPanel target, Boolean editable) {
        JLabel componentLabel = new JLabel(label);
        target.add(componentLabel);
        field.setEditable(editable);
        target.add(field);
    }

    public class KeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            reinitializeFields();
            service.addLetter(e.getKeyChar());
            displayCodes(service.getRoot(), "");
            entropyField.setText(Double.toString(service.getEntropy()));
            averageField.setText(Double.toString(service.getAverage()));
            pack();
        }
    }

    private void reinitializeFields(){
        this.service.getPanel().setGraph(new mxGraph());
        graphPanel.removeAll();
        this.service.setEntropy(0.0);
        this.service.setAverage(0.0);
        codesPanel.removeAll();
    }

    private void displayTree(){
        this.graphPanel.removeAll();
        mxGraph graph = this.service.getPanel().getGraph();
        this.graphPanel.add(new mxGraphComponent(graph));
    }

    public void displayCodes(Vertex vertex, String code) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.service.calculateCodes(vertex, code, this.textArea.getText().length(), 0,(int)dimension.getWidth()*2/3,null);
        this.service.getElements().forEach(leaf -> {
            try {
                if (((Leaf) leaf).getSign() != null) {
                    JLabel label = new JLabel(leaf.toString());
                    this.codesPanel.add(label);
                }
            }catch (Exception e){}
        });
        displayTree();
    }
}


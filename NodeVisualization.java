package AI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NodeVisualization extends JFrame {
    NeuralNetwork Nnetwork = new NeuralNetwork(2,2,4);
    public JFrame frame= new JFrame();
    public int timer = 0;
    public NodeVisualization() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame.setTitle("Node Visualization");
        frame.setSize(1650, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNodes(g);
            }
        };

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        JButton reset = new JButton("Reset");

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            double[][][] weights;
            weights = new double[NeuralNetwork.weights.length][][];
            weights[0] = new double[NeuralNetwork.weights[0].length][NeuralNetwork.weights[1].length];
            for (int i = 0; i < NeuralNetwork.weights.length - 3; i++) {
                weights[i + 1] = new double[NeuralNetwork.weights[1].length][NeuralNetwork.weights[1].length];
            }
            weights[NeuralNetwork.weights.length-2] = new double[NeuralNetwork.weights[1].length][1];
            weights[NeuralNetwork.weights.length - 1] = new double[1][1];
            for (int layer = 1; layer < weights.length; layer++) {
            for (int neuron = 0; neuron < weights[layer].length; neuron++) {
            for (int prevNeuron = 0; prevNeuron < weights[layer - 1].length; prevNeuron++) {
                weights[layer - 1][prevNeuron][neuron] = new Random().nextGaussian()*Math.sqrt(1);
            }
            }}
            
            new FileIO().save3DArrayToFile(weights,"AI/learnt.txt");
           System.exit(0);
            frame.dispose();}
        });
       
        JButton play = new JButton("Run");

        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(timer==0)
                 Main.run();
            }
        });

        panel.add(reset);
        panel.add(play);
    }

    private void drawNodes(Graphics g) {
        List<double[]> examples = Arrays.asList(new FileIO().vals2("AI\\training.txt"));
                    

        for (double[] currentExample : examples) {
             Nnetwork.run(true, new double[]{1, currentExample[1]});
            double[][] network = Nnetwork.network;
             int x = 50+(int)Math.floor((examples.indexOf(currentExample))/2)*500;
        for (int layer = 0; layer < network.length; layer++) {
            int y = 100+((examples.indexOf(currentExample)%2)*220);
            if(network[layer]==null){System.out.println(" sfg");return;}
            for (int nodeIndex = 0; nodeIndex < network[layer].length; nodeIndex++) {
                if(network[layer][nodeIndex]==Double.NaN){System.out.println("gjsfg sfg");return;}
                // Draw the node
                g.setColor(Color.BLUE);
                g.fillOval(x, y, 32, 32);  // Node circle
                g.setColor(Color.lightGray);
                g.fillOval(x + 1, y + 1, 30, 30);  // Background circle
                g.setColor(Color.BLACK);
                g.drawString(String.format("%.2f", network[layer][nodeIndex]), x + 4, y + 22);  // Node state
                y += 50;  // Adjust the spacing between nodes
            }
            x += 100;  // Adjust the spacing between layers
        }
       
                    }
        g.fillRect(550+(int)Math.ceil((examples.size()-1)/2)*500, 100, 50, 400);
        g.setColor(Color.lightGray);
        g.fillRect(550+(int)Math.ceil((examples.size()-1)/2)*500, 100, 50, 400-timer*4);
        g.setColor(Color.black); 
        g.drawString(String.valueOf(timer), 570+(int)Math.ceil((examples.size()-1)/2)*500,490-timer*4);
    }

    private void resetPanel() {
        
        frame.repaint(); // Repaint the panel instead of creating a new one
    }

    
    public void updateTimer(int percent){
    timer +=percent;
    System.out.println(timer+"% complete");
    visualise();
    if(timer>=100){timer=0;}
    }
    public void visualise() {
       
        resetPanel();
        initializeUI();
        frame.setVisible(true);

    }
}

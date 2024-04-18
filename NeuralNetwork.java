package AI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NeuralNetwork {
    public static double[][][] weights= new FileIO().vals3("learnt.txt");;
    public double[][] network;

    public NeuralNetwork(int inputs, int hiddenLayers, int hiddenLayerSize) {

 
    }
public void train(){
    int iterations = 10000; // Number of available CPU cores
    ExecutorService executor = Executors.newFixedThreadPool(1);
           
    executor.submit(()->weights = new FileIO().vals3("learnt.txt"));
        
        for (int i = 0; i < iterations; i ++) {
             List<double[]> examples = Arrays.asList(new FileIO().trainingimages());
             Collections.shuffle(examples);

             for (double[] currentExample : examples) {
                 executor.submit(() -> {
                 run(false, new double[]{currentExample[0], currentExample[1]});
                 gradientDescent(currentExample[2], 0.00001);
            });}  
        }
        executor.shutdown();
        try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {}

    executor = null; 
    new FileIO().save3DArrayToFile(NeuralNetwork.weights,"learnt.txt");   
}
public double sigmoid(double x) {
    return 1 / (1 + Math.exp(-x));
}
    public  void run(Boolean test, double[] inputs) {
        
        network = new double[NeuralNetwork.weights.length][];
        for (int i = 0; i < network.length; i++) {
            network[i] = new double[weights[i].length];
        }

        network[weights.length - 1] = new double[1];
        for(int i = 0;i<inputs.length;i++)network[0][i]=inputs[i];
         
        double[][] clone = Arrays.asList(network).toArray(new double[0][]);
        for (int i = 0; i < network.length - 1; i++) {
            for (int j = 0; j < network[i].length; j++) {
                if(Double.isInfinite(network[i][j])||Double.isNaN(network[i][j])){
                    //clone[network.length - 1][0]*=-1;
                    network=clone;
                    System.exit(j);;
                    continue;}
                if(Math.random()<0.01&&i>0&&!test) continue;
                for (int o = 0; o < network[i + 1].length; o++) {
                    network[i + 1][o] += network[i][j] * weights[i][j][o];
                    
                    if( Math.abs(network[i + 1][o])>1000) network[i + 1][o]=1000*( Math.abs(network[i + 1][o])/ network[i + 1][o]);
                }
                    
                } 
               
            }

        }
    

    public void gradientDescent(double trueOutput, double learningRate) {
    double loss = calculateMSELoss(network[network.length - 1][0], trueOutput);
    
    double[][][] gradients = new double[weights.length][][];

    for (int layer = weights.length - 1; layer >= 1; layer--) {
        gradients[layer] = new double[weights[layer].length][];
        if (layer == weights.length - 1) {
            gradients[layer] = new double[1][weights[layer - 1].length];
            for (int i = 0; i < gradients[layer][0].length; i++) {
                gradients[layer][0][i] = Math.max(Math.min(
                network[layer-1][i]*-loss ,1),-1);
                           if(Double.isNaN(gradients[layer][0][i]))System.out.println(network[layer-1][i]+" "+loss);

            }
            continue;
            
        }
        for (int neuron = 0; neuron < weights[layer].length; neuron++) {
            gradients[layer][neuron] = new double[weights[layer - 1].length];
            double delta = 0;
            int i=0;
            for (double[] next : gradients[layer + 1]) {
                delta += (Double.isNaN(next[neuron]/network[layer][neuron])?0:next[neuron]/network[layer][neuron])*weights[layer][neuron][i];
                if(Double.isNaN(delta))System.out.println((next[neuron]+" "+network[layer][neuron]+" "+(next[neuron]/network[layer][neuron])+" "+weights[layer][neuron][i]));
                i++;
            }
            for (int o = 0; o < weights[layer - 1].length; o++) {

                 gradients[layer][neuron][o] = Math.max(Math.min(
                           
                         
                          delta*network[layer-1][o],
                           1), -1);
                           if(Double.isNaN(gradients[layer][neuron][o]))System.out.println(network[layer-1][o]);
                        //System.out.println(gradients[layer][neuron][o] + " " + o+ " "+ neuron+" "+ layer);

            }
        }
    }
    updateWeights(gradients, learningRate);
}


    public void updateWeights(double[][][] gradients, double learningRate) {
        for (int layer = 1; layer < weights.length; layer++) {
            for (int neuron = 0; neuron < weights[layer].length; neuron++) {
                for (int prevNeuron = 0; prevNeuron < weights[layer - 1].length; prevNeuron++) {
                   // System.out.print(0/3);
                  // System.out.println(gradients[layer][neuron][prevNeuron]);
                // if(weights[layer][prevNeuron][neuron]==0)weights[layer][prevNeuron][neuron]=0.0001;
                  weights[layer - 1][prevNeuron][neuron] -= learningRate * gradients[layer][neuron][prevNeuron];
                    if(Double.isNaN(weights[layer - 1][prevNeuron][neuron])){System.out.print(gradients[layer][neuron][prevNeuron]);System.exit(0);}
                }
            }
        }
    }

    public double calculateMSELoss(double predictedOutput, double trueOutput) {
        double error = trueOutput - predictedOutput;
        return error;
    }
}

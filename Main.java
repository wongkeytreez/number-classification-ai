package AI;

import java.sql.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{
  public static NodeVisualization visualize = new NodeVisualization();
  public static void main(String[] args){
      run();
  }
    
  public static void run(){
    System.out.println("starting....");
    long time = System.currentTimeMillis();
    System.out.println(new FileIO().trainingimages()[0][500]);
    // for(int l=0;l<1000;l++){  
    //   new NeuralNetwork(2,2,4).train();
      
    //   // if(l%10==0)visualize.updateTimer(1);
    //   // else visualize.visualise();                                                          
    //   System.out.println("epoch "+ l +" took "+(System.currentTimeMillis()-time)+" seconds");
    //   time = System.currentTimeMillis();
    // }
     visualize.timer=0;  
  }
}
package AI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.w3c.dom.Node;

public class FileIO {
    
          public double[][][] vals3(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            ArrayList<double[][]> array = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.substring(3, line.length() - 3).split("] \\[");
                double[][] row = new double[values.length][];
                for (int i = 0; i < values.length; i++) {
                   
                    String[] strings = values[i].split(" ");
                     row[i]= new double[strings.length];
                    for(int j = 0; j < strings.length; j++)
                    row[i][j] = Double.parseDouble(strings[j]);
                }
                array.add(row);
            }
            return array.toArray(new double[0][][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    public double[][] vals2(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            ArrayList<double[]> array = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.substring(1, line.length() - 1).split(" ");
                double[] row = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                   
                    
                     
                    
                    row[i] = Double.parseDouble(values[i]);
                }
                array.add(row);
            }
            return array.toArray(new double[0][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    public static BufferedImage load(File f) throws IOException
{
    byte[] bytes = Files.readAllBytes(f.toPath());
    try (InputStream is = new ByteArrayInputStream(bytes))
    {
        return ImageIO.read(is);
    }
}
    public double[][] trainingimages(){
    File folder = new File("images");
    File[] listOfFiles = folder.listFiles();
    double[][] training = new double[listOfFiles.length][785];

    for (int file = 0;file<listOfFiles.length;file++) {
    if (listOfFiles[file].isFile()) {
        
        try{BufferedImage image =load(listOfFiles[file]);
            
            for(int y=0;y<image.getWidth();y++)for(int x=0;x<image.getWidth();x++){
            training[file][y*image.getWidth()+x] = new Color(image.getRGB(x, y)).getRed();
            }
            training[file][784]=Double.parseDouble(listOfFiles[file].getName().split(".")[0]);
        }
        catch(Exception e){ };
    }
}  
return  training;
    }
    public void save3DArrayToFile(double[][][] array, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (double[][] row : array) {
                writer.write("[ ");
                for (double elements[] : row) {
                    writer.write("[");
                    for (double element : elements) 
                    writer.write(element + " ");
                    writer.write("] ");
                }
                writer.write("]\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
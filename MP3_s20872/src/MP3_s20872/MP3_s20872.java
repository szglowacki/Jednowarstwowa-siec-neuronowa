package MP3_s20872;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MP3_s20872 {
    public static void main(String [] args)
    {
        File dataDir = new File("data");

       // System.out.println((int)Character.toUpperCase('z'));
       // System.out.println((int)'a'-65);
        double alfa = 0.5;
        List<Perceptron> perceptronsList = new ArrayList<>();
        for (File file : dataDir.listFiles()) {
            if (file.isDirectory())
                perceptronsList.add(new Perceptron(createRandomWagesVector(), getRandomDouble(0, 999), file.getName()));
        }
        //nauka
        int k = 0;
        for(File file : dataDir.listFiles())
            if(file.isDirectory())
            {
                Perceptron currentPerceptron = perceptronsList.get(k);
                for(File text : file.listFiles())
                {
                    if(text.isFile())
                    {
                        double[] proportionVector = getProportionVectorFromText(getStringFromFile(text));
                        for(Perceptron perceptron : perceptronsList)
                        {
                            double net = countNet(proportionVector,perceptron.getWages());
                            int realOutput = returnRealOutput(net,perceptron);
                            int correctOutput = returnCorrectOutput(perceptron,currentPerceptron);

                            if(!checkCorrectnessOfOutput(realOutput,correctOutput))
                            {
                                updateWeightVector(proportionVector, perceptron.getWages(),alfa,correctOutput,realOutput);
                                updateThreshold(perceptron,correctOutput,realOutput,alfa);
                                //net = countNet(proportionVector,perceptron.getWages());
                                // realOutput = returnRealOutput(net,perceptron);
                                // correctOutput = returnCorrectOutput(perceptron,currentPerceptron);
                            }
                        }
                    }
                }
                k++;
            }

        //sprawdzanie
        for(File file : dataDir.listFiles())
        {
            if(file.isDirectory())
            {
                for(File textFile : file.listFiles())
                {
                   double[] proportionVector = getProportionVectorFromText(getStringFromFile(textFile));
                   double[] outputTable = new double[perceptronsList.size()];
                   int counter = 0;
                   normaliseVector(proportionVector);
                  // System.out.println(textFile.getName());
                   for(Perceptron p : perceptronsList)
                   {
                      outputTable[counter] = countSigmoidFunction(proportionVector,p);
                      counter++;
                   }
                    int index = maximumSelector(outputTable);

                   System.out.println(textFile.getName() + " Wyliczony język: "+ perceptronsList.get(index).getLanguage() +" Prawdziwy język: "+file.getName());

                }
            }
        }

        JFrame frame = new Gui(perceptronsList);
        frame.setVisible(true);
    }

       public static double[] createRandomWagesVector()
       {
           double[] result = new double[26];
           for(int i = 0; i < result.length; i++)
           {
               result[i] = getRandomDouble(0,999);
           }
           normaliseVector(result);
           return result;
       }

       public static double getRandomDouble(double min,double max)
       {
           return (Math.random()*(max-min+1))+min;
       }

       public static String getStringFromFile(File textFile)
       {
           StringBuilder stringBuilder = new StringBuilder();
           try {
               Scanner fileScanner = new Scanner(textFile);
               while (fileScanner.hasNextLine())
               {
                   stringBuilder.append(fileScanner.nextLine());
               }
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }

           return stringBuilder.toString();
       }

       public static double[] getProportionVectorFromText(String text)
       {
           double[] proportionVector = new double[26];
           for(int i = 0; i < text.length(); i++)
           {
            addOrRejectChar(text.charAt(i),proportionVector);
           }
           normaliseVector(proportionVector);
           return proportionVector;
       }

       public static void addOrRejectChar(char sign, double[] proportionVector)
       {
           int charNumber = (int) Character.toUpperCase(sign) ;
           if(charNumber >=65 && charNumber <=90)
           {
               proportionVector[charNumber-65] ++;
           }
       }

       public static double countNet(double[] proportionVector, double[]perceptronWeightVector)
       {
           double net = 0;

           for(int i = 0; i < proportionVector.length; i++)
           {
               net += (proportionVector[i] * perceptronWeightVector[i]);
           }

           return net;
       }

       public static int returnRealOutput(double net, Perceptron perceptron)
       {
           if(net > perceptron.getThreshold()) return 1;
           else return -1;
       }

       public static int returnCorrectOutput(Perceptron perceptron, Perceptron currentLanguagePerceptron)
       {
           if(currentLanguagePerceptron.getLanguage().equals(perceptron.getLanguage())) return 1;
           else return -1;


       }

       public static boolean checkCorrectnessOfOutput(int realOutput, int correctOutput )
       {
           return realOutput == correctOutput;
       }

       public static void updateWeightVector(double[] proportionVector, double[] weightVector, double a , int d, int y)
       {
           double[] tmpVector = new double[26];
           for(int i = 0; i < weightVector.length; i++)
           {
               tmpVector[i] = (d-y) * a * proportionVector[i];
               weightVector[i] = tmpVector[i] + weightVector[i];
           }
           normaliseVector(weightVector);
       }

       public static void updateThreshold(Perceptron perceptron, int d, int y, double a)
       {
           perceptron.setThreshold(perceptron.getThreshold()+ (d-y) * a * (-1));
       }

       public static double countSigmoidFunction(double[] proportionVector, Perceptron perceptron)
       {
           double net = countNet(proportionVector,perceptron.getWages());
            //System.out.println(1/(1+Math.pow(Math.E,(-1*net))));

           return (1/(1+Math.pow(Math.E,(-1*net))));

       }

       public static void normaliseVector(double[] vector)
       {
            double sum = 0;
            for(int i = 0; i < vector.length; i++)
            {
                sum += Math.pow(vector[i],2);
            }
            sum = Math.sqrt(sum);
            for( int i = 0; i < vector.length; i++)
            {
                vector[i] = vector[i]/sum;
            }

       }

       public static int maximumSelector(double[] tab )
       {
           double max = 0;
           int maxIndex = 0;
           for(int i = 0; i < tab.length; i++)
           {
               if (tab[i] > max)
               {
                   max = tab[i];
                   maxIndex = i;

               }
           }

           for(int i = 0; i < tab.length; i++)
           {
               if(i == maxIndex) tab[i] = 1;
               else tab[i] = 0;
           }
           return maxIndex;
       }


    }


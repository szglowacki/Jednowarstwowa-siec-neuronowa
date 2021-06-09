package MP3_s20872;

public class Perceptron {
    private String language;
    private double[] wages;
    private double threshold;
    public Perceptron(double [] wages, double threshold, String language)
    {
        this.language = language;
        this.wages = wages;
        this.threshold = threshold;
    }

    public String getLanguage()
    {
        return language;
    }

    public double[] getWages()
    {
        return wages;
    }
    public void setWages()
    {
       this.wages = wages;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public void setThreshold(double threshold)
    {
        this.threshold = threshold;
    }
}

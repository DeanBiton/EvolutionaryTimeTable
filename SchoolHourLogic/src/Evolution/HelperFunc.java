package Evolution;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class HelperFunc {
    public static int getRandomNumber(int first, int last) {
        return ThreadLocalRandom.current().nextInt(first,last+1);
    }

    public static ArrayList<String> getParameters(String configuration)
    {
        ArrayList<String> arr = new ArrayList<>();

        int startIndex = 0;
        int endIndex = 0;
        while(true){

            startIndex = configuration.indexOf("=",endIndex);
            endIndex = (configuration.indexOf(",",startIndex)>0)?configuration.indexOf(",",startIndex):configuration.length();
            if(startIndex != -1)
                arr.add(configuration.substring(startIndex + 1,endIndex));
            else
                break;
        }
        return arr;
    }
}

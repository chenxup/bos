package utils;

import java.util.Random;

public class GetRandomcode {
    public static String getCode(Integer start, Integer end){
        Random random = new Random();
        Integer rs = random.nextInt(end-start+1)+start;
        return rs.toString();
    }


}

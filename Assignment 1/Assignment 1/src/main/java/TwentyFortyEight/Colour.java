/*
Haochen Liu
hliu0010
540243105
*/

package TwentyFortyEight;

import java.util.Map;
import java.util.HashMap;

public class Colour {
    
    public static class RGB {
        public int R, G, B;
        public RGB(int R, int G, int B){
            this.R=R;
            this.G=G;
            this.B=B;
        }
    }

    private static Map<Integer, RGB> background, number;
    
    public static void setup(){
        background=new HashMap<>();
        number=new HashMap<>();

        //number 2  
        background.put(2, new RGB(238, 228, 218));
        number    .put(2, new RGB(124, 109, 96));

        //number 4 
        background.put(4, new RGB(236, 224, 202));
        number    .put(4, new RGB(116, 110, 101));

        //number 8 
        background.put(8, new RGB(242, 117, 121));
        number    .put(8, new RGB(255, 255, 255));

        //number 16
        background.put(16, new RGB(244, 150, 99));
        number    .put(16, new RGB(255, 255, 255));

        //number 32  
        background.put(32, new RGB(244, 124, 97));
        number    .put(32, new RGB(255, 255, 255));

        //number 64
        background.put(64, new RGB(244, 94, 58));
        number    .put(64, new RGB(255, 255, 255));

        //number 128  
        background.put(128, new RGB(237, 206, 115));
        number    .put(128, new RGB(255, 255, 255));

        //number 256
        background.put(256, new RGB(237, 204, 99));
        number    .put(256, new RGB(255, 255, 255));

        //number 512
        background.put(512, new RGB(236, 200, 81));
        number    .put(512, new RGB(255, 255, 255));

        //number 1024
        background.put(1024, new RGB(237, 200, 60));
        number    .put(1024, new RGB(255, 255, 255));

        //number 2048 
        background.put(2048, new RGB(236, 193, 52));
        number    .put(2048, new RGB(255, 255, 255));
        
        //number 4096 
        background.put(4096, new RGB(236, 193, 52));
        number    .put(4096, new RGB(255, 255, 255));
    }
    public static void setbackground(App app, int num){
        RGB tmp=background.get(num);
        app.fill(tmp.R, tmp.G, tmp.B);
    }
    public static void setnumber(App app, int num){
        RGB tmp=number.get(num);
        app.fill(tmp.R, tmp.G, tmp.B);
    }
}

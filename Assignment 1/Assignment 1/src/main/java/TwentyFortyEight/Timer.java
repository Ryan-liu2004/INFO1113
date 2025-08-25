/*
Haochen Liu
hliu0010
540243105
*/

package TwentyFortyEight;

public class Timer{
    public static final int Timer_w = App.GRID_SIZE*App.CELLSIZE;
    public static final int Timer_h = App.CELLSIZE;
    public final int x=0;
    public final int y=0;
    public float start_time;

    public Timer(App app){
        start_time=app.millis();
    }

    void draw(App app){
        app.stroke(156, 139, 124);
        app.fill(189, 172, 151);
        app.rect(x, y, Timer_w, Timer_h);

        app.fill(0,0,0);
        String time=String.format("%.1f", (app.millis()-start_time)/1000);
        if(App.GRID_SIZE==2) {
            app.textSize(25);
            app.text("Time: "+time+"s", 
                Timer_w-1.8f*App.CELLSIZE, (y+0.6f)*App.CELLSIZE);
        }
        else app.text("Time: "+time+"s", 
            Timer_w-((time.length()-2)*0.25f+2f)*App.CELLSIZE,   //使timer靠右
            (y+0.65f)*App.CELLSIZE);
    }
}
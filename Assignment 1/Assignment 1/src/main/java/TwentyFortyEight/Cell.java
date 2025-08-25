/*
Haochen Liu
hliu0010
540243105
*/

package TwentyFortyEight;

public class Cell {

    public final int pre_w=0;
    public final int pre_h=Timer.Timer_h;    //记录当前部分前面还有多少需要draw
    private float x, y;
    private int value;

    public static void print(String tmp) {System.out.print(tmp);}
    public static void println(String tmp) {System.out.println(tmp);}

    public Cell(float x, float y) {
        this.x=x;
        this.y=y;
        this.value=0;
    }

    public int getval(){return value;}
    public void setval(int val){value=val;}

    public void place() {
        this.value=(App.random.nextInt(2)+1)*2;
    }

    /**
     * This draws the cell
     */
    private float getx(float add_x){
        return (x+add_x)*App.CELLSIZE+pre_w;
    }
    private float getx(){
        return getx(0);
    }
    private float gety(float add_y){
        return (y+add_y)*App.CELLSIZE+pre_h;
    }
    private float gety(){
        return gety(0);
    }
    public void draw(App app, boolean flag, float move_offset_x, float move_offset_y) {
        //flag: true draw number cell;  false draw background cell
        if(!flag) app.stroke(156, 139, 124);
        else app.noStroke();

        if (app.mouseX > getx() && app.mouseX < getx(1)
            && app.mouseY > gety() && app.mouseY < gety(1))
            app.fill(232, 207, 184);
        else if(value!=0)
            Colour.setbackground(app, value);
        else
            app.fill(189, 172, 151);
            
        //when draw number, adjust size
        int offset=flag?App.StrokeWeight:0;
        if(!(flag && value==0))   //not null number
            app.rect(getx()+ offset/2f+ move_offset_x, 
                     gety()+ offset/2f+ move_offset_y, 
                     App.CELLSIZE-offset, App.CELLSIZE-offset);

        if (value>0) {
            Colour.setnumber(app, value);

            String tmp=String.valueOf(value);
            app.textSize(App.TextSize-(tmp.length()/2)*2);
            app.text(tmp,
                getx(-(tmp.length()/2f)*0.2f+0.5f)+ move_offset_x,  //0.4f
                gety(0.65f)+ move_offset_y);
        }
    }

}
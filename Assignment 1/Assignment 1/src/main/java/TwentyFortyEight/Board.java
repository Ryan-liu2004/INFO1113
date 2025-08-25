/*
Haochen Liu
hliu0010
540243105
*/

package TwentyFortyEight;

public class Board {
    public static final int Move_FPS=App.FPS/2;
    private Cell[][] board, backboard;  //number cell;  background cell
    private int n, move_count;
    private int[][] cache_val;
    private float[][] move_cache_x, move_cache_y;

    public Board(int GRID_SIZE){
        n=GRID_SIZE;
        move_count=0;
        this.board=new Cell[n][n];
        this.backboard=new Cell[n][n];
        this.move_cache_x=new float[n][n];
        this.move_cache_y=new float[n][n];
        this.cache_val=new int[n+10][n+10];  //provent overindex
    }

    private void release_cache(){
        int i,j;
        for(i=0;i<n;i++)
            for(j=0;j<n;j++){
                move_cache_x[i][j]=0;
                move_cache_y[i][j]=0;
                cache_val[i][j]=0;
            }
        move_count=0;
    }

    public void setup(){
        int i,j;
        for(i=0;i<n;i++)
            for(j=0;j<n;j++){
                board[i][j]=new Cell(j, i);
                backboard[i][j]=new Cell(j, i);
            }
        release_cache();

        /*board[0][0].setval(2);
        board[0][1].setval(4);
        board[0][2].setval(8);
        board[0][3].setval(16);
        board[1][0].setval(2);
        board[1][1].setval(64);
        board[1][2].setval(128);
        board[1][3].setval(256);
        board[2][0].setval(4);
        board[2][1].setval(1024);
        board[2][2].setval(2048);
        board[2][3].setval(4096);*/
    }

    private boolean is_same(){
        int i,j;
        for(i=0;i<n;i++) 
            for(j=0;j<n;j++) 
                if(cache_val[i][j]!=board[i][j].getval()) return false;
        return true;
    }

    public void up(){
        int i,j,k;
        //release_cache();

        for(j=0;j<n;j++){
            boolean merge_flag=true;
            for(i=0;i<n;i++){
                if(board[i][j].getval()==0) continue;
                for(k=i-1;k>=0;k--) if(cache_val[k][j]!=0) break;
                if(k!=-1 && cache_val[k][j]==board[i][j].getval() && merge_flag) {
                    //k=k;
                    merge_flag=false;
                    cache_val[k][j]*=2;
                }
                else{
                    k++;
                    merge_flag=true;
                    cache_val[k][j]=board[i][j].getval();
                }
                move_cache_y[i][j]=(k-i)*App.CELLSIZE/1f/Move_FPS;
            }
        }
        move_count=Move_FPS-1;
        if(is_same()) release_cache();
    }
    public void down(){
        int i,j,k;
        //release_cache();

        for(j=0;j<n;j++){
            boolean merge_flag=true;
            for(i=n-1;i>=0;i--){
                if(board[i][j].getval()==0) continue;
                for(k=i+1;k<n;k++) if(cache_val[k][j]!=0) break;
                if(k!=n && cache_val[k][j]==board[i][j].getval() && merge_flag) {
                    //k=k;
                    merge_flag=false;
                    cache_val[k][j]*=2;
                }
                else{
                    k--;
                    merge_flag=true;
                    cache_val[k][j]=board[i][j].getval();
                }
                move_cache_y[i][j]=(k-i)*App.CELLSIZE/1f/Move_FPS;
            }
        }
        move_count=Move_FPS-1;
        if(is_same()) release_cache();
    }
    public void left(){
        int i,j,k;
        //release_cache();

        for(i=0;i<n;i++){
            boolean merge_flag=true;
            for(j=0;j<n;j++){
                if(board[i][j].getval()==0) continue;
                for(k=j-1;k>=0;k--) if(cache_val[i][k]!=0) break;
                if(k!=-1 && cache_val[i][k]==board[i][j].getval() && merge_flag) {
                    //k=k;
                    merge_flag=false;
                    cache_val[i][k]*=2;
                }
                else{
                    k++;
                    merge_flag=true;
                    cache_val[i][k]=board[i][j].getval();
                }
                move_cache_x[i][j]=(k-j)*App.CELLSIZE/1f/Move_FPS;
            }
        }
        move_count=Move_FPS-1;
        if(is_same()) release_cache();
    }
    public void right(){
        int i,j,k;
        //release_cache();

        for(i=0;i<n;i++){
            boolean merge_flag=true;
            for(j=n-1;j>=0;j--){
                if(board[i][j].getval()==0) continue;
                for(k=j+1;k<n;k++) if(cache_val[i][k]!=0) break;
                if(k!=n && cache_val[i][k]==board[i][j].getval() && merge_flag) {
                    //k=k;
                    merge_flag=false;
                    cache_val[i][k]*=2;
                }
                else{
                    k--;
                    merge_flag=true;
                    cache_val[i][k]=board[i][j].getval();
                }
                move_cache_x[i][j]=(k-j)*App.CELLSIZE/1f/Move_FPS;
            }
        }
        move_count=Move_FPS-1;
        if(is_same()) release_cache();
    }

    public void place(App app, int x, int y){
        if(board[x][y].getval()==0) board[x][y].place();
        check(app);
    }

    public void generate_new(){
        int tmp=(App.random.nextInt(n*n));
        int x=tmp/n, y=tmp%n;
        while(board[x][y].getval()!=0) {
            tmp=(App.random.nextInt(n*n));
            x=tmp/n;
            y=tmp%n;
        }
        board[x][y].place();
    }

    private boolean check(App app){
        int i,j;
        for(i=0;i<n;i++) for(j=0;j<n;j++) if(board[i][j].getval()==0) return false;
        for(i=0;i<n;i++){
            for(j=0;j<n;j++) 
                if((j!=n-1 && board[i][j].getval()==board[i][j+1].getval()) ||
                   (i!=n-1 && board[i][j].getval()==board[i+1][j].getval())) return false;
        }
        app.setendflag();
        draw(app);
        display_end(app);
        return true;
    }

    private void display_end(App app){
        app.fill(156, 139, 124);
        float w=App.CELLSIZE*(2+(n-2)/2), h=App.CELLSIZE;
        float x=n/2f*App.CELLSIZE-w/2f, y=n/2f*App.CELLSIZE+Timer.Timer_h-h/2f;
        app.rect(x, y, w, h);

        app.fill(255, 255, 255);
        app.textSize(n<4?App.TextSize-10:App.TextSize);
        app.text("GAME OVER", x+x/2f, y+h/1.5f);
    }

    public void draw(App app){
        int i,j;
        //draw back first
        for(i=0;i<n;i++)
            for(j=0;j<n;j++)
                backboard[i][j].draw(app, false, 0, 0);

        //Set move cache
        if(move_count==Move_FPS-1){   //最后一帧合并了
            app.setmoveflag(true);
        }
        //draw number
        for(i=0;i<n;i++)
            for(j=0;j<n;j++)
                if(app.getmoveflag())
                    board[i][j].draw(app, true, 
                        (Move_FPS-move_count)*move_cache_x[i][j],
                        (Move_FPS-move_count)*move_cache_y[i][j]);
                else     //when caches are calcuating
                    board[i][j].draw(app, true, 0, 0);
        
        if(move_count>0) move_count--;
        if(move_count==0 && app.getmoveflag()){
            for(i=0;i<n;i++)
                for(j=0;j<n;j++)
                    board[i][j].setval(cache_val[i][j]);
            release_cache();

            generate_new();
            app.setmoveflag(false);
            check(app);
        }
    }
}

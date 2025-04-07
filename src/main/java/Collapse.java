
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class Collapse extends WinApp implements ActionListener {

    public static final int nC = 13, nR = 15;
    public static final int W = 60, H = 40;
    public static int xM = 100, yM = 100;
    public static Timer timer;

    public static Color[] color = {Color.LIGHT_GRAY, Color.CYAN, Color.GREEN,
    Color.YELLOW, Color.RED, Color.PINK};
    public static int[][] grid = new int[nC][nR];
    public static int nColor = 3;
    public static int bricksRemaining;

    public static final int screenWidth = 1000;
    public static final int screenHeight = 750;

    public Collapse() {
        super("Collapse", screenWidth, screenHeight);
        startNewGame();
        timer = new Timer(20,this);
        timer.start();
    }

    public static void startNewGame(){
        rndColors(nColor);
        nColor++;
        if (nColor == color.length){nColor = 3;}
        xM = 100;
        bricksRemaining = nR * nC;
    }

    public static void showRemaining(Graphics g){
        String str = "Bricks remaining: " + bricksRemaining;
        if (noMorePlays()){str += "No More Plays Left!";}
        g.setColor(Color.BLACK);
        g.drawString(str, 50, 25);
    }

    public void paintComponent (Graphics g) {
        g.setColor(color[0]);
        g.fillRect(0, 0, 5000, 5000);
        showGrid(g);
        bubble();
        if(slideCol()){xM += W/2;}
        showRemaining(g);
    }

    public void mouseClicked (MouseEvent me){
        int x = me.getX(), y = me.getY();
        if (x < 20 && y < 20){startNewGame();}
        if (x < xM || y < yM){return;}
        int r = r(y), c = c(x);
        if (r < nR && c < nC){
            crAction(c,r);
        }
        repaint();
    }

    public static Boolean noMorePlays(){
        for (int r = 0; r <nR; r++){
            for (int c = 0; c <nC; c++){
                if (infectable(c,r)){return false;}

            }
        }
        return true;
    }

    public static void crAction(int c, int r) {
        //System.out.println("("+c+","+r+")");
        if (infectable(c,r)){infect(c,r,grid[c][r]);}
    }

    public static boolean infectable(int c, int r){
        int v = grid[c][r];
        if (v == 0){return false;}
        if (r > 0 && grid[c][r-1] == v){return true;}
        if (c > 0 && grid[c-1][r] == v){return true;}
        if (r < nR -1){if (grid[c][r+1] == v){return true;}}
        if (c < nC -1){if (grid[c+1][r] == v){return true;}}

        return false;

        }
    public static void infect(int c, int r, int v) {
        if (grid[c][r] != v){return;}
        grid[c][r] = 0;
        bricksRemaining -- ;
        if (r > 0){infect(c,r - 1, v);}
        if (c > 0){infect(c-1,r,v);}
        if (r < nR - 1){infect(c, r+1, v);}
        if (c < nC -1){infect(c+1, r, v);}
    }

    public static int rowCanBubble(int c){
        for (int r = nR-1; r > 0; r--){
            if (grid[c][r] == 0 && grid[c][r-1] != 0){return r;}
        }
        return nR;
    }

    public static void bubble(){
        for (int c = 0; c < nC; c++){
            int r = rowCanBubble(c);
            if (r < nR){
                grid[c][r] = grid[c][r-1];
                grid[c][r-1] = 0;
            }

        }
    }

    public static boolean colIsEmpty(int c){
        for (int r = 0; r < nR; r++){
            if (grid[c][r] != 0){return false;}
        }
        return true;
    }

    public static void swapCol(int c){  // c is not empty, c-1 is empty
        for (int r = 0; r < nR; r++){
            grid[c-1][r] = grid[c][r];
            grid[c][r] = 0;
        }
    }

    public static boolean slideCol(){
        boolean res = false;
        for (int c = 1; c < nC; c++) {
            if (colIsEmpty(c-1) && !colIsEmpty(c)){
                swapCol(c);
                res = true;
            }
        }
        return res;
    }

    public static void rndColors (int k){
        for (int c = 0; c < nC; c++) {
            for (int r = 0; r < nR; r++) {
                grid[c][r] = 1 + G.rnd(k);
            }
        }
    }

    public void showGrid(Graphics g){
        for (int c = 0; c < nC; c++) {
            for (int r = 0; r < nR; r++) {
                g.setColor(color[grid[c][r]]);
                g.fillRect(x(c),y(r),W,H);
            }
        }
    }

    public static int x(int c){return xM + c * W;} // unsafe fixed in mouseClicked
    public static int y(int r){return yM + r * H;}

    private static int c(int x){return (x - xM)/W;}
    private static int r(int y){return (y - yM)/H;}



    public static void main (String [] args) {
        PANEL = new Collapse();
        WinApp.launch();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package blobjump.blobjump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.image.BufferedImage;






/**
 *
 * @author beomagi
 */

public class Blobjump {
    public static int maxx=1200;
    public static int maxy=800;
    public static int ground=640;
    public static int maxsquare=Math.min(maxx, maxy);
    public static int minDx=(maxx-maxsquare)/2;
    public static int maxDx=(maxx+maxsquare)/2;
    public static int minDy=(maxy-maxsquare)/2;
    public static int maxDy=(maxy+maxsquare)/2;
    public static int level=0;
    public static int levelblockrowlengthmax=0;
    public static int levelblockrowcount=0;
    public static double blockwidth=0;
    public static double blockheight=0;
    public static double gravity=0;
    public static double ninjax=0;
    public static double ninjay=0;
    public static double ninjaxvelocity=0;
    public static double ninjayvelocity=0;
    public static double ninjaxmaxvelocity=0;
    public static double ninjaymaxvelocity=0;
    public static double ninjasize=0;
    public static int ninjacanjump=0;

    
    public static String Level1=
            "0000000000000000000000000000000000000000,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0                            00000000000,"+
            "0                            0         0,"+
            "0                            0         0,"+
            "0                            0         0,"+
            "0                            0         0,"+
            "0  0                         0   000   0,"+
            "0 s0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                         0   0 0   0,"+
            "0  0                             0 0   0,"+
            "0  0                             0 0   0,"+
            "0  0000000000000000000000000000000 0   0,"+
            "0                                  0   0,"+
            "0                                      0,"+
            "0                                      0,"+
            "0000000000000000000000000000000000000000";
    
    
    public double[] rotPointOverPoint(double px,double py,double cx,double cy,double rotangle){
        double newX = cx + (px - cx) * Math.cos(rotangle) - (py - cy) * Math.sin(rotangle);
        double newY = cy + (py - cx) * Math.sin(rotangle) + (py - cy) * Math.cos(rotangle);
        return new double[]{newX,newY};  
    }
    
    
    public static class PaintPanel extends JPanel implements MouseListener {
        public static BufferedImage bgImage;
        public static ArrayList<String> slevels = new ArrayList<>();
        public static ArrayList<double[]> blocks = new ArrayList<>();//type,x1,y1,x2,y2,v1,v2,v3,v4

        public static void drawninja(Graphics2D g){
            g.setColor(new Color(222,30,30));
            g.fillOval((int)(ninjax-ninjasize/2), (int)(ninjay-ninjasize/2), (int)ninjasize, (int)ninjasize);
        }
        public static void ninjagravity(){
            // current position = ninjax, ninjay, bottom is ninjay + ninjasize
            ninjayvelocity+=gravity;
            if (ninjayvelocity>ninjaymaxvelocity) {ninjayvelocity=ninjaymaxvelocity;}
            if (ninjayvelocity<-ninjaymaxvelocity) {ninjayvelocity=-ninjaymaxvelocity;}
            ninjay+=ninjayvelocity;
            for (int bc=0;bc<blocks.size();bc++){
                double[] blockinfo = blocks.get(bc);
                double blocktype=blockinfo[0];
                double blockx1=blockinfo[1];
                double blocky1=blockinfo[2];
                double blockx2=blockinfo[3];
                double blocky2=blockinfo[4];
                if ((ninjax>blockx1-0.2*ninjasize)&&(ninjax<blockx2+0.2*ninjasize)) {
                    if (ninjay<blocky1){
                        if (ninjay+ninjasize/2+ninjayvelocity>blocky1){
                            ninjay=blocky1-ninjasize/2;
                            ninjayvelocity=0;
                            ninjacanjump=1;
                        }
                    }
                }
            }
        }
        
        public void background(Graphics g){
            g.drawImage(bgImage, 0, 0, this);
        }
        
        public static void setlevel(String alevel){
            String[] salevel = alevel.split(",");
            levelblockrowlengthmax=0;
            levelblockrowcount=salevel.length;
            blocks = new ArrayList<>();
            for (int a=0; a<levelblockrowcount;a++){
                levelblockrowlengthmax=Math.max(levelblockrowlengthmax,salevel[a].length());
            }
            blockwidth=maxsquare/(levelblockrowlengthmax*1.0);
            blockheight=maxsquare/(levelblockrowcount*1.0);
            ninjasize=(blockwidth+blockheight)/2;
            for (int row=0; row<levelblockrowcount;row++){
                for (int col=0; col<salevel[row].length();col++){
                    char bloktype=salevel[row].charAt(col);
                    double blokx1=(col/(levelblockrowlengthmax*1.0))*maxsquare;
                    double blokx2=blokx1+blockwidth;
                    double bloky1=(row/(levelblockrowcount*1.0))*maxsquare;
                    double bloky2=bloky1+blockheight;
                    blokx1+=minDx;
                    blokx2+=minDx;
                    bloky1+=minDy;
                    bloky2+=minDy;
                    if (bloktype=='0'){
                        System.out.println(0 + ", " + blokx1 + ", " + bloky1 + ", " + blokx2 + ", " + bloky2);
                        blocks.add(new double[]{0,blokx1,bloky1,blokx2,bloky2});
                    }
                    if (bloktype=='s'){
                        ninjax=blokx1;
                        ninjay=bloky1;
                    }
                }
            }
        }
        
        
        public static void drawlevel(Graphics2D g){
            int cnt=0;
            for (int bc=0;bc<blocks.size();bc++){
                cnt+=1;
                //if(cnt>50) {break;}
                double[] blockinfo = blocks.get(bc);
                double blocktype=blockinfo[0];
                double blockx1=blockinfo[1];
                double blocky1=blockinfo[2];
                double blockx2=blockinfo[3];
                double blocky2=blockinfo[4];
                //System.out.println(blocktype + ", " + blockx1 + ", " + blocky1 + ", " + blockx2 + ", " + blocky2);
                g.setColor(new Color(222,222,222));
                g.fillRect((int)blockx1, (int)blocky1, (int)(0.5+blockx2-blockx1), (int)(blocky2-blocky1));
                g.setColor(new Color(144,144,144));
                g.drawRect((int)blockx1, (int)blocky1, (int)(0.5+blockx2-blockx1), (int)(blocky2-blocky1));
            }
        }


        
        public void inits(){
            maxx=getWidth();
            maxy=getHeight();
            ground=(int) (0.9*maxy);
            maxsquare=Math.min(maxx, maxy);
            minDx=(maxx-maxsquare)/2;
            maxDx=(maxx+maxsquare)/2;
            minDy=(maxy-maxsquare)/2;
            maxDy=(maxy+maxsquare)/2;
            blitbackground();
            level=0;
            slevels.add(Level1);
            levelblockrowlengthmax=0;
            levelblockrowcount=0;
            setlevel(slevels.get(level));
            gravity=maxsquare*0.0001;
            ninjaxmaxvelocity=maxsquare/100;
            ninjaymaxvelocity=maxsquare/100;
        }

        //initialize static background to prevent waste cpu
        public static void blitbackground(){
            bgImage = new BufferedImage(maxx, maxy, BufferedImage.TYPE_INT_RGB);
            Graphics g = bgImage.getGraphics();
            //sky
            for (int a=0; a<=ground; a++){
                double frcrem=(ground-a)/(ground*1.0);
                double frcreminv=1-frcrem;
                double crs=0;
                double cgs=0;
                double cbs=200;
                double crf=220;
                double cgf=220;
                double cbf=100;
                int cr = (int) (crs*frcrem + crf*frcreminv);
                int cg = (int) (cgs*frcrem + cgf*frcreminv);
                int cb = (int) (cbs*frcrem + cbf*frcreminv);
                g.setColor(new Color(cr,cg,cb));
                g.drawLine(0, a, maxx, a);
            }
            int grounddiff=maxy-ground;
            for (int a=0; a<=grounddiff; a++){
                double frcrem=(grounddiff-a)/(grounddiff*1.0);
                double frcreminv=1-frcrem;
                double crs=150;
                double cgs=100;
                double cbs=30;
                double crf=100;
                double cgf=50;
                double cbf=0;
                int cr = (int) (crs*frcrem + crf*frcreminv);
                int cg = (int) (cgs*frcrem + cgf*frcreminv);
                int cb = (int) (cbs*frcrem + cbf*frcreminv);
                g.setColor(new Color(cr,cg,cb));
                g.drawLine(0, a+ground, maxx, a+ground);
            }
            g.setColor(new Color(10,20,40));
            g.fillRect(minDx, minDy, maxsquare, maxsquare);
        }


        //for animation
        public PaintPanel(Color backgroundColor) {
            setBackground(backgroundColor);
            addMouseListener(this);
            // Timer to update position every 30ms
            Timer timer = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint(); // Request panel to redraw
                    //animation variables can be calculated here
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ninjagravity();
            //draw stuff here
            background(g);
            drawlevel(g2d);
            drawninja(g2d);
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int button = e.getButton();
            if (button == MouseEvent.BUTTON1){//left
                if (ninjacanjump==1){
                    ninjayvelocity=-ninjaymaxvelocity/2.5;
                    ninjacanjump=0;
                }
            }
            if (button == MouseEvent.BUTTON3){//right
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

    }




    public static void main(String[] args) {
        JFrame canvasish = new JFrame();
        canvasish.setTitle("BlobJump");
        canvasish.setUndecorated(true); // Removes title bar and borders
        canvasish.setBackground(Color.BLACK);
        canvasish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel panel = new PaintPanel(Color.WHITE);
        //panel.setPreferredSize(new Dimension(PaintPanel.maxx, PaintPanel.maxy));//size fix for linux swing
        Container pane = canvasish.getContentPane(); // Fixing theGUI reference
        pane.add(panel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
         if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(canvasish);
        } else {
            // fallback if fullscreen isn't supported
            canvasish.setExtendedState(JFrame.MAXIMIZED_BOTH);
            canvasish.setVisible(true);
        }
        panel.inits();
    }
}

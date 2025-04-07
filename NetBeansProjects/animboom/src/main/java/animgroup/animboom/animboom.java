/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package animgroup.animboom;

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


class PaintPanel extends JPanel implements MouseListener {
    public static int maxx=1200;
    public static int maxy=800;
    public static int ground=640;
    public static int blok=10;
    public static double oggravity=2;
    public static double gravity=2;
    public static int boom=0;
    double boomforce=170;
    double boomvertratio=2;
    double boomrand=0.35;
    double dsf=500; //force curve gradient
    public static int revcounter=100;
    public static BufferedImage bgImage;
    
    
    //lets store x/y/rot and color
    public static ArrayList<double[]> partlist = new ArrayList<>();

    public static void bloklineh(double x1,double y1,double x2,int r,int g,int b){
        if (x1>x2) {double swp=x1;x1=x2;x2=swp;}//swap if needed
        for (double x=x1; x<x2; x+=blok){
            addblock(x,y1,0,r,g,b);
        }
    }
    public static void bloklinev(double x1,double y1,double y2,int r,int g,int b){
         if (y1>y2) {double swp=y1;y1=y2;y2=swp;}
        for (double y=y1; y<y2; y+=blok){
            addblock(x1,y,0,r,g,b);
        }
    }
    
    public static void blokinit2(){
        //house base
        int xsize=60;
        int ysize=30;
        for (int y=0; y<ysize; y++){
            int sx1=(int)((maxx/2.0)-((xsize*blok)/2.0));
            int sx2=(int)((maxx/2.0)-((xsize*blok)/2.0) + xsize * blok);
            int sy=(int)((ground-ysize*blok)+y*blok+blok/2);
            bloklineh(sx1,sy,sx2,250,250,250);
        }
        //roof
        for (int y=0; y<ysize/2; y++){
            int sx1=(int)((maxx/2.0)-((xsize*blok)/2.0) + blok*(ysize/2-y));
            int sx2=(int)((maxx/2.0)-((xsize*blok)/2.0) + xsize * blok - blok*(ysize/2-y));
            int sy=(int)((ground-1.5*ysize*blok)+y*blok+blok/2);
            bloklineh(sx1,sy,sx2,250,0,0);
        }

        //door
        for (int y=0; y<ysize/2; y++){
            int sx1=(int)((maxx/2.0)-((xsize*blok)/8.0));
            int sx2=(int)((maxx/2.0)+((xsize*blok)/8.0));
            int sy=(int)((ground-(ysize/2)*blok)+y*blok+blok/2);
            bloklineh(sx1,sy,sx2,220,0,0);
        }
        //windows
        for (int y=0; y<ysize/2; y++){
            double midx=maxx/2.0;
            int lx1=(int)(midx-((xsize*blok)/2.5));
            int lx2=(int)(midx-((xsize*blok)/5));
            int rx2=(int)(midx+((xsize*blok)/2.5));
            int rx1=(int)(midx+((xsize*blok)/5));
            int sy=(int)((ground-(ysize/1.5)*blok)+y*blok+blok/2);
            bloklineh(lx1,sy,lx2,20,100,220);
            bloklineh(rx1,sy,rx2,20,100,220);
        }
        boom=0;
        gravity=oggravity;
        revcounter=0;
        blitbackground();
    }    
    
    public static void addblock(double x1, double y1, double rot, int r, int g, int b){
        double rgb = r*256*256+g*256+b;
        double xvel=0;
        double yvel=0;
        double rvel=0;
        double mass=1;
        //                         0  1  2   3   4    5    6    7   8  9  10
        partlist.add(new double[]{x1,y1,rot,rgb,xvel,yvel,rvel,mass,x1,y1,rot});
    }
    
    public static void drawblok(Graphics2D g2d,double x1, double y1, double rot, double rgb){
        int irgb = (int) rgb;
        int cr = (irgb >> 16 ) & 0xFF;
        int cg = (irgb >> 8 ) & 0xFF;
        int cb = irgb & 0xFF;
        g2d.translate((int)x1,(int)y1);
        g2d.rotate(rot);
        g2d.setColor(new Color(cr,cg,cb));
        g2d.fillRect(-blok/2, -blok/2, blok, blok);
        g2d.rotate(-rot);
        g2d.translate((int)-x1,(int)-y1);
    }
    
    public static void drawbloks(Graphics2D g2d){
        for (int i=0; i<partlist.size(); i++){
            double[] partparam = partlist.get(i);
            drawblok(g2d, partparam[0], partparam[1], partparam[2],partparam[3]);
        }
    }

    public static void processbloks(){
        for (int i=0; i<partlist.size(); i++){
            double[] partparam = partlist.get(i);
            /*
            x=0, y=1, angle=2, color=3, vx=4, vy=5, vr=6, mass=7, ogx=8, ogy=9, ogangle=10
            */
            partparam[0]+=partparam[4];
            if (partparam[0]<0){partparam[4]*=(-1*(1-Math.random()*0.05));}
            if (partparam[0]>maxx){partparam[4]*=(-1*(1-Math.random()*0.05));}
            if (partparam[1]+partparam[5]<ground){
                partparam[1]+=partparam[5];
            } else {
                partparam[4]=0;
                //partparam[5]=0;
                partparam[6]=0;
            }
            partparam[2]+=partparam[6];
            partparam[5]+=gravity/6.0;
        }
        if (revcounter>0){
            revcounter-=1;
            if (revcounter<=0){
                for (int i=0; i<partlist.size(); i++){
                    double[] partparam = partlist.get(i);
                    partparam[0]=partparam[8];
                    partparam[1]=partparam[9];
                    partparam[2]=partparam[10];
                    partparam[4]=partparam[0];
                    partparam[5]=partparam[0];
                    partparam[6]=partparam[0];
                }
                boom=0;
                gravity=oggravity;
                revcounter=0;
            }
        }
    }
    
    public double[] rotPointOverPoint(double px,double py,double cx,double cy,double rotangle){
        double newX = cx + (px - cx) * Math.cos(rotangle) - (py - cy) * Math.sin(rotangle);
        double newY = cy + (py - cx) * Math.sin(rotangle) + (py - cy) * Math.cos(rotangle);
        return new double[]{newX,newY};  
    }
    
    public void background(Graphics g){
        g.drawImage(bgImage, 0, 0, this);
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
        //draw stuff here
        background(g);
        drawbloks(g2d);
        if ((boom==1)||(boom==2)) {processbloks();}
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1){
            if (boom==1) {
                boom=2;//state is un-booming
                revcounter=100;
                for (int i=0; i<partlist.size(); i++){
                    double[] partparam = partlist.get(i);
                    gravity=0;
                    partparam[4]=(partparam[8]-partparam[0])/revcounter;
                    partparam[5]=(partparam[9]-partparam[1])/revcounter;
                    partparam[6]=(partparam[10]-partparam[2])/revcounter;
                }
            } else if (boom==2) {
                boom=0;//state is reset
                for (int i=0; i<partlist.size(); i++){
                    double[] partparam = partlist.get(i);
                    partparam[0]=partparam[8];
                    partparam[1]=partparam[9];
                    partparam[2]=partparam[10];
                    partparam[4]=partparam[0];
                    partparam[5]=partparam[0];
                    partparam[6]=partparam[0];
                }
                gravity=oggravity;
                revcounter=0;
            }
        }

        if (button == MouseEvent.BUTTON3){
            boom=1;//state is booming!
            if (boom==1) {
                double mx = e.getX();
                double my = e.getY();
                for (int i=0; i<partlist.size(); i++){
                    double[] partparam = partlist.get(i);
                    /*
                    x=0, y=1, angle=2, color=3, vx=4, vy=5, vr=6, mass=7, ogx=8, ogy=9, ogangle=10
                    */
                    double bx=partparam[0];
                    double by=partparam[1];
                    double dx=bx-mx;
                    double dy=by-my;
                    double dist=Math.sqrt(dx*dx/dsf+dy*dy/dsf+1);
                    double force=boomforce/(dist*dsf);
                    partparam[4]=dx*force*(1+(Math.random()-0.5)*boomrand);
                    partparam[5]=dy*force*boomvertratio*(1+(Math.random()-0.5)*boomrand);
                    partparam[6]=Math.random()*0.5;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}



public class animboom {

    public static void main(String[] args) {
        JFrame canvasish = new JFrame();
        canvasish.setTitle("ForceBoomSim");
        canvasish.setSize(PaintPanel.maxx, PaintPanel.maxy);
        canvasish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel.blokinit2();
        PaintPanel panel = new PaintPanel(Color.WHITE);
        panel.setPreferredSize(new Dimension(PaintPanel.maxx, PaintPanel.maxy));//size fix for linux swing
        Container pane = canvasish.getContentPane(); // Fixing theGUI reference
        pane.add(panel);
        canvasish.pack(); //size fix for linux swing implementation
        canvasish.setVisible(true);
    }
}

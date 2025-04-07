/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.lynefun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class PaintPanel extends JPanel {
    private double move=0;
    private double randsin=1;
    private double wind=0;
    private int ground=640;
    public static int maxx=1200;
    public static int maxy=800;
    public static int starcount=2000;
    private double[] xpoints = new double[starcount];
    private double[] ypoints = new double[starcount];

    public double frandom() {
        var x = Math.sin(randsin+=10);
        return (x+1)/2;
    }
    
    public void initstars(){
        double maxdist=880;
        for (int a=0; a<starcount; a++){
            xpoints[a]=(Math.random()*maxx*2-maxx/2);
            ypoints[a]=(Math.random()*maxdist*2)-maxdist;
        }
    }
    
    public void tree(Graphics g, int x, int y, double ang, double branchlength, int branchcount, int branchrem){
        Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D
        g2d.setStroke(new BasicStroke(3)); // Set line thickness to 3 pixels

        double brnchangle=0.5;
        double brnchredox=0.8;
        g.setColor(Color.BLACK);
        // point x,y is the start of the tree bottom.
        // xt,yt is the top of the branch at angle ang to the vertical, distance of branchlength
        double frcrem=(branchcount-branchrem)/(branchcount*1.0);
        double frcreminv=1-frcrem;
        int xt=(int) (x+branchlength*Math.sin(ang+wind*frcrem));
        int yt=(int) (y-branchlength*Math.cos(ang+wind*frcrem));
        double crs=200;
        double cgs=150;
        double cbs=0;
        double crf=00;
        double cgf=250;
        double cbf=40;
        int cr = (int) (crs*frcreminv + crf*frcrem);
        int cg = (int) (cgs*frcreminv + cgf*frcrem);
        int cb = (int) (cbs*frcreminv + cbf*frcrem);
        g.setColor(new Color(cr,cg,cb));
        g.drawLine(x, y, xt, yt);
        if (branchrem>0) {
            tree(g,xt,yt,ang+brnchangle+frandom()/5,brnchredox*branchlength,branchcount,branchrem-1);
            tree(g,xt,yt,ang-brnchangle+frandom()/5,brnchredox*branchlength,branchcount,branchrem-1);
        }
    }
    public void stars(Graphics g){
        for (int a=0; a<starcount; a++){
            g.setColor(new Color(240,240,240));
            g.fillRect((int)(xpoints[a]),(int)(ypoints[a]),2,2);
        }
    }
    
    public void rotStars(double cx, double cy, double a) {
        for (int i = 0; i < starcount; i++) {
            // Apply the rotation formula
            double newX = cx + (xpoints[i] - cx) * Math.cos(a) - (ypoints[i] - cy) * Math.sin(a);
            double newY = cy + (xpoints[i] - cx) * Math.sin(a) + (ypoints[i] - cy) * Math.cos(a);
            // Store the new coordinates
            xpoints[i] = newX;
            ypoints[i] = newY;
        }
    }
    public void house(Graphics g, int x, int y){
        int housewidth=240;
        int househeight=300;
        int brickwidth=15;
        int brickheight=15;
        //todo - add chimney smoke?
       
        g.setColor(new Color(220,10,10));
        //main house
        g.fillRect(x,y-househeight,housewidth,househeight);
        //roof triangle
        g.fillPolygon(new int[]{x, x+housewidth, x+housewidth/2}, new int[]{y-househeight, y-househeight, (int)(y-househeight-housewidth*0.5)}, 3);
        //chimney
        g.fillRect((int)(x+housewidth*0.75),(int)(y-househeight-housewidth*0.5),(int)(housewidth*0.1),(int)(housewidth*0.5));
        Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D
        g2d.setStroke(new BasicStroke(4)); // Set line thickness to 3 pixels
        g.setColor(new Color(240,240,240));
        g.drawLine(x-brickwidth, y-househeight+brickwidth, x+housewidth/2, (int)(y-househeight-housewidth*0.5));
        g.drawLine(x+brickwidth+housewidth, y-househeight+brickwidth, x+housewidth/2, (int)(y-househeight-housewidth*0.5));
        g2d.setStroke(new BasicStroke(2)); // Set line thickness to 3 pixels
        for (int a=y-househeight;a<=y;a+=brickheight){
            g.drawLine(x, a, x+housewidth, a);
        }
        for (int a=y-househeight;a<y;a+=brickheight){
            for (int b=brickwidth+x;b<x+housewidth;b+=brickwidth){
                if (((a/brickheight+b/brickwidth) % 2)==0) { g.drawLine(b, a, b, a+brickheight);}
            }
        }
        g.drawLine(x, y-househeight, x, y);
        g.drawLine(x+housewidth, y-househeight, x+housewidth, y);
        //door
        g.setColor(new Color(240,240,240));
        g.fillRect((int)(x+housewidth*0.35),(int)(y-househeight*0.40),(int)(housewidth*0.3),(int)(househeight*0.40));
        g.setColor(new Color(140,140,140));
        g.drawRect((int)(x+housewidth*0.35)+3,(int)(y-househeight*0.40)+3,(int)(housewidth*0.3)-2*3,(int)(househeight*0.40)-2*3);
        //windows
        for (int a=0;a<3;a++){
            for (int b=0;b<2;b++){
                if (!(a==1 && b==1)){
                    g.setColor(new Color(240,240,240));
                    g.fillRect((int)(x+housewidth*0.1+a*housewidth*0.3),(int)(y-househeight*0.80+b*househeight*0.45),(int)(housewidth*0.2),(int)(househeight*0.20));
                    g.setColor(new Color(40,140,200));
                    g.fillRect((int)(x+housewidth*0.1+a*housewidth*0.3)+3,(int)(y-househeight*0.80+b*househeight*0.45)+3,(int)(housewidth*0.2)-6,(int)(househeight*0.20)-6);
                }
            }
        }
        
    }
    
    public void background(Graphics g){
        //sky
        for (int a=0; a<=ground; a++){
            double frcrem=(ground-a)/(ground*1.0);
            double frcreminv=1-frcrem;
            double crs=0;
            double cgs=0;
            double cbs=0;
            double crf=0;
            double cgf=0;
            double cbf=200;
            int cr = (int) (crs*frcrem + crf*frcreminv);
            int cg = (int) (cgs*frcrem + cgf*frcreminv);
            int cb = (int) (cbs*frcrem + cbf*frcreminv);
            g.setColor(new Color(cr,cg,cb));
            g.drawLine(0, a, maxx, a);
        }
    }
    public void dground(Graphics g){
        //ground
        for (int a=ground; a<=maxy; a++){
            double frcrem=(maxy-a)/(maxy-ground*1.0);
            double frcreminv=1-frcrem;
            double crs=200;
            double cgs=200;
            double cbs=0;
            double crf=150;
            double cgf=160;
            double cbf=0;
            int cr = (int) (crs*frcrem + crf*frcreminv);
            int cg = (int) (cgs*frcrem + cgf*frcreminv);
            int cb = (int) (cbs*frcrem + cbf*frcreminv);
            g.setColor(new Color(cr,cg,cb));
            g.drawLine(0, a, maxx, a);
        }
    }
    
    //for animation
    public PaintPanel(Color backgroundColor) {
        setBackground(backgroundColor);
        // Timer to update position every 30ms
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint(); // Request panel to redraw
                move+=0.05;
                randsin=0+move*2;
                wind=Math.sin(move*1.5)/5;        
                rotStars(maxx/2,0,0.004);
    }
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //draw background
        background(g);
        //stars
        stars(g);
        //ground
        dground(g);
        //draw trees
        int offset=0;
        for (int b=0; b<8; b+=1){
            if (b>=4) {offset=300;}
            tree(g,offset+100+b*100,ground,0,60+Math.sin(b*20)*20,6,6);
        }        
        //draw house
        house(g, 500,ground);
    }
    
}



public class lynefun {

    public static void main(String[] args) {
        JFrame canvasish = new JFrame();
        canvasish.setTitle("Lyne");
        canvasish.setSize(PaintPanel.maxx, PaintPanel.maxy);
        canvasish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel panel = new PaintPanel(Color.WHITE);
        panel.initstars();
        Container pane = canvasish.getContentPane(); // Fixing theGUI reference
        pane.add(panel);
        canvasish.setVisible(true);
    }
}

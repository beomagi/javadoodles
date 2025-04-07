/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mynameisbill;


import java.awt.*;
import javax.swing.*;



public class MyNameIsBill {
    private static int winheight = 800, winwidth = 800;
    private static int panelsx = 64, panelsy = 64;
    private static JPanel[][] jpa = new JPanel[winwidth][winheight];
    private static Color[][] virtscreen = new Color[winwidth][winheight];
    private static Color[][] buffscreen = new Color[winwidth][winheight];
    private static int painterr, painterg, painterb;


    public static void pixelC(int x, int y, Color col){
        if (x>=0 && x<winwidth && y>=0 && y<winheight){
            virtscreen[x][y]=col;
        }
    }

    public static void pixel(int x, int y, double r, double g, double b){
        int ir=(int)Math.min(Math.max(r, 0),255);
        int ig=(int)Math.min(Math.max(r, 0),255);
        int ib=(int)Math.min(Math.max(r, 0),255);
        pixelC(x, y, new Color((int)ir, (int)ig, (int)ib));
    }

    public static void drawscreen(){
        for(int y=0; y<winheight; y++) {
            for(int x=0; x<winwidth; x++) {
                if (buffscreen[x][y] != virtscreen[x][y]){
                    buffscreen[x][y]=virtscreen[x][y];
                    jpa[x][y].setBackground(virtscreen[x][y]);
                }
            }
        }
    }

    public static void background(){
        for(int y=0; y<panelsy; y++) {
            for(int x=0; x<panelsx; x++) {
                double r,g,b;
                r=g=b=30*((x+y)%2);
                pixelC(x,y,new Color((int)r, (int)g, (int)b));
            }
        }
    }

    public static void line(int x1, int y1, int x2, int y2, Color col){
        //draw a line with color col from x1,y1 to x2,y2
        double xd=Math.abs(x2-x1);
        double yd=Math.abs(y2-y1);
        if (xd>=yd){ // ydiff is greater, iterate here to prevent gaps in line
            if (x1>x2){ //swap points
                int temp=x1; x1=x2; x2=temp;
                temp=y1; y1=y2; y2=temp;
            }
            double m=0;//gradient
            if (x1==x2){
                m=100e100; //avoid div by zero
            } else {
                m=((double)y2-y1)/(x2-x1);
            }
            for (int x=x1; x<=x2; x++){
                int y=(int)Math.round(y1+m*(x-x1));
                pixelC(x,y,col);
            }
        } else { // ydiff is greater, iterate here to prevent gaps in line
            if (y1>y2){ //swap points
                int temp=x1; x1=x2; x2=temp;
                temp=y1; y1=y2; y2=temp;
            }
            double im;
            if (y1==y2){
                im=100e100; //avoid div by zero
            } else {
                im=((double)x2-x1)/(y2-y1);
            }
            for (int y=y1; y<=y2; y++){
                int x=(int)Math.round(x1+im*(y-y1));
                pixelC(x,y,col);
            }

        }
    }

    public static void triangle(int x1, int y1, int x2, int y2, int x3, int y3, Color col){
        //draw a filled triangle of color col
        //sort the points
        if (y1>y2){int temp=x1; x1=x2; x2=temp; temp=y1; y1=y2; y2=temp;}
        if (y2>y3){int temp=x2; x2=x3; x3=temp; temp=y2; y2=y3; y3=temp;}
        if (y1>y2){int temp=x1; x1=x2; x2=temp; temp=y1; y1=y2; y2=temp;}
        //at this point, x1,y1 is the top, x2,y2 is the middle, x3,y3 is the bottom
        for (int drwy=y1; drwy<=y3; drwy++){
            if (drwy<=y2){
                double dy13=y1-y3; if (dy13==0) {dy13=10e-10;}//avoid div by zero
                double dy12=y1-y2; if (dy12==0) {dy12=10e-10;}
                double a=(x1-x3)/dy13;
                double b=(x1-x2)/dy12;
                int xL=(int)Math.round(x1+a*(drwy-y1));
                int xR=(int)Math.round(x1+b*(drwy-y1));
                line(xL,drwy,xR,drwy,col);
            } else {
                double dy23=y2-y3; if (dy23==0) {dy23=10e-10;}
                double dy13=y1-y3; if (dy13==0) {dy13=10e-10;}
                double a=(x2-x3)/dy23;
                double b=(x1-x3)/dy13;
                int xL=(int)Math.round(x2+a*(drwy-y2));
                int xR=(int)Math.round(x1+b*(drwy-y1));
                line(xL,drwy,xR,drwy,col);
            }
        }//*/
    }

    public static void circle(int x, int y, int r, Color col){
        //draw a circle of radius r with color col at x,y
        for (int drwy=y-r; drwy<=y+r; drwy++){
            double dx=Math.sqrt(r*r-(drwy-y)*(drwy-y));
            int xL=(int)Math.round(x-dx);
            int xR=(int)Math.round(x+dx);
            line(xL,drwy,xR,drwy,col);
        }
    }

    public static void sphere(int x, int y, int r, Color col){
        //draw a circle of radius r with color col at x,y
        for (int drwy=y-r; drwy<=y+r; drwy++){
            double dx=Math.sqrt(r*r-(drwy-y)*(drwy-y));
            int xL=(int)Math.round(x-dx);
            int xR=(int)Math.round(x+dx);
            for (int hx=xL; hx<=xR; hx++){
                double dist=Math.sqrt((hx-x)*(hx-x)+(drwy-y)*(drwy-y));
                double darken=1-dist/(2*r);
                int ncr=(int) (col.getRed()*darken);
                int ncg=(int) (col.getGreen()*darken);
                int ncb=(int) (col.getBlue()*darken);
                pixelC(hx, drwy, new Color(ncr, ncg, ncb));
            }
        }
    }

    public static void pyrcenter(double tvar,int xcenter,int ytop, int ybot, double xmag, double ymag, double incline){
        double cpyrx1=Math.sin(-tvar)*xmag;
        double cpyry1=Math.cos(-tvar)*ymag;
        double cpyrx2=Math.sin(-tvar-Math.PI/2)*xmag;
        double cpyry2=Math.cos(-tvar-Math.PI/2)*ymag;
        double cpyrx3=Math.sin(-tvar-Math.PI)*xmag;
        double cpyry3=Math.cos(-tvar-Math.PI)*ymag;
        double cpyrx4=Math.sin(-tvar-3*Math.PI/2)*xmag;
        double cpyry4=Math.cos(-tvar-3*Math.PI/2)*ymag;
        double dpyrx1=cpyrx1*incline;
        double dpyry1=cpyry1*incline;
        double dpyrx2=cpyrx2*incline;
        double dpyry2=cpyry2*incline;
        double dpyrx3=cpyrx3*incline;
        double dpyry3=cpyry3*incline;
        double dpyrx4=cpyrx4*incline;
        double dpyry4=cpyry4*incline;

        int ct_x1=xcenter+(int)cpyrx1;
        int ct_y1=ytop+(int)cpyry1;
        int ct_x2=xcenter+(int)cpyrx2;
        int ct_y2=ytop+(int)cpyry2;
        int ct_x3=xcenter+(int)cpyrx3;
        int ct_y3=ytop+(int)cpyry3;
        int ct_x4=xcenter+(int)cpyrx4;
        int ct_y4=ytop+(int)cpyry4;

        int dt_x1=xcenter+(int)dpyrx1;
        int dt_y1=ybot+(int)dpyry1;
        int dt_x2=xcenter+(int)dpyrx2;
        int dt_y2=ybot+(int)dpyry2;
        int dt_x3=xcenter+(int)dpyrx3;
        int dt_y3=ybot+(int)dpyry3;
        int dt_x4=xcenter+(int)dpyrx4;
        int dt_y4=ybot+(int)dpyry4;

        double cyrz1=(cpyry1+cpyry2)/8.0;
        double cyrz2=(cpyry2+cpyry3)/8.0;
        double cyrz3=(cpyry3+cpyry4)/8.0;
        double cyrz4=(cpyry4+cpyry1)/8.0;

        double ccolfac1=Math.max(Math.min(cyrz1,1),0.3);
        double ccolfac2=Math.max(Math.min(cyrz2,1),0.3);
        double ccolfac3=Math.max(Math.min(cyrz3,1),0.3);
        double ccolfac4=Math.max(Math.min(cyrz4,1),0.3);
        Color CApxF1 = new Color((int)(200*ccolfac1),(int)(200*ccolfac1),0);
        Color CApxF2 = new Color((int)(180*ccolfac2),(int)(180*ccolfac2),0);
        Color CApxF3 = new Color((int)(200*ccolfac3),(int)(200*ccolfac3),0);
        Color CApxF4 = new Color((int)(180*ccolfac4),(int)(180*ccolfac4),0);


        triangle (ct_x1,ct_y1,ct_x2,ct_y2,ct_x3,ct_y3,new Color(100,100,0));
        triangle (ct_x1,ct_y1,ct_x3,ct_y3,ct_x4,ct_y4,new Color(100,100,0));
        if (cyrz1>cyrz3) {
            triangle(ct_x1,ct_y1,ct_x2,ct_y2,dt_x1,dt_y1,CApxF1);
            triangle(dt_x1,dt_y1,dt_x2,dt_y2,ct_x2,ct_y2,CApxF1);
        }
        if (cyrz2>cyrz4) {
            triangle(ct_x2,ct_y2,ct_x3,ct_y3,dt_x2,dt_y2,CApxF2);
            triangle(dt_x2,dt_y2,dt_x3,dt_y3,ct_x3,ct_y3,CApxF2);
        }
        if (cyrz3>cyrz1) {
            triangle(ct_x3,ct_y3,ct_x4,ct_y4,dt_x3,dt_y3,CApxF3);
            triangle(dt_x3,dt_y3,dt_x4,dt_y4,ct_x4,ct_y4,CApxF3);
        }
        if (cyrz4>cyrz2) {
            triangle(ct_x4,ct_y4,ct_x1,ct_y1,dt_x4,dt_y4,CApxF4);
            triangle(dt_x4,dt_y4,dt_x1,dt_y1,ct_x1,ct_y1,CApxF4);
        }

    }

    public static void pyramid(double tvar){
        //Apex
        double pyrx1=Math.sin(tvar)*8;
        double pyry1=Math.cos(tvar)*3;
        double pyrx2=Math.sin(tvar+Math.PI/2)*8;
        double pyry2=Math.cos(tvar+Math.PI/2)*3;
        double pyrx3=Math.sin(tvar+Math.PI)*8;
        double pyry3=Math.cos(tvar+Math.PI)*3;
        double pyrx4=Math.sin(tvar+3*Math.PI/2)*8;
        double pyry4=Math.cos(tvar+3*Math.PI/2)*3;

        double pyrz1=(pyry1+pyry2)/5.0;
        double pyrz2=(pyry2+pyry3)/5.0;
        double pyrz3=(pyry3+pyry4)/5.0;
        double pyrz4=(pyry4+pyry1)/5.0;

        int apex_x1=32+(int)pyrx1;
        int apex_y1=20+(int)pyry1;
        int apex_x2=32+(int)pyrx2;
        int apex_y2=20+(int)pyry2;
        int apex_x3=32+(int)pyrx3;
        int apex_y3=20+(int)pyry3;
        int apex_x4=32+(int)pyrx4;
        int apex_y4=20+(int)pyry4;

        double colfac1=Math.max(Math.min(pyrz1,1),0.3);
        double colfac2=Math.max(Math.min(pyrz2,1),0.3);
        double colfac3=Math.max(Math.min(pyrz3,1),0.3);
        double colfac4=Math.max(Math.min(pyrz4,1),0.3);
        Color ApxF1 = new Color((int)(200*colfac1),(int)(200*colfac1),0);
        Color ApxF2 = new Color((int)(180*colfac2),(int)(180*colfac2),0);
        Color ApxF3 = new Color((int)(200*colfac3),(int)(200*colfac3),0);
        Color ApxF4 = new Color((int)(180*colfac4),(int)(180*colfac4),0);

        if (pyrz1>pyrz3) {triangle(32,10,apex_x1,apex_y1,apex_x2,apex_y2,ApxF1);}
        if (pyrz2>pyrz4) {triangle(32,10,apex_x2,apex_y2,apex_x3,apex_y3,ApxF2);}
        if (pyrz3>pyrz1) {triangle(32,10,apex_x3,apex_y3,apex_x4,apex_y4,ApxF3);}
        if (pyrz4>pyrz2) {triangle(32,10,apex_x4,apex_y4,apex_x1,apex_y1,ApxF4);}

        //Center and bottom
        pyrcenter(tvar,32,28,34,12,4,1.2);
        pyrcenter(-tvar,32,45,54,20,7,1.25);

        //eye
        sphere(32, 7, 5, new Color(250,250,250));
        int pupilx = 32+(int)(Math.sin(4*tvar)*3);
        int pupily = 7+(int)(Math.cos(5.5*tvar)*2);
        circle(pupilx, pupily, 2, new Color(0,0,0));

    }

    public static void main(String[] args) {
        //initial setup
        JFrame framemain = new JFrame("FunkyFrame");
        framemain.setSize(winwidth, winheight);
        framemain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = framemain.getContentPane();
        contentPane.setLayout(new GridLayout(panelsx,panelsy));

        //place panels
        for(int y=0; y<panelsy; y++) {
            for(int x=0; x<panelsy; x++) {
                jpa[x][y] = new JPanel();
                contentPane.add(jpa[x][y]);
                buffscreen[x][y]=Color.BLACK;
                virtscreen[x][y]=Color.BLACK;
            }
        }

        framemain.setVisible(true);

        double tvar=0; //frame time variable
        while (true) { 
            background();
            pyramid(tvar);
            drawscreen();
            contentPane.repaint();//force repaint of all panels
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tvar+=0.1;
        }
    }
}
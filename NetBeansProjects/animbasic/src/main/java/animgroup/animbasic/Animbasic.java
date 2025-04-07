/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package animgroup.animbasic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 *
 * @author beomagi
 */


class PaintPanel extends JPanel {
    public static int maxx=1200;
    public static int maxy=800;
    
    //lets store x/y/rot and color
    public static ArrayList<int[]> partlist = new ArrayList<>();


    
    public double[] rotPointOverPoint(double px,double py,double cx,double cy,double rotangle){
        double newX = cx + (px - cx) * Math.cos(rotangle) - (py - cy) * Math.sin(rotangle);
        double newY = cy + (py - cx) * Math.sin(rotangle) + (py - cy) * Math.cos(rotangle);
        return new double[]{newX,newY};  
    }
    
    
    public void background(Graphics g){
        //sky
        int ground=maxy;
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

    
    //for animation
    public PaintPanel(Color backgroundColor) {
        setBackground(backgroundColor);
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
        //draw stuff here
        background(g);
    }
}



public class Animbasic {

    public static void main(String[] args) {
        JFrame canvasish = new JFrame();
        canvasish.setTitle("Lyne");
        canvasish.setSize(PaintPanel.maxx, PaintPanel.maxy);
        canvasish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel panel = new PaintPanel(Color.WHITE);
        //init anim vars here
        //- - - - 
        Container pane = canvasish.getContentPane(); // Fixing theGUI reference
        pane.add(panel);
        canvasish.setVisible(true);
    }
}

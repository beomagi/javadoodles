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


class PaintPanel extends JPanel implements MouseListener {
    public static int maxx=1200;
    public static int maxy=800;
    public static int ground=640;

    public static BufferedImage bgImage;
    
    
    public double[] rotPointOverPoint(double px,double py,double cx,double cy,double rotangle){
        double newX = cx + (px - cx) * Math.cos(rotangle) - (py - cy) * Math.sin(rotangle);
        double newY = cy + (py - cx) * Math.sin(rotangle) + (py - cy) * Math.cos(rotangle);
        return new double[]{newX,newY};  
    }
    
    public void background(Graphics g){
        g.drawImage(bgImage, 0, 0, this);
    }
    
    public void inits(){
        blitbackground();
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
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1){
        }
        if (button == MouseEvent.BUTTON3){
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



public class Blobjump {

    public static void main(String[] args) {
        JFrame canvasish = new JFrame();
        canvasish.setTitle("ForceBoomSim");
        canvasish.setSize(PaintPanel.maxx, PaintPanel.maxy);
        canvasish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel panel = new PaintPanel(Color.WHITE);
        panel.inits();
        panel.setPreferredSize(new Dimension(PaintPanel.maxx, PaintPanel.maxy));//size fix for linux swing
        Container pane = canvasish.getContentPane(); // Fixing theGUI reference
        pane.add(panel);
        canvasish.pack(); //size fix for linux swing implementation
        canvasish.setVisible(true);
    }
}

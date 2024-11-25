package mandelRenderUI;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author Ryan Cipriani
 */

public class mandelRenderUI {
    /**
     * @param args the command line arguments
     */

    private static double offsetX;
    private static double offsetY;
    private static double zoom;
    private static double maxiter;
    private static int w;
    private static int h;
    private static double cro;
    private static double cgo;
    private static double cbo;
    private static double crr;
    private static double cgr;
    private static double cbr;


    public static BufferedImage loadImageToIcon(String filename, JFrame frame, int maxwidth, int maxheight) {
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new java.io.File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //get dimentions of image
        int owidth = originalImage.getWidth();
        int oheight = originalImage.getHeight();
        double winscale = 1;
        if (maxwidth < owidth*winscale) {
            winscale = (double)maxwidth / (double)owidth;
        }
        if (maxheight < oheight*winscale) {
            winscale = (double)maxheight / (double)oheight;
        }
        int winwidth = (int)(owidth*winscale);
        int winheight = (int)(oheight*winscale);
        Image scaledImage = originalImage.getScaledInstance(winwidth, winheight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(winwidth, winheight, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
        //ImageIcon icon = new ImageIcon(filename);
        return resizedImage;
    }

    public static void updateInputs(JTextField tf_x, JTextField tf_y, JTextField tf_z, JTextField tf_i, JTextField tf_ix, JTextField tf_iy, JTextField tf_cro, JTextField tf_cgo, JTextField tf_cbo, JTextField tf_crr, JTextField tf_cgr, JTextField tf_cbr) {
        tf_x.setText(offsetX+"");
        tf_y.setText(offsetY+"");
        tf_z.setText(zoom+"");
        tf_i.setText(maxiter+"");
        tf_ix.setText(w+"");
        tf_iy.setText(h+"");
        tf_cro.setText(cro+"");
        tf_cgo.setText(cgo+"");
        tf_cbo.setText(cbo+"");
        tf_crr.setText(crr+"");
        tf_cgr.setText(cgr+"");
        tf_cbr.setText(cbr+"");
    }

    public static void showpng(String filename, String outfile, int numThreads, int chunkSize) {
        // create scaled preview window of the created image
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Preview - Press ESC to close");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int maxwidth = 800;
            int maxheight = 600;
            JLabel mandelicon = new JLabel();
            BufferedImage mimage = loadImageToIcon(filename, frame, maxwidth, maxheight);
            ImageIcon icon = new ImageIcon(mimage);
            mandelicon.setIcon(icon);
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.add(mandelicon, BorderLayout.NORTH);
            frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        frame.dispose();
                    }
                }
            });
            JPanel inputPanel1 = new JPanel();
            JLabel label_x = new JLabel("X:");
            JTextField tf_x = new JTextField(15);
            JLabel label_y = new JLabel("Y:");
            JTextField tf_y = new JTextField(15);
            JLabel label_z = new JLabel("Zoom:");
            JTextField tf_z = new JTextField(15);
            JButton butt_go = new JButton("GO");
            inputPanel1.add(label_x);
            inputPanel1.add(tf_x);
            inputPanel1.add(label_y);
            inputPanel1.add(tf_y);
            inputPanel1.add(label_z);
            inputPanel1.add(tf_z);
            inputPanel1.add(butt_go);
            JPanel inputPanel2 = new JPanel();
            JLabel label_i = new JLabel("Iterations:");
            JTextField tf_i = new JTextField(15);
            JLabel label_ix = new JLabel("width:");
            JTextField tf_ix = new JTextField(15);
            JLabel label_iy = new JLabel("height:");
            JTextField tf_iy = new JTextField(15);
            inputPanel2.add(label_ix);
            inputPanel2.add(tf_ix);
            inputPanel2.add(label_iy);
            inputPanel2.add(tf_iy);
            inputPanel2.add(label_i);
            inputPanel2.add(tf_i);
            JPanel inputPanel3 = new JPanel();
            JLabel JL_cro = new JLabel("rd-O:");
            JTextField tf_cro = new JTextField(5);
            JLabel JL_cgo = new JLabel("gr-O:");
            JTextField tf_cgo = new JTextField(5);
            JLabel JL_cbo = new JLabel("bl-O:");
            JTextField tf_cbo = new JTextField(5);
            JLabel JL_crr = new JLabel("rd-R:");
            JTextField tf_crr = new JTextField(5);
            JLabel JL_cgr = new JLabel("gr-R:");
            JTextField tf_cgr = new JTextField(5);
            JLabel JL_cbr = new JLabel("bl-R:");
            JTextField tf_cbr = new JTextField(5);
            inputPanel3.add(JL_cro);
            inputPanel3.add(tf_cro);
            inputPanel3.add(JL_cgo);
            inputPanel3.add(tf_cgo);
            inputPanel3.add(JL_cbo);
            inputPanel3.add(tf_cbo);
            inputPanel3.add(JL_crr);
            inputPanel3.add(tf_crr);
            inputPanel3.add(JL_cgr);
            inputPanel3.add(tf_cgr);
            inputPanel3.add(JL_cbr);
            inputPanel3.add(tf_cbr);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(inputPanel1, BorderLayout.NORTH);
            mainPanel.add(inputPanel2, BorderLayout.CENTER);
            mainPanel.add(inputPanel3, BorderLayout.SOUTH);
            frame.getContentPane().add(mainPanel, BorderLayout.SOUTH);
            updateInputs(tf_x, tf_y, tf_z, tf_i, tf_ix, tf_iy, tf_cro, tf_cgo, tf_cbo, tf_crr, tf_cgr, tf_cbr);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setSize(maxwidth+100,maxheight+200);
            frame.setVisible(true);
            mandelicon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    int mx=arg0.getX();
                    int my=arg0.getY();
                    offsetX = Double.valueOf(tf_x.getText());
                    offsetY = Double.valueOf(tf_y.getText());
                    zoom = Double.valueOf(tf_z.getText());
                    maxiter = Double.valueOf(tf_i.getText());
                    w = Integer.parseInt(tf_ix.getText());
                    h = Integer.parseInt(tf_iy.getText());
                    cro = Double.valueOf(tf_cro.getText());
                    cgo = Double.valueOf(tf_cgo.getText());
                    cbo = Double.valueOf(tf_cbo.getText());
                    crr = Double.valueOf(tf_crr.getText());
                    cgr = Double.valueOf(tf_cgr.getText());
                    cbr = Double.valueOf(tf_cbr.getText());

                    double defaultzoom = (w + h) / 8;
                    double shiftx = (mx - maxwidth / 2) / (defaultzoom * zoom);
                    double shifty = (my - maxheight / 2) / (defaultzoom * zoom);
                    double newoffsetX = offsetX + shiftx;
                    double newoffsetY = offsetY + shifty;
                    offsetX = newoffsetX;
                    offsetY = newoffsetY;
                    printMandel(outfile, numThreads, chunkSize);
                    BufferedImage rmimage = loadImageToIcon(outfile, frame, maxwidth, maxheight);
                    ImageIcon ricon = new ImageIcon(rmimage);
                    mandelicon.setIcon(ricon);
                    System.err.println("Clicked at: " + mx + "," + my + " shift:" + shiftx + "," + shifty);
                   updateInputs(tf_x, tf_y, tf_z, tf_i, tf_ix, tf_iy, tf_cro, tf_cgo, tf_cbo, tf_crr, tf_cgr, tf_cbr);
                }
            }); 
            butt_go.addActionListener(e -> {
                offsetX = Double.valueOf(tf_x.getText());
                offsetY = Double.valueOf(tf_y.getText());
                zoom = Double.valueOf(tf_z.getText());
                maxiter = Double.valueOf(tf_i.getText());
                w = Integer.parseInt(tf_ix.getText());
                h = Integer.parseInt(tf_iy.getText());
                cro = Double.valueOf(tf_cro.getText());
                cgo = Double.valueOf(tf_cgo.getText());
                cbo = Double.valueOf(tf_cbo.getText());
                crr = Double.valueOf(tf_crr.getText());
                cgr = Double.valueOf(tf_cgr.getText());
                cbr = Double.valueOf(tf_cbr.getText());

                printMandel(outfile, numThreads, chunkSize);
                BufferedImage rmimage = loadImageToIcon(outfile, frame, maxwidth, maxheight);
                ImageIcon ricon = new ImageIcon(rmimage);
                mandelicon.setIcon(ricon);
                updateInputs(tf_x, tf_y, tf_z, tf_i, tf_ix, tf_iy, tf_cro, tf_cgo, tf_cbo, tf_crr, tf_cgr, tf_cbr);
            });
        });
    }


    public static String getParam(String[] args, String key, String defaultValue) {
        for (int i = 0; i < args.length; i++) {
            //if args[i] starts with ":" then return true/false
            if (key.startsWith(":")) {
                if ((":"+args[i]).equals(key)) {
                    return "1";
                }
            }
            if (args[i].equals(key)) {
                return args[i + 1];
            }
        }
        return defaultValue;
    }

    public static double log2 = Math.log(2); // precalculate log2
    public static int mandelCalcRGBatXY(double x, double y, double w, double h, double offsetX, double offsetY,
            double zoom, double maxiter, double cro, double cgo, double cbo, double crr, double cgr, double cbr) {
        // calculate the mandelbrot value at x,y, given w and h, return pixel color
        double defaultzoom = (w + h) / 8;
        double tx, ty, xtmp, ytmp, xtmp2, iter;
        xtmp = ytmp = xtmp2 = iter = 0;
        tx = offsetX + (x - w / 2) / (defaultzoom * zoom);
        ty = offsetY + (y - h / 2) / (defaultzoom * zoom);
        double xsq = xtmp * xtmp;
        double ysq = ytmp * ytmp;
        double normalize = maxiter/255;
        while (xsq + ysq < 4 && iter < maxiter) {
            xtmp2 = xsq - ysq + tx;
            ytmp = 2 * xtmp * ytmp + ty;
            xtmp = xtmp2;
            iter += 1;
            xsq = xtmp*xtmp;
            ysq = ytmp*ytmp;
        }
        // smoother transition
        if (iter < maxiter) {
            double zn = Math.sqrt(xsq + ysq);
            double zl = Math.log(Math.log(zn)/log2)/log2;
            iter += 1 - zl;
        } else {
            // inner bulb shading
            iter = Math.log((1/(xsq+ysq)))/(log2);
        }
        // set the pixel at x,y to the RGB value
        double cycle = (maxiter/normalize) / (3.14159265359 * 100);
        int cr = 128 + (int) (127 * Math.sin(cro + iter / (cycle * crr)));
        int cg = 128 + (int) (127 * Math.sin(cgo + iter / (cycle * cgr)));
        int cb = 128 + (int) (127 * Math.sin(cbo + iter / (cycle * cbr)));
        int color=(cr << 16) | (cg << 8) | cb;
        return color;
    }

    public static void printMandel(String outfile, int numThreads, int chunkSize) {
        System.out.println("Rendering " + w + "x" + h + " image...");
        long startTime = System.currentTimeMillis();

        ExecutorService threadExecutor = Executors.newFixedThreadPool(numThreads);
        int numChunks = (int) Math.ceil((double) w * h / chunkSize);
        int[] allpixels = new int[w * h]; // main blank canvas
        try {
            for (int chunk = 0; chunk < numChunks; chunk++) { // Thread loop
                int start = chunk * chunkSize;
                int end = Math.min(start + chunkSize, w * h);
                threadExecutor.submit(() -> {
                    int[] pixels = new int[end - start];
                    for (int i = start; i < end; i++) {
                        int x = i % w;
                        int y = i / w;
                        int rgb = mandelCalcRGBatXY(x, y, w, h, offsetX, offsetY, zoom, maxiter, cro, cgo, cbo, crr, cgr, cbr);
                        pixels[i - start] = rgb;
                    }
                    System.arraycopy(pixels, 0, allpixels, start, pixels.length);
                });
            } // Thread loop
            threadExecutor.shutdown();
            try {
                threadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(w, h,
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, w, h, allpixels, 0, w);
            java.io.File file = new java.io.File(outfile);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        long endTime = System.currentTimeMillis();
        double renderTime = (endTime-startTime)/1000.0;
        System.out.println("Took " + renderTime + " seconds to render");

    }

    public static void main(String[] args) {
        System.out.println("MandelTest V1");
        // get all parameters
        String help1 = getParam(args, ":help", "-1");
        String help2 = getParam(args, ":-?", "-1");
        String width = getParam(args, "-w", "-1");
        String height = getParam(args, "-h", "-1");
        String SoffsetX = getParam(args, "-x", "0");
        String SoffsetY = getParam(args, "-y", "0");
        String Szoom = getParam(args, "-z", "1");
        String outfile = getParam(args, "-o", "mandel.png");
        String iterstr = getParam(args, "-m", "-1");
        String numthreadsstr = getParam(args, "-t", "-1");
        String chunksizestr = getParam(args, "-c", "-1");
        String view = getParam(args, ":-v", "");
        System.out.println("View:" + view);
        // check parameters and set defaults
        if (width == "-1") {width = "800";}
        if (height == "-1") {height = "600";}
        if (help1 == "1" || help2 == "1") {
            System.out.println(
                    "Usage: MandelTest -w <width> -h <height> [-x <offsetX> -y <offsetY> -z <zoom> -o <filename(png)> -m <Z-Iterations> -t <threads> -c <chunkSize>] -v");
            return;
        }
        int wint = Integer.parseInt(width);
        int hint = Integer.parseInt(height);
        zoom = Double.parseDouble(Szoom);
        maxiter = Double.parseDouble(iterstr);
        if (maxiter == -1) {
            maxiter=255 * Math.sqrt(zoom);
        }
        int numthreads = Integer.parseInt(numthreadsstr);
        if (numthreads == -1) {
            numthreads = Runtime.getRuntime().availableProcessors();
        }
        int chunkSize = Integer.parseInt(chunksizestr);
        if (chunkSize == -1) {
            chunkSize = (int) Math.ceil(2*Math.sqrt(wint * hint));
        }
        int totalChunks = (int) Math.ceil((double) ((wint * hint) / chunkSize));

        offsetX = Double.parseDouble(SoffsetX);
        offsetY = Double.parseDouble(SoffsetY);
        w = wint;
        h = hint;
        cro=0.00;
        cgo=1.57;
        cbo=3.14;
        crr=1.5000;
        cgr=1.6000;
        cbr=1.5666;
        
        System.out.println("Width:" + width + " Height:" + height + " offsetX:" + offsetX + " offsetY:"
                + offsetY + " zoom:" + zoom + " filename:" + outfile + " Z-Iterations:" + maxiter
                + " Threads:" + numthreads + " chunkSize:" + chunkSize+ " Total chunks:" + totalChunks  );
        long startProcessTime = System.nanoTime();
        printMandel(outfile, numthreads, chunkSize);
        long endProcessTime = System.nanoTime();        
        double ProcessTimeSeconds = (double) (endProcessTime - startProcessTime) / 10e8;
        System.out.println("Processing time: " + ProcessTimeSeconds + "s");
        if (view.equals("1")) {
            showpng(outfile, outfile, numthreads, chunkSize);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mandelRender;

import javax.imageio.ImageIO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ryan Cipriani
 */
public class mandelRender {
    /**
     * @param args the command line arguments
     */

    public static String getParam(String[] args, String key) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(key)) {
                return args[i + 1];
            }
        }
        return "";
    }

    public static int[] mandelCalcRGBatXY(double x, double y, double w, double h, double offsetX, double offsetY,
            double zoom, double maxiter) {
        // calculate the mandelbrot value at x,y, given w and h, return 3 ints, R, G,
        // and B
        double defaultzoom = (w + h) / 8;
        double tx, ty, xtmp, ytmp, xtmp2, iter;
        xtmp = ytmp = xtmp2 = iter = 0;
        tx = offsetX + (x - w / 2) / (defaultzoom * zoom);
        ty = offsetY + (y - h / 2) / (defaultzoom * zoom);
        while (xtmp * xtmp + ytmp * ytmp < 4 && iter < maxiter) {
            xtmp2 = xtmp * xtmp - ytmp * ytmp + tx;
            ytmp = 2 * xtmp * ytmp + ty;
            xtmp = xtmp2;
            iter += 1;
        }
        // smoother transition
        double log2 = Math.log(2);
        if (iter < maxiter) {
            double zn = Math.sqrt(xtmp * xtmp + ytmp * ytmp);
            double zl = Math.log(Math.log(zn) / log2) / log2;
            iter += 1 - zl;
        } else {
            // inner bulb shading
            iter = Math.log((1 / (xtmp * xtmp + ytmp * ytmp))) / (log2);
        }
        // set the pixel at x,y to the RGB value
        double cycle = (maxiter) / (3.14159265359 * 100);
        int cr = 128 + (int) (127 * Math.sin(0.00 + iter / (cycle * 1.5000)));
        int cg = 128 + (int) (127 * Math.sin(1.57 + iter / (cycle * 1.6000)));
        int cb = 128 + (int) (127 * Math.sin(3.14 + iter / (cycle * 1.5666)));
        return new int[] { cr, cg, cb };
    }

    public static void printMandel(int w, int h, double offsetX, double offsetY, double zoom, String outfile,
            double maxiter, int numThreads, int chunkSize) {
        // lets create a blank png file, size w x h
        ExecutorService threadExecutor = Executors.newFixedThreadPool(numThreads);
        int numChunks = (int) Math.ceil((double) w * h / chunkSize);
        int[] allpixels = new int[w * h];
        try {
            for (int chunk = 0; chunk < numChunks; chunk++) {
                int start = chunk * chunkSize;
                int end = Math.min(start + chunkSize, w * h);
                threadExecutor.submit(() -> {
                    int[] pixels = new int[end - start];
                    for (int i = start; i < end; i++) {
                        int x = i % w;
                        int y = i / w;
                        int[] rgb = mandelCalcRGBatXY(x, y, w, h, offsetX, offsetY, zoom, maxiter);
                        int r = rgb[0];
                        int g = rgb[1];
                        int b = rgb[2];
                        pixels[i - start] = (r << 16) | (g << 8) | b;
                    }
                    System.arraycopy(pixels, 0, allpixels, start, pixels.length);
                });
            }
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
    }

    public static void main(String[] args) {
        System.out.println("MandelTest V1");
        // key checks - -w width -h height
        String width = getParam(args, "-w");
        String height = getParam(args, "-h");
        String offsetX = getParam(args, "-x");
        String offsetY = getParam(args, "-y");
        String zoom = getParam(args, "-z");
        String outfile = getParam(args, "-o");
        String iterstr = getParam(args, "-m");
        String numthreadsstr = getParam(args, "-t");
        String chunksizestr = getParam(args, "-c");
        if (width == "" || height == "") {
            System.out.println(
                    "Usage: MandelTest -w <width> -h <height> [-x <offsetX> -y <offsetY> -z <zoom> -o <filename(png)> -m <Z-Iterations> -t <threads> -c <chunkSize>]");
            return;
        }
        int wint = Integer.parseInt(width);
        int hint = Integer.parseInt(height);
        double offsetXdouble = 0;
        if (offsetX != "") {
            offsetXdouble = Double.parseDouble(offsetX);
        }
        double offsetYdouble = 0;
        if (offsetY != "") {
            offsetYdouble = Double.parseDouble(offsetY);
        }
        double zoomdouble = 1;
        if (zoom != "") {
            zoomdouble = Double.parseDouble(zoom);
        }
        double maxiter = 255 * Math.sqrt(zoomdouble);
        if (iterstr != "") {
            maxiter = Double.parseDouble(iterstr);
        }
        if (outfile == "") {
            outfile = "mandel.png";
        }
        int numthreads = 1;
        if (numthreadsstr != "") {
            numthreads = Integer.parseInt(numthreadsstr);
        } else {
            numthreads = Runtime.getRuntime().availableProcessors();
        }
        int chunkSize = (int) Math.ceil(Math.sqrt(wint * hint));
        if (chunksizestr != "") {
            if (chunksizestr.equals("max")) {
                chunkSize = (int) Math.ceil((wint * hint) / numthreads);
            } else {
                chunkSize = Integer.parseInt(chunksizestr);
            }
        }
        System.out.println("Width:" + width + " Height:" + height + " offsetX:" + offsetXdouble + " offsetY:"
                + offsetYdouble + " zoom:" + zoomdouble + " filename:" + outfile + " Z-Iterations:" + maxiter
                + " Threads:" + numthreads + " chunkSize:" + chunkSize);
        numthreads = Runtime.getRuntime().availableProcessors();
        long startProcessTime = System.nanoTime();
        printMandel(wint, hint, offsetXdouble, offsetYdouble, zoomdouble, outfile, maxiter, numthreads, chunkSize);
        long endProcessTime = System.nanoTime();
        long ProcessTimeMicroseconds = (endProcessTime - startProcessTime) / 1000;
        double ProcessTimeSeconds = (double) ProcessTimeMicroseconds / 1000000.0;
        System.out.println("Processing time: " + ProcessTimeSeconds + "s");

    }

}

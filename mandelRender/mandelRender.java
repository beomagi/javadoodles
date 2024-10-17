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

    public static String getParam(String[] args, String key, String defaultValue) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(key)) {
                return args[i + 1];
            }
        }
        return defaultValue;
    }

    public static double log2 = Math.log(2); // precalculate log2
    public static int mandelCalcRGBatXY(double x, double y, double w, double h, double offsetX, double offsetY,
            double zoom, double maxiter) {
        // calculate the mandelbrot value at x,y, given w and h, return pixel color
        double defaultzoom = (w + h) / 8;
        double tx, ty, xtmp, ytmp, xtmp2, iter;
        xtmp = ytmp = xtmp2 = iter = 0;
        tx = offsetX + (x - w / 2) / (defaultzoom * zoom);
        ty = offsetY + (y - h / 2) / (defaultzoom * zoom);
        double xsq = xtmp * xtmp;
        double ysq = ytmp * ytmp;
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
        double cycle = (maxiter) / (3.14159265359 * 100);
        int cr = 128 + (int) (127 * Math.sin(0.00 + iter / (cycle * 1.5000)));
        int cg = 128 + (int) (127 * Math.sin(1.57 + iter / (cycle * 1.6000)));
        int cb = 128 + (int) (127 * Math.sin(3.14 + iter / (cycle * 1.5666)));
        int color=(cr << 16) | (cg << 8) | cb;
        return color;
    }

    public static void printMandel(int w, int h, double offsetX, double offsetY, double zoom, String outfile,
            double maxiter, int numThreads, int chunkSize) {
        System.out.println("Rendering " + w + "x" + h + " image...");
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
                        int rgb = mandelCalcRGBatXY(x, y, w, h, offsetX, offsetY, zoom, maxiter);
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
    }

    public static void main(String[] args) {
        System.out.println("MandelTest V1");
        // get all parameters
        String width = getParam(args, "-w", "-1");
        String height = getParam(args, "-h", "-1");
        String offsetX = getParam(args, "-x", "0");
        String offsetY = getParam(args, "-y", "0");
        String zoom = getParam(args, "-z", "1");
        String outfile = getParam(args, "-o", "mandel.png");
        String iterstr = getParam(args, "-m", "-1");
        String numthreadsstr = getParam(args, "-t", "-1");
        String chunksizestr = getParam(args, "-c", "-1");
        // check parameters and set defaults
        if (width == "-1" || height == "-1") {
            System.out.println(
                    "Usage: MandelTest -w <width> -h <height> [-x <offsetX> -y <offsetY> -z <zoom> -o <filename(png)> -m <Z-Iterations> -t <threads> -c <chunkSize>]");
            return;
        }
        int wint = Integer.parseInt(width);
        int hint = Integer.parseInt(height);
        double offsetXdouble = Double.parseDouble(offsetX);
        double offsetYdouble = Double.parseDouble(offsetY);
        double zoomdouble = Double.parseDouble(zoom);
        double maxiter = Double.parseDouble(iterstr);
        if (maxiter == -1) {
            maxiter=255 * Math.sqrt(zoomdouble);
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
        
        System.out.println("Width:" + width + " Height:" + height + " offsetX:" + offsetXdouble + " offsetY:"
                + offsetYdouble + " zoom:" + zoomdouble + " filename:" + outfile + " Z-Iterations:" + maxiter
                + " Threads:" + numthreads + " chunkSize:" + chunkSize+ " Total chunks:" + totalChunks  );
        long startProcessTime = System.nanoTime();
        printMandel(wint, hint, offsetXdouble, offsetYdouble, zoomdouble, outfile, maxiter, numthreads, chunkSize);
        long endProcessTime = System.nanoTime();        
        double ProcessTimeSeconds = (double) (endProcessTime - startProcessTime) / 10e8;
        System.out.println("Processing time: " + ProcessTimeSeconds + "s");

    }

}

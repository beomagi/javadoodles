# javadoodles

# Stuff

## mandelRender:
  running example:
```
    java mandelRender.java -w 2560 -h 1600
```
  Options:
  - -w width
  - -h height
  - -x offsetX
  - -y offsetY
  - -z zoom
  - -o filename
  - -m maximum iterations
  - -t threads
  - -c chunk size

e.g.
```
> java mandelRender.java -w 800 -h 600 -z 200 -x -0.71 -y -0.25 -m 1900 -o test.png
MandelTest V1
Width:800 Height:600 offsetX:-0.71 offsetY:-0.25 zoom:200.0 filename:test.png Z-Iterations:1900.0 Threads:16 chunkSize:693
Processing time: 0.247811s
```
Creates:
![test](https://github.com/user-attachments/assets/8fa5d845-7231-4bee-a00d-b5f68b9c2a00)


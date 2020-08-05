# AP-CS-Image-Project
AP CS Project to Process BMP Images (Inspired by CS50)


Simply download the Java files, download the BMP image to be edited, compile, and execute the Filter.java file in terminal as follows:

"java Filter [-b, -e, -g, or -r] InputRelativePath OutputRelativePath"

-b is for blur

-e is for edge detection

-g is for grayscale

and -r is for reflection

For example, if in.bmp is in the same folder as the project and you would like to grayscale into an output file out.bmp, you would execute:
"java Filter -g in.bmp out.bmp"

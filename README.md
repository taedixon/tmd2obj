# tmd2obj

This project is a small application used to convert from the `TMD` 3D model format used by some PSX games
to a more easily viewed `Wavefront OBJ`. The converter is capable of extracting material colours and texture mapping
information and supports most common TMD packet formats.

Originally developed to convert models from Bubsy 3D, I can't guarantee a lot of compatibility for other titles at this time.

# Building instructions for Linux

In a terminal :

 1. Clone the repo with `git clone  https://github.com/taedixon/tmd2obj.git`  
 2. Cd to the repo directory with `cd tmd2obj`  
 3. Make a build dir, then cd to it : `mkdir build && cd build`  
 4. Compile sources to classes with : `javac -d ./ ../src/*.java`  
 5. Create the jar archive with : `jar cfe ../tmd2obj.jar Converter *`
 6. Move back to parent folder : `cd ../`
 7. Execute program with : `java -jar tmd2obj.jar`

# Usage

You can use any number of tmd files as arguments, and they will be converted to obj. 

```bash
java -jar tmd2obj.jar file1.tmd file2.tmd
```

You can also use a wildcard asterisk (*); e.g : `java -jar tmd2obj.jar some/folder/*.tmd`  

The resulting file(s) will be placed in the same folder as the original.  

If no argument is provided, running the application will automatically search for any files in the `models` directory with a `.tmd` file extension, and
will place converted `*.obj` and `*.mtl` files in the same directory.

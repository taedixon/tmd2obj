# tmd2obj

This project is a small application used to convert from the `TMD` 3D model format used by some PSX games
to a more easily viewed `Wavefront OBJ`. The converter is capable of extracting material colours and texture mapping
information and supports most common TMD packet formats.

Originally developed to convert models from Bubsy 3D, I can't guarantee a lot of compatibility for other titles at this time.

# Usage

Running the application will automatically search for any files in the `models` directory with a `.tmd` file extension, and
will place converted `*.obj` and `*.mtl` files in the same directory.

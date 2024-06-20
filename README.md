About
-----
This is a console-like interface implemented in swing. Java so far is lacking something like this, the only way to achieve something similar is to 
run the program from the command line and use the OS's terminal. However, this is far from a stand alone terminal window to handle very simple
user IO that does not require a fancy GUI.

Building
--------
This is a gradle project, clone the repository and build gradle. Then run the 'build' task, your jar can be found in 'build/libs'

How to use
----------
TinySound is licensed under the BSD 2-Clause license.  A copy of the license can
be found in the header of every source file as well as in the LICENSE file
included with the TinySound system.

Enhancing functionality
-----------------------
TinySound stores all audio as 16-bit, 44.1kHz, 2-channel, linear PCM data
internally.  It makes an effort to convert other formats, but will not be able
to handle all formats.  As for container formats, TinySound should be able to
load any container types supported by your version of Java.  This should include
WAV at the very least.
TinySound can also load Ogg files containing audio in the Vorbis format with the
inclusion of the libraries found in the lib directory.  If you intend to use Ogg
Vorbis files with TinySound just include the jorbis, tritonus_share and
vorbisspi jar files on your CLASSPATH along with TinySound.

Example
-------

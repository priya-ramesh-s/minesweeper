# Minesweeper

WORK IN PROGRESS: An implementation of the well-known video game as a standalone Java Swing application. I also plan to include a command line program in which the board is always 10 &times; 26.

Although in most people's minds the Minesweeper game is closely associated with Microsoft and the earliest versions of the Windows operating system, the general idea of the game seems to be public domain.

I am aware that a few years ago Roberto Virga at Carnegie Mellon University did a Java implementation that was intended for use as an applet in a Web browser. I'm guessing it was a pure AWT application. As of December 20, 2020, I have not looked at any of his source, nor the source of any other already existing implementation.

It almost goes without saying that I don't intend this game to be an applet in a Web browser. For that purpose, it makes more sense to use JavaScript. I have played some JavaScript implementations, but I have not looked at their source.

This project uses JUnit 4. Initially, NetBeans set it up as a JUnit 5 project, but I'm missing something to enable NetBeans to run JUnit 5, so I changed it to JUnit 4.
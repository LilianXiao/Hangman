# Hangman

1. File I/O is integral in my Hangman game.  On a basic level, it can efficiently handle image loading
    and updating mechanisms (as seen in ImageManager.java, where the imageLoader and updateImage methods
    are found).  A BufferedImage field reads the file of the given filepath to create or update a
    JLabel component directly in the Hangman GUI.  Similarly, the WordManager class uses File I/O to
    read from files given the filepath of the word bank I use for this game, dictionary.txt.  This text
    file includes 200 words (individually on each line and all lower case) taken from an online database
    of SAT vocabulary terms.  If the filepath given is null or empty, this constitutes as an invalid
    path in all scenarios and would trigger an IllegalArgumentException.  If the filepath is nonempty
    but is not actually an existing file, this IOException is handled and simply prints that an error
    has occurred.
    File I/O also handles saving essential game data.  In this case, Hangman implements save and load
    functions to store the specific word in a certain game "round" as well as updating every time the
    user clicks on a JButton character (the clicked characters are stored with the hidden word--a
    FileWriter writes this data to the text file gamestate.txt).  The player can save their game, then
    reload it when they want to come back to it, and play regularly.

  2. Collections is implemented using ArrayLists, which are conveniently indexed lists
    for storing essential game information.  In WordManager, the BufferedReader reads from the dictionary
    file and encapsulates the data in the ArrayList<String> wordsList.  I considered using a HashMap
    or TreeMap in the beginning because of the key--value system, but I ultimately decided to use a
    simpler indexed and mutable list like ArrayList to store just dictionary words and the index they
    are at (for easy access).

  3. A central component of the Hangman GUI is a panel of JButtons that the player clicks
    to reveal the hidden word.  Because the number of buttons required is an unchanging value (26 letters)
    and can be stored as such, I implemented a 2D array JButton[][] letterArray that is a 4x7 grid
    populated by these JButtons.  This provides efficient and easy access as well as iteration through
    the JButtons if they require to be changed after user interaction in ActionPerformed.

  4. The final component is the JUnit Testable component that is included mainly to assess the main game
    logic/helper methods in WordManager and ImageManager.  The tests are thoroughly comprehensive and ensure
    that there are no unhandled exceptions in the case that the filepath is null, empty, or nonempty
    but not an actual file.  The JUnit tests were made throughout the development of the rest of the
    game to address any common exceptions arising from null values being passed or incorrect filepaths.

Hangman.java: builds the main layout for the Hangman GUI as well as handling actions.
RunHangman.java: implements Runnable and overrides run to set running game status.
WordManager.java: handles accessing words from the dictionary, pulling random words, and hiding
the word as represented in the actual layout.
ImageManager: handles image loading from image files 0.png through 6.png which are all activated
depending on the state of the numIncorrect guesses the user makes--also handles image updates for
this purpose (receives new filepath and updates JLabel image).
WordManagerTest, ImageManagerTest: JUnit component tests that comprehensively test the key game
logic classes.

There are images 0-6 for the hangman, drawn by myself.
gamestate.txt saves the game state, as will be described.
dictionary.txt includes 200 SAT vocab words from this website:
https://www.collegetransitions.com/blog/sat-vocabulary-words-list/

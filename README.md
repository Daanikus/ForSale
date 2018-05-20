# CHANGES 2018-05-01

- Code now works for fewer than five players
- Logging now uses `java.util.logger` rather than collection in a `StringBuilder`
- Preparation of decks (both Properties and Cheques) is now dealt with by the `prepareDeck` method
  which takes a List of \<T\> and returns a shuffled copy of that list with the correct number of
  cards removed.
- Changed lots of references to `java.util.ArrayList<T>` to `java.util.List<T>`. This should not
  affect user implementations of the `Strategy` interface.
- Add `initialise` method to `Player` class to account for different starting monies.
- Much of the initialisation of the game state now occurs in the constructor of `GameManager` since
  this is a more logical place to put it, the `run` method only runs the game.
- Changed one instance of an indexed for loop without use of the index to a foreach loop.
- Reorder Logging so that correct prices are reported.

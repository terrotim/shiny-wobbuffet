# Projects List

-------

## DNA string matching 

Test program that compares different algorithms to find recurring motifs in a potential string in DNA quickly and accurately.

**Features**

* Currently uses both genetic algorithm and expectation maximization to find hardcoded motifs in a large set of randomly generated DNA strings.

-------

## Fire Emblem Fates Pairing Planner

A small Java application that provides stats and information of all playable units in the game Fire Emblem Fates to facilitate players in pairing up units, either for a casual play or optimized run. User interface uses Slick2d. Trying to address bugs and inaccurate data.

**Features**

* Just drag a unit's image and drop them in the top boxes.  Click the arrows to shift pairings over.

* Unit's growth rates: click on a class to see its full calculated growth rates

* Unit's maximum stats: click on a class to see the unit's maximum stats as that class; otherwise, it lists the stat modifiers

* Unit's pairup bonuses: click on a class to see the full pairup bonus when a unit is a certain class; otherwise, it lists the personal pairup bonuses

* Unit's maximum stats+S-rank pairup bonuses; only works if there is another unit paired up and both units have a class assigned

* Full class set: Changes depending on the pairing

* Full skill pool: Changes depending on class pool. Plan your skills by clicking on a skill to change it to the next skill in the pool.

* Skill Inheritance: Last skill slot of a parent unit will also be available in the associated child unit's skill pool

* Children Pairing: All pairing functionality of the parents apply to the children as well

* Avatar stats: Ability to adjust an Avatar's boon, bane, and talent, letting you see the adjusted growths, max stats, and pairup bonuses

* Availability: Right click a unit to see who that unit can S-rank with; good for seeing who is left that can be paired.

* Save your pairings to a file and load it up again when needed.

-------

## Robot Maneuvering Genetic Algorithm

Project that was started in grad school as a final project that uses genetic algorithm to generate robot AI that can maneuver a generated map and combat enemy bot teams.  Project is being expanded upon to include more parameters to enhance bot decision making. Documentation for use coming soon.

**Features**

* Saves bots generated by the map derived from a file (map currently used is hardcoded into the main java file at the moment).

* Reads a text file that contains map data (ie location of obstacles)

* Saves the best performing bot that the user can choose to send into the map at any time.

-------

## Set game

A small recreation of the card game Set.  Instead of competing against other people, the goal is to find as many "sets" as possible before time runs out.  Finding a set increases the time you have remaining.

**Features**

* Can click the cards with the mouse to select/unselect.

* Can use the keys Q, W, E, R, A, S, D, F, Z, X, C, and V on the keyboard to select cards as well.

* Score and time added depends on how many sets exist in the current card layout.

-------

# Tile Puzzle Genetic Algorithm

Randomly generates a 5x5 tile layout and uses genetic algorithm to attempt to solve the layout. A solution consists of a layout such that a head (circle) matches with the same colored body (square) from another tile. In an ideal solution, each square's edge that is connected to another square's edge generates a proper head and body of the same color.

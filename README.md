<a name="readme-top"></a>

<br />

<h1 align="center">BubbleShooter</h1>

  <p align="center">
BubbleShooter is a classic arcade-style puzzle game implemented in Java. The objective is to clear the field by shooting colored bubbles to form groups of three or more of the same color, causing them to pop and disappear. The game ends when bubbles cross the bottom line.
    <br />
    <div align="center">
      <img src="https://github.com/jurajondovcikupjs/BubbleShooter/blob/master/src/main/java/sk/upjs/ondovcik/juraj/res/promo.png?raw=true" alt="Banner">
    <!-- <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a> https://discord.com/invite/fZNDfG2xv3 -->
  </p>
</div>

## Features
- Colorful bubble graphics and smooth animations
- Sound effects for popping, placing, and game over events
- Toast notifications for game events (confirmations, errors, game over)
- Import/export game state
- Screenshot functionality
- Logitech lighting integration (if supported)

## Gameplay
- Aim and shoot bubbles from the turret at the bottom of the screen
- Match three or more bubbles of the same color to pop them
- Every 5th shot, bubbles move down and two new rows are added
- The game ends if any bubble crosses the bottom line

## Controls
- **Mouse Click**: Aim and shoot bubbles
- **Exit Button**: Quit the game
- **Screenshot Button**: Save a screenshot of the current game state
- **Lighting Button**: Toggle Logitech lighting effects
- **Import/Export Buttons**: Load or save your game progress
- **Reload**: Start a new game

## Requirements
- Java 8 or higher
- [JPAZ2 library](https://github.com/ics-upjs/JPAZ2) (for Turtle graphics)
- (Optional) Logitech Gaming hardware for lighting effects and [Logitech SDK](https://www.logitechg.com/sdk/LED_SDK_9.00.zip)

## Running the Game
1. Get JAR file of the game from the releases section.
2. Place the LogiLED JAR file in a directory of your choice together with the game JAR.
2. Run the game, either by double-clicking the JAR file or using the command line:
   ```
   java -cp BubbleShooter.jar;logiled.jar sk.upjs.ondovcik.juraj.Start
   ```
This is to ensure that the Logitech SDK is available for the game to use.


## License
See [LICENSE.txt](LICENSE.txt) for license information.


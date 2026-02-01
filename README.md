# Bubble Shooter

Bubble Shooter is a classic arcade-style puzzle game implemented in Java. The objective is to clear the field by shooting colored bubbles to form groups of three or more of the same color, causing them to pop and disappear. The game ends when bubbles cross the bottom line.

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

## Requirements
- Java 8 or higher
- [JPAZ2 library](https://github.com/ics-upjs/JPAZ2) (for Turtle graphics)
- (Optional) Logitech Gaming hardware for lighting effects and [Logitech SDK](https://www.logitechg.com/sdk/LED_SDK_9.00.zip)

## Running the Game
1. Build the project using Maven:
   ```
   mvn clean package
   ```
2. Run the game:
   ```
   java -cp target/classes;src/main/java/sk/upjs/ondovcik/juraj/libs/logiled.jar sk.upjs.ondovcik.juraj.Field
   ```

## Assets
- All images and sounds are located in `src/main/resources/sk/upjs/ondovcik/juraj/res/`

## License
See [LICENSE.txt](LICENSE.txt) for license information.


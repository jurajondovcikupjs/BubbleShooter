<a name="readme-top"></a>

<br />

<h1 align="center">BubbleShooter</h1>

  <p align="center">
BubbleShooter is a classic arcade-style puzzle game implemented in Java. The objective is to clear the field by shooting colored bubbles to form groups of three or more of the same color, causing them to pop and disappear. The game ends when bubbles cross the bottom line.
    <br />
    <div align="center">
      <img src="https://github.com/GeorgeYT9769/cardabase-app/blob/main/fastlane/metadata/android/en-US/images/featureGraphic.png?raw=true" alt="Banner">
    <!-- <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a> https://discord.com/invite/fZNDfG2xv3 -->
    <br />
    <div align="center">
      <img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/GeorgeYT9769/cardabase-app?style=for-the-badge&label=Stars">
      <img alt="GitHub forks" src="https://img.shields.io/github/forks/GeorgeYT9769/cardabase-app?style=for-the-badge&label=Forks">
      <img alt="GitHub license" src="https://img.shields.io/github/license/GeorgeYT9769/cardabase-app?style=for-the-badge&label=License">
      <img alt="GitHub Downloads (all assets, all releases)" src="https://img.shields.io/github/downloads/GeorgeYT9769/cardabase-app/total?style=for-the-badge&label=Downloads">
      <br />
      <a href="https://discord.com/invite/fZNDfG2xv3">
        <img alt="Discord" src="https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white">
      </a>
    </div>
  </p>
</div>

<br />


BubbleShooter is a classic arcade-style puzzle game implemented in Java. The objective is to clear the field by shooting colored bubbles to form groups of three or more of the same color, causing them to pop and disappear. The game ends when bubbles cross the bottom line.

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


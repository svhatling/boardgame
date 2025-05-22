# Board Game Application – Digital Snakes & Ladders and Quiz
## Project: IDATx2003 – Group 002
This project is developed as part of the course **IDATT2003 – Programming 2** at **NTNU** (Norwegian University of Science and Technology).  
It is a two-person project with the goal of building a Java application for digital board games with a graphical user interface.

---

## Project Description
**Board Game Application** is a desktop application built to provide players with a digital experience of classic board games. 
The system allows users to:
- Choose between different game variants (snakes & ladders and quiz)
- Play with up to 5 players simultaneously
- Choose between different game boards via configuration files
- Enjoy graphical user interface
- Compete to reach the goal first in a fun and engaging gaming experience

The application follows MVC architecture and utilizes established software development principles such as SOLID and low coupling/high cohesion.

---

## How to Use the App

### 1. **Choose Game Variant**
- Start the application and select from available game variants
- Snakes & Ladders: Classic board with ladders up and snakes down
- Quiz: Combination of knowledge questions and movement

### 2. **Set Up the Game**
- Choose number of players (2-5 players)
- Let each player select their unique piece/figure
- Give players names for personalization
- Players can be chosen from already saved players

### 3. **Play**
- Click the dice button to roll dice
- Move automatically based on dice roll
- Perform special actions when landing on special fields

### 4. **Finish**
- See the winner being crowned
- Start new game or check out the other one

---

## Smart Features
- **Flexible Game Boards**  
  Different board layouts can be loaded from JSON configuration files
- **Realistic Dice Rolling**  
  Two dice with random generation
- **Special Fields**  
  Ladders, snakes and other interactive elements
- **Graphical Interface**  
  Intuitive and colorful user interface
- **Quiz Mode**  
  Combination of knowledge and luck
- **Player Storage**  
  Player data saved in CSV format

---

## Technical Architecture
- **Design Pattern**: Model-View-Controller (MVC)
- **Data Storage**: JSON (game boards/questions) and CSV (player data)
- **User Interface**: JavaFX 
- **Programming Language**: Java
- **Principles**: SOLID, low coupling, high cohesion

---

## Notes for Developers (IDATT2003)
- Built using Java and JavaFX
- Data stored in JSON and CSV files for flexibility
- Follows MVC architecture for good code structure
- Implements established design principles
- All use of AI tools is documented in report

---

## Game Rules

### Snakes & Ladders
1. Each player starts at field 1
2. Roll two dice and move the sum of the eyes
3. Land at the bottom of a ladder → climb up
4. Land at the top of a snake → slide down
5. First to field 90 wins!

### Quiz
1. Answer questions to be able to move
2. Correct answer = 1 point
3. Wrong answer or skip = 0 points
4. Combination of knowledge and strategy

---

Thank you for using **Board Game Application** – where classic games meet modern technology for maximum fun!

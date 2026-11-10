# Duel Arena – A 2D Combat Game

## Project Details

**Project Name:** Duel Arena – A 2D Combat Game

**Course:** Bachelor of Computer Applications (BCA)

**Programming Language:** Java

**Database:** MySQL

**Development Environment:** IntelliJ IDEA


## About the Project

Duel Arena is a 2D fighting game developed using Java. It is a two-player game in which both players compete against each other in a virtual arena.

The main idea behind developing this project was to create an interactive game where two players can choose their characters, select an arena and fight against each other. The game includes different features such as character movement, shooting, health management, power-ups, shields and a leaderboard.

While developing this project, we worked with Java programming, Java Swing, database connectivity and different programming concepts. We also focused on making the game easy to understand and interesting to play.

## Features of the Game

The game includes the following features:

- **Two-player gameplay:** Two players can play against each other on the same screen.
- **Character selection:** Players can choose their characters before starting the game.
- **Arena selection:** Different backgrounds are available for players to choose from.
- **Player names:** Players can enter their own names before the match.
- **Movement and shooting:** Both players can move around the arena and shoot at their opponents.
- **Health system:** Each player has a health bar that decreases when they get hit.
- **Power-ups:** Players can collect power-ups during the match.
- **Shield system:** Shields provide additional protection during gameplay.
- **Adaptive balancing:** The game includes a feature that helps balance the match when one player is significantly behind.
- **Sound and music:** Background music and shooting sound effects make the gameplay more interactive.
- **Winner screen:** After the match ends, the winner's name and match result are displayed.
- **Leaderboard:** Players can view their scores and performance.
- **Match history:** Completed match details are stored in the database.

## Technologies Used

The following technologies and tools were used to develop this project:

| Technology | Use |
|---|---|
| Java | Main programming language |
| Java Swing | Designing the game interface |
| Java AWT | Graphics and keyboard controls |
| MySQL | Storing player and match information |
| JDBC | Connecting Java with MySQL |
| IntelliJ IDEA | Writing and running the code |
| GitHub | Managing and storing project files |

## How the Game Works

The game begins from the main menu where players can access the game.

Initially, both players enter their names and select their respective characters. After this step, they have to select the arena for the game.

With the selections being done, the game begins by a countdown. Now, both players can move and shoot each other inside the arena using the shooting controls.

Additionally, during the game, the players can use some power ups and shields. The health of the players is shown on the screen to monitor the game.

After the completion of the game, the name of the winner will be shown on the winner screen. In addition, the information about the match will be stored in the database and will be used for the leaderboard.

## Game Controls

| **Action** | **Player 1** | **Player 2** |
| --- | --- | --- |
| **Move Up** | **W** | **Up Arrow** |
| **Move Down** | **S** | **Down Arrow** |
| **Move Left** | **A** | **Left Arrow** |
| **Move Right** | **D** | **Right Arrow** |
| **Shoot** | **F** | **L** |

## Project Folder Structure

The project includes all necessary Java source files and resources to run the game.

```text
DuelArena/
│
├── src/
│   └── duelarena/
│       ├── Game.java
│       ├── GamePanel.java
│       ├── Player.java
│       ├── Bullet.java
│       ├── PowerUp.java
│       ├── ShieldPowerUp.java
│       ├── MainMenuPanel.java
│       ├── ArenaSelectionPanel.java
│       ├── WinnerPanel.java
│       ├── LeaderboardPanel.java
│       ├── LeaderboardDAO.java
│       ├── MatchHistoryDAO.java
│       └── PlayerAccount.java
│
├── resources/
│   ├── arenas/
│   ├── menu_background/
│   └── sounds/
│
├── .gitignore
└── README.md
```

*This is just the basic folder structure of the project. There might be more files inside it.*

## Database Connectivity

MySQL database is being used for storing the data related to players and matches.

We have used JDBC to connect our Java application with MySQL database.

The database name is `duel_arena_db`.

This database manages data related to players information, match information, winners and points. This information is then being used by leaderboard to display player performance.

## How to Run the Project

Following steps should be followed to run this project on your computer:

1. Install Java JDK on your computer.
2. Install IntelliJ IDEA.
3. Clone or download this repository from GitHub.
4. Import the project in IntelliJ IDEA.
5. Add MySQL connector/j library in the project.
6. Make sure that MySQL is installed and running.
7. Create a database named `duel_arena_db`.
8. Verify the database connection details in the Java program.
9. Ensure that all the required images, background and sound files are present in the resources folder.
10. Run `Game.java` file to start the game.

## What We Learned

While developing Duel Arena, we have got a chance to practically use those concepts which we have studied in our programming subjects.

We have learned how to create GUI using Java Swing, handling keyboard events, controlling players and implementing other game features.

We have also practically used MySQL and JDBC by connecting our game with database. In addition to coding, this project has taught us how various classes and features can be integrated to create one application.

## Future Improvements

There are still a lot of improvements that we can do in the game. Some of the improvements that we would like to make in the future are:

1. Adding more characters to the game.
2. Creation of new arenas.
3. Improving the animation of the game.
4. Improving other gameplay features.

We can also improve the leaderboard feature by adding more statistics and improving the display of player performance.


## Acknowledgement

Duel Arena is an academic project developed as part of our BCA course. Working on this project gave us practical experience in Java programming, interface development, database connectivity and teamwork.

It also helped us understand how the concepts we learn in class can be used to develop a working software application.

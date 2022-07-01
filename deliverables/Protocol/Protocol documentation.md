# Eriantys Protocol Documentation

Niccolò Nicolosi, Emanuele Musto, Francesco Maccari

Group 9

## Messages

### AckMessage

Sent from server to client to confirm a generic message has been acknowledged

#### Arguments

- No arguments

#### Possible responses

- No responses

### InvalidResponseMessage

Sent from server to client to notify the client that the response it sent is not included 
in the possible responses of the message it is responding to, therefore it will be ignored

#### Arguments

- Request: the request message needing a response
- Response: the invalid response received

#### Possible responses

- No responses

### AbortMessage

Sent from client to server to abort the current operation

#### Arguments

- No arguments

#### Possible responses

- No responses

### ConnectedMessage

Sent from server to client to notify the client of the successful connection to the server

#### Arguments

- No arguments

#### Possible responses

- No responses

### ChooseUsernameMessage

Sent from server to client to request the choice of a username

#### Arguments

- No argument

#### Possible responses

- UsernameChoiceMessage: if the client wants to continue joining a game lobby
- AbortMessage: if the client wants to abort joining a game lobby

### UsernameChoiceMessage

Sent from client to server to communicate the choice of a username

#### Arguments

 - Username: the chosen username

#### Possible responses

- AckMessage: if the chosen username is valid
- InvalidUsernameMessage: if the chosen username is invalid
- InvalidResponseMessage: if the server was not expecting this message as response

### InvalidUsernameMessage

Sent from server to client if the chosen username is invalid

#### Arguments

- InvalidFormat: true if the format of the chosen username is invalid, false otherwise
- AlreadyTaken: true if the chosen username is already taken

#### Possible responses

- UsernameChoiceMessage: if the client wants to continue joining a game lobby
- AbortMessage: if the client wants to abort joining a game lobby

### ChooseGameSettingsMessage

Sent from server to client if the client is the first to connect

#### Arguments

- No arguments

#### Possible responses

- GameSettingsMessage: if the client wants to continue the creation of the game
- AbortMessage: if the client wants to abort the creation of the game

### GameSettingsMessage

Sent from client to server to communicate the chosen game settings

#### Arguments

- GameSettings: an object containing all the game settings, like the number of players and the game mode 

#### Possible responses

- AckMessage: if the chosen game settings are valid
- InvalidGameSettings: if the chosen game settings are invalid
- InvalidResponseMessage: if the server was not expecting this message as response

### InvalidGameSettingsMessage

Sent from server to client if the chosen game settings are invalid

#### Arguments

- No arguments

#### Possible responses

- GameSettingsMessage: if the client wants to continue the creation of the game
- AbortMessage: if the client wants to abort the creation of the game

### GameJoinedMessage

Sent from server to client if the client successfully joined the game lobby

#### Arguments

- Players: the list of players currently connected to the game lobby
- GameSettings: the settings of the joined game lobby

#### Possible responses

- No response

### NewPlayerJoinedMessage

Sent from server to all clients (except the one that has just joined) if any new client has joined the game lobby

#### Arguments

- Players: the list of players currently connected to the game lobby

#### Possible responses

- No response

### DisconnectMessage

Sent from client to server if the client wants to disconnect

#### Arguments

- No arguments

#### Possible responses

- No response

### PlayerDisconnectedMessage

Sent from server to all clients if any of the clients disconnected

#### Arguments

- No arguments

#### Possible responses

- No response

### StartingGameMessage

Sent from server to all clients whenever the requested amount of player is reached and the game is starting

#### Arguments

- Players: a sorted list of the username of every player
- Update: an object containing all the changes to apply to the initialized view

#### Possible responses

- No response

### UpdateGameMessage

Sent from server to all clients whenever the game state is updated

#### Arguments

- Update: an object containing all the changes to apply to the view

#### Possible responses

- No response

### MoveRequestMessage

Sent from server to client whenever a move from the player is requested

#### Arguments

- MoveType: the requested move

#### Possible responses

- PerformedMoveMessage: if the player chooses his move in time

### PerformedMoveMessage

Sent from client to server whenever the player chooses his move

#### Arguments

- Move: the performed move

#### Possible responses

- AckMessage: if the performed move is valid
- InvalidMoveMessage: if the performed move is invalid
- InvalidResponseMessage: if the server was not expecting this message as response

### InvalidMoveMessage

Sent from server to client if the chosen move is invalid

#### Arguments

- Cause: an explanation of why the move is invalid

#### Possible responses

- PerformedMoveMessage: if the player chooses his move in time

### GameEndedMessage

Sent from server to all clients when the game ends

#### Arguments

- WinnerTeam: the team that won the game

#### Possible responses

- No responses

## Scenarios

### Joining game

![Joining_game](sequencediagram.org%20code/Initial%20Protocol/Joining_game.png)

Client1 is the first client that connects to the Server. The Server notifies Client1 of the successful connection
and asks to provide a username. Client1 provides a username that can be either valid or invalid. Client1 continues
providing new usernames until one is valid. When a valid username is received, the Sever send an ack and asks 
Client1 to provide the game settings. Client1 continues providing new settings until they are valid.
Whenever valid settings are received, the Server notifies Client1 that the game was joined. After that Client2
and Client3 connect to the Server, they are notified of the successful connection and they are asked to provide
a username. They get notified when they join the game. Whenever a client connects, all the other clients get notified.
After the required number of player is reached, the game is started and every client is notified of the initial
game state. Every time a client is asked to provide information, it can provide an abortMessage instead. Whenever
this happens the Server sends a PlayerDisconnectedMessage to every client and shuts down the game (not
reported in the diagram for clarity)

### Planning phase

![Planning_phase](sequencediagram.org%20code/Initial%20Protocol/Planning_phase.png)

The Server fills the clouds with new students and notifies every client of the change. Then, Client1 is asked to 
select a helper card by performing a move in a certain amount of time. If the submitted move is invalid and the time
has not expired yet, the client can submit a new one. Once the time is up, the move is chosen randomly if invalid or 
not received by the Server. After the move has been chosen, the Server updates the game and notifies all the clients.
After that, every other client is asked to select a helper the same way (not reported in the diagram for brevity)

### Action phase: moving students

![Action_phase_1](sequencediagram.org%20code/Initial%20Protocol/Action_phase_1.png)

Client1 is asked to position 3 of the students in its school entrance either on an island or in its school by performing
3 different moves, each in a certain amount of time. If any of the submitted moves is invalid and the time has not expired
yet, the client can submit a new one. Once the time is up, the move is chosen randomly if invalid or not received by the 
Server. After the move has been chosen, the Server updates the game and notifies all the clients. 

### Action phase: moving mother nature

![Action_phase_2](sequencediagram.org%20code/Initial%20Protocol/Action_phase_2.png)

Client1 is asked to move mother nature by performing a move in a certain amount of time. If the submitted move is invalid 
and the time has not expired yet, the client can submit a new one. Once the time is up, the move is chosen randomly if 
invalid or not received by the Server. After the move has been chosen, the Server updates the game and notifies all the 
clients. 

### Action phase: choosing clouds

![Action_phase_3](sequencediagram.org%20code/Initial%20Protocol/Action_phase_3.png)

Client1 is asked to choose a cloud by performing a move in a certain amount of time. If the submitted move is invalid
and the time has not expired yet, the client can submit a new one. Once the time is up, the move is chosen randomly if
invalid or not received by the Server. After the move has been chosen, the Server updates the game and notifies all the
clients. 

### Game ending

![Game_ending](sequencediagram.org%20code/Initial%20Protocol/Game_ending.png)

Whenever the Server updates the game and acknowledges the game ending, the last update is sent to every client and subsequently
every client is notified of the game ending.
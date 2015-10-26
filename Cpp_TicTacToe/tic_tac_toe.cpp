#include <iostream>
using namespace std;

// function prototypes

/*
Inputs:		Array that holds the game "board"
Function:	Initiates and manages gameplay
Called by:	main
*/
void playGame(char board[]);

/*
Inputs:		Char of which player's turn it is + array that holds game "board"
Function:	Plays out the turn of whichever player is up
Called by:	playGame
*/
char play(char p, char board[]);

/*
Inputs:		Array that holds the game "board"
Function:	Displays the current "board"
Called by:	play
*/
void displayArray(char board[]);

/*
Inputs:		Current player + array that holds the game "board"
Function:	Returns whether X wins, O wins, T wins, or if game is not yet over
Called by:	play
*/
char score(char player, char board[]);

// entry point of program
int main() {
	char board[9];
	playGame(board);
	return 0;
}

/*
Inputs:		Pointer to the char of the current player
Function:	Switch the current player
Called by:	playGame
*/
void switchPlayers(char* current_player) {
	if (*current_player == 'X') {
		*current_player = 'O';
	}
	else {
		*current_player = 'X';
	}
}

void playGame(char board[]) {
	// initialize board
	for (int i = 0; i < 9; i++) {
		board[i] = i + '1'; // + '1' instead of + '0' because I want to add an extra 1 so its (1 - 9)
	}

	char current_player = 'X'; // player X starts
	displayArray(board); // display board
	char winner = play(current_player, board); // play the first round

	while (winner == '\0') { // while the game isn't over, keep playing
		switchPlayers(&current_player); // switch players
		winner = play(current_player, board); // play the next round
		if (winner == 'T') {
			cout << "Tie. Better luck next time." << endl;
			char exit;
			cin >> exit;
		}
		else if (winner != '\0') { // if winner != 'T' && winner != '\0' than it's either 'X' or 'Y'
			cout << "Player " << current_player << " wins! Congratulations" << endl;
			char exit;
			cin >> exit;
		}
	}
}

/*
Inputs:		Digit of place on board + board
Function:	Check if place on board is valid
Called by:	play
*/
bool isValidSpot(int digit, char board[]) {
	return (digit >= 0 && digit <= 8 && board[digit] == digit + '1');
}

char play(char current_player, char board[]) {
	//promt user for their turn
	int digit = 0;
	cout << "Player " << current_player << ", please enter the number in the display (1 - 9) where you wish your mark to be entered." << endl;
	cin >> digit;
	digit--; // subtract 1 to get from user inputted range (1 - 9) into index range (0 - 8)

	while (!isValidSpot(digit, board)) { //make sure spot is valid
		cout << "The spot you entered is either not in range, or is already taken. Please enter another number in the display (1 - 9) where you wish your mark to be entered" << endl;
		cin >> digit;
		digit--; // subtract 1 to get from user inputted range (1 - 9) into index range (0 - 8)
	}
	board[digit] = current_player; // set the place on the board to the character of the current player

	//system("cls"); // clear screen - Windows only
	cout << string( 100, '\n' );  // clear screen-ish - platform independent
	displayArray(board); // display board

	return score(current_player, board);
}

void displayArray(char board[]) {
	for (int y = 0; y < 3; y++) {
		for (int x = 0; x < 3; x++) {
			cout << board[y * 3 + x] << '\t';
		}
		cout << endl;
		cout << endl;
	}
}

/*
Inputs:		char of player + starting index + board
Function:	Check if player won on row with starting index
Called by:	score
*/
bool checkRow(char player, int start_index, char board[]) {
	return (board[start_index] == player && board[start_index + 1] == player && board[start_index + 2] == player);
}

/*
Inputs:		char of player + starting index + board
Function:	Check if player won on column with starting index
Called by:	score
*/
bool checkColumn(char player, int start_index, char board[]) {
	return (board[start_index] == player && board[start_index + 3] == player && board[start_index + 6] == player);
}

/*
Inputs:		char of player + board
Function:	Check if player won on diagonals
Called by:	score
*/
bool checkDiagonals(char player, char board[]) {
	return (board[0] == player && board[4] == player && board[8] == player) || (board[2] == player && board[4] == player && board[6] == player);
}

char score(char player, char board[]) {
	//check winner - only need to check the player that just went
	bool winner =
		checkRow(player, 0, board) ||
		checkRow(player, 3, board) ||
		checkRow(player, 6, board) ||

		checkColumn(player, 0, board) ||
		checkColumn(player, 1, board) ||
		checkColumn(player, 2, board) ||

		checkDiagonals(player, board);

	if (winner) {
		return player;
	}

	//display # of plays yet to be made
	int open_spots = 0;
	for (int i = 0; i < 9; i++) {
		if (board[i] == i + '1') {
			open_spots++;
		}
	}
	cout << "There are " << open_spots << " plays yet to be made" << endl;
	if (open_spots == 0) {
		return 'T';
	}

	//player didn't win, and wasn't a tie, so keep playing
	return '\0';
}

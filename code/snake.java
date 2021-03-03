import java.util.List;
import java.util.Scanner;

class Game
{
	
	    /////////////////////////
	   //                     //
	  //       Classes       //
	 //                     //
	/////////////////////////
	
	// Stocke des coordonnées x,y dans une seule variable (-> un seul tableau)
	class Pos
	{
		public int x;
		public int y;
		
		// Initialisation par défaut
		public Pos()
		{
			this.x = 0;
			this.y = 0;
		}
		
		// Initialisation avec une valeur
		public Pos(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}

	    /////////////////////////
	   //                     //
	  //      Constants      //
	 //                     //
	/////////////////////////

	private static int DEFAULT_BOARD_SIZE = 10;

	    /////////////////////////
	   //                     //
	  //   Class variables   //
	 //                     //
	/////////////////////////

	// 'board' et 'snake' pas des classes : problème avec la fonction moveSnake()
	protected int[][] board; // [x][y]; -1 = unused, 0 = empty, 1 = wall, 2 = snake, 3 = food
	protected Pos[] snake; // snake[0] = head, snake[snake.length - 1] = tail
	protected char direction; // U = up, D = down, L = left, R = right
	protected int size;

	    /////////////////////////
	   //                     //
	  //   Board functions   //
	 //                     //
	/////////////////////////

	// Initialiseur de board
	public void initBoard()
	{
		initBoard(DEFAULT_BOARD_SIZE);
	}
	
	// Initialiseur de board
	public void initBoard(int size)
	{
		initBoard(size, size);
		this.size = size;
	}
	
	// Initialiseur de board
	public void initBoard(int height, int width)
	{
		this.board = new int[height * 2 + 1][width * 2 + 1];
		for(int x = 0; x < board.length; x ++) {
			for(int y = 0; y < board[x].length; y ++) {
				if(x % 2 == 0 || y % 2 == 0) {
					this.board[x][y] = -1;
				}
				else {
					this.board[x][y] = 0;
				}
			}
		}
	}

	// currentCase == food
	public boolean isFood(Pos pos)
	{
		int toTest = this.board[pos.x][pos.y];
		if(toTest == 3) {
			return true;
		}
		return false;
	}
	
	// currentCase == snake
	public boolean isSnake(Pos pos)
	{
		int toTest = this.board[pos.x][pos.y];
		if(toTest == 2 || toTest == 4) {
			return true;
		}
		return false;
	}

	// currentCase == wall
	public boolean isWall(Pos pos)
	{
		int toTest = this.board[pos.x][pos.y];
		if(toTest == 1) {
			return true;
		}
		return false;
	}
	
	// Affichage dans la console, inutile avec swing
	public void printBoard()
	{
		for(int x = 0; x < board.length; x++) {
			if(x % 2 == 0) {
				for(int y = 0; y < board[x].length; y++) {
					switch(board[x][y]) {
					case -1:
						System.out.print(" ");
							break;
					case 1:
						System.out.print("------");
							break;
					}
				}
				System.out.print("\n");
			}
			else {
				for(int i = 0; i < 3; i++) {
					for(int y = 0; y < board[x].length; y++) {
						switch(board[x][y]) {
						case -1:
							System.out.print(" ");
							break;
						case 0:
							System.out.print("      ");
							break;
						case 1:
							System.out.print("|");
							break;
						case 2:
							System.out.print("OOOOOO");
							break;
						case 3:
							System.out.print("......");
							break;
						}
					}
					System.out.print("\n");
				}
			}
		}
	}
	
	// currentCase = empty
	public void setEmpty(Pos pos)
	{
		this.board[pos.x][pos.y] = 0;
	}
	
	// currentCase = food
	public void setFood(int x, int y)
	{
		this.board[x][y] = 3;
	}
	
	// currentCase = snake
	public void setSnake(Pos pos)
	{
		this.board[pos.x][pos.y] = 2;
	}
	
	// currentCase = wall
	public void setWall(int x, int y)
	{
		this.board[x][y] = 1;
	}

	public void randomFood(){
        int x = (int)(Math.random() * this.size) * 2 + 1;
        int y = (int)(Math.random() * this.size) * 2 + 1;
        while(isSnake(new Pos(x,y))) {
            x = (int)(Math.random() * this.size) * 2 + 1;
            y = (int)(Math.random() * this.size) * 2 + 1;
        }
        this.setFood(x, y);
    }

	    /////////////////////////
	   //                     //
	  //   Snake functions   //
	 //                     //
	/////////////////////////

	// Initialiseur de snake
	public void initSnake(int x, int y, char direction)
	{
		this.snake = new Pos[1];
		this.snake[0] = new Pos(x, y);
		this.direction = direction;
		this.setSnake(snake[0]);
	}

	public boolean moveSnake()
	{
    boolean isFood = false;
    Pos tempPos1 = new Pos(this.snake[0].x, this.snake[0].y);
    Pos tempPos2;
    
    // Avance de 1 (sur une case potentiellement de mur)
    switch(this.direction) {
    case 'U':
        this.snake[0].x--;
        break;
    case 'D':
        this.snake[0].x++;
        break;
    case 'L':
        this.snake[0].y--;
        break;
    case 'R':
        this.snake[0].y++;
        break;
    }
    
    // Test : currentCase == wall
    if(this.isWall(snake[0])) {
        this.end();
        return false;
    }
    
    // Avance de 1 (sur une case normale)
    else {
        switch(this.direction) {
        case 'U':
            this.snake[0].x--;
            break;
        case 'D':
            this.snake[0].x++;
            break;
        case 'L':
            this.snake[0].y--;
            break;
        case 'R':
            this.snake[0].y++;
            break;
        }
    }
    
    if(this.isFood(snake[0])) {
        isFood = true;
    }
    
    // Déplace le serpent, case par case
    for(int i = 1; i < snake.length; i++) {
        tempPos2 = tempPos1;
        tempPos1 = this.snake[i];
        this.snake[i] = tempPos2;
    }
    
    setEmpty(tempPos1);
        
    // Tests
    if(this.isSnake(snake[0])) {
        this.end();
        return false;
    }
    
    // Change currentCase en serpent
    this.setSnake(snake[0]);
    
    // Si il y a de la nourriture, agrandit le serpent de 1 case, là où la dernière case était avant
    if(isFood) {
        Pos[] newSnake = new Pos[this.snake.length + 1];
        for(int i = 0; i < snake.length; i++) {
            newSnake[i] = snake[i];
        }
        snake = newSnake;
        snake[snake.length - 1] = tempPos1;
        if(this.snake.length >= this.size * this.size) {
            this.end();
            return false;
        }
        this.randomFood();
    }
    
    return true;
	}
	
	// this.direction = direction
	public void setDirection(char direction)
	{
		if(direction == 'U' || direction == 'D' || direction == 'L' || direction == 'R') {
			this.direction = direction;
		}
	}

	    /////////////////////////
	   //                     //
	  //   Game functions    //
	 //                     //
	/////////////////////////
	
	public Game(int size)
	{
		this.initBoard(size);
		this.initSnake(1, 1, 'D');
		randomFood();
	}
	
	// Todo when loosing (don't stop the program)
	public void end()
	{
		System.out.println("End of the game.");
	}
}

class Main
{
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		// Set the board size
		System.out.print("Entrez une taille : ");
		int size = input.nextInt();
		
		Game game = new Game(size);
		
		// Put walls on borders of the board
		for(int i = 1; i < size * 2; i += 2) {
			game.setWall(i,0);
			game.setWall(i,size * 2);
			game.setWall(0,i);
			game.setWall(size * 2,i);
		}
		
		boolean isGame = true;
		
		// while not returning false to moveSnake()
		while(isGame) {
			game.printBoard();
			System.out.print(">>> ");
			char direction = Character.toUpperCase(input.next().charAt(0));
			game.setDirection(direction);
			isGame = game.moveSnake();
		}
	}
}
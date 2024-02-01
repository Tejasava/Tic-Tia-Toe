import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe{
    int boardWidth = 600;
    int boardHeight = 650;//50px for the text panel on top

    JFrame frame= new JFrame();
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    String playerX = "x";
    String playerO = "o";
    String currentPlayer = playerX;


    //this is gona to be used for condition of gameover
    boolean gameOver = false;

    //for tie condition
    int turns = 0 ; 


    //constructor
    TicTacToe(){
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//This will help to close the window when we have to close it by just clicling on x
        frame.setLayout(new BorderLayout());


        //heading design
        textLabel.setBackground(Color.black);
        textLabel.setForeground(Color.blue);
        textLabel.setFont(new Font("Arial",Font.BOLD,50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel,BorderLayout.NORTH);

        //for board panel design
        boardPanel.setLayout(new GridLayout(3,3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel); 

        //adding buttons by using for loops
        for(int r = 0; r < 3; r++){

            for(int c=0; c<3; c++){

                JButton tile = new JButton();
                board[r][c]= tile;
                boardPanel.add(tile);

                //styling the buttons
                tile.setBackground(Color.black);
                tile.setForeground(Color.blue);
                tile.setFont(new Font("Arial",Font.BOLD,120));
                tile.setFocusable(false);
                //tile.setText(currentPlayer);

                tile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JButton tile = (JButton) e.getSource();

                        //game over condition
                        if (gameOver) return;


                        //this if condition is used for overwrittion now we cannor overwrite x and o
                        if (tile.getText()==""){
                            tile.setText(currentPlayer);

                            //for tie condition
                            turns++;
                            
                            //this function is for checking the winner
                            checkWinner();
                            if(!gameOver){
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                            textLabel.setText(currentPlayer + "'s turns");

                            }
                        }
                        
                    }
                });

            }
        }

    }//all winning conditions are here
    void checkWinner(){
        //horizontal winning condition
        for (int r=0; r<3; r++){
            if (board[r][0].getText() == "") continue;

            //this condition will stop the game then and there.
        
            if (board[r][0].getText() == board[r][1].getText() &&
                board[r][1].getText() == board[r][2].getText()) {
                    //for chainging background color and font color
                    for(int i = 0;i<3;i++)
                    {
                        setWinner(board[r][i]);
                    }
                    gameOver = true;
                    return;
                }
        }

        //winning condition form vertical
        for(int c = 0 ; c < 3; c++){

            if (board[0][c].getText() == "" ) continue;

            if (board[0][c].getText ()== board[1][c].getText() &&
                board[1][c].getText ()== board[2][c].getText()){
                    for(int i = 0 ; i < 3; i++){
                        setWinner(board[i][c]);
                    }
                    gameOver = true;
                    return;
                }
        }

        // winning condition form dioganal side
        if(board[0][0].getText() == board[1][1].getText()&&
            board[1][1].getText() == board[2][2].getText()&&
            board[0][0].getText() != "" ){
                for(int i = 0; i<3 ;i++){
                    setWinner(board[i][i]);
                }
                gameOver = true;
                return;
            }

        //winning condition form anti dioganalo side
        if (board[0][2].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][0].getText() &&
            board[0][2].getText() !="")
            {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            gameOver = true;
            return;
            }
            if (turns == 9){
                for (int r = 0; r < 3; r++){
                    for(int c = 0; c < 3; c++){
                        setTie(board[r][c]);
                    }
                }
                gameOver = true ; 
            }


    }//set winner function

    void setWinner(JButton tile){
        tile.setForeground(Color.green);
        tile.setBackground(Color.black);
        textLabel.setText(currentPlayer + "'s is the winner");
    }
     //set for tie
     void setTie(JButton tile){
        tile.setForeground(Color.red);
        tile.setBackground(Color.black);
        textLabel.setText("TIE!");
     }
}









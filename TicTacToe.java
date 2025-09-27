import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import javax.swing.Timer;
import javax.swing.border.*;  // Add this for Border classes

public class TicTacToe{
    int boardWidth = 600;
    int boardHeight = 650;//50px for the text panel on top
    Timer animationTimer;
    Color hoverColor = new Color(30, 30, 30);
    float animationAlpha = 0.0f;
    Color glowColor = new Color(0, 150, 255, 50);
    int glowRadius = 20;

    JFrame frame= new JFrame();
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    GlowButton[][] board = new GlowButton[3][3];

    // Custom button class with glow effect
    class GlowButton extends JButton {
        private float glowIntensity = 0.0f;
        private boolean isGlowing = false;

        public GlowButton() {
            setOpaque(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw glow effect
            if (isGlowing) {
                int glow = (int) (glowIntensity * glowRadius);
                Color baseGlow = new Color(
                    glowColor.getRed(),
                    glowColor.getGreen(),
                    glowColor.getBlue(),
                    (int)(glowColor.getAlpha() * glowIntensity)
                );

                for (int i = glow; i > 0; i--) {
                    g2d.setColor(new Color(
                        baseGlow.getRed(),
                        baseGlow.getGreen(),
                        baseGlow.getBlue(),
                        baseGlow.getAlpha() / (i)
                    ));
                    g2d.fillRoundRect(i/2, i/2, getWidth()-i, getHeight()-i, 10, 10);
                }
            }

            // Draw button background
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

            // Draw the text
            super.paintComponent(g2d);
            g2d.dispose();
        }

        public void setGlowing(boolean glowing) {
            this.isGlowing = glowing;
            repaint();
        }

        public void setGlowIntensity(float intensity) {
            this.glowIntensity = intensity;
            repaint();
        }
    }
    String playerX = "x";
    String playerO = "o";
    String currentPlayer = playerX;


    //this is gona to be used for condition of gameover
    boolean gameOver = false;

    //for tie condition
    int turns = 0 ; 


    //constructor
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }

    TicTacToe(){
        setupAnimationTimer();
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

                GlowButton tile = new GlowButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                //styling the buttons
                tile.setBackground(Color.black);
                tile.setForeground(Color.blue);
                tile.setFont(new Font("Arial",Font.BOLD,120));
                tile.setFocusable(false);
                
                // Creating a raised bevel border with custom colors
                Border compound = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 2),
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                        new Color(60, 60, 60),
                        new Color(40, 40, 40),
                        new Color(30, 30, 30),
                        new Color(50, 50, 50))
                );
                tile.setBorder(compound);
                
                // Add hover effect
                tile.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (tile.getText().isEmpty() && !gameOver) {
                            tile.setBackground(hoverColor);
                            tile.setGlowing(true);
                            animateGlow(tile, true);
                        }
                    }
                    
                    public void mouseExited(MouseEvent e) {
                        if (tile.getText().isEmpty()) {
                            tile.setBackground(Color.black);
                            animateGlow(tile, false);
                        }
                    }
                });

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

    private void animateGlow(GlowButton button, boolean glowIn) {
        Timer glowTimer = new Timer(20, null);
        glowTimer.addActionListener(new ActionListener() {
            float intensity = glowIn ? 0.0f : 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (glowIn) {
                    intensity += 0.1f;
                    if (intensity >= 1.0f) {
                        intensity = 1.0f;
                        glowTimer.stop();
                    }
                } else {
                    intensity -= 0.1f;
                    if (intensity <= 0.0f) {
                        intensity = 0.0f;
                        button.setGlowing(false);
                        glowTimer.stop();
                    }
                }
                button.setGlowIntensity(intensity);
            }
        });
        glowTimer.start();
    }

    void setupAnimationTimer() {
        animationTimer = new Timer(50, (_) -> {
            animationAlpha += 0.1f;
            if (animationAlpha > 1.0f) {
                animationAlpha = 0.0f;
            }
            if (gameOver) {
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        if (board[r][c].getForeground() == Color.green) {
                            float hue = (System.currentTimeMillis() % 3000) / 3000f;
                            board[r][c].setForeground(Color.getHSBColor(hue, 0.8f, 1.0f));
                        }
                    }
                }
            }
        });
        animationTimer.start();
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.black);
        textLabel.setText(currentPlayer + "'s is the winner");
        
        // Add winner animation
        Timer winTimer = new Timer(100, (_) -> {
            float scale = 1.0f + 0.1f * (float)Math.sin(animationAlpha * Math.PI);
            tile.setFont(new Font("Arial", Font.BOLD, (int)(120 * scale)));
        });
        winTimer.start();
    }
     //set for tie
     void setTie(JButton tile){
        tile.setForeground(Color.red);
        tile.setBackground(Color.black);
        textLabel.setText("TIE!");
        
        // Add tie animation
        Timer tieTimer = new Timer(100, (_) -> {
            float brightness = 0.5f + 0.5f * (float)Math.sin(animationAlpha * Math.PI);
            tile.setBackground(new Color(0.2f * brightness, 0f, 0f));
        });
        tieTimer.start();
     }
}









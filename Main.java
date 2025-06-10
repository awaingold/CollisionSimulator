import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Main extends JFrame {

  private CollisionSimulationPanel simulationPanel;

  private JTextField obj1MassField;
  private JTextField obj1VxField;
  private JTextField obj1VyField;

  private JTextField obj2MassField;
  private JTextField obj2VxField;
  private JTextField obj2VyField;

  private JButton startResetButton;
  private JButton pauseButton;
  
  public Main() {

    super("Collision \"Simulation\"");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel inputPanel = new JPanel();

    inputPanel.setLayout(new GridLayout(0,6,5,5));

    inputPanel.add(new JLabel("Obj 1 Mass (kg):"));
    obj1MassField = new JTextField("2.0", 5); 
    inputPanel.add(obj1MassField);

    inputPanel.add(new JLabel("Obj 1 Vx (m/s):"));
    obj1VxField = new JTextField("10.0", 5);
    inputPanel.add(obj1VxField);

    inputPanel.add(new JLabel("Obj 1 Vy (m/s):"));
    obj1VyField = new JTextField("5.0", 5);
    inputPanel.add(obj1VyField);

    inputPanel.add(new JLabel("Obj 2 Mass (kg):"));
    obj2MassField = new JTextField("3.0", 5);
    inputPanel.add(obj2MassField);

    inputPanel.add(new JLabel("Obj 2 Vx (m/s):"));
    obj2VxField = new JTextField("-8.0", 5);
    inputPanel.add(obj2VxField);

    inputPanel.add(new JLabel("Obj 2 Vy (m/s):"));
    obj2VyField = new JTextField("2.0", 5);
    inputPanel.add(obj2VyField);

    startResetButton = new JButton("Start/Reset");

    startResetButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            resetSimulationWithInputs();
        }
    });

    pauseButton = new JButton("Pause/Resume");

    pauseButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

              pauseOrResumeSimulation();
          }
      });

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(startResetButton);
    buttonPanel.add(pauseButton);
    inputPanel.add(buttonPanel); 
    add(inputPanel, BorderLayout.NORTH);

    simulationPanel = new CollisionSimulationPanel();
    add(simulationPanel, BorderLayout.CENTER); 

    pack();
    setLocationRelativeTo(null);
    setVisible(true);

  }

  private void resetSimulationWithInputs() {
      try {
          double m1 = Double.parseDouble(obj1MassField.getText());
          double vx1 = Double.parseDouble(obj1VxField.getText());
          double vy1 = Double.parseDouble(obj1VyField.getText());

          double m2 = Double.parseDouble(obj2MassField.getText());
          double vx2 = Double.parseDouble(obj2VxField.getText());
          double vy2 = Double.parseDouble(obj2VyField.getText());

          if (m1 < 0 || m2 < 0) {
              JOptionPane.showMessageDialog(this, "Masses must be positive", "Input Error", JOptionPane.ERROR_MESSAGE);
              return;
          }
        
          simulationPanel.initializeSimulation(m1, vx1, vy1, m2, vx2, vy2);
          simulationPanel.startSimulation();
        
          simulationPanel.repaint();

      } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
      } catch (Exception ex) {

          JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace(); 
      }
  }

  private void pauseOrResumeSimulation() {
    if(simulationPanel.isRunning()) {
      simulationPanel.stopTimer();
    } else{
      simulationPanel.resumeTimer();
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Main());
  }

}

class CollisionSimulationPanel extends JPanel implements ActionListener {

  private JPanel infoPanel = new JPanel();

  
  private JLabel obj1VelXLabel;
  private JLabel obj1VelYLabel;
  private JLabel obj1VelMagLabel;
  private JLabel obj2VelXLabel;
  private JLabel obj2VelYLabel;
  private JLabel obj2VelMagLabel;
  
  private double ObjectOneX = 300;
  private double ObjectOneY = 300;
  private double ObjectTwoX = 900;
  private double ObjectTwoY = 300;
  private double Diameter = 60;

  private double ObjectOneMass = 10;
  private double ObjectTwoMass = 10;

  private double ObjectOneVelX = 0;
  private double ObjectOneVelY = 0;
  private double ObjectTwoVelX = 0;
  private double ObjectTwoVelY = 0;
  private double ObjectOneVel = Math.hypot(ObjectOneVelX, ObjectOneVelY);
  private double ObjectTwoVel = Math.hypot(ObjectTwoVelX, ObjectTwoVelY);
  


  private Timer simTimer;
  private int Delay = 16;

  public CollisionSimulationPanel() {

    setPreferredSize(new Dimension(1200,600));
    setBackground(Color.LIGHT_GRAY);


    obj1VelXLabel = new JLabel("Obj1 Vx: 0.00");
    obj1VelYLabel = new JLabel("Obj1 Vy: 0.00");
    obj1VelMagLabel = new JLabel("Obj1 Vmag: 0.00");
    obj2VelXLabel = new JLabel("Obj2 Vx: 0.00");
    obj2VelYLabel = new JLabel("Obj2 Vy: 0.00");
    obj2VelMagLabel = new JLabel("Obj2 Vmag: 0.00");

    infoPanel.add(obj1VelXLabel);
    infoPanel.add(obj1VelYLabel);
    infoPanel.add(obj1VelMagLabel);
    infoPanel.add(obj2VelXLabel);
    infoPanel.add(obj2VelYLabel);
    infoPanel.add(obj2VelMagLabel);

    

    add(infoPanel, BorderLayout.NORTH);

    simTimer = new Timer(Delay, this);
    simTimer.start();
    
  }

  public void startSimulation() {
      if (!simTimer.isRunning()) {
          simTimer.start();
      }
  }

  public void initializeSimulation(double m1, double vx1, double vy1, double m2, double vx2, double vy2) {
  
     if (simTimer.isRunning()) {
          simTimer.stop();
      }

      ObjectOneMass = m1;
      ObjectOneVelX = vx1;
      ObjectOneVelY = vy1;

      ObjectTwoMass = m2;
      ObjectTwoVelX = vx2;
      ObjectTwoVelY = vy2;

    ObjectOneX = 300;
    ObjectOneY = 300;
    ObjectTwoX = 900;
    ObjectTwoY = 300;
    
      updateVelocityLabels();
      repaint(); 
      simTimer.start();
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    
    g.setColor(Color.BLUE);
    g.fillOval((int)ObjectOneX, (int)ObjectOneY, (int)Diameter, (int)Diameter);

    g.setColor(Color.RED);
    g.fillOval((int)ObjectTwoX, (int)ObjectTwoY, (int)Diameter, (int)Diameter);
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    updateSimulation();
    repaint();
    updateVelocityLabels();
  }

  
  public void updateSimulation(){

      ObjectOneX += ObjectOneVelX;
      ObjectOneY += ObjectOneVelY;
      ObjectTwoX += ObjectTwoVelX;
      ObjectTwoY += ObjectTwoVelY;

      
      
      if (Math.abs(ObjectOneX - ObjectTwoX) <= Diameter && Math.abs(ObjectOneY - ObjectTwoY) <= Diameter) {
        
          //Define Line of Impact

          double impactAngle = Math.atan2(ObjectTwoY-ObjectOneY, ObjectTwoX-ObjectOneX);

          //Resolve Initial Velocities

          double firstInitialAngle = Math.atan2(ObjectOneVelY, ObjectOneVelX);
          double secondInitialAngle = Math.atan2(ObjectTwoVelY, ObjectTwoVelX);

          double ObjectOneLineVelX = ObjectOneVel*Math.cos(firstInitialAngle-impactAngle);
          double ObjectOneLineVelY = ObjectOneVel*Math.sin(firstInitialAngle-impactAngle);
        
          double ObjectTwoLineVelX = ObjectTwoVel*Math.cos(secondInitialAngle-impactAngle);
          double ObjectTwoLineVelY = ObjectTwoVel*Math.sin(secondInitialAngle-impactAngle);

          //Caculate final x components

          double FirstFinalX = (((ObjectOneMass-ObjectTwoMass)/(ObjectOneMass+ObjectTwoMass))*ObjectOneLineVelX) + (((2*ObjectTwoMass)/(ObjectOneMass+ObjectTwoMass))*ObjectTwoLineVelX);

        double SecondFinalX = (((ObjectTwoMass-ObjectOneMass)/(ObjectOneMass+ObjectTwoMass))*ObjectTwoLineVelX) + (((2*ObjectOneMass)/(ObjectOneMass+ObjectTwoMass))*ObjectOneLineVelX);

          //calculate final y components

          double FirstFinalY = ObjectOneLineVelY;
          double SecondFinalY = ObjectTwoLineVelY;

          //recombine

          ObjectOneVelX = FirstFinalX*Math.cos(impactAngle) - FirstFinalY*Math.sin(impactAngle);
          ObjectOneVelY = FirstFinalX*Math.sin(impactAngle) + FirstFinalY*Math.cos(impactAngle);

          ObjectTwoVelX = SecondFinalX*Math.cos(impactAngle) - SecondFinalY*Math.sin(impactAngle);
          ObjectTwoVelY = SecondFinalX*Math.sin(impactAngle) + SecondFinalY*Math.cos(impactAngle);

          //separate to hopefully prevent sticking

          double overlapDist = Diameter - Math.hypot(ObjectOneX-ObjectTwoX, ObjectOneY-ObjectTwoY);

          while(overlapDist>0){

            if(ObjectOneX < ObjectTwoX){
              ObjectOneX--;
              ObjectTwoX++;
            } else {
              ObjectOneX++;
              ObjectOneY--;
            }

            if(ObjectOneY < ObjectTwoY){
              ObjectOneY--;
              ObjectTwoY++;
            } else {
              ObjectOneY++;
              ObjectOneY--;
            }
            
            overlapDist = Diameter - Math.hypot(ObjectOneX-ObjectTwoX, ObjectOneY-ObjectTwoY);
          }
      }

      

      if (ObjectOneX < 0 || ObjectOneX > this.getWidth() - Diameter) {
        ObjectOneVelX = -ObjectOneVelX;
      }
      if (ObjectOneY < 0 || ObjectOneY > this.getHeight() - Diameter) {
        ObjectOneVelY = -ObjectOneVelY;
      }
      if (ObjectTwoX < 0 || ObjectTwoX > this.getWidth() - Diameter) {
        ObjectTwoVelX = -ObjectTwoVelX;
      }
      if (ObjectTwoY < 0 || ObjectTwoY > this.getHeight() - Diameter) {
        ObjectTwoVelY = -ObjectTwoVelY;
      }

    

      ObjectOneVel = Math.hypot(ObjectOneVelX, ObjectOneVelY);
      ObjectTwoVel = Math.hypot(ObjectTwoVelX, ObjectTwoVelY);
  }

  private void updateVelocityLabels() {
    obj1VelXLabel.setText(String.format("Obj1 Vx: %.2f m/s", ObjectOneVelX));
    obj1VelYLabel.setText(String.format("Obj1 Vy: %.2f m/s", ObjectOneVelY));
    obj1VelMagLabel.setText(String.format("Obj1 Vmag: %.2f m/s", ObjectOneVel));

    obj2VelXLabel.setText(String.format("Obj2 Vx: %.2f m/s", ObjectTwoVelX));
    obj2VelYLabel.setText(String.format("Obj2 Vy: %.2f m/s", ObjectTwoVelY));
    obj2VelMagLabel.setText(String.format("Obj2 Vmag: %.2f m/s", ObjectTwoVel));
  }

  public void resumeTimer() {
    simTimer.start();
  }

  public void stopTimer() {
    simTimer.stop();
  }

  public boolean isRunning() {
    if(simTimer.isRunning()) {
      return true;
    }
    return false;
  }

}

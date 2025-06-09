import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Main extends JFrame {
  public Main() {

    super("Collision \"Simulation\"");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CollisionSimulationPanel simulationPanel = new CollisionSimulationPanel();
    add(simulationPanel);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);

  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Main());
  }

}

class CollisionSimulationPanel extends JPanel implements ActionListener {

  double ObjectOneX = 100;
  double ObjectOneY = 150;
  double ObjectTwoX = 500;
  double ObjectTwoY = 150;
  double Diameter = 30;

  double ObjectOneMass = 15;
  double ObjectTwoMass = 10;

  double ObjectOneVelX = 1;
  double ObjectOneVelY = 2;
  double ObjectTwoVelX = -1;
  double ObjectTwoVelY = -3;
  double ObjectOneVel = Math.hypot(ObjectOneVelX, ObjectOneVelY);
  double ObjectTwoVel = Math.hypot(ObjectTwoVelX, ObjectTwoVelY);

  int FrameWidth;
  int FrameHeight;

  Timer simTimer;
  int Delay = 16;

  public CollisionSimulationPanel() {

    setPreferredSize(new Dimension(600,300));
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

            System.out.println(overlapDist);

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

      
      //Basic boundary detection
      if (ObjectOneX < 0 || ObjectOneX > FrameWidth - Diameter) {
        ObjectOneVelX = -ObjectOneVelX;
      }
      if (ObjectOneY < 0 || ObjectOneY > FrameHeight - Diameter) {
        ObjectOneVelY = -ObjectOneVelY;
      }
      if (ObjectTwoX < 0 || ObjectTwoX > FrameWidth - Diameter) {
        ObjectTwoVelX = -ObjectTwoVelX;
      }
      if (ObjectTwoY < 0 || ObjectTwoY > FrameHeight - Diameter) {
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

}


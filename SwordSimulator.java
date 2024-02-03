import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwordSimulator extends JFrame {
    private Arm upperArm;
    private Arm forearm;
    private Sword sword;
    private Timer animationTimer;


    private JTextField upperArmLengthField;
    private JTextField forearmLengthField;
    private JTextField swordLengthField;
    private JTextField upperArmRotationSpeedField;
    private JTextField forearmRotationSpeedField;
    private JTextField swordRotationSpeedField;
    private JButton startButton;
    private JButton changeSimulation;
    
    private int w1;
    private int w2;

    public SwordSimulator(double upperArmLength, double forearmLength, double swordLength) {
        this.upperArm = new Arm(upperArmLength);
        this.forearm = new Arm(forearmLength);
        this.sword = new Sword(swordLength);

        upperArmLengthField = new JTextField(Double.toString(upperArmLength), 5);
        forearmLengthField = new JTextField(Double.toString(forearmLength), 5);
        swordLengthField = new JTextField(Double.toString(swordLength), 5);
        upperArmRotationSpeedField = new JTextField("1000", 5);
        forearmRotationSpeedField = new JTextField("900", 5);
        swordRotationSpeedField = new JTextField("15000000", 5);
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w1 = 0;
                w2 = 0;
                startSimulation();
            }
        });

        changeSimulation = new JButton("Change Simulation");
        changeSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                TriplePendulumSimulation tps = new TriplePendulumSimulation();
                tps.main(new String[0]);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 4));
        inputPanel.add(new JLabel("Upper Arm Length:"));
        inputPanel.add(upperArmLengthField);
        inputPanel.add(new JLabel("Forearm Length:"));
        inputPanel.add(forearmLengthField);
        inputPanel.add(new JLabel("Sword Length:"));
        inputPanel.add(swordLengthField);
        inputPanel.add(new JLabel("Upper Arm Rotation Speed:"));
        inputPanel.add(upperArmRotationSpeedField);
        inputPanel.add(new JLabel("Forearm Rotation Speed :"));
        inputPanel.add(forearmRotationSpeedField);
        inputPanel.add(new JLabel("Sword Rotation Speed :"));
        inputPanel.add(swordRotationSpeedField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(changeSimulation);
        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.NORTH);


        // ウィンドウ設定
        setTitle("Sword Simulator");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 描画パネルを追加
        add(new DrawingPanel());

        // ウィンドウ表示
        setVisible(true);

        startSimulation();
        
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // アームと剣の位置を取得
            int upperArmX = (int) (getWidth() / 2 + upperArm.getX());
            int upperArmY = (int) (getHeight() / 2 - upperArm.getY());
            int forearmX = (int) (upperArmX + forearm.getX());
            int forearmY = (int) (upperArmY - forearm.getY());
            int swordX = (int) (forearmX + sword.getX());
            int swordY = (int) (forearmY - sword.getY());

            // アームと剣を描画
            g.setColor(Color.BLUE);
            g.drawLine(getWidth() / 2, getHeight() / 2, upperArmX, upperArmY);
            g.setColor(Color.RED);
            g.drawLine(upperArmX, upperArmY, forearmX, forearmY);
            g.setColor(Color.BLACK);
            g.drawLine(forearmX, forearmY, swordX, swordY);

            // アームと剣の位置を表示
            g.setColor(Color.BLACK);
            g.drawString("Upper Arm: (" + upperArmX + ", " + upperArmY + ")", 10, 20);
            g.drawString("Forearm: (" + forearmX + ", " + forearmY + ")", 10, 40);
            g.drawString("Sword: (" + swordX + ", " + swordY + ")", 10, 60);
            g.drawString("w1: "+w1,10,80);
            g.drawString("w2: "+w2,10,100);
        }
    }

    private void startSimulation() {
        double upperArmLength = Double.parseDouble(upperArmLengthField.getText());
        double forearmLength = Double.parseDouble(forearmLengthField.getText());
        double swordLength = Double.parseDouble(swordLengthField.getText());
        double upperArmRotationSpeed = (upperArmLength*Double.parseDouble(upperArmRotationSpeedField.getText()))/((upperArmLength*upperArmLength*(1.1/0.04*0.04*Math.PI*upperArmLength))/12+0.04*0.04*(1.1/0.04*0.04*Math.PI*upperArmLength)/4);
        double forearmRotationSpeed = (forearmLength*Double.parseDouble(forearmRotationSpeedField.getText()))/((forearmLength*forearmLength*(1.05/0.04*0.04*Math.PI*forearmLength))/12+0.04*0.04*(1.05/0.04*0.04*Math.PI*forearmLength)/4);
        double swordRotationSpeed = (swordLength*Double.parseDouble(swordRotationSpeedField.getText()))/((swordLength*swordLength*(7800/0.04*0.04*Math.PI*swordLength))/12+0.04*0.04*(7800/0.04*0.04*Math.PI*swordLength)/4);

        upperArm = new Arm(upperArmLength);
        forearm = new Arm(forearmLength);
        sword = new Sword(swordLength);
        

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(1, new ActionListener() {
            int upperArmFrameCount =  0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //角度の計算
                upperArmFrameCount++;
                
                if(upperArmRotationSpeed * upperArmFrameCount +90 < 225){
                    upperArm.setAngle(Math.toRadians(upperArmRotationSpeed * upperArmFrameCount+90));
                }
                else if (upperArmRotationSpeed * upperArmFrameCount +90>= 225) {
                    upperArm.setAngle(Math.toRadians(225));
                }

                if(forearmRotationSpeed * upperArmFrameCount +30 < 225 && forearmRotationSpeed * upperArmFrameCount +30 < upperArmRotationSpeed * upperArmFrameCount+90){
                    forearm.setAngle(Math.toRadians(forearmRotationSpeed * upperArmFrameCount+30));
                }
                else if(forearmRotationSpeed * upperArmFrameCount +30 >= upperArmRotationSpeed * upperArmFrameCount+90){
                    w1 = 1; //前腕と上腕が追いついたというフラグを立てる
                    forearm.setAngle(Math.toRadians(upperArmRotationSpeed * upperArmFrameCount+90));
                }
                
                if(swordRotationSpeed * upperArmFrameCount -30 < 225 && swordRotationSpeed * upperArmFrameCount -30 < forearmRotationSpeed * upperArmFrameCount +30){
                    sword.setAngle(Math.toRadians(swordRotationSpeed * upperArmFrameCount-30));
                }
                
                else if(swordRotationSpeed * upperArmFrameCount -30 >= forearmRotationSpeed * upperArmFrameCount +30 && w1 == 0){
                    w2 = 1;
                    sword.setAngle(Math.toRadians(forearmRotationSpeed * upperArmFrameCount+30));
                }

                else if(swordRotationSpeed * upperArmFrameCount -30 >= forearmRotationSpeed * upperArmFrameCount +30 && w1 == 1){
                    w2 = 1;
                    sword.setAngle(Math.toRadians(upperArmRotationSpeed * upperArmFrameCount+90));
                }

                if(w1 == 1){
                    if(upperArmRotationSpeed * upperArmFrameCount +90 >= 225){
                        forearm.setAngle(Math.toRadians(225));
                    }
                }
                else{
                    if(forearmRotationSpeed * upperArmFrameCount +30 >= 225) {
                        forearm.setAngle(Math.toRadians(225));
                    }
                }
            
                if(w2 == 1 && w1 ==1){
                    if(upperArmRotationSpeed * upperArmFrameCount +90 >= 225){
                        sword.setAngle(Math.toRadians(225));
                    }
                }
                else if(w2 == 1 && w1 == 0){
                    if(forearmRotationSpeed * upperArmFrameCount +30 >= 225){
                        sword.setAngle(Math.toRadians(225));
                    }
                }
                else{
                    if(swordRotationSpeed * upperArmFrameCount -30 >= 225){
                        sword.setAngle(Math.toRadians(225));
                    }
                }
                repaint();

            }
        });

        animationTimer.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SwordSimulator(100, 80, 100);
        });
    }
}


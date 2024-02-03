import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TriplePendulumSimulation extends JFrame {

    private TriplePendulumPanel pendulumPanel;

    public TriplePendulumSimulation() {
        setTitle("Triple Pendulum Simulation");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pendulumPanel = new TriplePendulumPanel();
        add(pendulumPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JTextField mass1Field = new JTextField("1.0", 5);
        JTextField mass2Field = new JTextField("1.0", 5);
        JTextField mass3Field = new JTextField("1.0", 5);
        JTextField length1Field = new JTextField("1.0", 5);
        JTextField length2Field = new JTextField("1.0", 5);
        JTextField length3Field = new JTextField("1.0", 5);

        controlPanel.add(new JLabel("MASS1:"));
        controlPanel.add(mass1Field);
        controlPanel.add(new JLabel("MASS2:"));
        controlPanel.add(mass2Field);
        controlPanel.add(new JLabel("MASS3:"));
        controlPanel.add(mass3Field);
        controlPanel.add(new JLabel("LENGTH1:"));
        controlPanel.add(length1Field);
        controlPanel.add(new JLabel("LENGTH2:"));
        controlPanel.add(length2Field);
        controlPanel.add(new JLabel("LENGTH3:"));
        controlPanel.add(length3Field);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ボタンが押されたときにシミュレーションを初期化
                pendulumPanel.initSimulation(
                        Double.parseDouble(mass1Field.getText()),
                        Double.parseDouble(mass2Field.getText()),
                        Double.parseDouble(mass3Field.getText()),
                        Double.parseDouble(length1Field.getText()),
                        Double.parseDouble(length2Field.getText()),
                        Double.parseDouble(length3Field.getText())
                );
            }
        });

        JButton changeSimulation = new JButton("Change Simulation");
        changeSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                SwordSimulator ss = new SwordSimulator(100,80,100);
                ss.main(new String[0]);
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(changeSimulation);

        add(controlPanel, BorderLayout.SOUTH);

        Timer timer = new Timer(4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pendulumPanel.update();
                pendulumPanel.repaint();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TriplePendulumSimulation simulation = new TriplePendulumSimulation();
            simulation.setVisible(true);
        });
    }
}

class TriplePendulumPanel extends JPanel {

    private double GRAVITY = 9.8;
    private double LENGTH1 = 1.0;
    private double LENGTH2 = 1.0;
    private double LENGTH3 = 1.0;
    private double MASS1 = 1.0;
    private double MASS2 = 1.0;
    private double MASS3 = 1.0;
    private double DELTA_T = 0.001;

    private double theta1;
    private double theta2;
    private double theta3;
    private double omega1;
    private double omega2;
    private double omega3;

    public TriplePendulumPanel() {
        setPreferredSize(new Dimension(800, 600));
        initSimulation(MASS1, MASS2, MASS3, LENGTH1, LENGTH2, LENGTH3);
    }

    public void initSimulation(double mass1, double mass2, double mass3, double length1, double length2, double length3) {
        MASS1 = mass1;
        MASS2 = mass2;
        MASS3 = mass3;
        LENGTH1 = length1;
        LENGTH2 = length2;
        LENGTH3 = length3;

        // シミュレーションの初期化
        theta1 = Math.PI / 4;
        theta2 = Math.PI / 4;
        theta3 = Math.PI / 4;
        omega1 = 0;
        omega2 = 0;
        omega3 = 0;
    }

    public void update() {
        // オイラー法を使用して角度と角速度を更新
        double alpha1 = omega1;
        double beta1 = (-MASS1 * GRAVITY * Math.sin(theta1) - 0.5 * omega1 * omega1 * LENGTH1 * Math.sin(2 * (theta1 - theta2))
                - 0.5 * omega2 * omega2 * LENGTH2 * Math.sin(2 * (theta1 - theta2 - theta3))) / (MASS1 * LENGTH1);

        double alpha2 = omega2;
        double beta2 = (-MASS2 * GRAVITY * Math.sin(theta2) + 0.5 * omega1 * omega1 * LENGTH1 * Math.sin(2 * (theta1 - theta2))
                - 0.5 * omega2 * omega2 * LENGTH2 * Math.sin(2 * (theta1 - theta2 - theta3))
                + 0.5 * omega3 * omega3 * LENGTH3 * Math.sin(2 * (theta2 - theta3))) / (MASS2 * LENGTH2);

        double alpha3 = omega3;
        double beta3 = (-MASS3 * GRAVITY * Math.sin(theta3) + 0.5 * omega2 * omega2 * LENGTH2 * Math.sin(2 * (theta1 - theta2 - theta3))
                - 0.5 * omega3 * omega3 * LENGTH3 * Math.sin(2 * (theta2 - theta3))) / (MASS3 * LENGTH3);

        omega1 += beta1 * DELTA_T;
        omega2 += beta2 * DELTA_T;
        omega3 += beta3 * DELTA_T;

        theta1 += alpha1 * DELTA_T;
        theta2 += alpha2 * DELTA_T;
        theta3 += alpha3 * DELTA_T;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x0 = getWidth() / 2;
        int y0 = getHeight() / 4;
        int x1 = x0 + (int) (LENGTH1 * Math.sin(theta1) * 100);
        int y1 = y0 + (int) (LENGTH1 * Math.cos(theta1) * 100);
        int x2 = x1 + (int) (LENGTH2 * Math.sin(theta2) * 100);
        int y2 = y1 + (int) (LENGTH2 * Math.cos(theta2) * 100);
        int x3 = x2 + (int) (LENGTH3 * Math.sin(theta3) * 100);
        int y3 = y2 + (int) (LENGTH3 * Math.cos(theta3) * 100);

        g.drawLine(x0, y0, x1, y1);
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);

        g.setColor(Color.RED);
        g.fillOval(x1 - 5, y1 - 5, 10, 10);
        g.fillOval(x2 - 5, y2 - 5, 10, 10);
        g.fillOval(x3 - 5, y3 - 5, 10, 10);

        g.setColor(Color.BLACK);
    }
}

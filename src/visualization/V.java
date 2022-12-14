package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class V {
    /**
     * Double Buffer.
     */
    private Image iBuffer;
    private Graphics gBuffer;

    private final static int TABLE_WIDTH = 600;
    private final static int TABLE_HEIGHT = 800;

    private final Frame frame = new Frame("V");

    private boolean isPause = true;

    private int mode = 0;

    private final static int fps = 100;
    private static final double dt = 1000.0 / (double) fps;
    private int t = 0;
    private double r = 0.0;

    private final static int particleSize = 4;

    private Points points;

    private int iteration = 0;

    private final Color danmuku_waveAndParticle = new Color(0xffbfff);

    private final int m2ccr = 10;

    private final class Canvas_0 extends Canvas {
        private final static Color lightYellow = new Color(0xffffffc0);

        @Override
        public void paint(Graphics graphics) {
            switch (mode) {
                case 1 -> {
                    graphics.setColor(Color.yellow);
                    graphics.drawLine(200, 0, 200, TABLE_HEIGHT);
                    graphics.drawLine(0, TABLE_HEIGHT / 2, TABLE_WIDTH, TABLE_HEIGHT / 2);
                    graphics.setColor(lightYellow);
                    for (int i = 0, space = 100; ++i < TABLE_HEIGHT / space; ) {
                        graphics.drawLine(0, TABLE_HEIGHT / 2 - space * i,
                                TABLE_WIDTH, TABLE_HEIGHT / 2 - space * i);
                        graphics.drawLine(0, TABLE_HEIGHT / 2 + space * i,
                                TABLE_WIDTH, TABLE_HEIGHT / 2 + space * i);
                    }
                    graphics.setColor(Color.white);
                    for (int i = 0; i < points.quantity; ++i) {
                        graphics.fillOval((int) points.coordinate[i][0] - particleSize / 2,
                                (int) points.coordinate[i][1] - particleSize / 2,
                                particleSize, particleSize);
                    }
                }
                case 2 -> {
                    graphics.setColor(Color.white);
                    graphics.drawOval(TABLE_WIDTH / 2 - m2ccr, TABLE_HEIGHT / 3 - m2ccr, 2 * m2ccr, 2 * m2ccr);
                    for (int i = 0; i < points.quantity; ++i) {
                        /*
                        //color mode: more
                        graphics.setColor(switch (i % 8) {
                            case 0, 4 -> Color.white;
                            case 1 -> Color.red;
                            case 2 -> Color.green;
                            case 3 -> Color.blue;
                            case 5 -> Color.cyan;
                            case 6 -> Color.magenta;
                            case 7 -> Color.yellow;
                            default -> Color.black;
                        });
                        //color mode : pure
                        graphics.setColor(danmuku_waveAndParticle);
                         */
                        graphics.setColor(danmuku_waveAndParticle);

                        graphics.fillOval((int) points.coordinate[i][0] - particleSize / 2,
                                (int) points.coordinate[i][1] - particleSize / 2,
                                particleSize, particleSize);
                    }
                }
                default -> {
                }
            }
        }

        @Override
        public void update(Graphics src) {
            if (iBuffer == null) {
                iBuffer = createImage(this.getSize().width, this.getSize().height);
                gBuffer = iBuffer.getGraphics();
            }
            gBuffer.setColor(getBackground());
            gBuffer.fillRect(0, 0, this.getSize().width, this.getSize().height);
            paint(gBuffer);
            src.drawImage(iBuffer, 0, 0, this);
        }
    }

    Canvas_0 drawArea = new Canvas_0();

    public void init(int mode) {
        this.mode = mode;

        points = switch (mode) {
            case 1 -> new Points(256, 2);
            case 2 -> new Points(1024, 2);
            default -> new Points(0, 0);
        };

        KeyListener listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_SPACE) {
                    isPause = !isPause;
                }
            }
        };

        frame.addKeyListener(listener);
        drawArea.addKeyListener(listener);

        ActionListener[] tasks = new ActionListener[]{
                e -> drawArea.repaint(),
                e -> {
                    if (isPause) {
                        drawArea.repaint();
                        return;
                    }
                    if (t > 0x40000000) {
                        isPause = true;
                        t = 0;
                    }
                    if (++iteration >= points.quantity) {
                        iteration = 0;
                    }
                    points.coordinate[iteration][0] = 200.0;
                    points.coordinate[iteration][1] = (double) TABLE_HEIGHT / 2.0 -
                                                      200.0 * Math.sin(Math.PI * (double) t / 50.0);

                    points.velocity[iteration][0] = 6.0;

                    points.move(dt);

                    ++t;
                    drawArea.repaint();
                },
                e -> {
                    final int tracks = 16;

                    final double v = 6.0;

                    final int st = 4;

                    final double tl = 240.0;

                    if (isPause) {
                        drawArea.repaint();
                        return;
                    }
                    if (t % st == 0) {
                        if (t > 0x40000000) {
                            isPause = true;
                            t = 0;
                        }
                        if (iteration >= points.quantity - tracks) {
                            iteration = 0;
                        }
                        /*
                        //t^2
                        r = (double) (t * t) / tl;
                        //sin(t)
                        r = (A) * Math.sin(t / tl);
                        as : r = 20 * Math.sin(t / tl);
                        //tan(t)
                        as : r = (A) * Math.tan(t / tl);
                         */
                        r = 20 * Math.sin(t / tl);

                        for (int i = 0; i < tracks; ++i, r += 2 * Math.PI / tracks) {
                            points.coordinate[iteration + i][0] = (double) TABLE_WIDTH / 2.0 - m2ccr * Math.sin(r);
                            points.coordinate[iteration + i][1] = (double) TABLE_HEIGHT / 3.0 + m2ccr * Math.cos(r);
                            points.velocity[iteration + i][0] = -v * Math.sin(r);
                            points.velocity[iteration + i][1] = v * Math.cos(r);
                        }

                        iteration += tracks;
                    }

                    points.move(dt);

                    ++t;
                    drawArea.repaint();
                },
                e -> drawArea.repaint(),
        };

        ActionListener taskX = e -> drawArea.repaint();

        Timer timer = new Timer(1000 / fps, this.mode < tasks.length ? tasks[this.mode] : taskX);
        timer.start();

        drawArea.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        frame.add(drawArea);
        frame.setBackground(Color.BLACK);

        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}

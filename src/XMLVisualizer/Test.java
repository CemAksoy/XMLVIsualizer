/**
 * Tester class for the visualizer.
 */
package XMLVisualizer;

import java.awt.Container;

import javax.swing.JFrame;

public class Test {

  public static void main(String[] args) {
    XMLVisualizer test = new XMLVisualizer("example.xml"); 
    
    JFrame frame = new JFrame();
    Container content = frame.getContentPane();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    content.add(test.getDrawingApplet());
    frame.pack();
    frame.setVisible(true);
  }
}

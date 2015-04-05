/**
 * An applet for visualizing the XML tree.
 * Some of the initialization codes in the file have been taken from 
 * TreeCollapseDemo.java provided with JUNG.
 * 
 * @author cem
 */
package XMLVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;

public class XMLTreeApplet extends JApplet {
  /**
   * List of XML nodes that has been parsed from the XML file.
   */
  LinkedList<XMLNode> xmlNodes;
  
  /**
   * JUNG graph. The following are JUNG related variables needed for the
   * visualization of the XML tree. Most of these code are derived from the 
   * sample codes provided with JUNG.
   */
  Forest<String,Integer> graph;

  Factory<Integer> edgeFactory = new Factory<Integer>() {
    int i=0;
    public Integer create() {
      return i++;
    }
  };
  
  VisualizationViewer<String,Integer> vv;
  TreeLayout<String,Integer> layout;

  TreeCollapser collapser;  
   
  /**
   * Constructor to set the graph and initialize it.
   * @param xmlNodes parsed XML nodes from the XML file.
   */
  public XMLTreeApplet(LinkedList<XMLNode> xmlNodes) {
    this.xmlNodes = xmlNodes;
    graph = new DelegateForest<String,Integer>();
    // Initialize the tree by using xmlNodes
    createTree(); 
    layout = new TreeLayout<String,Integer>(graph);
    collapser = new TreeCollapser();
    vv =  new VisualizationViewer<String,Integer>(layout,
        new Dimension(600,600));
    vv.setBackground(Color.white);
    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
    vv.setVertexToolTipTransformer(new ToStringLabeller());
    vv.getRenderContext().setArrowFillPaintTransformer(
        new ConstantTransformer(Color.lightGray));
    
    /**
     * Prepare the label of vertex by cutting out the dewey index.
     */
    Transformer<String, String> stringer = new Transformer<String,String>(){
        public String transform(String e) {
          StringTokenizer tok = new StringTokenizer(e, "_");
          String label = tok.nextToken();
          tok.nextToken();
          if (tok.hasMoreTokens())
           label += "\"" + tok.nextToken() + "\""; 
          return label;
        }
    };
      
    vv.getRenderContext().setVertexLabelTransformer(stringer);
    
    Container content = getContentPane();
    final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
    content.add(panel);
    
    final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
    vv.setGraphMouse(graphMouse);
    
    JComboBox modeBox = graphMouse.getModeComboBox();
    modeBox.addItemListener(graphMouse.getModeListener());
    graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

    final ScalingControl scaler = new CrossoverScalingControl();

    JButton plus = new JButton("+");
    plus.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        scaler.scale(vv, 1.1f, vv.getCenter());
      }
    });
    
    JButton minus = new JButton("-");
    minus.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        scaler.scale(vv, 1/1.1f, vv.getCenter());
      }
    });
    
    JPanel scaleGrid = new JPanel(new GridLayout(1,0));
    scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
        
    JButton save = new JButton("Save");
    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_h_mm_ss_a");
        String formattedDate = sdf.format(date) + ".png";
        writeToImageFile(formattedDate);
      }
    });
    
    // Set the control buttons.
    JPanel controls = new JPanel();
    scaleGrid.add(plus);
    scaleGrid.add(minus);
    controls.add(scaleGrid);
    controls.add(modeBox);
    controls.add(save);
    content.add(controls, BorderLayout.SOUTH);
  }
  
  /**
   * Insert the XML nodes into the tree.
   */
  private void createTree() {
    ListIterator<XMLNode> iterator = xmlNodes.listIterator();
    
    while (iterator.hasNext()) {
      XMLNode cur = iterator.next();
      LinkedList<XMLNode> children = cur.getChildren();
      String curNode = cur.getLabel() + "_" + cur.getDeweyID() + 
          (cur.getText() == null ? "" : "_" + cur.getText());
      
      // Add edges (and vertices of children) to children.
      ListIterator<XMLNode> childIterator = children.listIterator();
      while (childIterator.hasNext()) {
        XMLNode curChild = childIterator.next();
        String childNode = curChild.getLabel() + "_" + curChild.getDeweyID() +
            (curChild.getText() == null ? "" : "_" + curChild.getText());
        graph.addEdge(edgeFactory.create(), curNode, childNode);
      }
    }
  }
  
  /**
   * Helper method for writing the current screen to a file.
   * @param imageFileName file name for the saved image.
   */
  private void writeToImageFile(String imageFileName) {
    BufferedImage bufImage = ScreenImage.createImage((JComponent) vv);
    try {
      File outFile = new File(imageFileName);
      ImageIO.write(bufImage, "png", outFile);
    } catch (Exception e) {
        System.out.println("writeToImageFile(): " + e.getMessage());
    }
  }
}
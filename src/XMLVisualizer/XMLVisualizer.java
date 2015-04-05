/**
 * A class to visualize XML nodes. Retrieves the nodes from the parser and uses
 * JUNG framework to visualize the XML tree.
 * 
 * @author cem
 */
package XMLVisualizer;

import java.util.LinkedList;

import javax.swing.JApplet;

public class XMLVisualizer {
  /**
   * Name of the XML file to be visualized.
   */
  private String XMLFileName;
  private LinkedList<XMLNode> XMLNodes;
  
  /**
   * Constructor
   * @param XMLFileName name of the XML file to be visualized.
   */
  public XMLVisualizer(String XMLFileName) {
    this.XMLFileName = XMLFileName;
    XMLNodes = initializeXMLNodes();    
  }
  
  /**
   * Get the XML nodes from XMLParser.
   * @return
   */
  private LinkedList<XMLNode> initializeXMLNodes() {
    XMLParser parser = new XMLParser(this.XMLFileName);
    return parser.parse();
  }
  
  public LinkedList<XMLNode> getXMLNodes() {
    return XMLNodes;
  }
  
  public JApplet getDrawingApplet() {
    return new XMLTreeApplet(getXMLNodes());
  }
}
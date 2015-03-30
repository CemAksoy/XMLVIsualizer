/**
 * A class to represent XML nodes. An XML node can be an actual node or an 
 * attribute of a node (the attributes are shown as separate nodes).
 * 
 * @author cem
 */
package XMLVisualizer;

import java.util.LinkedList;

public class XMLNode {
  /**
   * Label (tag) of the XML node.
   */
  private String label;
  /**
   * Assigned Dewey ID of the XML node. Dewey indices are commonly used for
   * library indexing. It is also very commonly used for indexing XML nodes.
   * Basically, the root gets the dewey id 1 and for all other nodes, the dewey
   * id of a node gets as prefix its parent dewey id and a unique number among
   * its siblings. For example:
   * 
   * <bib>        //  1
   *  <paper>     //  1.1
   *    <author>  //  1.1.1
   *    </author> 
   *  </paper> 
   *  <paper>     //  1.2
   *  </paper>
   * </bib> 
   */
  private String deweyID;
  /**
   * The text of the node if there is any. We assumed that all XML nodes (leaf
   * or non-leaf) might have text attached to them.
   */
  private String text;
  /**
   * The list of children nodes of this node.
   */
  private LinkedList<XMLNode> children;
  
  /**
   * Constructor
   * @param label label of the node
   * @param deweyID dewey id of the node
   * @param text text of the node
   */
  public XMLNode(String label, String deweyID, String text) {
    this.label = label;
    this.deweyID = deweyID;
    this.text = text;
    
    children = new LinkedList<XMLNode>();
  }
  
  /**
   * Constructor
   * Text is set to null.
   * @param label label of the node
   * @param deweyID dewey id of the node
   */
  public XMLNode(String label, String deweyID) {
    this.label = label;
    this.deweyID = deweyID;
    this.text = null;
    
    children = new LinkedList<XMLNode>();
  }
  
  public String toString() {
    return "Label: " + label + " DeweyID: " + deweyID + " #children: " + 
        children.size();  
  }
  
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDeweyID() {
    return deweyID;
  }

  public void setDeweyID(String deweyID) {
    this.deweyID = deweyID;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  public LinkedList<XMLNode> getChildren() {
    return children;
  }
}

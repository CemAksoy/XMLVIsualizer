/**
 * A class to parse the XML file and create node representations for 
 * visualization. Uses SAX parser.
 * 
 * Note that the entire XML file is stored in the memory before visualization.
 * Since the initial aim is to visualize only small XML documents, the 
 * memory exhaustion problems are neglected for the moment.
 * 
 * @author cem
 */
package XMLVisualizer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
  /**
   * Name of the XML file to be parsed.
   */
  private String XMLFileName;
  
  /**
   * A stack that holds the currently active nodes of the XML tree while 
   * parsing.
   */
  private LinkedList<XMLNode> nodeStack;
  
  /**
   * Linked list of the tree nodes after parsing.
   */
  private LinkedList<XMLNode> resultNodes;
  
  /**
   * Constructor
   * @param XMLFileName name of the XML file to be parsed
   */
  public XMLParser(String XMLFileName) {
    this.XMLFileName = XMLFileName;
    this.nodeStack = new LinkedList<XMLNode>();
    this.resultNodes = new LinkedList<XMLNode>();
  }
  
  /**
   * This will be triggered any time the parser encounters a new tag.
   * @param uri
   * @param localName
   * @param nodeLabel label of the node (tag name)
   * @param attributes list of attributes of the node if any
   */
  public void startElement(String uri, String localName, String nodeLabel,
      Attributes attributes) {
    // Create the XML node for the node under consideration.
    XMLNode curNode = createStackEntryAndGetNode(nodeLabel);
        
    // Process the attributes of the node. Attributes are treated as children of
    // the current tree node and assigned Dewey IDs accordingly.
    for (int i = 0; i < attributes.getLength(); i++) {
      String attributeName = attributes.getQName(i).toLowerCase();
      String attributeValue = attributes.getValue(i).trim().toLowerCase();
      String attributeDeweyID = curNode.getDeweyID() + "." + (i+1);

      // Create the attribute node, store it as a children of the current node
      // and add the attribute node into the resultNodes as it does not have to
      // processed any further.
      XMLNode attributeNode = new XMLNode(attributeName, attributeDeweyID,
          attributeValue);
      curNode.getChildren().add(attributeNode);
      resultNodes.add(attributeNode);
    }
  }
  
  /**
   * Helper function for creating XML nodes. Decides the dewey id of the
   * current node by examining nodeStack.
   * @param nodeLabel
   * @return the new XML node
   */
  private XMLNode createStackEntryAndGetNode(String nodeLabel) {
    XMLNode newNode;
    // If the stack is empty, the current element is the root.
    if (nodeStack.isEmpty()) {
      newNode = new XMLNode(nodeLabel, "1");
    } else {  
      // Build the current Dewey ID from the parent's Dewey ID and # siblings.
      String parentDewey = nodeStack.peek().getDeweyID();
      int parentChildrenSize = nodeStack.peek().getChildren().size();
      String children = "" + (++parentChildrenSize);
      newNode = new XMLNode(nodeLabel, parentDewey + "." + children);
      nodeStack.peek().getChildren().add(newNode);
    }
    nodeStack.push(newNode);
    return newNode;
  }
  
  @Override
  /**
   * Assigns the text (if it is not empty) to the top of nodeStack.
   * @param ch character array to represent the text
   * @param start start index
   * @param length length of the text
   */
  public void characters(char[] ch, int start, int length) {
    String text = new String(ch, start, length).trim();
    if (!text.isEmpty()) {
      nodeStack.peek().setText(text);
    }
  }
  
  @Override
  /**
   * Triggered when an element ending tag is encountered.
   * Just pops the top of nodeStack and adds it to resultNodes.
   * @param uri
   * @param localName
   * @param nodeLabel label of the ending element
   */
  public void endElement(String uri, String localName, String nodeLabel) {
    // Add the node whose scope is finished to the resultNodes.
    resultNodes.add(nodeStack.pop());
  }
  
  /**
   * Initiates the parsing of the XML file. This code has been compiled from
   * various SAX parsing examples that are on the Web.
   * @return result nodes from parsing
   */
  public LinkedList<XMLNode> parse() {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    try {
      SAXParser sp = spf.newSAXParser();
      InputStream inputStream = new FileInputStream(XMLFileName);
      Reader reader = new InputStreamReader(inputStream, "UTF-8");
      InputSource is = new InputSource(reader);
      is.setEncoding("UTF-8");
      sp.parse(is, this);
      return resultNodes;
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

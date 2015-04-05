# XMLVisualizer
A project for visualizing XML files using JUNG framework (http://jung.sourceforge.net/). This is a
straightforward implementation which parses XML file and creates a JUNG visualization to draw an XML
tree. Please note that the drawing might be inefficient and memory exhaustion problems might be
encountered for large XML files. 

Run Test.java to run XMLVisualizer. You need to provide an XML file name. A sample XML file is
provided.

Functionalities:
- Draw an XML tree.
- Save the XML tree to an image.
- Manipulate the nodes of the XML tree as you want.

TODO:
- Planning to put a button to collapse/expand certain parts of the XML tree. There are some kinks to
  be ironed out about this; that is why this functionality is currently excluded.
- Instead of a driver program, implement a UI which accepts the XML file name.

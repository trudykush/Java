

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import dsai.core.Entry;
import dsai.core.Iterator;
import dsai.core.List;
import dsai.core.Position;
import dsai.impl.LinkedList;
import dsai.impl.ListIterator;
import dsai.impl.SortedListPriorityQueue;
import dsaii.core.Map;
import dsaii.core.Tree;
import dsaii.impl.ChainMap;
import dsaii.impl.LinkedTree;

/**
 * This class implements the dictionary component of the predictive text
 * module of our mobile phone demonstrator. Underlying the implementation
 * is a tree data structure in which each node contains:
 * 
 * <ul>
 *   <li> an integer value representing the current keystroke
 *   <li> a list of strings that contains the word fragments that correspond to
 *        the sequence of keystrokes in the path from the root node to that
 *        node.
 * </ul>
 * 
 * This node data is modelled through the inner Keystroke class.
 * 
 * @author remcollier
 */
public class Dictionary 
{
    /**
     * This class represents the contents of each node in the tree-based
     * implementation of the dictionary. Each node basically represents
     * a single keystroke, this class associates that keystroke with a list
     * of words the sequence of keystrokes corresponding to the path from the
     * root node to this node.
     */
    private class Keystroke 
	{
        int key;
			private List<String> words;
    
        /**
         * Constructor for the Keystroke class, that takes an integer (the
         * keystroke) as a parameter.
         * 
         * @param key
         */
        public Keystroke(int key) 
		{
            this.key = key;
            words = new LinkedList<String>();
        }
    
        /**
         * Add another word to this node (this means that the word is a
         * potential word for the combination of keystrokes that matches
         * the path from the root node to this node).
         * 
         * @param word
         */
        public void addWord(String word) 
        {
        	words.insertLast(word);	// for inserting the word at last position.
            // TODO: This method must be implemented in answer to question A1
        }
    
        /**
         * Return the list of words that is associated with this
         * keystroke. The current implementation does not impose any
         * ordering on the list (it is built based on the order in
         * which words are inserted into the node). In part B of the
         * assignment, you will need to modify this method to return
         * an ordered list of words.
         * 
         * @return a list of words
         */
        public List<String> getWords() 
        {
        	Map<String, Integer> map = new ChainMap<String, Integer>();
        	Position<String> currentWord = this.words.first();
				while(true)
				{
					map.put(currentWord.element(), 0);
						if(currentWord == this.words.last())		//Searching for second last word
							break;
						else
							currentWord = this.words.next(currentWord);
				}
        	
        	Position<Dictionary.Keystroke> currentPosition = null;
        	Iterator<Position<Dictionary.Keystroke>> treePositions = getTree().positions();	//find position
				while(treePositions.hasNext())		//rotate until tree's  next position is NULL
				{
					currentPosition = treePositions.next();
					if(currentPosition.element().equals(this))	//if current poisition is equal then it breaks the loop
						break;
				}
        	if(tree.parent(currentPosition) != tree.root())
        	{
        		Iterator<Position<Dictionary.Keystroke>> children = getTree().children(currentPosition);
	        	while(children.hasNext())
	        	{
	        		Position<Dictionary.Keystroke> child = children.next();
	        		List<String> childWords = child.element().words;
	        		Position<String> position = childWords.first();
	        		while(true)
	        		{
	        			map.put(position.element().substring(0, position.element().length()-1), map.get(position.element().substring(0, position.element().length()-1))+1);
	        			if(position == childWords.last())
	            			break;
	            		else
	            			position = childWords.next(position);
	        		}
	        	}
	        	Iterator<Entry<String,Integer>> mapIteration = map.entries();
	        	SortedListPriorityQueue<Integer, String> sortedList = new SortedListPriorityQueue<Integer, String>();
	        	while(mapIteration.hasNext())
	        	{
	        		Entry<String,Integer> entry = mapIteration.next();
	        		sortedList.insert(entry.value(), entry.key());
	        	}
	        	List<String> newWords = new LinkedList<String>();
	        	while(true)
	        	{
	        		if(sortedList.isEmpty())
	        			break;
	        		else
	        		{	
	        			Entry<Integer,String> sortedentry = sortedList.removeMin();
	        			newWords.insertFirst(sortedentry.value());
	        		}
	        	}
	        	words = newWords;
        	}
        	return words;
        }
        }
    
        /**
         * Generate a string representation of the node data for outputing
         * of the state of the tree during testing.
         * 
         * @return
         */
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(key);
            buf.append(":");
            Iterator<String> it = new ListIterator<String>(words);
            while(it.hasNext()) {
                buf.append(" ");
                buf.append(it.next());
            }
            return buf.toString();
        }
    }

    /**
     * This map associates characters with keystrokes and is used by the
     * insertion algorithm to work out how to add words to the tree.
     */
    private static Map<Character, Integer> characterMap;
    
    /**
     * Initialization block for the characterMap
     */
    static {
        characterMap = new ChainMap<Character, Integer>();
        	characterMap.put('a',2);
        	characterMap.put('b',2);
        	characterMap.put('c',2);
        	characterMap.put('d',3);
        	characterMap.put('e',3);
        	characterMap.put('f',3);
        	characterMap.put('g',4);
        	characterMap.put('h',4);
	        characterMap.put('i',4);
	        characterMap.put('j',5);
	        characterMap.put('k',5);
	        characterMap.put('l',5);
	        characterMap.put('m',6);
	        characterMap.put('n',6);
	        characterMap.put('o',6);
	        characterMap.put('p',7);
	        characterMap.put('q',7);
	        characterMap.put('r',7);
	        characterMap.put('s',7);
	        characterMap.put('t',8);
	        characterMap.put('u',8);
	        characterMap.put('v',8);
	        characterMap.put('w',9);
	        characterMap.put('x',9);
	        characterMap.put('y',9);
	        characterMap.put('z',9);
    }
    
    /**
     * The tree
     */
    private Tree<Keystroke> tree;
    
    /**
     * Default Constructor that creates an empty dictionary.
     */
    public Dictionary() {
        tree = new LinkedTree<Keystroke>();
        tree.addRoot(new Keystroke(-1));
    }
    
    public Dictionary() {
        tree = new LinkedTree<Keystroke>();
        tree.addRoot(new Keystroke(-1));
    }
    
    public Tree<Keystroke> getTree() 
    {
    	return tree;
    }
    
    /**
     * Load the specified dictionary file. Each word in the file must
     * be inserted into the dictionary.
     * 
     * @param filename the dictionary file to be loaded
     */
    public void load(String filename)
    {
        // TODO: This method must be completed for question A4
    	BufferedReader br = null;
    	try {
    		br = new BufferedReader(new FileReader(filename));
    	}
    	catch (FileNotFoundException ex) {
    		System.out.println("No Such File: " + filename);
    	}
    	try {
    		while(true){
    			String line = br.readLine(); // for reading a line from a file
    										
    			if(line != null)		// null if no more data
    				insert(line);
    			else
    				break;
    		}
    	}
    	catch (IOException ioe) {
    		System.out.println("Error reading from: " + filename);
    	}
    	try {
    		br.close(); // for closing the file
    	}
    	catch (IOException ioe) {
    		System.out.println("Error closing: " + filename);
    	}
    }
    
    /**
     * Insert the word into the dictionary. This algorithm loops through the
     * characters in the word, and uses the character map to work out what
     * keystroke should be used to select that character (e.g. a,b,c would
     * be selected by pressing the 2 key).
     * 
     * For each sequence of keystrokes, the substring that corresponds to that
     * sequence is stored at the corresponding node so that 
     * 
     * @param word
     */
    public void insert(String word) 
    {
        // TODO: This method must be completed for question A2
    	char[] charArray = word.toCharArray();
		Position<Keystroke> currentNode = tree.root();		//for finding the current or last postion of the tree node
		String wordToAdd = "";
			for(int i = 0; i<charArray.length; i++)
			{
				int keyStroke = characterMap.get(charArray[i]);		//for access index element of tree node
				wordToAdd = wordToAdd + charArray[i];
				boolean added = false;
				boolean hasChildren = false;
				Iterator<Position<Keystroke>> children = tree.children(currentNode);
				while(children.hasNext() && !added)					//for checking the child and last node
				{	
					hasChildren = true;
					currentNode = children.next();
					Keystroke currKeystroke = currentNode.element();
						if(currKeystroke.key == keyStroke)				
						{	
							boolean isPresent = false;
							List<String> words = currKeystroke.getWords();
							Position<String> currPosition = words.first();  //for getting the firstElement from the linkedLIst 
							Position<String> lastPosition = words.last();	 //for getting the lastElement from the linkedLIst 
								while(true)
								{
									if(currPosition.element().equalsIgnoreCase(wordToAdd))		 //if current and new element is same 
									{
									isPresent = true;
									added = true;
										break;
									}
										if(currPosition == lastPosition)
										break;
										else
										currPosition = words.next(currPosition);
								}
									if(!isPresent)
									{
									currKeystroke.addWord(wordToAdd);
									added = true;
									}
						}		
				}		
						if(!hasChildren)
						{
							Keystroke newKeystroke= new Keystroke(keyStroke);
							newKeystroke.addWord(wordToAdd);
							currentNode = tree.addChild(currentNode, newKeystroke);
						}
						else if(hasChildren && !added)
						{
							currentNode = tree.parent(currentNode);
							Keystroke newKeystroke= new Keystroke(keyStroke);
							newKeystroke.addWord(wordToAdd);
							currentNode = tree.addChild(currentNode, newKeystroke);
						}
			}	
    }
    
    /**
     * Output the state of the dictionary (via delegation to the underlying
     * tree implementation).
     * 
     * @return
     */
    @Override
    public String toString() {
        return tree.toString();
    }
    
    /**
     * Find the list of words that corresponds to the given sequence of
     * keystrokes.
     * 
     * @param keystrokes a sequence of keystrokes
     * 
     * @return a list of words
     */
    public List<String> findWords(List<Integer> keystrokes) {
        // TODO: This method must be completed for question A3
        
        /**
         * The default null response will cause the invoking method to
         * create a default word based on the sequence of keystrokes (i.e.
         * the same behaviour as if there was not entry in the dictionary
         * for the sequence of keystrokes).
         */
    	List<String> predictedWords = new LinkedList<String>();		//create new link list for finding predictedWords
    	Position<Keystroke> currentNode = tree.root();				//for creating access root of the tree
		Position<Integer> currKeystroke = keyStrokes.first();		//for creating access first position link list
    	for(int i=0; i<keyStrokes.size(); i++)
    	{
    		Iterator<Position<Keystroke>> children = tree.children(currentNode);	//for creating access currentnode child
    		boolean hasChildren = false;
			boolean keyPresent = false;
    		while(children.hasNext())				//iterate until childern is nulls 
    		{
    			hasChildren = true;
    			currentNode = children.next();		
    			if(currentNode.element().key == currKeystroke.element())
    			{
    				keyPresent = true;
    				if(i == keyStrokes.size()-1)
    					predictedWords = currentNode.element().getWords();
    				break;
    			}
    		}
    		if(!hasChildren)
    			return null;
    		else if(hasChildren && !keyPresent)
    			return null;
    		if(currKeystroke != keyStrokes.last())
    			currKeystroke = keyStrokes.next(currKeystroke);
    	}

        /**
         * The default null response will cause the invoking method to
         * create a default word based on the sequence of keystrokes (i.e.
         * the same behaviour as if there was not entry in the dictionary
         * for the sequence of keystrokes).
         */
        return predictedWords;	// to return predictedWords
    }
    }
}

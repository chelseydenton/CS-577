//credit to rosettacode.org for code sample
	
import java.awt.Dimension;
import java.io.File;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;
import org.jfree.ui.*;


public class Huffman {
	
	//instance variables
	String fileList;
	int start;
	int end;
	Hashtable<String, Integer> dictionary;
	Hashtable<String, String>  encodedHash;
	PriorityQueue<Word> pqWord;
	
	
	//Constructor
	Huffman(String fileList, int start, int end) {
    	dictionary = new Hashtable<String, Integer>();
    	encodedHash = new Hashtable<String, String>();
    	pqWord = new PriorityQueue<Word>();
    	
    	this.fileList = fileList;
    	this.start = start;
    	this.end = end;
	}
    	
	
	//Methods
	public void makeGraph() throws Exception {
		//init graph
		ApplicationFrame demo = new ApplicationFrame("Huffman Code");
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(new XYSeries(0));
		
		//init variables
	    File file = new File(fileList);
	    Scanner sc = new Scanner(file);
	    int count = 0;
	    String fileName = "speechdata\\" + sc.next();
	    
	    //iterate through the list of files to make and combine dictionaries 
	    System.out.println("\nCreating a dictionary that contains all of the words used and their frequencies...\n");
	    
	    //loop until we get to the start point
	    while (count < start) {
	    	fileName = "speechdata\\"+sc.next();
	    	count++;
	    }
	    String firstName = fileName;
	    //System.out.println("First Name : "+fileName);
	    
	    //loop until the end point or no more file names
	    while (count < end && fileName != null) {
	    	//use the file given to update global hash
	    	updateHash(fileName);
	    	//System.out.println("Added file: " + fileName);
	    	fileName = "speechdata\\" + sc.next();
	    	count++;
	    }
	    String lastName = fileName;
	    //System.out.println("Last Name : "+fileName);
	    sc.close();
	    
	    
	    //smooth out the global dictionary
	    System.out.println("Smoothing out the dictionary...");
		smooth();
		
		//build tree
		System.out.println("\nBuilding Huffman Code Tree...");
		HuffmanTree tree = buildTree();
		//printCodes(tree, new StringBuffer());
		
		//build a hash to quickly look up a given words code
		System.out.println("\nBuilding encoded dictionary...");
		encodedHash = encodeWords(tree, new StringBuffer());
		
		
		/*use the dictionary created to compress all of the files in the fileList
		we compare the compression of Huffman code to that of block compression to get a compression ratio
		we use these ratios and dates to make a graph*/
		int year = 0;
		float huff = 0;
		float bloc = 0;
		float ratio = 0;
		
		float min = 99999999;
		float max = 0;
		float avg = 0;
		int avgCount = 0;
		Word word = new Word("", 0);
		
		sc = new Scanner(file);
		
		System.out.println("\nMaking graph using speeches " + firstName + " to " + lastName);
		while (sc.hasNext()) {
			fileName = sc.next();
			//compute number of bits in huffman code
			huff = huffCount(fileName);
			bloc = blocCount(fileName);
			//System.out.println("\nBloc count: " + bloc + " Huffman count: " + huff);
			ratio = (float) huff/bloc;
			
			//update min max and average
			if ( ratio < min )
				min = ratio;
			if ( ratio > max )
				max = ratio;
			avg += ratio;
			avgCount++;
			
			//remove all but the first 4 chars of the file name (the year)
			fileName = fileName.substring(0, 4);
			year = Integer.parseInt(fileName);
			
			//add data to graph
			data.getSeries(0).add(year, ratio);
			//System.out.println("Added data series: Year:" + year + " Ratio:" + ratio);
			
			word.setName(fileName);
			word.setValue( (int) (1000 * ratio));
			pqWord.add(word);
			//System.out.println(pqWord.peek());
		}
		sc.close();
		
		//printBestWorst();
		
		System.out.println("\nMinimum Ratio: " + min + "\nMaximum Ratio: " + max + "\nAverage Ratio: " + avg/avgCount);

		//create scatter plot
		String chartName = new String("Huffman Ratio using speeches " + firstName + " to " + lastName);
		JFreeChart chart = ChartFactory.createScatterPlot(
		chartName, 					// chart title
		"Year", 					// domain axis label
		"Compression Ratio (Huffman/Bloc)", 		// range axis label
		data, // data
		PlotOrientation.VERTICAL,	// orientation
		false, 						// include legend
		true, 						// tooltips
		false 						// urls
		);
		 
		JPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(1000, 500));
		XYPlot plot = (XYPlot) chart.getPlot();
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setRange(0.5, 2.0);
		demo.setContentPane(chartPanel);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
	
	public void updateHash(String fileName) throws Exception{
	    //try to open and setup to read file  
	    File file = new File(fileName);
	    Scanner sc = new Scanner(file);
	    String word = new String();
	    
	    //read all the words in the file
	    while(sc.hasNext()) {
	    	word = sc.next();
	    	
	    	//convert to lower case and remove punctuation
	    	word = word.toLowerCase();
	    	word = word.replaceAll("([a-z]+)[?:!.,;]*", "$1");
	    	
	    	//insert words into dictionary
	    	if ( dictionary.get(word) == null ) {
	    		//if it doesn't exist we add it
	    		dictionary.put(word, 2); //accounting for smoothing immediatly,
	    	} else {
	    		//if the word does exist we increment by 1
	    		int value = dictionary.get(word) + 1;
	    		dictionary.remove(word);
	    		dictionary.put(word, value);
	    	}
	    }
	    sc.close();
	}
	
	public void smooth() throws Exception {
		//try to open and setup to read file  
	    File files = new File(fileList);
	    Scanner sc = new Scanner(files);
	    String fileName = "speechdata\\";
	    String word = "";
	    
	    //read all of the files
	    while ( sc.hasNext() ) {
	    	fileName = "speechdata\\" + sc.next();
	    	
	    	//get current file
	    	File file = new File(fileName);
		    Scanner sc1 = new Scanner(file);
	    	
		    //read all of the words in the file
		    while( sc1.hasNext() ) {
		    	word = sc1.next();
				//convert to lower case and remove punctuation
				//word = word.toLowerCase();
				//word = word.replaceAll("([a-z]+)[?:!.,;]*", "$1");
				
				//insert words into dictionary
				if ( dictionary.get(word) == null ) {
					//if it doesn't exist we add it
					dictionary.put(word, 1);
				} else {
					//if the word does exist we do NOTHING
				}
		    }
		    sc1.close();
	    }
	    sc.close();
	}
	
	public HuffmanTree buildTree() {
		PriorityQueue<HuffmanTree> tree = new PriorityQueue<HuffmanTree>();
	    
		for (Entry<String, Integer> entry : dictionary.entrySet()) {
	    // initially, we have a forest of leaves
	    // one for each non-empty character
			tree.offer( new HuffmanLeaf(entry.getValue(), entry.getKey()) );
		}
	
	    assert tree.size() > 0;
	    // loop until there is only one tree left
	    while (tree.size() > 1) {
	        // two trees with least frequency
	        HuffmanTree a = tree.poll();
	        HuffmanTree b = tree.poll();
	
	        // put into new node and re-insert into queue
	        tree.offer(new HuffmanNode(a, b));
	    }
	    return tree.poll();
	}
	
	public Hashtable<String, String> encodeWords(HuffmanTree tree, StringBuffer prefix) {
		Hashtable<String, String> encodedHash = new Hashtable<String, String>();
	    assert tree != null;
	    if (tree instanceof HuffmanLeaf) {
	        HuffmanLeaf leaf = (HuffmanLeaf)tree;
	
	        //create a new word with the same name but different value (instead of frequency we use its code)
	        encodedHash.put(leaf.word, prefix.toString());
	        //System.out.println(prefix.toString()+" "+leaf.word);
	
	    } else if (tree instanceof HuffmanNode) {
	        HuffmanNode node = (HuffmanNode)tree;
	
	        // traverse left
	        prefix.append('0');
	        encodedHash.putAll(encodeWords(node.left, prefix));
	        prefix.deleteCharAt(prefix.length()-1);
	
	        // traverse right
	        prefix.append('1');
	        encodedHash.putAll(encodeWords(node.right, prefix));
	        prefix.deleteCharAt(prefix.length()-1);
	    }
		return encodedHash;
	}
	
	public int blocCount(String fileName) throws Exception {
		//to get the number of bits for block encoding we count the number of words in the file and multiply by the log base 2 of that number
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		File file = new File("speechdata\\" + fileName);
		Scanner sc = new Scanner(file);
		String word = "";
		int uCount = 0;
		int count = 0;
		
		while (sc.hasNext()) {
			word = sc.next();
			count++;
			if ( !hash.contains(word)) {
				uCount++;
				hash.put(word, 1);
			}
		}
		
	    //System.out.println("\n\nWord Count: " + count + "Unique Count: " + uCount);
	    count = (int)((float)count * (Math.log(uCount)/Math.log(2)));
	    sc.close();
	    return count;
	}
	
	
	public int huffCount(String fileName) throws Exception {
		//to get bits from Huffman we can read the file and use the dictionary to look up the length of each word encoded
		File file = new File("speechdata\\" + fileName);
		Scanner sc = new Scanner(file);
		String word = "";
		int count = 0;
		
//		int shortest = 99999;
//		String shortestS = "";
//		int longest = 0;
//		String longestS = "";
		
		while (sc.hasNext()) {
			word = sc.next();
			count += encodedHash.get(word).length();
			
//			if ( longest < encodedHash.get(word).length()) {
//				longest = encodedHash.get(word).length();
//				longestS = word;
//			}
//			
//			if ( shortest > encodedHash.get(word).length()) {
//				shortest = encodedHash.get(word).length();
//				shortestS = word;
//			}
		}
		//System.out.println("Shortest: " + shortestS + " " + shortest);
		//System.out.println("Longest: " + longestS + " " + longest);
		
		sc.close();
		
		return count;
	}
	
	public void printCodes(HuffmanTree tree, StringBuffer prefix) {
	    assert tree != null;
	    
	    if (tree instanceof HuffmanLeaf) {
	        HuffmanLeaf leaf = (HuffmanLeaf)tree;
	
	        // print out character, frequency, and code for this leaf (which is just the prefix)
	        //System.out.println(leaf.word + "\t\t" + leaf.frequency + "\t\t" + prefix);

	    } else if (tree instanceof HuffmanNode) {
	        HuffmanNode node = (HuffmanNode)tree;
	
	        // traverse left
	        prefix.append('0');
	        printCodes(node.left, prefix);
	        prefix.deleteCharAt(prefix.length()-1);
	
	        // traverse right
	        prefix.append('1');
	        printCodes(node.right, prefix);
	        prefix.deleteCharAt(prefix.length()-1);
	    }
	}
	
	public void printBestWorst() {
		Word test = new Word("", 0);
		while ( !pqWord.isEmpty() ) { 
			test = pqWord.poll();
			System.out.println(") Filename: " + test.getName() + " Ratio: " + (float)test.getValue()/1000.0);
			pqWord.remove(test);
		}
//		}
//		System.out.println("Top 10 best ratios:");
//		Word test = new Word("", 0);
//		for ( int i = 0; i < 10; i++ ) {
//			test = pqWord.poll();
//			System.out.println(i + ") Filename: " + test.getName() + " Ratio: " + (float)test.getValue()/1000.0);
//		}
//		
//		while (pqWord.size() > 10 ) {
//			test = pqWord.poll();
//		}
//		
//		System.out.println("\nBottom 10 ratios: ");
//		for ( int i = 0; i < 10; i++ ) {
//			test = pqWord.poll();
//			System.out.println(i + ") Filename: " + test.getName() + " Ratio: " + (float)test.getValue()/1000.0);
//		}
	}
	
    /*public void printEncoded(String fileName, Hashtable<String, String> dictionary)throws IOException {
	    //try to open and read file  
	    File file = new File(fileName);
	    Scanner sc = new Scanner(file);
	    String word = new String("");
	    int count = 0;
	    
	    //setup file to write to
	    File fileOut = new File("G:\\Programming\\Eclipse\\huffman\\fileOut.txt");
	    
	    // if file doesn't exist, then create it
	    if (!fileOut.exists()) {
	    	fileOut.createNewFile();
	    }
	    FileWriter fw = new FileWriter(fileOut.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    //start reading the file.
	    while( sc.hasNext() ) {
	    	word = sc.next();
	    	//convert to lower case and remove punctuation
	    	word = word.toLowerCase();
	    	word = word.replaceAll("([a-z]+)[?:!.,;]*", "$1");
	    	
	    	//if it's in the dictionary print it, otherwise just print out the actual word
		    if ( dictionary.containsKey(word) ) {
		    	System.out.print(dictionary.get(word));
		    	bw.write(dictionary.get(word));
		    } else {
		    	System.out.print(word);
		    	bw.write(word);
		    	count++;
		    }
    	}
	    System.out.println("\nNumber of NID errors: " + count);
	    //bw.write("\nNumber of NID errors: " + count);
	    sc.close();
	    bw.close();
	}
    

	public static void main(String argv[]) throws IOException {
		
	}*/
}

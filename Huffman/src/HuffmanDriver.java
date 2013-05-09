
public class HuffmanDriver {

	public static void main(String[] args) throws Exception {
		
//		Huffman oldTest0 = new Huffman("listOfFiles.txt", 0, 1);
//		Huffman oldTest1 = new Huffman("listOfFiles.txt", 0, 2);
//		Huffman oldTest2 = new Huffman("listOfFiles.txt", 0, 10);
//		Huffman oldTest3 = new Huffman("listOfFiles.txt", 0, 100);
//		Huffman oldTest4 = new Huffman("listOfFiles.txt", 0, 300);
//		Huffman newTest0 = new Huffman("listOfFiles.txt", 624, 625);
//		Huffman newTest1 = new Huffman("listOfFiles.txt", 623, 625);
//		Huffman newTest2 = new Huffman("listOfFiles.txt", 615, 625);
//		Huffman newTest3 = new Huffman("listOfFiles.txt", 525, 625);
//		Huffman newTest4 = new Huffman("listOfFiles.txt", 225, 625);
		Huffman all = new Huffman("listOfFiles.txt", 0, 625);
//		
//		oldTest0.makeGraph();
//		oldTest1.makeGraph();
//		oldTest2.makeGraph();
//		oldTest3.makeGraph();
//		oldTest4.makeGraph();
//	
//		newTest0.makeGraph();
//		newTest1.makeGraph();
//		newTest2.makeGraph();
//		newTest3.makeGraph();
//		newTest4.makeGraph();
//		
		all.makeGraph();
		
		System.out.println("\nDone!");
	}

}

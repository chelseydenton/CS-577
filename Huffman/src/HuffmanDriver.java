
public class HuffmanDriver {

	public static void main(String[] args) throws Exception {
//		Huffman Newtest0 = new Huffman("listOfFiles.txt", 000, 100);
//		Huffman Newtest1 = new Huffman("listOfFiles.txt", 100, 200);
//		Huffman Newtest2 = new Huffman("listOfFiles.txt", 200, 300);
//		Huffman Newtest3 = new Huffman("listOfFiles.txt", 300, 400);
//		Huffman Newtest4 = new Huffman("listOfFiles.txt", 400, 500);
//		Huffman Newtest5 = new Huffman("listOfFiles.txt", 500, 602);
//		/*Huffman Newtest1 = new Huffman("listOfFiles.txt", 600, 602);
//		Huffman Newtest2 = new Huffman("listOfFiles.txt", 592, 602);
//		Huffman Newtest3 = new Huffman("listOfFiles.txt", 502, 602);
//		Huffman Newtest4 = new Huffman("listOfFiles.txt", 302, 602);*/
//		Newtest0.makeGraph();		
//		Newtest1.makeGraph();
//		Newtest2.makeGraph();
//		Newtest3.makeGraph();		
//		Newtest4.makeGraph();
//		Newtest5.makeGraph();
//		/*Newtest1.makeGraph();
//		Newtest2.makeGraph();
//		Newtest3.makeGraph();
		
		
		Huffman Oldtest0 = new Huffman("listOfFiles.txt", 0, 1);
		Huffman Oldtest1 = new Huffman("listOfFiles.txt", 0, 625);
		Huffman Oldtest2 = new Huffman("listOfFiles.txt", 601, 602);
		Oldtest0.makeGraph();
		Oldtest1.makeGraph();
		Oldtest2.makeGraph();
		
		
//		Huffman Oldtest0 = new Huffman("listOfFiles.txt", 0, 1);
//		Huffman Oldtest1 = new Huffman("listOfFiles.txt", 0, 2);
//		Huffman Oldtest2 = new Huffman("listOfFiles.txt", 0, 10);
//		Huffman Oldtest3 = new Huffman("listOfFiles.txt", 0, 100);
//		Huffman Oldtest4 = new Huffman("listOfFiles.txt", 0, 300);
//		Oldtest0.makeGraph();
//		Oldtest1.makeGraph();
//		Oldtest2.makeGraph();
//		Oldtest3.makeGraph();
//		Oldtest4.makeGraph();
		
		Huffman all = new Huffman("listOfFiles.txt", 0, 602);
		all.makeGraph();
	}

}

package template;
class CSVParser{
	public void parse(){
		openFile();

		System.out.println("Parsing csv file");
		closeFile();
	}
	public void openFile(){
		System.out.println("opening file");
	}
	public void closeFile(){
		System.out.println("close file");
	}
}
class JSONParser{
	public void parse(){
		openFile();

		System.out.println("Parsing JSON file");
		closeFile();
	}
	public void openFile(){
		System.out.println("opening file");
	}
	public void closeFile(){
		System.out.println("close file");
	}
}
class XMLParser{
	public void parse(){
		openFile();

		System.out.println("Parsing XML file");
		closeFile();
	}
	public void openFile(){
		System.out.println("opening file");
	}
	public void closeFile(){
		System.out.println("close file");
	}
}
public class WithOutTemplateMethod {
	public static void main(String[] args) {
		CSVParser csvParser=new CSVParser();
		csvParser.parse();
		JSONParser jsonParser=new JSONParser();
		jsonParser.parse();
	}
}

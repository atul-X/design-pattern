package template;
abstract class DataParser{
	public final void parse(){
		openFile();
		parseData();
		closeFile();
	}
	protected void openFile(){
		System.out.println("Opening file");
	}
	protected void closeFile(){
		System.out.println("Closing file");
	}
	protected abstract void parseData();
}
class CSVParserII extends DataParser{

	@Override
	protected void parseData() {
		System.out.println("Parsing csv file");
	}
}
class JSONParserII extends DataParser{

	@Override
	protected void parseData() {
		System.out.println("Parsing json file");
	}
}
public class WithTemplatePattern {
	public static void main(String[] args) {
		CSVParserII csvParser = new CSVParserII();
		csvParser.parse();
	}
}

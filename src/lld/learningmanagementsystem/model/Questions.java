package lld.learningmanagementsystem.model;

public class Questions {
	private String id;
	private String name;
	private String description;
	private String type;
	private String options;
	private int marks;
	private String ans;

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}

	public Questions(String id, String name, String description, String type, String options, int marks, String ans) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.options = options;
		this.marks = marks;
		this.ans = ans;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}
}

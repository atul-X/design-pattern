package creational.singleton;

public class AppSetting {
	//private static final AppSetting instance=new AppSetting();
	private static AppSetting instance;

	private String databaseUrl;
	private String apiKey;

	private AppSetting() {
		this.databaseUrl = "jdbc:mysql://localhost:3306/mydb";
		this.apiKey= "wxxzzxzd";
	}

	public static AppSetting getInstance() {

		if (instance == null) {
			instance = new AppSetting();
		}
		return instance;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
}

package creational.singleton;

public class WithOutSingletonPattern {
	public static void main(String[] args) {
		AppSetting appSetting=AppSetting.getInstance();
		AppSetting appSetting1=AppSetting.getInstance();
		System.out.println(appSetting.getDatabaseUrl());
		System.out.println(appSetting1.getDatabaseUrl());
		System.out.println(appSetting==appSetting1);
	}
}

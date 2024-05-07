package model;

public class Juice {
	private String juiceId, juiceName, juiceDescription;
	private int price;
	
	public Juice(String juiceId, String juiceName, String juiceDescription, int price) {
		super();
		this.juiceId = juiceId;
		this.juiceName = juiceName;
		this.juiceDescription = juiceDescription;
		this.price = price;
	}
	
	public String getJuiceId() {
		return juiceId;
	}
	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}
	public String getJuiceName() {
		return juiceName;
	}
	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}
	public String getJuiceDescription() {
		return juiceDescription;
	}
	public void setJuiceDescription(String juiceDescription) {
		this.juiceDescription = juiceDescription;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}

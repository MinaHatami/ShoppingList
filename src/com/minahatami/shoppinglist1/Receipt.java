package com.minahatami.shoppinglist1;

public class Receipt {
	private int id;
	private String storeName, purchaseDate, receiptAmount, image;

	public Receipt(int id, String storeName, String purchaseDate, String receiptAmount,
			String image) {
		super();

		this.id = id;
		this.storeName = storeName;
		this.purchaseDate = purchaseDate;
		this.receiptAmount = receiptAmount;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public String getReceiptAmount() {
		return receiptAmount;
	}

	public String getImage() {
		return image;
	}

}

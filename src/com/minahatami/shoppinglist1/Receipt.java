package com.minahatami.shoppinglist1;

import java.io.Serializable;

public class Receipt implements Serializable{
	private int id, receiptAmount;
	private String storeName, purchaseDate, image;
	
	public Receipt(int id, String storeName, String purchaseDate, int receiptAmount,
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

	public int getReceiptAmount() {
		return receiptAmount;
	}

	public String getImage() {
		return image;
	}

}

package com.paymentology.mujokoreconcile.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "UnmatchTransactions")
public class UnmatchTransaction extends BaseEntity {

	private String profileName;

	private String createdDate;

	private String amount;

	private String narrative;

	private String description;

	private String transactionID;

	private String transactionType;

	private String walletReference;

	private String dataSource;

	/**
	 * @return String return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * @param profileName the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @return String return the narrative
	 */
	public String getNarrative() {
		return narrative;
	}

	/**
	 * @param narrative the narrative to set
	 */
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	/**
	 * @return String return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return String return the transactionID
	 */
	public String getTransactionID() {
		return transactionID;
	}

	/**
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	/**
	 * @return String return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return String return the walletReference
	 */
	public String getWalletReference() {
		return walletReference;
	}

	/**
	 * @param walletReference the walletReference to set
	 */
	public void setWalletReference(String walletReference) {
		this.walletReference = walletReference;
	}

	/**
	 * @return Date return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return Double return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", profileName='" + getProfileName() + "'" + ", createdDate='"
				+ getCreatedDate() + "'" + ", amount='" + getAmount() + "'" + ", narrative='" + getNarrative() + "'"
				+ ", description='" + getDescription() + "'" + ", transactionID='" + getTransactionID() + "'"
				+ ", transactionType='" + getTransactionType() + "'" + ", walletReference='" + getWalletReference()
				+ "'" + "}";
	}

	/**
	 * @return String return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

}

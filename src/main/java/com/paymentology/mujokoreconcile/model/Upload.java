package com.paymentology.mujokoreconcile.model;

import javax.persistence.Entity;
import javax.persistence.Table;

// @Entity
// @Table(name = "uploads")
public class Upload extends BaseEntity {

	private String fileSource;

	private String fileTarget;

	/**
	 * @return String return the fileSource
	 */
	public String getFileSource() {
		return fileSource;
	}

	/**
	 * @param fileSource the fileSource to set
	 */
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	/**
	 * @return String return the fileTarget
	 */
	public String getFileTarget() {
		return fileTarget;
	}

	/**
	 * @param fileTarget the fileTarget to set
	 */
	public void setFileTarget(String fileTarget) {
		this.fileTarget = fileTarget;
	}

}

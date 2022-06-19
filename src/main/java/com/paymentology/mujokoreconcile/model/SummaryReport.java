package com.paymentology.mujokoreconcile.model;

import javax.persistence.Entity;

@Entity
public class SummaryReport extends BaseEntity {

	private String fileNameSource;

	private Long totalRecordSource;

	private Long matchSource;

	private Long unmatchSource;

	private String fileNameTarget;

	private Long totalRecordTarget;

	private Long matchTargete;

	private Long unmatchTarget;

	/**
	 * @return String return the fileNameSource
	 */
	public String getFileNameSource() {
		return fileNameSource;
	}

	/**
	 * @param fileNameSource the fileNameSource to set
	 */
	public void setFileNameSource(String fileNameSource) {
		this.fileNameSource = fileNameSource;
	}

	/**
	 * @return Long return the totalRecordSource
	 */
	public Long getTotalRecordSource() {
		return totalRecordSource;
	}

	/**
	 * @param totalRecordSource the totalRecordSource to set
	 */
	public void setTotalRecordSource(Long totalRecordSource) {
		this.totalRecordSource = totalRecordSource;
	}

	/**
	 * @return Long return the matchSource
	 */
	public Long getMatchSource() {
		return matchSource;
	}

	/**
	 * @param matchSource the matchSource to set
	 */
	public void setMatchSource(Long matchSource) {
		this.matchSource = matchSource;
	}

	/**
	 * @return Long return the unmatchSource
	 */
	public Long getUnmatchSource() {
		return unmatchSource;
	}

	/**
	 * @param unmatchSource the unmatchSource to set
	 */
	public void setUnmatchSource(Long unmatchSource) {
		this.unmatchSource = unmatchSource;
	}

	/**
	 * @return String return the fileNameTarget
	 */
	public String getFileNameTarget() {
		return fileNameTarget;
	}

	/**
	 * @param fileNameTarget the fileNameTarget to set
	 */
	public void setFileNameTarget(String fileNameTarget) {
		this.fileNameTarget = fileNameTarget;
	}

	/**
	 * @return Long return the totalRecordTarget
	 */
	public Long getTotalRecordTarget() {
		return totalRecordTarget;
	}

	/**
	 * @param totalRecordTarget the totalRecordTarget to set
	 */
	public void setTotalRecordTarget(Long totalRecordTarget) {
		this.totalRecordTarget = totalRecordTarget;
	}

	/**
	 * @return Long return the matchTargete
	 */
	public Long getMatchTargete() {
		return matchTargete;
	}

	/**
	 * @param matchTargete the matchTargete to set
	 */
	public void setMatchTargete(Long matchTargete) {
		this.matchTargete = matchTargete;
	}

	/**
	 * @return Long return the unmatchTarget
	 */
	public Long getUnmatchTarget() {
		return unmatchTarget;
	}

	/**
	 * @param unmatchTarget the unmatchTarget to set
	 */
	public void setUnmatchTarget(Long unmatchTarget) {
		this.unmatchTarget = unmatchTarget;
	}

	@Override
	public String toString() {
		return "{" + " fileNameSource='" + getFileNameSource() + "'" + ", totalRecordSource='" + getTotalRecordSource()
				+ "'" + ", matchSource='" + getMatchSource() + "'" + ", unmatchSource='" + getUnmatchSource() + "'"
				+ ", fileNameTarget='" + getFileNameTarget() + "'" + ", totalRecordTarget='" + getTotalRecordTarget()
				+ "'" + ", matchTargete='" + getMatchTargete() + "'" + ", unmatchTarget='" + getUnmatchTarget() + "'"
				+ "}";
	}

}

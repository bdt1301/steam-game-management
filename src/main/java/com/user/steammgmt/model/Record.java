package com.user.steammgmt.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "record")
public class Record {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;

	@Column(nullable = false)
	private String objectType;

	@Column(nullable = false)
	private String objectId;

	@Column(nullable = false)
	private String actionType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date actionDate;

	public Record() {
	}

	public Record(String objectType, String objectId, String actionType, Date actionDate) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.actionType = actionType;
		this.actionDate = actionDate;
	}

	// Getters and Setters
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
}

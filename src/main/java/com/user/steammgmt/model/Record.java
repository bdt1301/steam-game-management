package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "records")
@Getter
@Setter
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

}

package com.donaton.bff.exception;

import org.springframework.http.HttpStatusCode;

public class UpstreamServiceException extends RuntimeException {

	private final HttpStatusCode status;
	private final String upstreamMessage;

	public UpstreamServiceException(HttpStatusCode status, String upstreamMessage) {
		super(upstreamMessage != null ? upstreamMessage : status.toString());
		this.status = status;
		this.upstreamMessage = upstreamMessage;
	}

	public HttpStatusCode getStatus() {
		return status;
	}

	public String getUpstreamMessage() {
		return upstreamMessage;
	}
}

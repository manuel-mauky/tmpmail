package eu.lestard.tmpmail.config;

public enum IntKey {

	/**
	 * This key is only needed for CDI injection and may not be used.
	 */
	_DEFAULT,

	/**
	 * The port of the internal SMTP server that handles incomming emails.
	 */
	INCOMMING_SMTP_PORT,

	/**
	 * THe port of the SMTP server that the outgoing emails are send to.
	 */
	OUTGOING_SMTP_PORT,

}

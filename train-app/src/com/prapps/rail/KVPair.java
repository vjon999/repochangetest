package com.prapps.rail;

public class KVPair implements Comparable<KVPair> {

	private String key;
	private String value;

	public KVPair() {
	}

	public KVPair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public int compareTo(KVPair o) {
		if(value.equals(o.getValue())) {
			return 0;
		}
		else {
			return -1;
		}
	}
}

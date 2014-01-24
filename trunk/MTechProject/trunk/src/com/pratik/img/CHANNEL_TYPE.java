package com.pratik.img;

public enum CHANNEL_TYPE {
	TRANSPARENCY_CHANNEL(24), RED_CHANNEL(16), GREEN_CHANNEL(8), BLUE_CHANNEL(0),
	Y(16), U(8), V(0);
	
	int channelType;
	
	CHANNEL_TYPE(int channelType) {
		this.channelType = channelType;
	}
	
	public int getChannelType() {
		return channelType;
	}
}

package com.tiaa.domain.json;

public class Match {
	private Cmfoodchain cmfoodchain;

	public Cmfoodchain getCmfoodchain() {
		return cmfoodchain;
	}

	public void setCmfoodchain(Cmfoodchain cmfoodchain) {
		this.cmfoodchain = cmfoodchain;
	}

	@Override
	public String toString() {
		return "ClassPojo [cmfoodchain = " + cmfoodchain + "]";
	}
}
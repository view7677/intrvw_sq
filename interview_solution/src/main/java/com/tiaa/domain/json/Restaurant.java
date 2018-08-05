package com.tiaa.domain.json;

import generated.CmfoodchainType;

public class Restaurant {
	private CmfoodchainType cmfoodchain;

	public CmfoodchainType getCmfoodchain() {
		return cmfoodchain;
	}

	public void setCmfoodchain(CmfoodchainType cmfoodchain) {
		this.cmfoodchain = cmfoodchain;
	}

	@Override
	public String toString() {
		return "ClassPojo [cmfoodchain = " + cmfoodchain + "]";
	}
}
package com.tiaa.domain.json;

public class Cmfoodchain {
	private Branch[] branch;

	public Branch[] getBranch() {
		return branch;
	}

	public void setBranch(Branch[] branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "ClassPojo [branch = " + branch + "]";
	}
}

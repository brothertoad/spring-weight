package net.brothertoad.home.weight.bean;

import java.math.BigDecimal;

public class DailyWeight implements Comparable<DailyWeight> {
	
	int date;
	BigDecimal weight;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	public int compareTo(DailyWeight that) {
		return date - that.date;
	}
	
}

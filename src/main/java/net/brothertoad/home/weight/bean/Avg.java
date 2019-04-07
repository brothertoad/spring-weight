package net.brothertoad.home.weight.bean;

import java.math.BigDecimal;

public class Avg {
	
	private int month;
	private int year;
	private BigDecimal avg;
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public BigDecimal getAvg() {
		return avg;
	}
	
	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

}

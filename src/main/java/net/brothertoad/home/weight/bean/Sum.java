package net.brothertoad.home.weight.bean;

import java.math.BigDecimal;

public class Sum {
	
	int month;
	int year;
	int count;
	BigDecimal sum;
	BigDecimal avg;
	
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
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}
	
	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

}

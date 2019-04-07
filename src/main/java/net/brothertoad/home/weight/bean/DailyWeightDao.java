package net.brothertoad.home.weight.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyWeightDao {
	
	int day = 0;
	int month = 0;
	int year = 0;
	String weight;
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

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

	public String getWeight() {
		return weight;
	}
	
	public void setWeight(String weight) {
		this.weight = weight;
	}

}

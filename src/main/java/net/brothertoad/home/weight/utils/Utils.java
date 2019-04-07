package net.brothertoad.home.weight.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.brothertoad.home.weight.bean.Avg;
import net.brothertoad.home.weight.bean.AvgDao;
import net.brothertoad.home.weight.bean.DailyWeight;
import net.brothertoad.home.weight.bean.DailyWeightDao;

public class Utils {
	
	public static String createSortName(String name) {
		String sortName = name;
		String lcName = name.toLowerCase();
		if (lcName.startsWith("a ")) {
			sortName = name.substring(2) + ", " + name.substring(0, 1);
		}
		else if (lcName.startsWith("an ")) {
			sortName = name.substring(3) + ", " + name.substring(0, 2);
		}
		else if (lcName.startsWith("the ")) {
			sortName = name.substring(4) + ", " + name.substring(0, 3);
		}
		return sortName;
	}
	
	public static LocalDate julianToLocalDate(int julian) {
		return LocalDate.MIN.with(java.time.temporal.JulianFields.JULIAN_DAY, julian);
	}
	
	public static int julianToIntegerDate(int julian) {
		return localDateToIntegerDate(julianToLocalDate(julian));
	}
	
	public static int localDateToIntegerDate(LocalDate localDate) {
		return localDate.getYear() * 10000 + localDate.getMonthValue() * 100 + localDate.getDayOfMonth();
	}
	
	public static int yearFromIntegerDate(int date) {
		return date / 10000;
	}
	
	public static int monthFromIntegerDate(int date) {
		return (date / 100) % 100;
	}
	
	public static int dayFromIntegerDate(int date) {
		return date % 100;
	}
	
	public static BigDecimal seedWeightToDecimal(int seedWeight) {
		BigDecimal tenTimesTooBig = new BigDecimal(seedWeight);
		return tenTimesTooBig.divide(BigDecimal.TEN);
	}
	
	public static DailyWeightDao toDao(DailyWeight dw) {
		DailyWeightDao dao = new DailyWeightDao();
		dao.setDay(dayFromIntegerDate(dw.getDate()));
		dao.setMonth(monthFromIntegerDate(dw.getDate()));
		dao.setYear(yearFromIntegerDate(dw.getDate()));
		dao.setWeight(dw.getWeight().toString());
		return dao;
	}
	
	public static AvgDao toDao(Avg avg) {
		AvgDao dao = new AvgDao();
		dao.setYear(avg.getYear());
		dao.setMonth(avg.getMonth());
		dao.setAvg(avg.getAvg().toString());
		return dao;
	}

}

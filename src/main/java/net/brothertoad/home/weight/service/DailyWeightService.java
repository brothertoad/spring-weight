package net.brothertoad.home.weight.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.brothertoad.home.weight.bean.Avg;
import net.brothertoad.home.weight.bean.DailyWeight;
import net.brothertoad.home.weight.bean.Sum;
import net.brothertoad.home.weight.utils.Utils;

@Service
public class DailyWeightService implements IDailyWeightService {

	private static final Logger logger = LoggerFactory.getLogger(DailyWeightService.class);
	
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public int addSeedWeights(List<DailyWeight> seeds) {
		Map<Integer,Set<Integer>> monthsToUpdate = new HashMap<>();
		int alreadyHave = latestDate();
		int total = 0;
		for (DailyWeight seed : seeds) {
			if (seed.getDate() <= alreadyHave) {
				continue;
			}
			int year = Utils.yearFromIntegerDate(seed.getDate());
			int month = Utils.monthFromIntegerDate(seed.getDate());
			total += insertWeight(seed);
			
			// Add the month/year to the map of months to update.
			Set<Integer> monthSet = monthsToUpdate.get(year);
			if (monthSet == null) {
				monthSet = new HashSet<>();
			}
			monthSet.add(month);
			monthsToUpdate.put(year, monthSet);
		}
		logger.info("Inserted {} seeds", total);
		updateMonths(monthsToUpdate);
		return total;
	}
	
	public void insertDailyWeight(DailyWeight dw) {
		int month = Utils.monthFromIntegerDate(dw.getDate());
		int year = Utils.yearFromIntegerDate(dw.getDate());
		insertWeight(dw);
		updateMonth(month, year);
		updateYear(year);
	}
	
	public List<DailyWeight> getLatest(int count) {
		List<DailyWeight> dailys = new ArrayList<>(count);
		String sql = "select date, weight from daily order by date desc limit " + count;
		jdbc.query(sql,  (rs) -> {
			DailyWeight daily = new DailyWeight();
			daily.setDate(rs.getInt(1));
			daily.setWeight(rs.getBigDecimal(2));
			dailys.add(daily);
		});
		return dailys;
	}
	
	public List<DailyWeight> getLowest(int count) {
		List<DailyWeight> dailys = new ArrayList<>(count);
		String sql = "select date, weight from daily order by weight asc limit " + count;
		jdbc.query(sql,  (rs) -> {
			DailyWeight daily = new DailyWeight();
			daily.setDate(rs.getInt(1));
			daily.setWeight(rs.getBigDecimal(2));
			dailys.add(daily);
		});
		return dailys;
	}
	
	public List<Avg> getLatestMonthlyAverages(int count) {
		List<Avg> averages = new ArrayList<>();
		String sql = "select year, month, avg from sum where month != 0 order by year desc, month desc limit " + count;
		jdbc.query(sql,  (rs) -> {
			Avg avg = new Avg();
			avg.setYear(rs.getInt(1));
			avg.setMonth(rs.getInt(2));
			avg.setAvg(rs.getBigDecimal(3));
			averages.add(avg);
		});
		return averages;
	}
	
	public List<Avg> getMonthlyAverages(int count, boolean desc) {
		String order = desc ? "desc" : "asc";
		List<Avg> averages = new ArrayList<>();
		String sql = "select year, month, avg from sum where month != 0 order by avg " + order + " limit " + count;
		jdbc.query(sql,  (rs) -> {
			Avg avg = new Avg();
			avg.setYear(rs.getInt(1));
			avg.setMonth(rs.getInt(2));
			avg.setAvg(rs.getBigDecimal(3));
			averages.add(avg);
		});
		return averages;
	}

	public List<Avg> getYearlyAverages() {
		List<Avg> averages = new ArrayList<>();
		String sql = "select year, avg from sum where month = 0 order by year desc";
		jdbc.query(sql,  (rs) -> {
			Avg avg = new Avg();
			avg.setYear(rs.getInt(1));
			avg.setAvg(rs.getBigDecimal(2));
			averages.add(avg);
		});
		return averages;
	}
	
	private int insertWeight(DailyWeight dw) {
		return jdbc.update("insert into daily (date, weight) values (?, ?)",
				dw.getDate(), dw.getWeight());
	}
	
	private void updateMonths(Map<Integer,Set<Integer>> monthMap) {
		for (Map.Entry<Integer, Set<Integer>> entry: monthMap.entrySet()) {
			for (Integer month: entry.getValue()) {
				updateMonth(month, entry.getKey());
			}
			updateYear(entry.getKey());
		}
	}
	
	private void updateMonth(int month, int year) {
		List<BigDecimal> weights = new ArrayList<>();
		int startDate = year * 10000 + month * 100;
		int endDate = startDate + 99;
		String sql = "select weight from daily where date >= " + startDate + " and date <= " + endDate;
		jdbc.query(sql,  (rs) -> {
			weights.add(rs.getBigDecimal(1));
		});
		if (weights.isEmpty()) return; // nothing to do, this is odd
		// Perhaps use stream reduce to calculate the sum
		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal weight: weights) {
			sum = sum.add(weight);
		}
		BigDecimal avg = sum.divide(new BigDecimal(weights.size()), BigDecimal.ROUND_HALF_UP);
		// Need to check to see if an entry is already there, and do an update rather than
		// an insert if that is the case.
		if (monthSumExists(month, year)) {
			jdbc.update("update sum set count = ?, sum = ?, avg = ? where month = ? and year = ?",
					weights.size(), sum, avg, month, year);
		}
		else {
			jdbc.update("insert into sum (month, year, count, sum, avg) values (?, ?, ?, ?, ?)",
					month, year, weights.size(), sum, avg);
		}
	}
	
	private void updateYear(int year) {
		List<Sum> sums = new ArrayList<>();
		String sql = "select count, sum from sum where year = " + year + " and month != 0";
		jdbc.query(sql,  (rs) -> {
			Sum sum = new Sum();
			sum.setCount(rs.getInt(1));
			sum.setSum(rs.getBigDecimal(2));
			sums.add(sum);
		});
		if (sums.isEmpty()) return; // nothing to do, this is odd
		// Perhaps use stream reduce to calculate the sum
		BigDecimal total = BigDecimal.ZERO;
		int count = 0;
		for (Sum sum: sums) {
			total = total.add(sum.getSum());
			count += sum.getCount();
		}
		BigDecimal avg = total.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_UP);
		// Need to check to see if an entry is already there, and do an update rather than
		// an insert if that is the case.
		if (yearSumExists(year)) {
			jdbc.update("update sum set count = ?, sum = ?, avg = ? where month = 0 and year = ?",
					count, total, avg, year);
		}
		else {
			jdbc.update("insert into sum (month, year, count, sum, avg) values (?, ?, ?, ?, ?)",
					0, year, count, total, avg);
		}
	}
	
	private boolean monthSumExists(int month, int year) {
		String sql = "select count(*) from sum where month = " + month + " and year = " + year;
		int count = jdbc.queryForObject(sql, Integer.class);
		return count > 0;
	}
	
	private boolean yearSumExists(int year) {
		return monthSumExists(0, year);
	}
	
	/*
	 * Return the last date that we already have a weight for.
	 */
	private int latestDate() {
		Integer[] latest = new Integer[1];
		jdbc.query("select date from daily order by date desc limit 1", (rs) -> {
			latest[0] = rs.getInt(1);
		});
		if (latest[0] == null) {
			latest[0] = 0;
		}
		return latest[0];
	}

}

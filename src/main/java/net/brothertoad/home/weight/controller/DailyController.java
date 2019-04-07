package net.brothertoad.home.weight.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.brothertoad.home.weight.bean.Avg;
import net.brothertoad.home.weight.bean.AvgDao;
import net.brothertoad.home.weight.bean.DailyWeight;
import net.brothertoad.home.weight.bean.DailyWeightDao;
import net.brothertoad.home.weight.service.IDailyWeightService;
import net.brothertoad.home.weight.utils.Utils;

@RestController
public class DailyController {
	
	private static final Logger logger = LoggerFactory.getLogger(DailyController.class);
	
	@Autowired
	IDailyWeightService dailyWeightService;
	
	@CrossOrigin()
	@PostMapping("/daily/add")
	public String add(@RequestParam(value="date") String ds, @RequestParam(value="weight") BigDecimal w) {
		if (ds == null || w == null) {
			return "Error\n";
		}
		DailyWeight dailyWeight = new DailyWeight();
		LocalDate local = LocalDate.parse(ds);
		dailyWeight.setDate(Utils.localDateToIntegerDate(local));
		dailyWeight.setWeight(w);
		// logger.info("About to add " + dailyWeight.getWeight() + " for " + dailyWeight.getDate());
		dailyWeightService.insertDailyWeight(dailyWeight);
		return "OK\n";
	}
	
	@CrossOrigin()
	@GetMapping("/latest")
	public List<DailyWeightDao> getLatest(@RequestParam(value="count", defaultValue="30") Integer count) {
		if (count == null) {
			count = 30;
		}
		List<DailyWeight> dailyList = dailyWeightService.getLatest(count);
		List<DailyWeightDao> doaList = new ArrayList<DailyWeightDao>(count);
		for (DailyWeight dw: dailyList) {
			DailyWeightDao dao = Utils.toDao(dw);
			doaList.add(dao);
		}
		return doaList;
	}
	
	@CrossOrigin()
	@GetMapping("/lowest")
	public List<DailyWeightDao> getLowest(@RequestParam(value="count", defaultValue="30") Integer count) {
		if (count == null) {
			count = 30;
		}
		List<DailyWeight> dailyList = dailyWeightService.getLowest(count);
		List<DailyWeightDao> doaList = new ArrayList<DailyWeightDao>(count);
		for (DailyWeight dw: dailyList) {
			DailyWeightDao dao = Utils.toDao(dw);
			doaList.add(dao);
		}
		return doaList;
	}
	
	@CrossOrigin()
	@GetMapping("/latest-months")
	public List<AvgDao> getLatestMonthlyAverages(@RequestParam(value="count", defaultValue="12") Integer count) {
		List<Avg> avgs = dailyWeightService.getLatestMonthlyAverages(count);
		List<AvgDao> avgDaos = new ArrayList<>(avgs.size());
		for (Avg avg: avgs) {
			avgDaos.add(Utils.toDao(avg));
		}
		return avgDaos;
	}
	
	@CrossOrigin()
	@GetMapping("/month-averages/high")
	public List<AvgDao> getMonthAveragesHigh(@RequestParam(value="count", defaultValue="10") Integer count) {
		return getMonthAverages(count, true);
	}
	
	@CrossOrigin()
	@GetMapping("/month-averages/low")
	public List<AvgDao> getMonthAveragesLow(@RequestParam(value="count", defaultValue="10") Integer count) {
		return getMonthAverages(count, false);
	}
	
	private List<AvgDao> getMonthAverages(Integer count, boolean desc) {
		List<Avg> avgs = dailyWeightService.getMonthlyAverages(count, desc);
		List<AvgDao> avgDaos = new ArrayList<>(avgs.size());
		for (Avg avg: avgs) {
			avgDaos.add(Utils.toDao(avg));
		}
		return avgDaos;
	}
	
	@CrossOrigin()
	@GetMapping("/year-averages")
	public List<AvgDao> getYearAverages() {
		List<Avg> avgs = dailyWeightService.getYearlyAverages();
		List<AvgDao> avgDaos = new ArrayList<>(avgs.size());
		for (Avg avg: avgs) {
			avgDaos.add(Utils.toDao(avg));
		}
		return avgDaos;
	}
	
}

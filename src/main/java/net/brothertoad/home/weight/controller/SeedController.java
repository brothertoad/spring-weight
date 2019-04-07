package net.brothertoad.home.weight.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.brothertoad.home.weight.bean.DailyWeight;
import net.brothertoad.home.weight.bean.SeedWeightDao;
import net.brothertoad.home.weight.service.IDailyWeightService;
import net.brothertoad.home.weight.utils.Utils;

@RestController
public class SeedController {
	
	private static final Logger logger = LoggerFactory.getLogger(SeedController.class);
	
	@Autowired
	IDailyWeightService dailyWeightService;
	
	@PostMapping("/seed")
	public String seed(@RequestBody List<SeedWeightDao> seedWeights) {
		if (seedWeights == null) {
			return "Error\n";
		}
		List<DailyWeight> dailyWeights = new ArrayList<DailyWeight>(seedWeights.size());
		for (SeedWeightDao seedWeight: seedWeights) {
			DailyWeight dailyWeight = new DailyWeight();
			dailyWeight.setDate(Utils.julianToIntegerDate(seedWeight.getJulian()));
			dailyWeight.setWeight(Utils.seedWeightToDecimal(seedWeight.getWeight()));
			dailyWeights.add(dailyWeight);
		}
		Collections.sort(dailyWeights);
		logger.info("There are " + dailyWeights.size() + " daily weights to be seeded.");
		int n = dailyWeightService.addSeedWeights(dailyWeights);
		return "Added " + n + " seed weights\n";
	}
	
}

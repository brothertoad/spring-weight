package net.brothertoad.home.weight.service;

import java.util.List;

import net.brothertoad.home.weight.bean.Avg;
import net.brothertoad.home.weight.bean.DailyWeight;

public interface IDailyWeightService {
	
	public int addSeedWeights(List<DailyWeight> seeds);
	public void insertDailyWeight(DailyWeight dw);
	public List<DailyWeight> getLatest(int count);
	public List<DailyWeight> getLowest(int count);
	public List<Avg> getLatestMonthlyAverages(int count);
	public List<Avg> getMonthlyAverages(int count, boolean high);
	public List<Avg> getYearlyAverages();

}

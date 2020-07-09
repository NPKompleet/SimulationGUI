package org.eclipse.app4mc.visualization.timeline.unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.visualization.timeline.utils.TimingUtils;
import org.eclipse.app4mc.visualization.timeline.utils.UnalignedPeriodException;
import org.junit.Before;
import org.junit.Test;

public class TimingUtilTest {
	List<Time> unalignedPeriodList;
	List<Time> alignedPeriodList;

	@Before
	public void setUp() {

		unalignedPeriodList = new ArrayList<>();
		Time time1 = AmaltheaFactory.eINSTANCE.createTime();
		time1.setValue(BigInteger.valueOf(100));
		time1.setUnit(TimeUnit.NS);
		unalignedPeriodList.add(time1);

		Time time2 = AmaltheaFactory.eINSTANCE.createTime();
		time2.setValue(BigInteger.valueOf(1));
		time2.setUnit(TimeUnit.MS);
		unalignedPeriodList.add(time2);

		Time time2b = AmaltheaFactory.eINSTANCE.createTime();
		time2b.setValue(BigInteger.valueOf(37));
		time2b.setUnit(TimeUnit.S);
		unalignedPeriodList.add(time2b);

		alignedPeriodList = new ArrayList<>();
		Time time3 = AmaltheaFactory.eINSTANCE.createTime();
		time3.setValue(BigInteger.valueOf(20));
		time3.setUnit(TimeUnit.MS);
		alignedPeriodList.add(time3);

		Time time4 = AmaltheaFactory.eINSTANCE.createTime();
		time4.setValue(BigInteger.valueOf(10));
		time4.setUnit(TimeUnit.MS);
		alignedPeriodList.add(time4);

		Time time5 = AmaltheaFactory.eINSTANCE.createTime();
		time5.setValue(BigInteger.valueOf(5));
		time5.setUnit(TimeUnit.MS);
		alignedPeriodList.add(time5);

	}

	@Test
	public void getMinimumUnitShouldWork() {

		assertEquals(TimeUnit.NS, TimingUtils.getMinimumTimeUnit(unalignedPeriodList));
	}

	@Test
	public void getMaximumUnitShouldWork() {

		assertEquals(TimeUnit.S, TimingUtils.getMaximumTimeUnit(unalignedPeriodList));
	}

	@Test
	public void getAlignedPeriodShouldWork() {
		assertTrue(TimingUtils.getAlignedPeriods(unalignedPeriodList, TimeUnit.NS).stream()
				.allMatch(x -> x.getUnit() == TimeUnit.NS));

		assertTrue(TimingUtils.getAlignedPeriods(unalignedPeriodList, TimeUnit.MS).stream()
				.allMatch(x -> x.getUnit() == TimeUnit.MS));
	}

	@Test
	public void computeHyperperiodWithAlignedPeriodsShouldWork() throws UnalignedPeriodException {
		assertEquals(BigInteger.valueOf(20), TimingUtils.computeHyperPeriod(alignedPeriodList));
	}

	@Test(expected = UnalignedPeriodException.class)
	public void computeHyperperiodWithUnalignedPeriodsShouldFail() throws UnalignedPeriodException {
		TimingUtils.computeHyperPeriod(unalignedPeriodList);
	}

	@Test
	public void convertTimeUnitShouldWork() {
		assertEquals(BigInteger.valueOf(3), TimingUtils.convertTimeUnit(3000, TimeUnit.MS, TimeUnit.S));

		assertEquals(BigInteger.valueOf(1000000), TimingUtils.convertTimeUnit(1000, TimeUnit.S, TimeUnit.MS));
	}
}

package cn.hutool.bloomfilter.filter;

import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.bitMap.BitMap;
import cn.hutool.bloomfilter.bitMap.IntMap;
import cn.hutool.bloomfilter.bitMap.LongMap;
import cn.hutool.core.lang.Assert;

/**
 * 抽象Bloom过滤器
 *
 * @author loolly
 *
 */
public abstract class AbstractFilter implements BloomFilter {
	private static final long serialVersionUID = 1L;

	protected static int DEFAULT_MACHINE_NUM = BitMap.MACHINE32;

	private BitMap bm = null;

	protected long size;

	/**
	 * 构造
	 *
	 * @param maxValue 最大值
	 * @param machineNum 机器位数
	 */
	public AbstractFilter(long maxValue, int machineNum) {
		init(maxValue, machineNum);
	}

	/**
	 * 构造32位
	 *
	 * @param maxValue 最大值
	 */
	public AbstractFilter(long maxValue) {
		this(maxValue, DEFAULT_MACHINE_NUM);
	}

	/**
	 * 初始化
	 *
	 * @param maxValue 最大值
	 * @param machineNum 机器位数
	 */
	public void init(long maxValue, int machineNum) {
		this.size = Assert.checkBetween(maxValue, 1, Integer.MAX_VALUE);
		final int capacity = (int) ((this.size + machineNum - 1) / machineNum);
		switch (machineNum) {
		case BitMap.MACHINE32:
			bm = new IntMap(capacity);
			break;
		case BitMap.MACHINE64:
			bm = new LongMap(capacity);
			break;
		default:
			throw new RuntimeException("Error Machine number!");
		}
	}

	@Override
	public boolean contains(String str) {
		return bm.contains(Math.abs(hash(str)));
	}

	@Override
	public boolean add(String str) {
		final long hash = Math.abs(hash(str));
		if (bm.contains(hash)) {
			return false;
		}

		bm.add(hash);
		return true;
	}

	/**
	 * 自定义Hash方法
	 *
	 * @param str 字符串
	 * @return HashCode
	 */
	public abstract long hash(String str);
}

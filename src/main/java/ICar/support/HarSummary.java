package ICar.support;

/**
 * HarSummary consists of retrieving total PayLoadsize,call count, total Load
 * time
 */
public class HarSummary {
	private double totalPayloadSize;
	private int callCount;
	private long totalLoadTime;

	public HarSummary(double totalPayloadSize, int callCount, long totalLoadTime) {
		this.totalPayloadSize = totalPayloadSize;
		this.callCount = callCount;
		this.totalLoadTime = totalLoadTime;
	}
	/**
     * To get the total Pay load value
     * 
     * @return total Pay load value
     */
	public double getTotalPayloadSize() {
		return totalPayloadSize;
	}
	/**
     * To get the total load time
     * 
     * @return totalLoadTime
     */
	public long getTotalLoadTime() {
		return totalLoadTime;
	}
	/**
     * To get the call count
     * 
     * @return call count
     */
	public int getCallCount() {
		return callCount;
	}
}

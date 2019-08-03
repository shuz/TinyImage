package tinyimage.histogram;

public final class OtsuThresholder {
	public static int threshold(int[] histogram) {
		int sum = 0, n = 0;
		for (int i = 0; i < histogram.length; ++i) {
			sum += i * histogram[i];
			n += histogram[i];
		}
		if (n == 0) return 0;
		
		int threshold = 0;
		double max = -1;
		int n1 = 0, sum1 = 0;
		for (int i = 0; i < histogram.length; ++i) {
			n1 += histogram[i];
			if (n1 == 0) continue;
			int n2 = n - n1;
			if (n2 == 0) break;
			sum1 += i * histogram[i];
			int sum2 = sum - sum1;
			
			double w1 = n1;
			double w2 = n2;
			double u1 = sum1 / (double)n1;
			double u2 = sum2 / (double)n2;
			double val = w1 * w2 * (u1 - u2) * (u1 - u2);
			if (val > max) {
				max = val;
				threshold = i;
			}
		}

		return threshold;
	}
}

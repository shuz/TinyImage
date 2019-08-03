package tinyimage.histogram;

public final class EntropyThresholder {
	public static int threshold(int[] histogram) {
		double sum = 0;
		int n = 0;
		for (int i = 0; i < histogram.length; ++i) {
			if (histogram[i] == 0) continue;
			double pi = histogram[i];
			sum += - pi * Math.log(pi);
			n += histogram[i];
		}
		if (n == 0) return 0;
		
		int threshold = 0;
		double max = -1;
		int n1 = 0;
		double sum1 = 0;
		for (int i = 0; i < histogram.length; ++i) {
			n1 += histogram[i];
			if (n1 == 0) continue;
			int n2 = n - n1;
			if (n2 == 0) break;
			
			double pi = histogram[i];
			if (pi != 0) sum1 += - pi * Math.log(pi);
			double e1 = sum1 / n1 + Math.log(n1);
			double e2 = (sum - sum1) / n2 + Math.log(n2);
			double val = e1 + e2;
			
			if (val > max) {
				max = val;
				threshold = i;
			}
		}

		return threshold;
	}
}

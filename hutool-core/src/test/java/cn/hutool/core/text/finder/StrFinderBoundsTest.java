package cn.hutool.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrFinderBoundsTest {
	@Test
	void directionsAndExclusiveBoundsMatchReferenceSearch() {
		final String text = "aBaBaba中中";
		for (String pattern : new String[]{"a", "aba", "中中", "absent", text + text}) {
			for (boolean reverse : new boolean[]{false, true}) {
				for (int end = -text.length() - 2; end <= text.length() + 2; end++) {
					for (int from = -2; from <= text.length() + 2; from++) {
						for (boolean ignoreCase : new boolean[]{false, true}) {
							final int expected = find(text, pattern, from, end, reverse, ignoreCase);
							assertEquals(expected, new StrFinder(pattern, ignoreCase).setText(text)
								.setNegative(reverse).setEndIndex(end).start(from));
							assertEquals(expected, new StrFinder(new StringBuilder(pattern), ignoreCase)
								.setText(new StringBuilder(text)).setNegative(reverse).setEndIndex(end).start(from));
						}
					}
				}
			}
		}
	}

	private static int find(String text, String pattern, int from, int end, boolean reverse, boolean ignoreCase) {
		final int bound = reverse && end == -1 ? -1 :
			(end < 0 ? end + text.length() + 1 : Math.min(end, text.length()));
		if (reverse) {
			for (int i = Math.min(Math.max(0, from), text.length() - pattern.length()); i >= 0 && i > bound; i--) {
				if (text.regionMatches(ignoreCase, i, pattern, 0, pattern.length())) {
					return i;
				}
			}
		} else {
			for (int i = Math.max(0, from); i + pattern.length() <= bound; i++) {
				if (text.regionMatches(ignoreCase, i, pattern, 0, pattern.length())) {
					return i;
				}
			}
		}
		return -1;
	}
}

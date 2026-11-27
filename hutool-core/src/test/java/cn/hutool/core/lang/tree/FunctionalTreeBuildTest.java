package cn.hutool.core.lang.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalTreeBuildTest {
	@Test
	void retainsFirstReachableDuplicateAndPostOrderCallbacks() {
		final Row root = new Row(1, 0);
		final Row first = new Row(2, 1);
		final Row duplicate = new Row(2, 1);
		final Row sibling = new Row(3, 1);
		final Row grandchild = new Row(4, 2);
		final Row orphan = new Row(8, 9);
		final Row otherRoot = new Row(5, 0);
		final List<Integer> calls = new ArrayList<>();
		final List<Row> roots = TreeUtil.build(Arrays.asList(first, root, grandchild, duplicate, orphan, sibling, otherRoot),
			0, row -> row.id, row -> row.parent, (row, children) -> {
				row.children = children;
				calls.add(row.id);
			});
		assertEquals(Arrays.asList(root, otherRoot), roots);
		assertEquals(Arrays.asList(first, sibling), root.children);
		assertEquals(Arrays.asList(grandchild), first.children);
		assertNull(duplicate.children);
		assertNull(orphan.children);
		assertEquals(Arrays.asList(4, 2, 3, 1, 5), calls);
	}

	@Test
	void stopsAtAnAlreadyVisitedIdInAReachableCycle() {
		final Row root = new Row(1, 0);
		final Row child = new Row(2, 1);
		final Row repeatedId = new Row(1, 2);
		TreeUtil.build(Arrays.asList(root, child, repeatedId), 0, row -> row.id,
			row -> row.parent, (row, children) -> row.children = children);
		assertEquals(Arrays.asList(child), root.children);
		assertEquals(Arrays.asList(repeatedId), child.children);
		assertTrue(repeatedId.children.isEmpty());
	}

	@Test
	void deepChainVisitsEachParentIdOnce() {
		final List<Row> input = new ArrayList<>();
		for (int i = 12000; i > 0; i--) {
			input.add(new Row(i, i - 1));
		}
		final AtomicInteger lookups = new AtomicInteger();
		final List<Row> roots = TreeUtil.build(input, 0, row -> row.id, row -> {
			lookups.incrementAndGet();
			return row.parent;
		}, (row, children) -> row.children = children);
		assertEquals(input.size(), lookups.get());
		Row node = roots.get(0);
		for (int i = 1; i < input.size(); i++) {
			assertEquals(i, node.id);
			node = node.children.get(0);
		}
		assertTrue(node.children.isEmpty());
	}

	static class Row {
		final int id;
		final int parent;
		List<Row> children;

		Row(int id, int parent) {
			this.id = id;
			this.parent = parent;
		}
	}
}

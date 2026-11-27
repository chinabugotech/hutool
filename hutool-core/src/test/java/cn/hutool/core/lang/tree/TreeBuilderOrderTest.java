package cn.hutool.core.lang.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TreeBuilderOrderTest {
	@Test
	void sortOnlyNewSiblingsAndResolveParentsByInputMapKey() {
		final Tree<Integer> root = node(0, -1, 0);
		final Tree<Integer> parent = node(10, 0, 2);
		final Tree<Integer> oldChild = node(99, 10, 99);
		parent.addChildren(oldChild);
		final Map<Integer, Tree<Integer>> input = new LinkedHashMap<>();
		input.put(91, parent);
		input.put(11, node(11, 91, 5));
		input.put(20, node(20, 0, 1));
		input.put(12, node(12, 91, 1));
		input.put(13, node(13, 91, 5));
		input.put(44, node(44, 404, 1));
		final Tree<Integer> tree = new TreeBuilder<>(root).append(input).build();
		assertEquals(Arrays.asList(20, 10), ids(tree.getChildren()));
		assertEquals(Arrays.asList(99, 12, 11, 13), ids(parent.getChildren()));
		assertSame(parent, input.get(11).getParent());
		assertEquals(Integer.valueOf(10), input.get(11).getParentId());
		assertNull(input.get(44).getParent());
	}

	@Test
	void comparatorFailureDoesNotPartiallyAttachOtherGroups() {
		final Tree<Integer> root = node(0, -1, 0);
		final Tree<Integer> parent = node(1, 0, 1);
		final Tree<Integer> child = node(2, 1, 1);
		final Tree<Integer> incompatible = node(3, 1, 2).setWeight("two");
		final TreeBuilder<Integer> builder = new TreeBuilder<>(root).append(Arrays.asList(parent, child, incompatible));
		assertThrows(ClassCastException.class, builder::build);
		assertNull(root.getChildren());
		assertNull(parent.getChildren());
	}

	@Test
	void shuffledForestsMatchGlobalStableWeightOrder() {
		final Random random = new Random(71);
		for (int round = 0; round < 20; round++) {
			final List<Tree<Integer>> input = new ArrayList<>();
			for (int id = 1; id <= 150; id++) {
				input.add(node(id, random.nextInt(id), random.nextInt(4)));
			}
			Collections.shuffle(input, random);
			final List<Tree<Integer>> ordered = new ArrayList<>(input);
			ordered.sort(Comparator.naturalOrder());
			final Map<Integer, List<Integer>> expected = new LinkedHashMap<>();
			for (Tree<Integer> child : ordered) {
				expected.computeIfAbsent(child.getParentId(), key -> new ArrayList<>()).add(child.getId());
			}
			final Tree<Integer> root = TreeBuilder.of(0).append(input).build();
			input.add(root);
			for (Tree<Integer> parent : input) {
				if (expected.containsKey(parent.getId())) {
					assertEquals(expected.get(parent.getId()), ids(parent.getChildren()));
				} else {
					assertNull(parent.getChildren());
				}
			}
		}
	}

	private static Tree<Integer> node(int id, int parent, int weight) {
		return new Tree<Integer>().setId(id).setParentId(parent).setWeight(weight);
	}

	private static List<Integer> ids(List<Tree<Integer>> nodes) {
		return nodes.stream().map(Tree::getId).collect(Collectors.toList());
	}
}

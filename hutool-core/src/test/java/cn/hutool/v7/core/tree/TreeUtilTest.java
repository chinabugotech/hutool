/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.tree;

import cn.hutool.v7.core.tree.parser.NodeParser;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TreeUtil} 单元测试
 */
class TreeUtilTest {

	// region ----- buildSingle 方法测试 -----

	@Test
	void testBuildSingleWithIterableOfTreeNodesAndDefaultRootId() {
		// 准备数据
		final List<TreeNode<Integer>> nodes = createSampleTreeNodes();

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(nodes);

		// 验证结果
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertTrue(result.hasChild());

		final List<MapTree<Integer>> children = result.getChildren();
		assertEquals(2, children.size());

		// 验证第一个子节点
		final MapTree<Integer> dept1 = children.get(0);
		assertEquals(1, dept1.getId());
		assertEquals("研发部", dept1.getName());
		assertEquals(0, dept1.getParentId());
		assertTrue(dept1.hasChild());

		// 验证第二个子节点
		final MapTree<Integer> dept2 = children.get(1);
		assertEquals(2, dept2.getId());
		assertEquals("人事部", dept2.getName());
		assertEquals(0, dept2.getParentId());
		assertFalse(dept2.hasChild());
	}

	@Test
	void testBuildSingleWithIterableOfTreeNodesAndCustomRootId() {
		// 准备数据
		final List<TreeNode<Integer>> nodes = createSampleTreeNodes();
		final Integer customRootId = 999;

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(nodes, customRootId);

		// 验证结果
		assertNotNull(result);
		assertEquals(customRootId, result.getId());
		// 此处应为自定义根节点，因此不包含子节点
		assertFalse(result.hasChild());
		final List<MapTree<Integer>> children = result.getChildren();
		assertNull(children);
	}

	@Test
	void testBuildSingleWithIterableOfGenericObjects() {
		// 准备数据
		final List<SampleObject> objects = createSampleObjects();

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(objects, 0, new SampleObjectNodeParser());

		// 验证结果
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertTrue(result.hasChild());

		final List<MapTree<Integer>> children = result.getChildren();
		assertEquals(2, children.size());
	}

	@Test
	void testBuildSingleWithIterableOfGenericObjectsAndTreeNodeConfig() {
		// 准备数据
		final List<SampleObject> objects = createSampleObjects();
		final TreeNodeConfig config = new TreeNodeConfig()
			.setIdKey("key")
			.setParentIdKey("parentKey")
			.setNameKey("label")
			.setChildrenKey("childList");

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(objects, 0, config, new SampleObjectNodeParser());

		// 验证结果
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertEquals("key", result.getConfig().getIdKey());
	}

	@Test
	void testBuildSingleWithEmptyList() {
		// 准备数据
		final List<TreeNode<Integer>> emptyList = Collections.emptyList();

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(emptyList, 999);

		// 验证结果
		assertNotNull(result);
		assertEquals(999, result.getId());
		assertFalse(result.hasChild());
	}

	@Test
	void testBuildSingleWithNullList() {
		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle((Iterable<TreeNode<Integer>>) null, 999);

		// 验证结果
		assertNotNull(result);
		assertEquals(999, result.getId());
		assertFalse(result.hasChild());
	}

	@Test
	void testBuildSingleWithMapAndRootId() {
		// 准备数据
		final Map<Integer, MapTree<Integer>> map = new HashMap<>();
		final TreeNodeConfig config = TreeNodeConfig.DEFAULT_CONFIG;

		final MapTree<Integer> node1 = new MapTree<Integer>(config).setId(1).setParentId(0).setName("节点1");
		final MapTree<Integer> node2 = new MapTree<Integer>(config).setId(2).setParentId(1).setName("节点2");

		map.put(1, node1);
		map.put(2, node2);

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(map, 0);

		// 验证结果
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertTrue(result.hasChild());

		final List<MapTree<Integer>> children = result.getChildren();
		assertEquals(1, children.size());
		assertEquals(1, children.get(0).getId());
	}

	@Test
	void testBuildSingleWithEmptyMap() {
		// 准备数据
		final Map<Integer, MapTree<Integer>> emptyMap = Collections.emptyMap();

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(emptyMap, 999);

		// 验证结果
		assertNotNull(result);
		assertEquals(999, result.getId());
		assertFalse(result.hasChild());
	}

	// endregion

	// region ----- build 方法测试 -----

	@Test
	void testBuildWithIterableOfTreeNodesAndDefaultRootId() {
		// 准备数据
		final List<TreeNode<Integer>> nodes = createSampleTreeNodes();

		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build(nodes);

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size());

		final MapTree<Integer> dept1 = result.get(0);
		assertEquals(1, dept1.getId());
		assertEquals("研发部", dept1.getName());
		assertTrue(dept1.hasChild());

		final MapTree<Integer> dept2 = result.get(1);
		assertEquals(2, dept2.getId());
		assertEquals("人事部", dept2.getName());
		assertFalse(dept2.hasChild());
	}

	@Test
	void testBuildWithIterableOfTreeNodesAndCustomRootId() {
		// 准备数据
		final List<TreeNode<Integer>> nodes = createSampleTreeNodes();
		final Integer customRootId = 999;

		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build(nodes, customRootId);

		// 999没有对应的子节点，所以返回null
		assertNull(result);
	}

	@Test
	void testBuildWithIterableOfGenericObjects() {
		// 准备数据
		final List<SampleObject> objects = createSampleObjects();

		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build(objects, 0, new SampleObjectNodeParser());

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size());

		final MapTree<Integer> dept1 = result.get(0);
		assertEquals(1, dept1.getId());
		assertEquals("研发部", dept1.getName());
	}

	@Test
	void testBuildWithEmptyList() {
		// 准备数据
		final List<TreeNode<Integer>> emptyList = Collections.emptyList();

		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build(emptyList, 999);

		// 999下面没有子节点，所以返回null
		assertNull(result);
	}

	@Test
	void testBuildWithNullList() {
		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build((Iterable<TreeNode<Integer>>) null, 999);

		// 源数据为null，所以返回null
		assertNull(result);
	}

	@Test
	void testBuildWithMapAndRootId() {
		// 准备数据
		final Map<Integer, MapTree<Integer>> map = new HashMap<>();
		final TreeNodeConfig config = TreeNodeConfig.DEFAULT_CONFIG;

		final MapTree<Integer> node1 = new MapTree<Integer>(config).setId(1).setParentId(0).setName("节点1");
		final MapTree<Integer> node2 = new MapTree<Integer>(config).setId(2).setParentId(1).setName("节点2");

		map.put(1, node1);
		map.put(2, node2);

		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.build(map, 0);

		// 验证结果
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getId());
	}

	// endregion

	// region ----- getNode 方法测试 -----

	@Test
	void testGetNodeFound() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);

		// 执行测试
		final MapTree<Integer> result = TreeUtil.getNode(tree, 11);

		// 验证结果
		assertNotNull(result);
		assertEquals(11, result.getId());
		assertEquals("研发一部", result.getName());
		assertEquals(1, result.getParentId());
	}

	@Test
	void testGetNodeNotFound() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);

		// 执行测试
		final MapTree<Integer> result = TreeUtil.getNode(tree, 999);

		// 验证结果
		assertNull(result);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	void testGetNodeWithNullTree() {
		// 执行测试
		final MapTree<Integer> result = TreeUtil.getNode(null, 1);

		// 验证结果
		assertNull(result);
	}

	@Test
	void testGetNodeWithNullId() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);

		// 执行测试
		final MapTree<Integer> result = TreeUtil.getNode(tree, null);

		// 验证结果
		assertNull(result);
	}

	// endregion

	// region ----- getParentsName 方法测试 -----

	@Test
	void testGetParentsNameIncludeCurrentNode() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试
		final List<CharSequence> result = TreeUtil.getParentsName(targetNode, true);

		// 验证结果
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals("研发一部", result.get(0));
		assertEquals("研发部", result.get(1));
		assertEquals("根节点", result.get(2));
	}

	@Test
	void testGetParentsNameExcludeCurrentNode() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试
		final List<CharSequence> result = TreeUtil.getParentsName(targetNode, false);

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("研发部", result.get(0));
		assertEquals("根节点", result.get(1));
	}

	@Test
	void testGetParentsNameWithNullNode() {
		// 执行测试
		final List<CharSequence> result = TreeUtil.getParentsName(null, true);

		// 验证结果
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void testGetParentsNameWithRootNodeIncludeCurrent() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> rootNode = TreeUtil.getNode(tree, 0);

		// 执行测试
		final List<CharSequence> result = TreeUtil.getParentsName(rootNode, true);

		// 验证结果
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("根节点", result.get(0));
	}

	@Test
	void testGetParentsNameWithRootNodeExcludeCurrent() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> rootNode = TreeUtil.getNode(tree, 0);

		// 执行测试
		final List<CharSequence> result = TreeUtil.getParentsName(rootNode, false);

		// 验证结果
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// endregion

	// region ----- getParentsId 方法测试 -----

	@Test
	void testGetParentsIdIncludeCurrentNode() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试
		final List<Integer> result = TreeUtil.getParentsId(targetNode, true);

		// 验证结果
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(11, result.get(0));
		assertEquals(1, result.get(1));
		assertEquals(0, result.get(2));
	}

	@Test
	void testGetParentsIdExcludeCurrentNode() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试
		final List<Integer> result = TreeUtil.getParentsId(targetNode, false);

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(1, result.get(0));
		assertEquals(0, result.get(1));
	}

	@Test
	void testGetParentsIdWithNullNode() {
		// 执行测试
		final List<Integer> result = TreeUtil.getParentsId(null, true);

		// 验证结果
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// endregion

	// region ----- getParents 方法测试 -----

	@Test
	void testGetParentsWithCustomFunctionIncludeCurrent() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试 - 使用权重作为提取函数
		final List<Comparable<?>> result = TreeUtil.getParents(targetNode, true, MapTree::getWeight);

		// 验证结果
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(10, result.get(0));
		assertEquals(1, result.get(1));
		assertEquals(0, result.get(2));
	}

	@Test
	void testGetParentsWithCustomFunctionExcludeCurrent() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试 - 使用权重作为提取函数
		final List<Comparable<?>> result = TreeUtil.getParents(targetNode, false, MapTree::getWeight);

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(1, result.get(0));
		assertEquals(0, result.get(1));
	}

	@Test
	void testGetParentsWithNullFunction() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);
		final MapTree<Integer> targetNode = TreeUtil.getNode(tree, 11);

		// 执行测试
		final List<Object> result = TreeUtil.getParents(targetNode, true, node -> null);

		// 验证结果
		assertNotNull(result);
		assertEquals(2, result.size()); // 根节点的null不会被加入
		assertNull(result.get(0));
		assertNull(result.get(1));
	}

	@Test
	void testGetParentsWithNullNode() {
		// 执行测试
		final List<String> result = TreeUtil.getParents(null, true, node -> "test");

		// 验证结果
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// endregion

	// region ----- createEmptyNode 方法测试 -----

	@Test
	void testCreateEmptyNode() {
		// 执行测试
		final MapTree<String> result = TreeUtil.createEmptyNode("testId");

		// 验证结果
		assertNotNull(result);
		assertEquals("testId", result.getId());
		assertNull(result.getName());
		assertNull(result.getParentId());
		assertNull(result.getWeight());
		assertFalse(result.hasChild());
	}

	@Test
	void testCreateEmptyNodeWithNullId() {
		// 执行测试
		final MapTree<String> result = TreeUtil.createEmptyNode(null);

		// 验证结果
		assertNotNull(result);
		assertNull(result.getId());
	}

	// endregion

	// region ----- toList 方法测试 -----

	@Test
	void testToListDepthFirst() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);

		// 执行测试 - 深度优先遍历
		final List<MapTree<Integer>> result = TreeUtil.toList(tree, false);

		// 验证结果
		assertNotNull(result);
		assertEquals(5, result.size());
		// 深度优先：根 -> 研发部 -> 研发一部 -> 研发二部 -> 人事部
		assertEquals(0, result.get(0).getId());
		assertEquals(1, result.get(1).getId());
		assertEquals(11, result.get(2).getId());
		assertEquals(12, result.get(3).getId());
		assertEquals(2, result.get(4).getId());
	}

	@Test
	void testToListBreadthFirst() {
		// 准备数据
		final MapTree<Integer> tree = TreeUtil.buildSingle(createSampleTreeNodes(), 0);

		// 执行测试 - 广度优先遍历
		final List<MapTree<Integer>> result = TreeUtil.toList(tree, true);

		// 验证结果
		assertNotNull(result);
		assertEquals(5, result.size());
		// 广度优先：根 -> 研发部 -> 人事部 -> 研发一部 -> 研发二部
		assertEquals(0, result.get(0).getId());
		assertEquals(1, result.get(1).getId());
		assertEquals(2, result.get(2).getId());
		assertEquals(11, result.get(3).getId());
		assertEquals(12, result.get(4).getId());
	}

	@Test
	void testToListWithNullTree() {
		// 执行测试
		final List<MapTree<Integer>> result = TreeUtil.toList(null, true);

		// 验证结果
		assertNull(result);
	}

	// endregion

	// region ----- 边界值测试 -----

	@Test
	void testBuildWithCircularReference() {
		// 准备数据 - 创建循环引用的情况
		final List<TreeNode<Integer>> nodes = new ArrayList<>();
		nodes.add(new TreeNode<>(1, 2, "节点1", 1)); // 节点1的父节点是节点2
		nodes.add(new TreeNode<>(2, 1, "节点2", 2)); // 节点2的父节点是节点1

		// 执行测试 - 应该正常处理，不会出现无限循环
		final MapTree<Integer> result = TreeUtil.buildSingle(nodes, 0);

		// 验证结果
		assertNotNull(result);
		assertEquals(0, result.getId());
		// 循环引用的节点应该不会挂载到根节点下
		assertFalse(result.hasChild());
	}

	@Test
	void testBuildWithDuplicateIds() {
		// 准备数据 - 包含重复ID的节点
		final List<TreeNode<Integer>> nodes = new ArrayList<>();
		nodes.add(new TreeNode<>(1, 0, "节点1", 1));
		nodes.add(new TreeNode<>(1, 0, "节点1重复", 2)); // 重复ID

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(nodes, 0);

		// 验证结果 - 重复ID的节点应该覆盖前面的节点
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertTrue(result.hasChild());

		final List<MapTree<Integer>> children = result.getChildren();
		assertEquals(1, children.size());
		assertEquals("节点1重复", children.get(0).getName()); // 应该是第二个节点
	}

	@Test
	void testBuildWithVeryLargeTree() {
		// 准备数据 - 创建深度较大的树
		final List<TreeNode<Integer>> nodes = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			nodes.add(new TreeNode<>(i, i - 1, "节点" + i, i));
		}

		// 执行测试
		final MapTree<Integer> result = TreeUtil.buildSingle(nodes, 0);

		// 验证结果 - 应该能够正常构建大型树
		assertNotNull(result);
		assertEquals(0, result.getId());
		assertTrue(result.hasChild());

		// 验证树的深度
		MapTree<Integer> current = result;
		int depth = 0;
		while (current.hasChild() && depth < 100) {
			current = current.getChildren().get(0);
			depth++;
		}
		assertTrue(depth >= 1); // 至少有一层子节点
	}

	// endregion

	// region ----- 辅助方法 -----

	/**
	 * 创建示例树节点数据，创建结构为：
	 * <pre>
	 *     根节点[0]
	 *     - 研发部[1]
	 *     -- 研发一部[11]
	 *     -- 研发二部[12]
	 *     - 人事部[2]
	 * </pre>
	 */
	private List<TreeNode<Integer>> createSampleTreeNodes() {
		final List<TreeNode<Integer>> nodes = new ArrayList<>();

		// 根节点
		nodes.add(new TreeNode<>(0, null, "根节点", 0));

		// 一级节点
		nodes.add(new TreeNode<>(1, 0, "研发部", 1));
		nodes.add(new TreeNode<>(2, 0, "人事部", 2));

		// 二级节点
		nodes.add(new TreeNode<>(11, 1, "研发一部", 10));
		nodes.add(new TreeNode<>(12, 1, "研发二部", 20));

		return nodes;
	}

	/**
	 * 创建示例通用对象数据
	 */
	private List<SampleObject> createSampleObjects() {
		final List<SampleObject> objects = new ArrayList<>();

		objects.add(new SampleObject(1, 0, "研发部", 1));
		objects.add(new SampleObject(2, 0, "人事部", 2));
		objects.add(new SampleObject(11, 1, "研发一部", 10));
		objects.add(new SampleObject(12, 1, "研发二部", 20));

		return objects;
	}

	/**
	 * 示例对象类，用于测试通用对象构建
	 */
	private static class SampleObject {
		private final Integer id;
		private final Integer parentId;
		private final String name;
		private final Integer weight;

		public SampleObject(final Integer id, final Integer parentId, final String name, final Integer weight) {
			this.id = id;
			this.parentId = parentId;
			this.name = name;
			this.weight = weight;
		}

		public Integer getId() {
			return id;
		}

		public Integer getParentId() {
			return parentId;
		}

		public String getName() {
			return name;
		}

		public Integer getWeight() {
			return weight;
		}
	}

	/**
	 * 示例对象解析器
	 */
	private static class SampleObjectNodeParser implements NodeParser<SampleObject, Integer> {
		@Override
		public void parse(final SampleObject object, final MapTree<Integer> treeNode) {
			treeNode.setId(object.getId());
			treeNode.setParentId(object.getParentId());
			treeNode.setName(object.getName());
			treeNode.setWeight(object.getWeight());
		}
	}

	// endregion
}

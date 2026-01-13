package cn.hutool.v7.core.codec.hash;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConsistentHashTest {

    @Test
    public void testConstructorWithDefaultHashFunction() {
        final List<String> nodes = Arrays.asList("node1", "node2", "node3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(3, nodes);

        assertNotNull(consistentHash);
        // Cannot directly access numberOfReplicas and circle as they are private
        // We'll test the functionality instead
        final String node = consistentHash.get("testKey");
        assertNotNull(node);
        assertTrue(nodes.contains(node));
    }

    @Test
    public void testConstructorWithCustomHashFunction() {
        final Hash32<Object> customHash = Object::hashCode;
        final List<String> nodes = Arrays.asList("server1", "server2", "server3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(customHash, 2, nodes);

        assertNotNull(consistentHash);
        // Test functionality instead of accessing private fields
        final String node = consistentHash.get("testKey");
        assertNotNull(node);
        assertTrue(nodes.contains(node));
    }

    @Test
    public void testAddNode() {
        final List<String> nodes = Collections.singletonList("initial");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(2, nodes);

        // Test that we can add a new node
        consistentHash.add("newNode");

        // Verify that the new node can be retrieved for some keys
        final String result = consistentHash.get("someKey");
        assertNotNull(result);
    }

    @Test
    public void testRemoveNode() {
        final List<String> nodes = Arrays.asList("node1", "node2", "node3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(2, nodes);

        // Initially, there should be nodes
        final String initialResult = consistentHash.get("testKey");
        assertNotNull(initialResult);

        // Remove a node
        consistentHash.remove("node2");

        // After removal, there should still be nodes to handle requests
        final String resultAfterRemoval = consistentHash.get("testKey");
        assertNotNull(resultAfterRemoval);
        // The result might be different, but should not be null if other nodes exist
    }

    @Test
    public void testGetNode() {
        final List<String> nodes = Arrays.asList("server1", "server2", "server3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(3, nodes);

        // Test that we can get a node for a key
        final String node = consistentHash.get("key1");
        assertNotNull(node);
        assertTrue(nodes.contains(node));

        // Test with different keys
        final String node2 = consistentHash.get("key2");
        assertNotNull(node2);
        assertTrue(nodes.contains(node2));
    }

    @Test
    public void testGetNodeWithEmptyCircle() {
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(2, Collections.emptyList());

        // Should return null when there are no nodes
        final String node = consistentHash.get("anyKey");
        assertNull(node);
    }

    @Test
    public void testConsistency() {
        final List<String> nodes = Arrays.asList("server1", "server2", "server3", "server4", "server5");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(10, nodes);

        // Test that the same key always maps to the same node
        final String node1 = consistentHash.get("consistentKey");
        final String node2 = consistentHash.get("consistentKey");
        final String node3 = consistentHash.get("consistentKey");

        assertEquals(node1, node2);
        assertEquals(node2, node3);
    }

    @Test
    public void testLoadDistribution() {
        final List<String> nodes = Arrays.asList("server1", "server2", "server3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(10, nodes);

        // Map many keys to nodes to test distribution
        final int[] hits = new int[3];
        for (int i = 0; i < 100; i++) {
            final String node = consistentHash.get("key" + i);
            switch (node) {
                case "server1":
                    hits[0]++;
                    break;
                case "server2":
                    hits[1]++;
                    break;
                case "server3":
                    hits[2]++;
                    break;
            }
        }

        // Verify that all servers got some traffic (with higher replica count, distribution should be relatively balanced)
        for (final int hitCount : hits) {
            assertTrue(hitCount > 0, "Each server should receive some load");
        }
    }

    @Test
    public void testNodeAdditionDoesNotDisruptTooMuch() {
        final List<String> initialNodes = Arrays.asList("server1", "server2");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(10, initialNodes);

        // Map keys to nodes with initial setup
        final String[] initialMappings = new String[50];
        for (int i = 0; i < 50; i++) {
            initialMappings[i] = consistentHash.get("key" + i);
        }

        // Add a new node
        consistentHash.add("server3");

        // Map the same keys again
        int remappedCount = 0;
        for (int i = 0; i < 50; i++) {
            final String newMapping = consistentHash.get("key" + i);
            if (!initialMappings[i].equals(newMapping)) {
                remappedCount++;
            }
        }

        // Verify that not ALL keys are remapped (which would happen with naive modulo hashing)
        assertTrue(remappedCount < 50, "Not all keys should be remapped when adding a new node");
        // In a well-distributed consistent hash, typically only a fraction of keys are remapped
        assertTrue(remappedCount <= 20, "Remapping should be minimal");
    }

    @Test
    public void testNodeRemovalDoesNotDisruptTooMuch() {
        final List<String> initialNodes = Arrays.asList("server1", "server2", "server3");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(10, initialNodes);

        // Map keys to nodes with initial setup
        final String[] initialMappings = new String[50];
        for (int i = 0; i < 50; i++) {
            initialMappings[i] = consistentHash.get("key" + i);
        }

        // Remove a node
        consistentHash.remove("server3");

        // Map the same keys again
        int remappedCount = 0;
        for (int i = 0; i < 50; i++) {
            final String newMapping = consistentHash.get("key" + i);
            if (!newMapping.equals(initialMappings[i])) {
                remappedCount++;
            }
        }

        // Verify that not ALL keys are remapped
        assertTrue(remappedCount < 50, "Not all keys should be remapped when removing a node");
    }

    @Test
    public void testSingleNode() {
        final List<String> nodes = Collections.singletonList("onlyServer");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(5, nodes);

        // All keys should map to the same server
        for (int i = 0; i < 20; i++) {
            assertEquals("onlyServer", consistentHash.get("key" + i));
        }
    }

    @Test
    public void testManyVirtualNodes() {
        final List<String> nodes = Arrays.asList("node1", "node2");
        final ConsistentHash<String> consistentHash = new ConsistentHash<>(100, nodes); // Many replicas

        // Should still work normally
        final String node = consistentHash.get("testKey");
        assertNotNull(node);
        assertTrue(nodes.contains(node));
    }
}

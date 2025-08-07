package com.example.universe.simulator.entityservice;

import com.example.universe.simulator.common.config.CachingConfig;
import com.example.universe.simulator.common.config.RabbitMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Import({CachingConfig.class, RabbitMQConfig.class})
@EnableAsync
public class EntityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntityServiceApplication.class, args);
    }

    // @SuppressWarnings("unused")
    // public static class ArrayReader {
    //
    //     int length() {
    //         return 0;
    //     }
    //
    //     int compareSub(int l, int r, int x, int y) {
    //         return l + r + x + y;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public interface HtmlParser {
    //     List<String> getUrls(String url);
    // }
    //
    // @SuppressWarnings("unused")
    // public static class VersionControl {
    //
    //     boolean isBadVersion(int version) {
    //         return version > 0;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public static class Node1 {
    //
    //     int val;
    //     List<Node1> neighbors = new ArrayList<>();
    //
    //     Node1() { }
    //
    //     Node1(int val) {
    //         this(val, new ArrayList<>());
    //     }
    //
    //     Node1(int val, ArrayList<Node1> neighbors) {
    //         this.val = val;
    //         this.neighbors = neighbors;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public static class Node2 {
    //
    //     int val;
    //     Node2 left;
    //     Node2 right;
    //     Node2 next;
    //
    //     Node2() { }
    //
    //     Node2(int val) {
    //         this(val, null, null, null);
    //     }
    //
    //     Node2(int val, Node2 left, Node2 right, Node2 next) {
    //         this.val = val;
    //         this.left = left;
    //         this.right = right;
    //         this.next = next;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public static class Node3 {
    //
    //     int val;
    //     Node3 left;
    //     Node3 right;
    //     Node3 parent;
    //
    //     Node3() { }
    //
    //     Node3(int val) {
    //         this(val, null, null, null);
    //     }
    //
    //     Node3(int val, Node3 left, Node3 right, Node3 parent) {
    //         this.val = val;
    //         this.left = left;
    //         this.right = right;
    //         this.parent = parent;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public static class ListNode {
    //
    //     int val;
    //     ListNode next;
    //
    //     ListNode() { }
    //
    //     ListNode(int val) {
    //         this(val, null);
    //     }
    //
    //     ListNode(int val, ListNode next) {
    //         this.val = val;
    //         this.next = next;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // public static class TreeNode {
    //
    //     int val;
    //     TreeNode left;
    //     TreeNode right;
    //
    //     TreeNode() { }
    //
    //     TreeNode(int val) {
    //         this(val, null, null);
    //     }
    //
    //     TreeNode(int val, TreeNode left, TreeNode right) {
    //         this.val = val;
    //         this.left = left;
    //         this.right = right;
    //     }
    // }
    //
    // @SuppressWarnings("unused")
    // static class Solution {
    //     public int reverse(int x) {
    //         int res = 0;
    //
    //         int upperLimit = Integer.MAX_VALUE / 10;
    //         int lowerLimit = Integer.MIN_VALUE / 10;
    //
    //         while (x != 0) {
    //             if (res < lowerLimit || res > upperLimit) return 0;
    //
    //             res = res * 10 + x % 10;
    //             x /= 10;
    //         }
    //
    //         return res;
    //     }
    // }
}

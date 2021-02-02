package com.cjg.androidlearn;

import java.util.HashMap;
import java.util.Map;

class LinkNode{
    int key;
    int value;
    LinkNode front;
    LinkNode next;

    public LinkNode(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
public class LRUCache {

    int capacity;
    Map<Integer,LinkNode> map = new HashMap<>();
    LinkNode head = new LinkNode(0,0);
    LinkNode tail = new LinkNode(0,0);
    public LRUCache(int capacity){
        this.capacity = capacity;
        head.next  = tail;
        tail.front = head;
    }


    public int get(int key) {
        if(map.get(key) != null){
            LinkNode linkNode = map.get(key);
            moveNodeToFront(linkNode);
            return linkNode.value;
        }else{
            return -1;
        }
    }

    public void put(int key, int value) {
        if(!map.containsKey(key)){
            if(map.size() == capacity) deleteLastNode();
            LinkNode temp = head.next;
            LinkNode newNode = new LinkNode(key,value);
            head.next = newNode;
            newNode.front = head;
            newNode.next = temp;
            temp.front = newNode;
            map.put(key,newNode);
        }else{
             LinkNode linkNode = map.get(key);
             linkNode.value = value;
             moveNodeToFront(linkNode);
        }
    }

    private void moveNodeToFront(LinkNode linkNode) {
        linkNode.front.next = linkNode.next;
        linkNode.next.front = linkNode.front;
        LinkNode temp = head.next;
        linkNode.front = head;
        head.next = linkNode;
        linkNode.next = temp;
        temp.front = linkNode;
    }

    private void deleteLastNode() {
        LinkNode linkNode = tail.front;
        linkNode.front.next = tail;
        tail.front = linkNode.front;
        map.remove(linkNode.key);
    }
}

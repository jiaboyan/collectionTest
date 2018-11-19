package com.jiaboyan.collection.map;

import java.util.TreeMap;

/**
 * Created by jiaboyan on 2017/9/6.
 */
public class TreeNode {
    //节点的值：
    private int data;
    //节点的颜色：
    private String color;
    //指向左孩子的指针：
    private TreeNode leftTreeNode;
    //指向右孩子的指针：
    private TreeNode rightTreeNode;
    //指向父节点的指针
    private TreeNode parentNode;

    public TreeNode(int data, String color, TreeNode leftTreeNode,
                    TreeNode rightTreeNode, TreeNode parentNode) {
        this.data = data;
        this.color = color;
        this.leftTreeNode = leftTreeNode;
        this.rightTreeNode = rightTreeNode;
        this.parentNode = parentNode;
    }
    public int getData() {
        return data;
    }
    public void setData(int data) {
        this.data = data;
    }
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public TreeNode getLeftTreeNode() {
        return leftTreeNode;
    }
    public void setLeftTreeNode(TreeNode leftTreeNode) {
        this.leftTreeNode = leftTreeNode;
    }
    public TreeNode getRightTreeNode() {
        return rightTreeNode;
    }
    public void setRightTreeNode(TreeNode rightTreeNode) {
        this.rightTreeNode = rightTreeNode;
    }
    public TreeNode getParentNode() {return parentNode;}
    public void setParentNode(TreeNode parentNode) {this.parentNode = parentNode;}
}

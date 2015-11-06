package com.umpay;
import java.util.LinkedList;

//测试类  
public class Client {
	public static void main(String[] args) {
		Client c = new Client();
		BinaryTree binaryTree = c.new BinaryTree();
		
		TreeNode[] nodes = new TreeNode[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = c.new TreeNode();
			nodes[i].setValue(i);
			binaryTree.insertNode(nodes[i]);
		}
		System.out.println("先序遍历");
		binaryTree.frontOrder(binaryTree.getTree().getNode());
		System.out.println("中序遍历");
		binaryTree.midOrder(binaryTree.getTree().getNode());
		System.out.println("后序遍历");
		binaryTree.lastOrder(binaryTree.getTree().getNode());

	}

	public class TreeNode {

		private int value;
		private TreeNode leftchild;
		private TreeNode rightchild;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public TreeNode getLeftchild() {
			return leftchild;
		}

		public void setLeftchild(TreeNode leftchild) {
			this.leftchild = leftchild;
		}

		public TreeNode getRightchild() {
			return rightchild;
		}

		public void setRightchild(TreeNode rightchild) {
			this.rightchild = rightchild;
		}
	}

	public class Tree {
		private TreeNode node;

		public TreeNode getNode() {
			return node;
		}

		public void setNode(TreeNode node) {
			this.node = node;
		}
	}

	public class Queue {

		private LinkedList<TreeNode> list;

		public LinkedList<TreeNode> getList() {
			return list;
		}

		public void setList(LinkedList<TreeNode> list) {
			this.list = list;
		}

		public Queue() {
			list = new LinkedList<TreeNode>();
		}

		public void enQueue(TreeNode node) {
			list.add(node);
		}

		public TreeNode outQueue() {
			return list.removeFirst();
		}

		public boolean isEmpty() {
			return list.isEmpty();
		}

	}

	// 二叉树类
	public class BinaryTree {
		private Tree tree;
		private Queue queue;

		public BinaryTree() {
			tree = new Tree();
		}

		// 插入结点
		public void insertNode(TreeNode node) {
			if (tree.getNode() == null) {
				tree.setNode(node);
				return;
			} else {
				queue = new Queue();
				queue.enQueue(tree.getNode());
				while (!queue.isEmpty()) {
					TreeNode temp = queue.outQueue();
					if (temp.getLeftchild() == null) {
						temp.setLeftchild(node);
						return;
					} else if (temp.getRightchild() == null) {
						temp.setRightchild(node);
						return;
					} else {
						queue.enQueue(temp.getLeftchild());
						queue.enQueue(temp.getRightchild());
					}
				}
			}
		}

		// 中序遍历
		public void midOrder(TreeNode node) {
			if (node != null) {
				this.midOrder(node.getLeftchild());
				System.out.println(node.getValue());
				this.midOrder(node.getRightchild());
			}
		}

		// 前序遍历

		public void frontOrder(TreeNode node) {
			if (node != null) {
				System.out.println(node.getValue());
				frontOrder(node.getLeftchild());
				frontOrder(node.getRightchild());
			}
		}

		// 后序遍历

		public void lastOrder(TreeNode node) {
			if (node != null) {
				this.lastOrder(node.getLeftchild());
				this.lastOrder(node.getRightchild());
				System.out.println(node.getValue());
			}
		}

		public Tree getTree() {
			return tree;
		}

	}

}
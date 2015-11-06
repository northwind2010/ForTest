package com.umpay.animal;

public class Animal {

	private int weight;
	private int height;
	
	public Animal(){
		
	}
	
	public Animal(int weight, int height) {
		// TODO Auto-generated constructor stub
		this.weight = weight;
		this.height = height;
	}
	
	public void eat(){
		System.out.println("animail is eating!");
	}
	public void sleep(){
		System.out.println("animail is sleeping!");
	}
	public void breath(){
		System.out.println("animail is breathing!");
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}

package com.umpay.animal;

public class Fish extends Animal {

	public Fish(){
		
	}
	public Fish(int weight, int height) {
		super(weight, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void breath(){
		System.out.println("fish is breathing!"+getHeight());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Fish fish = new Fish(1,1);
		fish.breath();
		System.out.println(Long.MAX_VALUE);
	}
}

package javabot.knowledge;

import javabot.JNIBWAPI;

public class Request {

	public JNIBWAPI bwapi;
	public int timer;
	public int minerals;
	public int gas;
	
	public Request() {
		timer = 0;
		minerals = 0;
		gas = 0;
	}
	
	public void update() {
		if (timer > 0) timer--;
		
		if (minerals > 0 && bwapi.getFrameCount() % 4 == 0) minerals--;
		if (gas > 0 && bwapi.getFrameCount() % 4 == 0) gas--;
		
		if (timer == 0) {
			minerals = 0;
			gas = 0;
		}
	}
}

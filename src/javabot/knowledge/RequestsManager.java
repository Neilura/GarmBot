package javabot.knowledge;

import javabot.JNIBWAPI;
import javabot.JavaBot;

public class RequestsManager {

	public Request hatchRequest;
	public JNIBWAPI bwapi;
	
	public RequestsManager() {
		bwapi = JavaBot.bwapi;
		hatchRequest = new Request();
		hatchRequest.bwapi = bwapi;
	}
	
	public void update() {
		hatchRequest.update();
	}
	
	public void debug() {
		
		bwapi.drawText(220, 110, "HatchRequest: " + hatchRequest.timer, true);
		bwapi.drawText(220, 120, "Minerals     : " + hatchRequest.minerals, true);
		bwapi.drawText(220, 130, "Gas         : " + hatchRequest.gas, true);
	}

	public void requestRessources(int minerals, int gas) {
		minerals++;
		gas++;
		if (JavaBot.requestsManager.hatchRequest.gas < gas
				|| JavaBot.requestsManager.hatchRequest.minerals < minerals
				|| JavaBot.requestsManager.hatchRequest.timer < 100) {
			if (JavaBot.requestsManager.hatchRequest.minerals < minerals)
				JavaBot.requestsManager.hatchRequest.minerals = minerals;
			if (JavaBot.requestsManager.hatchRequest.gas < gas)
				JavaBot.requestsManager.hatchRequest.gas = gas;
			JavaBot.requestsManager.hatchRequest.timer = 80;
		}
	}
}

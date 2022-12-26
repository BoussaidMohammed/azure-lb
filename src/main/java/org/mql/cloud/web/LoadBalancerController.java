package org.mql.cloud.web;

import org.mql.cloud.algo.LoadBlancer;
import org.mql.cloud.models.Task;
import org.mql.cloud.models.VM;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoadBalancerController {
	
	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	@PostMapping("/testAlgorithme")
	public String testAlgorithme(Model model,@RequestParam int totalMips, @RequestParam int vmNumber) {
		System.out.println(totalMips+ " :: "+vmNumber);
		LoadBlancer lb = new LoadBlancer(vmNumber,totalMips);
		lb.calculateExpectedTimeOfTasks();
		lb.allocateTasksToAppropriateVm();
		model.addAttribute("result", lb.getResult());
		Task.setTaskIdCounter(0);
		VM.setVmIdCounter(0);
		return "algorithmePage";
	}
}

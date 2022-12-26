package org.mql.cloud.algo;

import java.util.List;
import java.util.Vector;

import org.mql.cloud.models.Host;
import org.mql.cloud.models.Task;
import org.mql.cloud.models.VM;
import org.mql.cloud.models.CompelationTaskVm;

public class LoadBlancer {
	private Host host;
	private List<Task> cloudlets;
	private List<String> result;
	
	public LoadBlancer(int vmNumber, int totalMips) {
		result = new Vector<>();
		/*: For each task, assign random Length & DeadLine*/
		cloudlets = new Vector<Task>();
		for(int i = 0;i < 5;i++) {
			cloudlets.add(new Task());
			/*Task Constructor assign random Length & DeadLine for the task*/
		}
		
		host = new Host();
		host.setMips(totalMips);
		for(int i = 0;i < vmNumber; i++) {
			/*for each VM, assign equal portions of mips*/
			int mips = host.getInitialMips() / vmNumber;
			host.getVms().add(new VM(mips));
			host.setUsedMips(host.getUsedMips() + mips);
		}
	}
	
	/* For all tasks, calculate expected Completion Time
	 * calculate violation of each vm based on deadline and completion time
	 *  */
	public void calculateExpectedTimeOfTasks() {
		List<VM> vms = host.getVms();
		int m = cloudlets.size(); int n = vms.size();
		for(int i = 0; i < m;i++) {
			for(int j = 0;j < n;j++) {
				Task task = cloudlets.get(i);
				VM vm = vms.get(j);
				long compelationTime = task.getLenght() / vm.getMips();
				CompelationTaskVm ctv = new CompelationTaskVm(task.getTaskId(), vm.getVmId(), compelationTime);
				ctv.setViolation(task.getDeadline() - compelationTime);
				task.getCtvs().add(ctv);
			}
		}
	}
	
	
	public void allocateTasksToAppropriateVm() {
		int m = cloudlets.size();
		for(int i = 0; i < m; i++) {
			Task task = cloudlets.get(i);
			/*the appropriate vm with the minimum compelation time*/
			CompelationTaskVm appropriateVm = task.getAppropriateVm();
			/**/
			long violation = appropriateVm.getViolation();
			VM vm = host.getVmById(appropriateVm.getIdVm());
			int id = (int) (Math.random()*host.getVms().size() + 1);
			if(violation < 0) {	
				int neededMips = (int) ((int)task.getLenght() / task.getDeadline()) - vm.getMips();
				if(host.getMips() - host.getUsedMips() > neededMips) {
					vm.setMips(vm.getMips() + neededMips);
					result.add("\ntask with id " + task.getTaskId() + " allocated to VM with id "
							+ /* appropriateVm.getIdVm() */ + id 
				 + " with cpu configured");
				}else {
					result.add("\n migrate task with id "+ task.getTaskId() + " to other host");
				}
			}else {
				result.add("\ntask with id "+ task.getTaskId() + " allocated to VM with id "+ id /*appropriateVm.getIdVm()*/);
			}
		}
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public List<Task> getCloudlets() {
		return cloudlets;
	}

	public void setCloudlets(List<Task> cloudlets) {
		this.cloudlets = cloudlets;
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
	
	
}

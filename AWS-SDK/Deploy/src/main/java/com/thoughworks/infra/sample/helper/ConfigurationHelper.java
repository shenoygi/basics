package com.thoughworks.infra.sample.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to help configuring the VM
 */
public class ConfigurationHelper {

	private String user;
	private String host;
	private SshHelper sshHelper;
	private String password;

	public ConfigurationHelper(String host, String user, String password) {
		this.host = host;
		this.user = user;
		sshHelper = new SshHelper(this.user, this.password, this.host, "");
	}

	/**
	 * Run a list of commands through SSH
	 * @param commandFile - list of commands
	 * @return boolean indicatin successful execution
	 * @throws Exception
	 */
	public boolean runSshCommands(String commandFile) throws Exception {
		List<String> commands = getCommandPerLine(commandFile);
		if(sshHelper.connect() == null) 
			return false;
		for(String command : commands){
			String out = sshHelper.sendCommand(command);
			if(out.contains("error") || out.contains("ERROR")){
				return false;
			}
		}
		return true;
	}

	private List<String> getCommandPerLine(String commandFile) throws Exception {
		List<String> commands = new ArrayList<String>();
		BufferedReader fileReader = new BufferedReader(new FileReader(commandFile));
		while (fileReader.ready()) {
			String command = fileReader.readLine();
			if(command.startsWith("%") || command.isEmpty()){
				continue;
			}
			commands.add(command);
		}
		fileReader.close();
		return commands;
	}

}

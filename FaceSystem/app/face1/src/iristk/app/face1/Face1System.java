/*******************************************************************************
 * Copyright (c) 2014 Gabriel Skantze.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Gabriel Skantze - initial API and implementation
 ******************************************************************************/
package iristk.app.face1;

import iristk.situated.SituatedDialogSystem;
import iristk.situated.SystemAgentFlow;
import iristk.speech.SpeechGrammarContext;
import iristk.speech.Voice.Gender;
import iristk.speech.windows.WindowsRecognizerFactory;
import iristk.speech.windows.WindowsSynthesizer;
import iristk.system.BrokerClient;
import iristk.system.Event;
import iristk.system.EventListener;
import iristk.system.IrisUtils;
import iristk.util.Language;
import iristk.cfg.SRGSGrammar;
import iristk.flow.FlowModule;

public class Face1System {
		
	public Face1System() throws Exception {
		SituatedDialogSystem system = new SituatedDialogSystem(this.getClass());
		SystemAgentFlow systemAgentFlow = system.addSystemAgent();
	
		system.setLanguage(Language.ENGLISH_US);
	
		//system.setupLogging(new File("c:/iristk_logging"), true);
		
		system.setupGUI();
		
		//system.setupKinect();
		
		//system.setupMonoMicrophone(new WindowsRecognizerFactory());
		system.setupStereoMicrophones(new WindowsRecognizerFactory());
		//system.setupKinectMicrophone(new KinectRecognizerFactory());
				
		system.connectToBroker("elfticket", "192.168.130.1");
		system.setupFace(new WindowsSynthesizer(), Gender.FEMALE);
		
		system.addModule(new FlowModule(new Face1Flow(systemAgentFlow)));
		system.loadContext("default", new SpeechGrammarContext(new SRGSGrammar(system.getPackageFile("Face1Grammar.xml"))));
		
		system.loadPositions(system.getPackageFile("situation.properties"));		
		system.sendStartSignal();
	}

	public static void main(String[] args) throws Exception {
		new Face1System();
		/*
		BrokerClient brokerClient = new BrokerClient("elfticket", "elfsystem", "192.168.130.1", 1932, new EventListener() {
			
			@Override
			public void onEvent(Event event) {
				// TODO Auto-generated method stub
				System.out.println(event.toString());
				
				
			}
		});
		brokerClient.connect();
		
		Event e = new Event();
		e.setId("asd");
		e.setName("name");
		e.put("event_name", "action.speech");
		brokerClient.send(e);
		*/
	}

}

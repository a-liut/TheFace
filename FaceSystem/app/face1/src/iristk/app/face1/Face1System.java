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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import iristk.cfg.SRGSGrammar;
import iristk.flow.FlowModule;

public class Face1System {

	public Face1System() throws Exception {
		SituatedDialogSystem system = new SituatedDialogSystem(this.getClass());
		SystemAgentFlow systemAgentFlow = system.addSystemAgent();

		system.setLanguage(Language.ENGLISH_US);

		// system.setupLogging(new File("c:/iristk_logging"), true);

		system.setupGUI();

		// system.setupKinect();

		// system.setupMonoMicrophone(new WindowsRecognizerFactory());
		system.setupStereoMicrophones(new WindowsRecognizerFactory());
		// system.setupKinectMicrophone(new KinectRecognizerFactory());

		system.connectToBroker("elfticket", "192.168.130.1");
		system.setupFace(new WindowsSynthesizer(), Gender.FEMALE);

		system.addModule(new FlowModule(new Face1Flow(systemAgentFlow)));
		system.loadContext("default",
				new SpeechGrammarContext(new SRGSGrammar(system.getPackageFile("Face1Grammar.xml"))));

		system.loadPositions(system.getPackageFile("situation.properties"));
		system.sendStartSignal();
	}

	private static void send(Event e) {
		try {

			BrokerClient brokerClient = new BrokerClient("elfticket", "elfsystem", "192.168.130.1", 1932,
					new EventListener() {

						@Override
						public void onEvent(Event event) {
							// TODO Auto-generated method stub
							System.out.println(event.toString());

						}
					});
			brokerClient.connect();

			System.out.println("Sending event");
			System.out.println(e.get("event_name"));

			brokerClient.send(e);

			brokerClient.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static Event buildSpeakEvent(String name) {
		Event e = new Event();
		e.setId("asd");
		e.setName("speech_event");
		e.put("event_name", "action.speech");
		e.put("text", name);
		e.put("display", name);
		return e;
	}
	
	private static Event buildGestureEvent(String name) {
		Event e0 = new Event();
		e0.setId("asd");
		e0.setName("geture_event");
		e0.put("event_name", "action.gesture");
		e0.put("name", "smile");
		return e0;
	}

	public static void main(String[] args) throws Exception {
		new Face1System();

		int cmd = 1;
		Scanner in = new Scanner(System.in);
		while (cmd != 0) {
			cmd = in.nextInt();
			switch (cmd) {
			case 1:
				send(buildSpeakEvent("Hi everybody! I'm Elf!"));
				break;
			case 2:
				send(buildSpeakEvent("I'm supposed to do lot of things!"));
				break;
			case 3:
				send(buildGestureEvent("smile"));
				break;
			case 4:
				send(buildGestureEvent("neutral"));
				break;
			case 5:
				String s = in.nextLine();
				send(buildSpeakEvent(s));
				break;
			case 0:
				break;
			}
		}
		in.close();
		
		System.out.println("Bye!");
	}

}

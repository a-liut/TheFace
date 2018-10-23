package iristk.app.face1;

import java.util.List;
import java.io.File;
import iristk.xml.XmlMarshaller.XMLLocation;
import iristk.system.Event;
import iristk.flow.*;
import iristk.util.Record;
import static iristk.util.Converters.*;
import static iristk.flow.State.*;

public class Face1Flow extends iristk.flow.Flow {

	private iristk.situated.SystemAgentFlow agent;
	private iristk.situated.SystemAgent system;
	private Integer number;

	private void initVariables() {
		system = (iristk.situated.SystemAgent) agent.getSystemAgent();
	}

	public iristk.situated.SystemAgent getSystem() {
		return this.system;
	}

	public void setSystem(iristk.situated.SystemAgent value) {
		this.system = value;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer value) {
		this.number = value;
	}

	public iristk.situated.SystemAgentFlow getAgent() {
		return this.agent;
	}

	@Override
	public Object getVariable(String name) {
		if (name.equals("system")) return this.system;
		if (name.equals("number")) return this.number;
		if (name.equals("agent")) return this.agent;
		return null;
	}


	public Face1Flow(iristk.situated.SystemAgentFlow agent) {
		this.agent = agent;
		initVariables();
	}

	@Override
	public State getInitialState() {return new Idle();}


	public class Idle extends Dialog implements Initial {

		final State currentState = this;


		@Override
		public void setFlowThread(FlowRunner.FlowThread flowThread) {
			super.setFlowThread(flowThread);
		}

		@Override
		public void onentry() throws Exception {
			int eventResult;
			Event event = new Event("state.enter");
			// Line: 14
			try {
				EXECUTION: {
					int count = getCount(1212899836) + 1;
					incrCount(1212899836);
					// Line: 15
					if (system.hasUsers()) {
						iristk.situated.SystemAgentFlow.attendRandom state0 = agent.new attendRandom();
						if (!flowThread.callState(state0, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 15, 33)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 17
						Greeting state1 = new Greeting();
						flowThread.gotoState(state1, currentState, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 17, 29)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
						// Line: 18
					} else {
						iristk.situated.SystemAgentFlow.attendNobody state2 = agent.new attendNobody();
						if (!flowThread.callState(state2, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 15, 33)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
					}
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 14, 12));
			}
		}

		@Override
		public int onFlowEvent(Event event) throws Exception {
			int eventResult;
			int count;
			// Line: 22
			try {
				count = getCount(1811075214) + 1;
				if (event.triggers("sense.user.enter")) {
					incrCount(1811075214);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.attend state3 = agent.new attend();
						state3.setTarget(event.get("user"));
						if (!flowThread.callState(state3, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 22, 36)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 24
						Greeting state4 = new Greeting();
						flowThread.gotoState(state4, currentState, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 24, 28)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 22, 36));
			}
			eventResult = super.onFlowEvent(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			eventResult = callerHandlers(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			return EVENT_IGNORED;
		}

	}


	private class Greeting extends Dialog {

		final State currentState = this;


		@Override
		public void setFlowThread(FlowRunner.FlowThread flowThread) {
			super.setFlowThread(flowThread);
		}

		@Override
		public void onentry() throws Exception {
			int eventResult;
			Event event = new Event("state.enter");
			// Line: 29
			try {
				EXECUTION: {
					int count = getCount(1940447180) + 1;
					incrCount(1940447180);
					iristk.situated.SystemAgentFlow.say state5 = agent.new say();
					StringCreator string6 = new StringCreator();
					string6.append("Hi there! I'm hungry");
					state5.setText(string6.toString());
					if (!flowThread.callState(state5, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 29, 12)))) {
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 29, 12));
			}
		}

		@Override
		public int onFlowEvent(Event event) throws Exception {
			int eventResult;
			int count;
			eventResult = super.onFlowEvent(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			eventResult = callerHandlers(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			return EVENT_IGNORED;
		}

	}


	private class Dialog extends State {

		final State currentState = this;


		@Override
		public void setFlowThread(FlowRunner.FlowThread flowThread) {
			super.setFlowThread(flowThread);
		}

		@Override
		public void onentry() throws Exception {
			int eventResult;
			Event event = new Event("state.enter");
		}

		@Override
		public int onFlowEvent(Event event) throws Exception {
			int eventResult;
			int count;
			// Line: 35
			try {
				count = getCount(2121744517) + 1;
				if (event.triggers("sense.user.speech.start")) {
					if (system.isAttending(event) && eq(event.get("speakers"), 1)) {
						incrCount(2121744517);
						eventResult = EVENT_CONSUMED;
						EXECUTION: {
							iristk.situated.SystemAgentFlow.gesture state7 = agent.new gesture();
							state7.setName("smile");
							if (!flowThread.callState(state7, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 35, 102)))) {
								eventResult = EVENT_ABORTED;
								break EXECUTION;
							}
						}
						if (eventResult != EVENT_IGNORED) return eventResult;
					}
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 35, 102));
			}
			// Line: 38
			try {
				count = getCount(1066376662) + 1;
				if (event.triggers("sense.user.speak")) {
					incrCount(1066376662);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.say state8 = agent.new say();
						StringCreator string9 = new StringCreator();
						string9.append("Sorry, I didn't get that.");
						state8.setText(string9.toString());
						if (!flowThread.callState(state8, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 38, 36)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 40
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 40, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 38, 36));
			}
			// Line: 42
			try {
				count = getCount(476402209) + 1;
				if (event.triggers("sense.user.speak.side")) {
					incrCount(476402209);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.attendOther state10 = agent.new attendOther();
						state10.setMode("eyes");
						if (!flowThread.callState(state10, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 42, 41)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						iristk.situated.SystemAgentFlow.say state11 = agent.new say();
						StringCreator string12 = new StringCreator();
						string12.append("I didn't ask you.");
						state11.setText(string12.toString());
						if (!flowThread.callState(state11, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 42, 41)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						iristk.situated.SystemAgentFlow.attendOther state13 = agent.new attendOther();
						state13.setMode("eyes");
						if (!flowThread.callState(state13, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 42, 41)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 46
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 46, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 42, 41));
			}
			// Line: 48
			try {
				count = getCount(460332449) + 1;
				if (event.triggers("sense.user.speak.multi")) {
					incrCount(460332449);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.say state14 = agent.new say();
						StringCreator string15 = new StringCreator();
						string15.append("Don't speak at the same time.");
						state14.setText(string15.toString());
						if (!flowThread.callState(state14, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 48, 42)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 50
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 50, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 48, 42));
			}
			// Line: 52
			try {
				count = getCount(1143839598) + 1;
				if (event.triggers("sense.user.silence")) {
					incrCount(1143839598);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.say state16 = agent.new say();
						StringCreator string17 = new StringCreator();
						string17.append("Sorry, I didn't hear anything.");
						state16.setText(string17.toString());
						if (!flowThread.callState(state16, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 52, 38)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 54
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 54, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 52, 38));
			}
			// Line: 56
			try {
				count = getCount(358699161) + 1;
				if (event.triggers("sense.user.leave")) {
					if (system.isAttending(event)) {
						incrCount(358699161);
						eventResult = EVENT_CONSUMED;
						EXECUTION: {
							// Line: 57
							if (system.hasUsers()) {
								iristk.situated.SystemAgentFlow.attendRandom state18 = agent.new attendRandom();
								if (!flowThread.callState(state18, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 57, 33)))) {
									eventResult = EVENT_ABORTED;
									break EXECUTION;
								}
								// Line: 59
							} else {
								// Line: 60
								Idle state19 = new Idle();
								flowThread.gotoState(state19, currentState, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 60, 25)));
								eventResult = EVENT_ABORTED;
								break EXECUTION;
							}
						}
						if (eventResult != EVENT_IGNORED) return eventResult;
					}
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 56, 69));
			}
			// Line: 63
			try {
				count = getCount(2143192188) + 1;
				if (event.triggers("repeat")) {
					incrCount(2143192188);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						// Line: 64
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 64, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 63, 26));
			}
			// Line: 66
			try {
				count = getCount(204349222) + 1;
				if (event.triggers("action.speech")) {
					incrCount(204349222);
					eventResult = EVENT_CONSUMED;
					EXECUTION: {
						iristk.situated.SystemAgentFlow.say state20 = agent.new say();
						StringCreator string21 = new StringCreator();
						string21.append("RECEIVED");
						state20.setText(string21.toString());
						if (!flowThread.callState(state20, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 66, 33)))) {
							eventResult = EVENT_ABORTED;
							break EXECUTION;
						}
						// Line: 68
						flowThread.reentryState(this, new FlowEventInfo(currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 68, 14)));
						eventResult = EVENT_ABORTED;
						break EXECUTION;
					}
					if (eventResult != EVENT_IGNORED) return eventResult;
				}
			} catch (Exception e) {
				throw new FlowException(e, currentState, event, new XMLLocation(new File("E:\\Andrea\\Desktop\\SmartApplication\\TheFace\\FaceSystem\\app\\face1\\src\\iristk\\app\\face1\\Face1Flow.xml"), 66, 33));
			}
			eventResult = super.onFlowEvent(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			eventResult = callerHandlers(event);
			if (eventResult != EVENT_IGNORED) return eventResult;
			return EVENT_IGNORED;
		}

	}


}

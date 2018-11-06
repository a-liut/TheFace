import * as EventReader from '../reader/EventReader';
import * as Message from './Message';

const DEBUG = false;

const KB_URL: string = "ws://localhost:5666"
const PROTOCOL_WS: string = 'ws';

export class KBEventReader extends EventReader.BaseEventReader {
	private sessionId: string = null;

	private protocol: string = PROTOCOL_WS
	private socket: WebSocket;

	constructor() {
		super();
	}

	public start(): void {
		try {
			this.socket = new WebSocket(KB_URL, PROTOCOL_WS);

			this.socket.onclose = () => {
				console.log("Socket closed...");
				this.socket = null;
			}

			var first = true;
			this.socket.onmessage = (message) => {
				console.log(message);

				if(first) {
					// First message is the response of registration.
					first = false;

					this.sessionId = message.data;

					console.log("SessionID set", this.sessionId);

					// Now subscribe to the events
					this.subscribeKB({
						relation: '$X'
					});

					if(DEBUG) {
						// DEBUG: Add some facts..
						let builder = new Message.MessageBuilder()
											.setMethod(KB_OP.ADD_FACT)
											.addParam('idSource', this.sessionId)
											.addParam('infoSum', 'rdf')
											.addParam('TTL', 3)
											.addParam('reliability', 90)
											.addParam('revisioning', false);

						let m1 = builder.addParam('jsonFact', { relation: 'teaches', subject: 'Gervasi', object: 'SmartApplication' }).build();
						let m2 = builder.addParam('jsonFact', { relation: 'teaches', subject: 'Gervasi', object: 'SAM' }).build();
						let m3 = builder.addParam('jsonFact', { relation: 'teaches', subject: 'Bruni', object: 'PSC' }).build();

						this.addFactKB(m1);
						this.addFactKB(m2);
						this.addFactKB(m3);
					}
				} else {
					if(this.sessionId) {
						console.log("message received correctly!!");
					} else {
						console.warn("No SessionID set");
					}
				}
			}

			this.socket.onopen = () => {
				console.log("Socket opened...");

				// First register the client to the KB
				this.registerKB();

			}
		} catch(ex) {
			this.socket.close();
			this.socket = null;
		}
	}

	private registerKB(): void {
		let message = new Message.MessageBuilder()
							.setMethod(KB_OP.REGISTER)
							.build();
		this.socket.send(JSON.stringify(message));
	}

	private subscribeKB(request: object): void {
		let message = new Message.MessageBuilder()
							.setMethod(KB_OP.SUBSCRIBE)
							.addParam('idSource', this.sessionId)
							.addParam('jsonFact', request)
							.build()
		this.socket.send(JSON.stringify(message));
	}

	private addFactKB(message: Message.Message): void {
		this.socket.send(JSON.stringify(message));
	}
}

export enum KB_OP {
	REGISTER = "register",
	ADD_FACT = "addFact",
	QUERY_BIND = "queryBind",
	SUBSCRIBE = "subscribe"
}
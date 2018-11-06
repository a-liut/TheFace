import * as KBEventReader from './KBEventReader';

export class MessageBuilder {
	private method: string;
	private params: object = {};

	public setMethod(op: KBEventReader.KB_OP): MessageBuilder {
		this.method = op;
		return this;
	}

	public addParam(key: KBEventReader.PARAMS, value: any): MessageBuilder {
		this.params[key] = value;
		return this;
	}

	public build(): Message {
		return new Message(this.method, this.params);
	}
}

export class Message {
	constructor(private method: string, private params: object) {}
}
import * as emotion from './Emotion';

export abstract class ElfUI {
	
	constructor() {
		// code...
	}

	abstract onEmotionChanged(e: emotion.Emotion): void;
	abstract onContentChanged(e: ElfUIEvent): void;
}

export class ElfUIEvent {
	private data = {};
	
	constructor() {
		
	}

	putString(key: string, s: string) {
		this.data[key] = s;
	}

	putArray(key: string, a: Array<string>) {
		this.data[key] = a;
	}

	readonly KEY_EMOTION: string = "key_emotion"
	readonly KEY_CONTENT: string = "key_event"
}

export class ElfUIEvent {
	private data = {};
	
	private constructor() {
		
	}

	putString(key: string, s: string) {
		this.data[key] = s;
	}

	putArray(key: string, a: Array<string>) {
		this.data[key] = a;
	}

	public toString() {
        return 'ElfUIEvent (' +
        Object.keys(this.data)
        + ')';
    }

	readonly KEY_EMOTION: string = "key_emotion"
	readonly KEY_CONTENT: string = "key_event"
}
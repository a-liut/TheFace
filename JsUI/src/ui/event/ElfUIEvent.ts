export class ElfUIEvent {
	private data = {};
	
	constructor() {
		
	}

	putString(key: string, s: string): ElfUIEvent {
		this.data[key] = s;
		return this;
	}

	putArray(key: string, a: Array<string>): ElfUIEvent {
		this.data[key] = a;
		return this;
	}

	putAny(key: string, o: any): ElfUIEvent {
		this.data[key] = o;
		return this;
	}

	getString(key: string): string {
		return this.data[key];
	}

	getArray(key: string): Array<any> {
		return this.data[key];
	}

	getAny(key: string): any {
		return this.data[key];
	}

	public toString() {
        return 'ElfUIEvent (' +
        Object.keys(this.data)
        + ')';
    }
}


export const KEY_EMOTION: string = "key_emotion"
export const KEY_CONTENT: string = "key_event"
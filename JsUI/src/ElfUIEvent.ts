
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

	putAny(key: string, o: any) {
		this.data[key] = o;
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
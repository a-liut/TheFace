import * as ElfUIEvent from '../ui/event/ElfUIEvent'

export interface Content {
	render(): string;
}

export class ContentSet {
	private data : Array<Content> = [];

	public add(c: Content) {
		this.data.push(c);
	}

	public get(): Content {
		if(this.data.length > 0) {
			return this.data.pop();
		}
		return null;
	}
}

export interface ContentFactory {
	create(object: ElfUIEvent.ElfUIEvent): Array<Content>;
}
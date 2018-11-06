import * as Emotion from '../emotion/Emotion';
import * as ElfUIEvent from './event/ElfUIEvent';
import * as EventReader from '../reader/EventReader';

export abstract class ElfUI implements EventReader.IEventListener {

	private eventReader: EventReader.BaseEventReader;
	
	constructor(protected root: HTMLElement) {
		this.eventReader = new EventReader.VoidReader();
	}

	public setEventReader(reader: EventReader.BaseEventReader) {
		this.eventReader = reader;
		this.eventReader.registerEventListener(this)
	}

	onEvent(e:ElfUIEvent.ElfUIEvent) {
		let emotion = e.getAny(ElfUIEvent.KEY_EMOTION) as Emotion.Emotion;
		if(emotion) {
			this.onEmotionChanged(e.getAny(ElfUIEvent.KEY_EMOTION) as Emotion.Emotion);
		}
		let content = e.getAny(ElfUIEvent.KEY_CONTENT);
		if(content) {
			this.onContentChanged(content);
		}
	}

	abstract onEmotionChanged(e: Emotion.Emotion): void;
	abstract onContentChanged(e: ElfUIEvent.ElfUIEvent): void;
	abstract getTemplate(): string;
}

export class Builder {
	private reader : EventReader.BaseEventReader = null;

	constructor(private factory: ElfUIFactory) {}

	public setEventReader(reader: EventReader.BaseEventReader) {
		this.reader = reader;
	}

	public build() {
		let ui = this.factory.create();

		ui.setEventReader(this.reader);
		
		return ui;
	}

}

export interface ElfUIFactory {
	create(): ElfUI;
}
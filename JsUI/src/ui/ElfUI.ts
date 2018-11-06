import * as Emotion from '../emotion/Emotion';
import * as ElfUIEvent from './event/ElfUIEvent';
import * as EventReader from '../reader/EventReader';
import * as Content from '../content/Content';

export abstract class ElfUI implements EventReader.IEventListener {
	private eventReader: EventReader.BaseEventReader = null;
	
	constructor(private root: HTMLElement) {
		this.onCreateView(root);
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
		let contents = this.getContentFactory().create(e);
		if(contents) {
			this.onContentChanged(contents);
		}
	}

	protected getRootElement(): HTMLElement {
		return this.root;
	}

	abstract onCreateView(root: HTMLElement): void;

	abstract onEmotionChanged(e: Emotion.Emotion): void;
	abstract onContentChanged(contents: Array<Content.Content>): void;
	abstract getTemplate(): string;
	abstract getContentFactory(): Content.ContentFactory;
}

export class Builder {
	private reader : EventReader.BaseEventReader = null;

	constructor(private factory: ElfUIFactory) {}

	public setEventReader(reader: EventReader.BaseEventReader): Builder {
		this.reader = reader;
		return this;
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
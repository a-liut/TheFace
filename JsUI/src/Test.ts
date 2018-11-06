
import * as ElfUI from './ui/ElfUI';
import * as EventReader from './reader/EventReader'
import * as Emotion from './emotion/Emotion';
import * as ElfUIEvent from './ui/event/ElfUIEvent';
import * as Content from './content/Content'

export class TestUI extends ElfUI.ElfUI {

	constructor(rootElement: HTMLElement) {
		super(rootElement);
	}

	public onCreateView(root: HTMLElement) {
		this.getRootElement().innerHTML = this.getTemplate();
	}

	public onEmotionChanged(e: Emotion.Emotion): void {
		console.log("onEmotionChanged", e);

		this.getRootElement().style.backgroundColor = e.getColor();
	}
	
	public onContentChanged(e: Array<Content.Content>): void {
		console.log("onContentChanged", e);
	}

	public getTemplate(): string {
		return "<div>TEST</div>";
	}

	public getContentFactory(): Content.ContentFactory {
		return null;
	}
}

export class TestUIFactory implements ElfUI.ElfUIFactory {

	constructor(private root: HTMLElement) {}

	create(): ElfUI.ElfUI {
		return new TestUI(this.root);
	}
}

export class TestEventReader extends EventReader.BaseEventReader {
	count = 0;

	constructor(private delay: number = 0) {
		super();
	}

	public start() {
		let handler = () => {
			let e = new ElfUIEvent.ElfUIEvent();

			e.putAny(ElfUIEvent.KEY_EMOTION, this.count % 2 == 0 ? new Emotion.Emotion(1, 1) : new Emotion.Emotion(1, 0));
			this.count = (this.count + 1) % 2;

			e.putAny(ElfUIEvent.KEY_CONTENT, {
				"text": "text content 1: " + Math.random(),
				"image": "text content 2: " + Math.random() 
			})

			this.listener.onEvent(e);

			setTimeout(handler, this.delay);
		};

		setTimeout(handler, this.delay);
	}
}

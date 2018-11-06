
import * as ElfUI from './ui/ElfUI';
import * as EventReader from './reader/EventReader'
import * as Emotion from './emotion/Emotion';
import * as ElfUIEvent from './ui/event/ElfUIEvent';

export class TestUI extends ElfUI.ElfUI {

	constructor(rootElement: HTMLElement) {
		super(rootElement);

		rootElement.innerHTML = this.getTemplate();
	}

	public onEmotionChanged(e: Emotion.Emotion): void {
		console.log("onEmotionChanged", e);

		this.root.style.backgroundColor = e.getColor();
	}
	public onContentChanged(e: ElfUIEvent.ElfUIEvent): void {
		console.log("onContentChanged", e);
	}
	public getTemplate(): string {
		return "<div>TEST</div>";
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

	start() {
		let handler = () => {
			let e = new ElfUIEvent.ElfUIEvent();
			e.putAny(ElfUIEvent.KEY_EMOTION, this.count % 2 == 0 ? new Emotion.Emotion(1, 1) : new Emotion.Emotion(1, 0));
			this.count = (this.count + 1) % 2;

			this.listener.onEvent(e);

			setTimeout(handler, this.delay);
		};

		setTimeout(handler, this.delay);
	}
}

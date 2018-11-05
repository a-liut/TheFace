import * as ElfUI from './ElfUI';
import * as EventReader from './EventReader'
import * as Emotion from './Emotion';
import * as ElfUIEvent from './ElfUIEvent';

let elem = document.getElementById("content");

class TestUI extends ElfUI.ElfUI {

	private test1Element: Element

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

class TestUIFactory implements ElfUI.ElfUIFactory {

	constructor(private root: HTMLElement) {}

	create(): ElfUI.ElfUI {
		return new TestUI(this.root);
	}
}

class TestEventReader extends EventReader.BaseEventReader {
	count = 0;

	start() {
		let handler = () => {
			let e = new ElfUIEvent.ElfUIEvent();
			e.putAny(ElfUIEvent.KEY_EMOTION, this.count % 2 == 0 ? new Emotion.Emotion(1, 1) : new Emotion.Emotion(1, 0));
			this.count = (this.count + 1) % 2;

			this.listener.onEvent(e);

			setTimeout(handler, 5000);
		};

		setTimeout(handler, 5000);
	}
}


let factory = new TestUIFactory(elem);
let reader = new TestEventReader();

let uiBuilder = new ElfUI.Builder(factory);
uiBuilder.setEventReader(reader);

let ui = uiBuilder.build();

reader.start();


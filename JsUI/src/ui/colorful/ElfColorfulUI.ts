import * as ElfUI from '../ElfUI';
import * as Emotion from '../../emotion/Emotion';
import * as ElfUIEvent from '../event/ElfUIEvent';

export class ElfColorfulUI extends ElfUI.ElfUI {

	private upperPanel: Element;
	private lowerPanel: Element;
	private textPanel: Element;
	private resourcePanel: Element;
	private facePanel: Element;

	constructor(rootElement: HTMLElement) {
		super(rootElement);

		rootElement.innerHTML = this.getTemplate();

		this.upperPanel = rootElement.getElementsByClassName("upper-panel")[0];
		this.lowerPanel = rootElement.getElementsByClassName("lower-panel")[0];
		this.textPanel = rootElement.getElementsByClassName("text-panel")[0];
		this.resourcePanel = rootElement.getElementsByClassName("resource-panel")[0];
		this.facePanel = rootElement.getElementsByClassName("face-panel")[0];
	}

	public onEmotionChanged(e: Emotion.Emotion): void {
		console.log("onEmotionChanged", e);

		this.root.style.backgroundColor = e.getColor();
	}
	public onContentChanged(e: ElfUIEvent.ElfUIEvent): void {
		console.log("onContentChanged", e);
	}

	public getTemplate(): string {
		return '<div class="ui-content">\
			<div class="upper-panel">\
				<div class="resource-panel"></div>\
				<div class="face-panel"></div>\
			</div>\
			<div class="lower-panel">\
				<div class="text-panel"></div>\
			</div>\
		</div>';
	}
}

export class ElfColorfulUIFactory implements ElfUI.ElfUIFactory {

	constructor(private root: HTMLElement) {}

	create(): ElfUI.ElfUI {
		return new ElfColorfulUI(this.root);
	}
}
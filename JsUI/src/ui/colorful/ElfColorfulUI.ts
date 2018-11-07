import * as ElfUI from '../ElfUI';
import * as Emotion from '../../emotion/Emotion';
import * as ElfUIEvent from '../event/ElfUIEvent';
import * as Content from '../../content/Content';
import * as Face from './face/Face';

class FooContent implements Content.Content {
	constructor(private data: any) {}

	public getData() {
		return this.data;
	}

	render(): string {
		return JSON.stringify(this.data);
	}
}

class TextContent implements Content.Content {
	constructor(private text: string) {}

	render(): string {
		return "<div>" + this.text + "</div>";
	}
}

class ColorfulUIContentFactory implements Content.ContentFactory {
	create(event: ElfUIEvent.ElfUIEvent): Array<Content.Content> {
		let data = event.getAny(ElfUIEvent.KEY_CONTENT);

		let contents = [];
		for (var key in data) {
			switch (key) {
				case "text":
					contents.push(new TextContent(data[key]));
					break;
				default:		
					let d = {};
					d[key] = data[key];
					contents.push(new FooContent(d));		
					break;
			}
		}

		return contents;
	}
}

export class ElfColorfulUI extends ElfUI.ElfUI {

	private upperPanel: Element;
	private lowerPanel: Element;
	private textPanel: Element;
	private resourcePanel: Element;
	private facePanel: Element;

	private face: Face.IFace;

	constructor(rootElement: HTMLElement, private contentFactory: Content.ContentFactory = new ColorfulUIContentFactory()) {
		super(rootElement);
	}

	onCreateView(root: HTMLElement): void {
		root.innerHTML = this.getTemplate();

		this.upperPanel = root.getElementsByClassName("upper-panel")[0];
		this.lowerPanel = root.getElementsByClassName("lower-panel")[0];

		this.textPanel = root.getElementsByClassName("text-panel")[0];
		this.resourcePanel = root.getElementsByClassName("resource-panel")[0];
		this.facePanel = root.getElementsByClassName("face-panel")[0];

		this.face = new Face.TestFace(this.facePanel as HTMLElement);
	}

	public onEmotionChanged(e: Emotion.Emotion): void {
		console.log("onEmotionChanged", e);

		this.getRootElement().style.backgroundColor = e.getColor();

		this.face.setEmotion(e);
	}
	public onContentChanged(contents: Array<Content.Content>): void {
		console.log("onContentChanged", contents);

		let document = this.getRootElement().ownerDocument

		this.resetView();

		contents.map(content => {
			let elem = document.createElement('div');
			elem.innerHTML = content.render();
			return {content: content, element: elem.firstChild};
		}).map(pair => {
			if(pair.content instanceof TextContent) {
				this.textPanel.appendChild(pair.element);
			} else if(pair.content instanceof FooContent) {
				this.resourcePanel.appendChild(pair.element);
			} else {
				console.error("Pair discarded: no matching type.", pair);
			}
		})
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

	public getContentFactory(): Content.ContentFactory {
		return this.contentFactory;
	}

	private resetView() {
		this.clearPanel(this.resourcePanel);
		this.clearPanel(this.textPanel);
	}

	private clearPanel(panel: Element) {
		while(panel.firstChild) {
			panel.removeChild(panel.firstChild);
		}
	}
}

export class ElfColorfulUIFactory implements ElfUI.ElfUIFactory {

	constructor(private root: HTMLElement) {}

	create(): ElfUI.ElfUI {
		return new ElfColorfulUI(this.root);
	}
}
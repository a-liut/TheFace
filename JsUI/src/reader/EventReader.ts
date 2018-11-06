import * as ElfUIEvents from '../ui/event/ElfUIEvent';

export interface IEventListener {
	onEvent(event: ElfUIEvents.ElfUIEvent): void;
}

export abstract class BaseEventReader {
	protected listener: IEventListener;

	public registerEventListener(listener: IEventListener): void {
		this.listener = listener;
	};
}

export class VoidReader extends BaseEventReader {}

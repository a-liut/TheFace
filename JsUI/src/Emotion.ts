
export interface IEmotion {
	color: string;
}

export class Emotion implements IEmotion {
	color: string;

	constructor(private x: number, private y: number) {

	}
}
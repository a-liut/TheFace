
export interface IEmotion {
	getColor(): string
}

export class Emotion implements IEmotion {
	color: string;

	constructor(private x: number, private y: number) {
		this.color = this.getColorFromCoord(x, y);
	}

	private getColorFromCoord(x: number, y: number): string {

		let angle = Math.atan2(x, y)*180.0/Math.PI;

		 if(angle == 0)
		 	return '#FF0000' //red angree
		 else if(angle <= 45)
		 	return'#FF0066' //pink upset
		 else if(angle <= 90)
		 	return '#FFFF66' //yellow suprised
		 else if(angle <= 135)
		 	return '#CCFF66' //light_green interested
		 else if(angle <= 180)
		 	return '#00FF00' //green happy
		 else if(angle <= 225)
		 	return '#66CCFF' //light_blue sad
		 else if(angle <= 270)
		 	return '#6600FF' //... disappointed
		 else if(angle <= 315)
		 	return '#CC33FF' //jasmin disgust
		 else if(angle < 360)
		 	return '#FF3366' //jasmin bored
	}

	public getColor(): string {
		return this.getColorFromCoord(this.x, this.y);
	}


}

/*
 class NameEmotion implements IEmotion {

 	constructor(private name: string) {

 	}
 	public getColor(): string {
 		if(this.name === "aytan") {
 			return "#FFF"
 		}
 		return "#000"
 	}
 }

 */
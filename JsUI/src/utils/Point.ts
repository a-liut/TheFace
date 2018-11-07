export class Point {
	constructor(private x: number, private y: number) {}

	public add(p: Point): Point {
		return new Point(this.x + p.x, this.y + p.y);
	}

	public distanceTo(p: Point): number {
		let diff = this.add(new Point(-p.x, -p.y));
		return Math.sqrt(Math.pow(diff.x, 2) + Math.pow(diff.y, 2));
	}
}
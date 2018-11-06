import * as ElfUI from './ui/ElfUI';
import * as ElfColorfulUI from './ui/colorful/ElfColorfulUI';
import * as EventReader from './reader/EventReader'
import * as Emotion from './emotion/Emotion';
import * as ElfUIEvent from './ui/event/ElfUIEvent';
import * as Test from './Test'
import * as KBEventReader from './kb/KBEventReader'

let elem = document.getElementById("content");

let factory = new ElfColorfulUI.ElfColorfulUIFactory(elem);

// let reader = new Test.TestEventReader(1000);
let reader = new KBEventReader.KBEventReader();

let ui = new ElfUI.Builder(factory)
				.setEventReader(reader)
				.build();

reader.start();


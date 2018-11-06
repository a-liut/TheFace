import * as ElfUI from './ElfUI';
import * as ElfColorfulUI from './ElfColorfulUI';
import * as EventReader from './EventReader'
import * as Emotion from './Emotion';
import * as ElfUIEvent from './ElfUIEvent';
import * as Test from './Test'

let elem = document.getElementById("content");

let factory = new ElfColorfulUI.ElfColorfulUIFactory(elem);
let reader = new Test.TestEventReader(1000);

let uiBuilder = new ElfUI.Builder(factory);
uiBuilder.setEventReader(reader);

let ui = uiBuilder.build();

reader.start();


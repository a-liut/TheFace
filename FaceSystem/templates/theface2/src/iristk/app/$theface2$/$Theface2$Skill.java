package iristk.app.$theface2$;

import java.io.File;

import org.slf4j.Logger;

import iristk.cfg.SRGSGrammar;
import iristk.furhat.skill.FlowResource;
import iristk.furhat.skill.Skill;
import iristk.furhat.skill.SkillHandler;
import iristk.furhat.skill.TextFileResource;
import iristk.furhat.skill.XmlResource;
import iristk.speech.OpenVocabularyContext;
import iristk.speech.SemanticGrammarContext;
import iristk.speech.SpeechGrammarContext;
import iristk.system.IrisUtils;
import iristk.util.Language;
import iristk.util.Record;

public class $Theface2$Skill extends Skill {

	private static final String RECOGNIZER_GRAMMAR = "grammar";
	private static final String RECOGNIZER_OPEN = "open";
	
	private static Logger logger = IrisUtils.getLogger($Theface2$Skill.class); 
	
	private $Theface2$Flow flow;
	private File propertiesFile;
	private String name = "$Theface2$Skill";
	private Language language = Language.ENGLISH_US;
	private String recognizer = "grammar";

	public $Theface2$Skill() {
		this.propertiesFile = getPackageFile("skill.properties");
		addResource(new TextFileResource(this, "Properties", propertiesFile));
		try {
			Record config = Record.fromProperties(propertiesFile);
			name = config.getString("name", name);
			language = new Language(config.getString("language", language.getCode()));
			recognizer = config.getString("recognizer", recognizer);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		addResource(new FlowResource(this, "Flow", getSrcFile("$Theface2$Flow.xml")));
		addResource(new XmlResource(this, "Grammar", getPackageFile("$Theface2$Grammar.xml")));
		getRequirements().setLanguage(language);
		getRequirements().setSpeechGrammar(recognizer.equals(RECOGNIZER_GRAMMAR));
		getRequirements().setOpenVocabulary(recognizer.equals(RECOGNIZER_OPEN));
		addEntriesFromFlow($Theface2$Flow.class, () -> flow);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void init() throws Exception {
		if (recognizer.equals(RECOGNIZER_GRAMMAR))  {
			getSkillHandler().loadContext("default", new SpeechGrammarContext(new SRGSGrammar(getPackageFile("$Theface2$Grammar.xml"))));
			getSkillHandler().setDefaultContext("default");
		} else if (recognizer.equals(RECOGNIZER_OPEN)) {
			getSkillHandler().loadContext("default", new OpenVocabularyContext(language));
			getSkillHandler().loadContext("default", new SemanticGrammarContext(new SRGSGrammar(getPackageFile("$Theface2$Grammar.xml"))));
			getSkillHandler().setDefaultContext("default");
		}
		flow = new $Theface2$Flow(getSkillHandler().getSystemAgentFlow());
	}

	@Override
	public void stop() throws Exception {
	}

}

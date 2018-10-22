package iristk.app.face1;

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

public class Face1Skill extends Skill {

	private static final String RECOGNIZER_GRAMMAR = "grammar";
	private static final String RECOGNIZER_OPEN = "open";
	
	private static Logger logger = IrisUtils.getLogger(Face1Skill.class); 
	
	private Face1Flow flow;
	private File propertiesFile;
	private String name = "Face1Skill";
	private Language language = Language.ENGLISH_US;
	private String recognizer = "grammar";

	public Face1Skill() {
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
		addResource(new FlowResource(this, "Flow", getSrcFile("Face1Flow.xml")));
		addResource(new XmlResource(this, "Grammar", getPackageFile("Face1Grammar.xml")));
		getRequirements().setLanguage(language);
		getRequirements().setSpeechGrammar(recognizer.equals(RECOGNIZER_GRAMMAR));
		getRequirements().setOpenVocabulary(recognizer.equals(RECOGNIZER_OPEN));
		addEntriesFromFlow(Face1Flow.class, () -> flow);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void init() throws Exception {
		if (recognizer.equals(RECOGNIZER_GRAMMAR))  {
			getSkillHandler().loadContext("default", new SpeechGrammarContext(new SRGSGrammar(getPackageFile("Face1Grammar.xml"))));
			getSkillHandler().setDefaultContext("default");
		} else if (recognizer.equals(RECOGNIZER_OPEN)) {
			getSkillHandler().loadContext("default", new OpenVocabularyContext(language));
			getSkillHandler().loadContext("default", new SemanticGrammarContext(new SRGSGrammar(getPackageFile("Face1Grammar.xml"))));
			getSkillHandler().setDefaultContext("default");
		}
		flow = new Face1Flow(getSkillHandler().getSystemAgentFlow());
	}

	@Override
	public void stop() throws Exception {
	}

}

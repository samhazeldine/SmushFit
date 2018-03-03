package com.shazeldine.smushfit;


import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

import static java.lang.Math.abs;

// Created by Samuel Hazeldine on 01/03/2018.
// Used to store NLG templates

public class NLGGenerator {
    private Lexicon lexicon;
    private NLGFactory nlgFactory;
    private Realiser realiser;

    public NLGGenerator() {
        lexicon = Lexicon.getDefaultLexicon();
        nlgFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
    }

    // Converts an attribute to
    private String attributeConverter (String attr) {
        switch (attr) {
            case "sleep":
                return "sleep";
            case "steps":
                return "steps";
            default:
                return "";
        }
    }

    // Converts a pearson correlation likelihood to a phrase of liklihood.
    // Uses the WHO mapping of likelihood
    public String pearsonToText(double pearsonLikelihood) {
        double absPearsonLikelihood = abs(pearsonLikelihood);
        if (absPearsonLikelihood > 0.99) {
            return "extremely likely";
        }
        else if (absPearsonLikelihood > 0.9 && absPearsonLikelihood <= 0.99) {
            return "very likely";
        }
        else if (absPearsonLikelihood > 0.7 && absPearsonLikelihood <= 0.9) {
            return "likely";
        }
        else if (absPearsonLikelihood > 0.55 && absPearsonLikelihood <= 0.7) {
            return "probable - more likely than not";
        }
        else if (absPearsonLikelihood > 0.45 && absPearsonLikelihood <= 0.55) {
            return "equally likely as not";
        }
        else if (absPearsonLikelihood > 0.3 && absPearsonLikelihood <= 0.45) {
            return "possible - less likely than not";
        }
        else if (absPearsonLikelihood > 0.1 && absPearsonLikelihood <= 0.3) {
            return "unlikely";
        }
        else if (absPearsonLikelihood > 0.01 && absPearsonLikelihood <= 0.1) {
            return "very unlikely";
        }
        else {
            return "extremely unlikely";
        }
    }

    /*
    public String correlationGenerator (String attr1, String attr2, double pearsonLikelihood) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("you");
        p.setFrontModifier("On days when");
        String attr1Converted = attributeConverter(attr1);
        p.setVerb(attr1Converted);

        SPhraseSpec p2 = nlgFactory.createClause();
        p2.setSubject("you");
        p2.setVerb("are");
        String attr2Converted = attributeConverter(attr2);
        p2.addComplement(attr2Converted);
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(p);
        c.addCoordinate(p2);

        c.setConjunction(",");
        String output = realiser.realiseSentence(c);
        return output;
    }
    */
    public String correlationGenerator (String attr1) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setFrontModifier("On days");
        p.setSubject(nlgFactory.createNounPhrase("you"));
        VPPhraseSpec verb = nlgFactory.createVerbPhrase(attributeConverter(attr1));
        verb.addPostModifier("more");
        p.setVerb(verb);
        String output = realiser.realiseSentence(p);
        return output;
    }
}

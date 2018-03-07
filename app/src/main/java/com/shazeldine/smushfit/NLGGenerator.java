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

    // Creates an NL phrase for max
    public String maxGenerator (String attr, double max) {
        return minMaxMeanGenerator(attr, max, "maximum");
    }

    // Creates an NL phrase for min
    public String minGenerator (String attr, double min) {
        return minMaxMeanGenerator(attr, min, "minimum");
    }

    public String averageGenerator (String attr, double mean) {
        return minMaxMeanGenerator(attr, mean, "average");
    }


    // Creates an NL phrase for min or max
    public String minMaxMeanGenerator (String attr, double max, String minMax) {
        String[] convertedValues = attributeConverter(attr);
        SPhraseSpec p = nlgFactory.createClause();
        p.setFeature(Feature.PERSON, Person.SECOND);
        NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
        String maxAttr = minMax + " " + convertedValues[1];
        NPPhraseSpec insightType = nlgFactory.createNounPhrase(maxAttr);
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("be");
        NPPhraseSpec number = nlgFactory.createNounPhrase(Double.toString(max) + " " + convertedValues[0]);
        verb.addComplement(number);
        subject.addModifier(insightType);
        p.setSubject(subject);
        p.setVerb(verb);
        String output = realiser.realiseSentence(p);
        return output;
    }

    // Converts an attribute to various values
    // [0] is units
    // [1] for amount (e.g. number of steps)
    // [2] increase (e.g. sleep more, listen to more tracks)
    // [3] you vs your
    private String[] attributeConverter (String attr) {
        String[] temp = new String[4];
        switch (attr) {
            case "sleep":
                temp[0] = "hours";
                temp[1] = "amount of sleep";
                temp[2] = "sleep more";
                temp[3] = "you";
                break;
            case "steps":
                temp[0] = "steps";
                temp[1] = "step-count";
                temp[2] = "step-count is higher";
                temp[3] = "your";
                break;
            case "distracting_min":
                temp[0] = "minutes";
                temp[1] = "time spent distracted";
                temp[2] = "are distracted more";
                temp[3] = "you";
                break;
            case "events":
                temp[0] = "";
                temp[1] = "number of events";
                temp[2] = "go to more events";
                temp[3] = "you";
                break;
            case "mood":
                temp[0] = "";
                temp[1] = "";
                temp[2] = "are in a better mood";
                temp[3] = "you";
                break;
            case "productive_min":
                temp[0] = "minutes";
                temp[1] = "time spent being productive";
                temp[2] = "are more productive";
                temp[3] = "you";
                break;
            case "sleep_awakenings":
                temp[0] = "";
                temp[1] = "times awoken in the night";
                temp[2] = "wake more in the night";
                temp[3] = "you";
                break;
            case "tracks":
                temp[0] = "songs";
                temp[1] = "number of tracks listened to";
                temp[2] = "listen to more music";
                temp[3] = "you";
                break;

        }
        return temp;
    }

    // Converts a double to positive or negative
    public String doubleToPositiveNegative (double pearsonLikelihood) {
        if (pearsonLikelihood > 0) {
            return "negative";
        }
        else if (pearsonLikelihood < 0) {
            return "positive";
        }
        else {
            return "no";
        }
    }

    // Converts a pearson correlation likelihood to a phrase of likelihood.
    // Uses the WHO mapping of likelihood
    public String pearsonToLikelihood(double pearsonLikelihood) {
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

    // Generates the statements for correlation
    public String correlationGenerator (String attr1, String attr2, double pearsonLikelihood) {
        //On days when you X
        SPhraseSpec p = nlgFactory.createClause();
        String[] attr1Converted = attributeConverter(attr1);
        p.setSubject(attr1Converted[3]);
        p.setFrontModifier("On days when");
        p.setVerb(attr1Converted[2]);


        //You are very likely to Y
        SPhraseSpec p2 = nlgFactory.createClause();
        String[] attr2Converted = attributeConverter(attr2);
        p2.setSubject("it");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("is");
        verb.setPostModifier(pearsonToLikelihood(pearsonLikelihood));
        p2.setVerb(verb);


        SPhraseSpec p3 = nlgFactory.createClause();
        p3.setSubject(attr2Converted[3]);
        p3.setVerb(attr2Converted[2]);
        p3.setFeature(Feature.TENSE, Tense.FUTURE);

        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        CoordinatedPhraseElement b = nlgFactory.createCoordinatedPhrase();
        b.addCoordinate(p2);
        b.addCoordinate(p3);
        b.setConjunction("");

        c.addCoordinate(p);
        c.addCoordinate(b);
        c.setConjunction(",");


        String output = realiser.realiseSentence(c);
        return output;
    }
}

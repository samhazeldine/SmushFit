package com.shazeldine.smushfit;


import android.util.Log;

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
        return realiser.realiseSentence(minMaxMeanGenerator(attr, max, "maximum"));
    }

    // Creates an NL phrase for min
    public String minGenerator (String attr, double min) {
        return realiser.realiseSentence(minMaxMeanGenerator(attr, min, "minimum"));
    }

    // Creates an NL phrase for average
    public String averageGenerator (String attr, double mean) {
        return realiser.realiseSentence(minMaxMeanGenerator(attr, mean, "average"));
    }


    // Creates an NL phrase for min or max
    // Your max/min/average step-count is 21,000 steps
    public SPhraseSpec minMaxMeanGenerator (String attr, double max, String minMax) {
        String[] convertedValues = attributeConverter(attr);
        SPhraseSpec p = nlgFactory.createClause();
        p.setFeature(Feature.PERSON, Person.SECOND);
        NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
        String maxAttr = minMax + " " + convertedValues[1];
        NPPhraseSpec insightType = nlgFactory.createNounPhrase(maxAttr);
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("be");
        NPPhraseSpec number = nlgFactory.createNounPhrase(doubleToString(max) + " " + convertedValues[0]);
        verb.addComplement(number);
        subject.addModifier(insightType);
        p.setSubject(subject);
        p.setVerb(verb);
        return p;
    }

    //converts a double to a String nicely
    public String doubleToString(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    // Converts an attribute to various values
    // [0] is units
    // [1] for amount (e.g. number of steps)
    // [2] increase (e.g. sleep more, listen to more tracks)
    // [3] you vs your
    // [4] object (e.g. step-count) (this is needed with sentences such as "Your step-count is higher"
    //      rather than sentences such as "You sleep more".
    // [5] decrease (e.g. sleep less, listen to less tracks)
    private String[] attributeConverter (String attr) {
        String[] temp = new String[6];
        switch (attr) {
            case "sleep":
                temp[0] = "hours";
                temp[1] = "amount of sleep";
                temp[2] = "sleep more";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "sleep less";
                break;
            case "steps":
                temp[0] = "steps";
                temp[1] = "step-count";
                temp[2] = "is higher";
                temp[3] = "your";
                temp[4] = "step-count";
                temp[5] = "is lower";
                break;
            case "distracting_min":
                temp[0] = "minutes";
                temp[1] = "time spent distracted";
                temp[2] = "are distracted more";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "are distracted less";
                break;
            case "events":
                temp[0] = "";
                temp[1] = "number of events";
                temp[2] = "go to more events";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "go to less events";
                break;
            case "mood":
                temp[0] = "";
                temp[1] = "";
                temp[2] = "are in a better mood";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "are in a worse mood";
                break;
            case "productive_min":
                temp[0] = "minutes";
                temp[1] = "time spent being productive";
                temp[2] = "are more productive";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "are less productive";
                break;
            case "sleep_awakenings":
                temp[0] = "";
                temp[1] = "times awoken in the night";
                temp[2] = "wake more in the night";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "wake up less in the night";
                break;
            case "tracks":
                temp[0] = "songs";
                temp[1] = "number of tracks listened to";
                temp[2] = "listen to more music";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "listen to less music";
                break;

        }
        return temp;
    }

    // Converts a double to positive or negative
    public int doubleToPositiveNegative (double pearsonLikelihood) {
        if (pearsonLikelihood > 0) {
            return 2;
        }
        else if (pearsonLikelihood < 0) {
            return 5;
        }
        else {
            return 0;
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
            return "probable (more likely than not) ";
        }
        else if (absPearsonLikelihood > 0.45 && absPearsonLikelihood <= 0.55) {
            return "equally likely as not";
        }
        else if (absPearsonLikelihood > 0.3 && absPearsonLikelihood <= 0.45) {
            return "possible (less likely than not)";
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
        int positiveNegative = doubleToPositiveNegative(pearsonLikelihood);
        //On days when you X
        SPhraseSpec p = nlgFactory.createClause();
        String[] attr1Converted = attributeConverter(attr1);
        NPPhraseSpec subject = nlgFactory.createNounPhrase(attr1Converted[3]);
        subject.addModifier(attr1Converted[4]);
        p.setSubject(subject);
        p.setFrontModifier("On days when ");
        p.setVerb(attr1Converted[2]);


        //it is likely
        SPhraseSpec p2 = nlgFactory.createClause();
        String[] attr2Converted = attributeConverter(attr2);
        p2.setSubject("it");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("be");
        verb.setPostModifier(pearsonToLikelihood(pearsonLikelihood));
        p2.setVerb(verb);

        //your x will be higher
        SPhraseSpec p3 = nlgFactory.createClause();
        NPPhraseSpec object = nlgFactory.createNounPhrase(attr2Converted[4]);
        NPPhraseSpec subject3 = nlgFactory.createNounPhrase(attr2Converted[3]);
        subject3.addModifier(object);
        p3.setSubject(subject3);
        p3.setVerb(attr2Converted[positiveNegative]);
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

    //Generates statement for current value
    public String currentGenerator (String attr, String value) {
        double dValue = Double.parseDouble(value);
        SPhraseSpec p = minMaxMeanGenerator(attr, dValue, "current");
        return realiser.realiseSentence(p);
    }


    //Generates statement for today goal
    public String todayGoalGenerator (String attr, String goal, String current, String highLow) {
        double dGoal = Double.parseDouble(goal);
        double dCurrent = Double.parseDouble(current);
        SPhraseSpec p = minMaxMeanGenerator(attr, dCurrent, "current");
        String[] convertedAttributes = attributeConverter(attr);
        SPhraseSpec p2 = nlgFactory.createClause();

        //If the type of data is something where higher is better (e.g. steps)
        if(highLow.equals("High")) {
            NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
            VPPhraseSpec verb = nlgFactory.createVerbPhrase("goal");
            NPPhraseSpec object = nlgFactory.createNounPhrase("of " + goal + " " + convertedAttributes[0]);
            verb.setPostModifier(object);
            p2.setSubject(subject);
            p2.setVerb(verb);
            if (dCurrent < dGoal) {
                //TODO - STOP IT SAYING GOALS PLURAL
                subject.setPreModifier("out of");
            }
            else {
               subject.setPreModifier("which is more than");
            }

        }
        else if (highLow.equals("Low")) {

        }
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(p);
        c.addCoordinate(p2);
        c.setConjunction(",");
        return realiser.realiseSentence(c);
    }

    public void testGenerator () {
        String[] convertedValues = attributeConverter("productive_min");
        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec subject = nlgFactory.createNounPhrase("my");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("is");
        NPPhraseSpec insightType = nlgFactory.createNounPhrase("current " + convertedValues[1] + "?");
        subject.addModifier(insightType);
        verb.setPreModifier("What");
        subject.setPreModifier(verb);
        p.setSubject(subject);
        String s = realiser.realiseSentence(p);
        Log.i("SMUSHFIT_TEST", s);
    }
}

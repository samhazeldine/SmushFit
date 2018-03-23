package com.shazeldine.smushfit;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        NPPhraseSpec number = nlgFactory.createNounPhrase(doubleToString(max, attr) + " " + convertedValues[0]);
        verb.addComplement(number);
        subject.addModifier(insightType);
        p.setSubject(subject);
        p.setVerb(verb);
        return p;
    }

    //converts a double to a String nicely
    public String doubleToString(double d, String attr) {
        if(attr.equals("mood")) {
            if (d == 5.0) {
                return "perfect";
            }
            else if (d == 4.0) {
                return "good";
            }
            else if (d == 3.0) {
                return "okay";
            }
            else if (d == 2.0) {
                return "bad";
            }
            else {
                return "terrible";
            }
        }
        else if(d == (long) d)
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
    public String[] attributeConverter (String attr) {
        String[] temp = new String[6];
        switch (attr) {
            case "sleep":
                temp[0] = "minutes";
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
                temp[1] = "mood";
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
            default:
                temp[0] = "";
                temp[1] = "numbers of tests";
                temp[2] = "take more tests";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "take less tests";
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
            return "likely ";
        }
        else if (absPearsonLikelihood > 0.45 && absPearsonLikelihood <= 0.55) {
            return "unlikely";
        }
        else if (absPearsonLikelihood > 0.3 && absPearsonLikelihood <= 0.45) {
            return "unlikely";
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

    // Generates phrase "when you X"
    public SPhraseSpec whenYouX(String attr) {
        SPhraseSpec p = nlgFactory.createClause();
        String[] attrConverted = attributeConverter(attr);
        NPPhraseSpec subject = nlgFactory.createNounPhrase(attrConverted[3]);
        subject.addModifier(attrConverted[4]);
        p.setSubject(subject);
        p.setFrontModifier("when");
        p.setVerb(attrConverted[2]);
        return p;
    }

    // "it is [likelihood] you will x more, y more, and z more.
    public SPhraseSpec correlationSingleLikelihood(List<CorrelationIdentifier> correlationIdentifiers) {
        //Three separate lists depending on likelihood
        List<CorrelationIdentifier> extremelyLikelyCorrelations = new ArrayList<>();
        List<CorrelationIdentifier> veryLikelyCorrelations = new ArrayList<>();
        List<CorrelationIdentifier> likelyCorrelations = new ArrayList<>();
        for(CorrelationIdentifier id : correlationIdentifiers) {
            String likelihood = pearsonToLikelihood(id.getCorrelationValue());
            if(likelihood.equals("extremely likely")) {
                extremelyLikelyCorrelations.add(id);
            }
            else if(likelihood.equals("very likely")) {
                veryLikelyCorrelations.add(id);
            }
            else {
                likelyCorrelations.add(id);
            }
        }

        //Generate clause for each
        SPhraseSpec pExtremely = nlgFactory.createClause();
        SPhraseSpec pVery = nlgFactory.createClause();
        SPhraseSpec pLikely = nlgFactory.createClause();

        return pExtremely;
        //TODO need to decide on the direction. I think I fucked it.
    }

    //Generates "your x will be higher"
    private SPhraseSpec yourXWillBeHigher(String attr, double pearsonLikelihood) {
        int positiveNegative = doubleToPositiveNegative(pearsonLikelihood);
        String[] attrConverted = attributeConverter(attr);
        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec object3 = nlgFactory.createNounPhrase(attrConverted[4]);
        NPPhraseSpec subject3 = nlgFactory.createNounPhrase(attrConverted[3]);
        subject3.addModifier(object3);
        p.setSubject(subject3);
        p.setVerb(attrConverted[positiveNegative]);
        p.setFeature(Feature.TENSE, Tense.FUTURE);
        return p;
    }

    //Geneartes "it is [likelihood]
    private SPhraseSpec itIsLikelihood(double pearsonLikelihood) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("it");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("be");
        verb.setPostModifier(pearsonToLikelihood(pearsonLikelihood));
        p.setVerb(verb);
        return p;
    }

    // Generates the statements for correlation
    public String correlationGenerator (String attr1, String attr2, double pearsonLikelihood) {
        String[] attr2Converted = attributeConverter(attr2);

        //When you X
        SPhraseSpec p = whenYouX(attr1);

        //it is likely
        SPhraseSpec p2 = itIsLikelihood(pearsonLikelihood);

        SPhraseSpec p3 = nlgFactory.createClause();
        if (abs(pearsonLikelihood) > 0.55) {
            //your x will be higher
            p3 = yourXWillBeHigher(attr2, pearsonLikelihood);
        }
        else {
            //your x will be affected
            NPPhraseSpec subject3 = nlgFactory.createNounPhrase("your");
            NPPhraseSpec object3 = nlgFactory.createNounPhrase(attr2Converted[1]);
            VPPhraseSpec verb3 = nlgFactory.createVerbPhrase("be");
            verb3.setPostModifier("affected");
            subject3.addModifier(object3);
            p3.setSubject(subject3);
            p3.setVerb(verb3);
           //p3.setFeature(Feature.TENSE, Tense.FUTURE);
        }

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

    //Overloaded - can handle double or String
    public String currentGenerator(String attr, double value) {
        SPhraseSpec p = minMaxMeanGenerator(attr, value, "current");
        return realiser.realiseSentence(p);
    }

    //Generates statement for today goal
    public String todayGoalGenerator (String attr, String goal, String current, String highLow) {
        String s = this.todayGoalGenerator(attr, Double.parseDouble(goal), Double.parseDouble(current), highLow);
        return s;
    }

    //Generates statement for today goal
    public String todayGoalGenerator (String attr, double dGoal, double dCurrent, String highLow) {
        String[] convertedAttributes = attributeConverter(attr);
        //No goal set
        if(highLow.equals("None")) {
            NLGElement s1 = nlgFactory.createSentence("You have no goal set for " + convertedAttributes[1]);
            return realiser.realiseSentence(s1);
        }
        SPhraseSpec p = minMaxMeanGenerator(attr, dCurrent, "current");
        SPhraseSpec p2 = nlgFactory.createClause();

        NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
        NPPhraseSpec object = nlgFactory.createNounPhrase("goal of " + doubleToString(dGoal, attr) + " " + convertedAttributes[0]);
        subject.setPostModifier(object);
        p2.setSubject(subject);

        //If the type of data is something where higher is better (e.g. steps)
        if(highLow.equals("High")) {

            if (dCurrent < dGoal) {
                //TODO - STOP IT SAYING GOALS PLURAL
                subject.setPreModifier("out of");
            }
            else {
               subject.setPreModifier("which means you have met");
            }

        }
        //If the type of data is something where lower is better (e.g. awakenings in the night)
        else if (highLow.equals("Low")) {
            if (dCurrent < dGoal) {
                subject.setPreModifier("so you are still on track for");
            }
            else {
                subject.setPreModifier("which means you have unfortunately exceeded");
            }
        }
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(p);
        c.addCoordinate(p2);
        c.setConjunction(",");
        return realiser.realiseSentence(c);
    }

    //Generates speech for trend
    public String trendGenerator(String attr, double slope) {
        String[] convertedValues = attributeConverter(attr);

        //For time frame
        SPhraseSpec pTimeFrame = nlgFactory.createClause();
        NPPhraseSpec subjectTimeFrame = nlgFactory.createNounPhrase("you");
        subjectTimeFrame.setPreModifier("Since");
        subjectTimeFrame.setPostModifier("starting tracking");
        pTimeFrame.setSubject(subjectTimeFrame);

        //Increase/decrease
        SPhraseSpec p = nlgFactory.createClause();
        p.setFeature(Feature.PERSON, Person.SECOND);
        NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
        NPPhraseSpec object = nlgFactory.createNounPhrase(convertedValues[1]);
        if (slope < 0) {
            object.setPostModifier("has decreased");
        }
        else if (slope > 0) {
            object.setPostModifier("has increased");
        }
        else {
            object.setPostModifier("has remained constant");
        }
        p.setSubject(subject);
        p.setObject(object);
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(pTimeFrame);
        c.addCoordinate(p);
        c.setConjunction(",");
        String output = realiser.realiseSentence(c);
        return output;
    }

    public String likelyCorrelationGenerator(String attr, List<CorrelationIdentifier> correlationIdentifiers) {
        String[] convertedAttributes = attributeConverter(attr);
        if(correlationIdentifiers.size() == 0) {
            NLGElement s = nlgFactory.createSentence("Nothing is likely to affect your " + convertedAttributes[1]);
            return realiser.realiseSentence(s);
        }
        else {
            SPhraseSpec p = whenYouX(attr);
            String output = realiser.realiseSentence(p);
            return output;
        }
    }


    //Just used for tests
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

package com.shazeldine.smushfit;


import android.util.Log;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
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
    public SPhraseSpec minMaxMeanGenerator (String attr, double value, String minMax) {
        String[] convertedValues = attributeConverter(attr);
        SPhraseSpec p = nlgFactory.createClause();
        p.setFeature(Feature.PERSON, Person.SECOND);
        NPPhraseSpec subject = nlgFactory.createNounPhrase("your");
        String maxAttr = minMax + " " + convertedValues[1];
        NPPhraseSpec insightType = nlgFactory.createNounPhrase(maxAttr);
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("be");
        NPPhraseSpec number = nlgFactory.createNounPhrase(doubleToString(value, attr) + " " + convertedValues[0]);
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
            else if (d == 1.0) {
                return "terrible";
            }


        }
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%.1f",d);
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
                //temp[2] = "is higher";
                temp[2] = "walk more";
                temp[3] = "your";
                temp[4] = "";
                //temp[4] = "step-count";
                //temp[5] = "is lower";
                temp[5] = "walk less";
                break;
            case "distracting_min":
                temp[0] = "minutes";
                temp[1] = "time spent distracted";
                temp[2] = "be distracted more";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "be distracted less";
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
                temp[2] = "be in a better mood";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "be in a worse mood";
                break;
            case "productive_min":
                temp[0] = "minutes";
                temp[1] = "time spent being productive";
                temp[2] = "be more productive";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "be less productive";
                break;
            case "sleep_awakenings":
                temp[0] = "";
                temp[1] = "number of awakenings";
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
            case "test1":
                temp[0] = "";
                temp[1] = "calories consumed";
                temp[2] = "consume more calories";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "consume less calories";
                break;
            case "test2":
                temp[0] = "";
                temp[1] = "number of tennis games";
                temp[2] = "play more tennis";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "play less tennis";
                break;
            case "test3":
                temp[0] = "bars";
                temp[1] = "amount of chocolate eaten";
                temp[2] = "eat more chocolate";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "eat less chocolate";
                break;
            case "test4":
                temp[0] = "glasses";
                temp[1] = "amount of water drunk";
                temp[2] = "drink more water";
                temp[3] = "you";
                temp[4] = "";
                temp[5] = "drink less water";
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

    // Generates phrase "On days you"
    public SPhraseSpec whenYouX(String attr) {
        SPhraseSpec p = nlgFactory.createClause();
        String[] attrConverted = attributeConverter(attr);
        NPPhraseSpec subject = nlgFactory.createNounPhrase("you");
        subject.addModifier(attrConverted[4]);
        p.setSubject(subject);
        p.setFrontModifier("on days");
        p.setVerb(attrConverted[2]);
        return p;
    }



    // "it is [likelihood] you will x more, y more, and z more.
    public CoordinatedPhraseElement correlationSingleLikelihood(List<CorrelationIdentifier> correlationIdentifiers) {
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
        CoordinatedPhraseElement c1 = nlgFactory.createCoordinatedPhrase();
        CoordinatedPhraseElement c2 = nlgFactory.createCoordinatedPhrase();
        c1.setConjunction("");
        c2.setConjunction("");
        if(extremelyLikelyCorrelations.size()!=0) {
            c1.addCoordinate(correlationOnlyOneLikelihood(extremelyLikelyCorrelations));
        }
        if(veryLikelyCorrelations.size()!=0) {
            c1.addCoordinate(correlationOnlyOneLikelihood(veryLikelyCorrelations));
        }
        if(likelyCorrelations.size()!=0) {
            if(c1.getChildren().size() == 2) {
                c1.setConjunction(";");
            }
            if(c1.getChildren().size()!=0) {
                c2.setConjunction("; and");
            }
            c2.addCoordinate(c1);
            c2.addCoordinate(correlationOnlyOneLikelihood(likelyCorrelations));
        }
        else{
            if(c1.getChildren().size()==2){
                c1.setConjunction("; and");
            }
        }
        return c2;
    }

    //Generates "it is [likelihood] you will x more, y more, and z more (for a single likelihood)
    public CoordinatedPhraseElement correlationOnlyOneLikelihood(List<CorrelationIdentifier> correlationIdentifiers) {
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        SPhraseSpec itIsLikely = itIsLikelihoodNew(correlationIdentifiers.get(0).getCorrelationValue());
        for(CorrelationIdentifier id: correlationIdentifiers) {
            c.addCoordinate(yourXWillBeHigher(id.getAttr2(), id.getCorrelationValue()));
        }
        c.setConjunction("and");
        CoordinatedPhraseElement c2 = nlgFactory.createCoordinatedPhrase();
        c2.addCoordinate(itIsLikely);
        c2.addCoordinate(c);
        c2.setConjunction("");
        return c2;
    }

    //Generates "your x will be higher"
    /*
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
    */

    private SPhraseSpec yourXWillBeHigher(String attr, double pearsonLikelihood) {
        int positiveNegative = doubleToPositiveNegative(pearsonLikelihood);
        String[] attrConverted = attributeConverter(attr);
        SPhraseSpec p = nlgFactory.createClause();
        p.setObject(attrConverted[positiveNegative]);
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


    //Geneartes "it is [likelihood]
    private SPhraseSpec itIsLikelihoodNew(double pearsonLikelihood) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("you");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("are");
        NPPhraseSpec object = nlgFactory.createNounPhrase(pearsonToLikelihood(pearsonLikelihood));
        object.setPostModifier("to");
        verb.setPostModifier(object);
        p.setVerb(verb);
        return p;
    }



    // Generates the statements for correlation
    public String correlationGenerator (String attr1, String attr2, double pearsonLikelihood) {
        String[] attr2Converted = attributeConverter(attr2);

        //On days you
        SPhraseSpec p = whenYouX(attr1);

        //it is likely
        SPhraseSpec p2;

        SPhraseSpec p3 = nlgFactory.createClause();
        if (abs(pearsonLikelihood) > 0.55) {
            //your x will be higher
            p3 = yourXWillBeHigher(attr2, pearsonLikelihood);
            p2 = itIsLikelihoodNew(pearsonLikelihood);
        }
        else {
            p2 = itIsLikelihood(pearsonLikelihood);
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
            if (dCurrent <= dGoal) {
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
        CoordinatedPhraseElement c = trendPhraseGenerator(attr, slope);
        String output = realiser.realiseSentence(c);
        return output;
    }

    //Generates speech for trend
    private CoordinatedPhraseElement trendPhraseGenerator(String attr, double slope) {
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
            object.setPostModifier("has been decreasing");
        }
        else if (slope > 0) {
            object.setPostModifier("has been increasing");
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
        return c;
    }

    //Generates statement for "What is affected by X?"
    public String likelyCorrelationGenerator(String attr, List<CorrelationIdentifier> correlationIdentifiers) {
        String[] convertedAttributes = attributeConverter(attr);
        if(correlationIdentifiers.size() == 0) {
            NLGElement s = nlgFactory.createSentence("Nothing is likely to be affected by your " + convertedAttributes[1]);
            return realiser.realiseSentence(s);
        }
        else {
            CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
            c.addCoordinate(whenYouX(attr));
            c.addCoordinate(correlationSingleLikelihood(correlationIdentifiers));
            c.setConjunction(",");
            return realiser.realiseSentence(c);
        }
    }

    //Generates full paragraph for an attribute.
    public String generateFullParagraphForAttribute(String attr, List<CorrelationIdentifier> correlationIdentifiers, double max, double min, double mean, double slope, double current, double goal, String highLow) {
        DocumentElement currentGoal;
        if(highLow.equals("None")) {
            currentGoal = nlgFactory.createSentence(minMaxMeanGenerator(attr, current, "current"));
        }
        else {
            currentGoal = nlgFactory.createSentence(todayGoalGenerator(attr, goal, current, highLow));
        }

        CoordinatedPhraseElement trend = trendPhraseGenerator(attr, slope);
        CoordinatedPhraseElement maxMin = maximumAndMinimum(attr, max, min);
        CoordinatedPhraseElement trendAndMaxAndMin = nlgFactory.createCoordinatedPhrase();
        trendAndMaxAndMin.addCoordinate(trend);
        trendAndMaxAndMin.addCoordinate(maxMin);
        trendAndMaxAndMin.setConjunction(",");
        DocumentElement trendAndMaxAndMinSentence = nlgFactory.createSentence(trendAndMaxAndMin);

        CoordinatedPhraseElement averageInTimeFrame = nlgFactory.createCoordinatedPhrase();
        NLGElement inTime = nlgFactory.createStringElement("in this time");
        SPhraseSpec average = minMaxMeanGenerator(attr, mean, "average");
        averageInTimeFrame.addCoordinate(inTime);
        averageInTimeFrame.addCoordinate(average);
        averageInTimeFrame.setConjunction(",");
        DocumentElement averageSentence = nlgFactory.createSentence(averageInTimeFrame);

        DocumentElement correlation = nlgFactory.createSentence(likelyCorrelationGenerator(attr, correlationIdentifiers));

        DocumentElement par = nlgFactory.createParagraph(Arrays.asList(currentGoal, trendAndMaxAndMinSentence, averageSentence, correlation));

        String output = realiser.realise(par).getRealisation();
        return output;
    }

    //Generates maximum AND minimum together
    private CoordinatedPhraseElement maximumAndMinimum(String attr, double max, double min) {
        String[] convertedValues = attributeConverter(attr);
        NLGElement maximum = nlgFactory.createStringElement("with a maximum of " + doubleToString(max, attr) + " " + convertedValues[0]);
        NLGElement minimum = nlgFactory.createStringElement("a minimum of " + doubleToString(min, attr) + " " + convertedValues[0]);
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(maximum);
        c.addCoordinate(minimum);
        c.setConjunction("and");
        return c;
    }

    public String generateAllGoals(List<String[]> goalProgresses) {
        List<String> met = new ArrayList<>();
        List<String> onTrack = new ArrayList<>();
        List<String> littleMore = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for(String[] goal :goalProgresses) {
            if(goal[1].equals("met")) {
                met.add(goal[0]);
            }
            else if (goal[1].equals("onTrack")) {
                onTrack.add(goal[0]);
            }
            else if (goal[1].equals("littleMore")) {
                littleMore.add(goal[0]);
            }
            else {
                failed.add(goal[0]);
            }
        }
        CoordinatedPhraseElement metPhrase = nlgFactory.createCoordinatedPhrase();
        for(String s:met) {
            String[] convertedAttributes = attributeConverter(s);
            metPhrase.addCoordinate(convertedAttributes[1]);
        }
        metPhrase.setConjunction("and");
        metPhrase.addPreModifier("Well done for meeting your goals for ");

        CoordinatedPhraseElement littleMorePhrase = nlgFactory.createCoordinatedPhrase();
        for(String s:littleMore) {
            String[] convertedAttributes = attributeConverter(s);
            littleMorePhrase.addCoordinate(convertedAttributes[1]);
        }
        littleMorePhrase.setConjunction("and");
        littleMorePhrase.addPreModifier("Put in a little more work to meet you goals for ");

        CoordinatedPhraseElement onTrackPhrase = nlgFactory.createCoordinatedPhrase();
        for(String s:onTrack) {
            String[] convertedAttributes = attributeConverter(s);
            onTrackPhrase.addCoordinate(convertedAttributes[1]);
        }
        onTrackPhrase.setConjunction("and");
        onTrackPhrase.addPreModifier("You are still on track to meet your goals for ");

        CoordinatedPhraseElement failedPhrase = nlgFactory.createCoordinatedPhrase();
        for(String s:failed) {
            String[] convertedAttributes = attributeConverter(s);
            failedPhrase.addCoordinate(convertedAttributes[1]);
        }
        failedPhrase.setConjunction("and");
        failedPhrase.addPreModifier("Unfortunately you have missed your goals for ");


        DocumentElement e1 = nlgFactory.createSentence(metPhrase);
        DocumentElement e2 = nlgFactory.createSentence(littleMorePhrase);
        DocumentElement e3 = nlgFactory.createSentence(onTrackPhrase);
        DocumentElement e4 = nlgFactory.createSentence(failedPhrase);

        DocumentElement par = nlgFactory.createParagraph(Arrays.asList(e1, e2, e3, e4));

        return realiser.realiseSentence(par);
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

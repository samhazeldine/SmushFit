package com.shazeldine.smushfit;


import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

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
}

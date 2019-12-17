package com.ali.speech.core.controller;

import com.ali.speech.core.components.AliSpeechApi;
import com.fast.dev.core.util.result.InvokerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vocab")
public class VocabController {

    @Autowired
    private AliSpeechApi api;

    @RequestMapping("list")
    public Object list() {
        return InvokerResult.success(api.listAsrVocab());
    }

    @RequestMapping("get")
    public Object get(String vocabId) {
        return InvokerResult.success(api.getAsrVocab(vocabId));
    }

    @RequestMapping("create")
    public Object create(String name, String description, String wordWeights) {
        return null;
    }

    @RequestMapping("update")
    public Object update(String vocabId, String name, String description, String wordWeights) {
        return InvokerResult.success(api.updateAsrVocab(vocabId, name, description, wordWeights));
    }

    @RequestMapping("delete")
    public Object delete() {
        return null;
    }
}

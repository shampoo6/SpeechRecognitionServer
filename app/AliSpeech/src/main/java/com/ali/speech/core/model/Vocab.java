package com.ali.speech.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vocab {
    public String Id;
    public String Name;
    public String Description;
    public int Size;
    public String Md5;
    public String CreateTime;
    public String UpdateTime;
    public Map<String, Integer> WordWeights = new HashMap<String, Integer>();
}

package com.ali.speech.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocabContent {
    public String Id;
    public String Name;
    public String Description;
    public int Size;
    public String Md5;
    public String CreateTime;
    public String UpdateTime;
}

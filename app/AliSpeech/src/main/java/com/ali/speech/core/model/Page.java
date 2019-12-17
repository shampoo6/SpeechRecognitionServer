package com.ali.speech.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    public int PageNumber;
    public int PageSize;
    public int TotalItems;
    public int TotalPages;
    public List<VocabContent> Content = new ArrayList<VocabContent>();
}

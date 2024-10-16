package com.example.cabonerfbe.dto;

import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageList <T> {
    private int currentPage;
    private int totalPage;
    private List<T> listResult = new ArrayList<>();
}

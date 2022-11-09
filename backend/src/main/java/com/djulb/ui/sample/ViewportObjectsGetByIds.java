package com.djulb.ui.sample;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ViewportObjectsGetByIds {
    List<String> ids;
}
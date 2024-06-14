package com.unison.scada.availability.global;

import java.util.List;

public interface ExcelGenerator {

    public void setHeaderNames(List<String> names);

    public void setBodyDatas(List<List<String>> bodyDatass);

}

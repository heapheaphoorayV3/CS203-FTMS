package cs203.ftms.overall.dto;

import java.util.Map;

public class SinglePouleTableDTO {
    private int pouleNumber;
    private Map<String, String> singleTable;

    public SinglePouleTableDTO(int pouleNumber, Map<String, String> singleTable) {
        this.pouleNumber = pouleNumber;
        this.singleTable = singleTable;
    }

    public int getPouleNumber() {
        return pouleNumber;
    }

    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    public Map<String, String> getSingleTable() {
        return singleTable;
    }

    public void setSingleTable(Map<String, String> singleTable) {
        this.singleTable = singleTable;
    }
}
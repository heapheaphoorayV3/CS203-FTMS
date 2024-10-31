package cs203.ftms.overall.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class PouleTableDTO {

    private List<Map<String, String>> pouleTable;

    public PouleTableDTO() {
        this.pouleTable = new LinkedList<>();
    }


    public void addPouleTable(Map<String, String> pouleTable) {
        this.pouleTable.add(pouleTable);
    }

    public List<Map<String, String>> getPouleTable() {
        return pouleTable;
    }

    public void setPouleTable(List<Map<String, String>> pouleTable) {
        this.pouleTable = pouleTable;
    }
}